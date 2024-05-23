import { Component } from '@angular/core';
import { Appointment } from 'src/app/models/appointment';
import { NgToastComponent, NgToastService } from 'ng-angular-popup';
import { DoctorAppointmentsService } from 'src/app/services/doctor-appointments.service';
import {
  trigger,
  state,
  style,
  animate,
  transition,
} from '@angular/animations';
import { BookModalComponent } from 'src/app/modals/book-modal/book-modal.component';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { Doctor } from 'src/app/models/doctor.model';
import { PatientAppointmentsService } from 'src/app/services/patient-appointments.service';
type StringPair = [string, string];
@Component({
  selector: 'app-doctor-slot-booking-screen',
  templateUrl: './doctor-slot-booking-screen.component.html',
  styleUrls: ['./doctor-slot-booking-screen.component.css'],
  animations: [
    trigger('slideInOut', [
      transition('* => next', [
        style({ transform: 'translateX(-100%)', opacity: 0 }),
        animate('0.8s ease', style({ transform: 'translateX(0)', opacity: 1 }))
      ]),
      transition('* => previous', [
        style({ transform: 'translateX(100%)', opacity: 0 }),
        animate('0.8s ease', style({ transform: 'translateX(0)', opacity: 1 }))
      ])
    ])
  ]
})


export class DoctorSlotBookingScreenComponent {
  timeMap: string[] = [
    '10:00 to 10:15', '10:15 to 10:30', '10:30 to 10:45', '10:45 to 11:00',
    '11:00 to 11:15', '11:15 to 11:30', '11:30 to 11:45', '11:45 to 12:00',
    '12:00 to 12:15', '12:15 to 12:30', '12:30 to 12:45', '12:45 to 13:00',
    '14:00 to 14:15', '14:15 to 14:30', '14:30 to 14:45', '14:45 to 15:00',
    '15:00 to 15:15', '15:15 to 15:30', '15:30 to 15:45', '15:45 to 16:00',
    '16:00 to 16:15', '16:15 to 16:30', '16:30 to 16:45', '16:45 to 17:00'
  ];
  slotMap: { [key: string]: boolean } = {};
  appointmentMap: { [key: string]: boolean } = {};
  currentDoctorAppointmentMap: { [key: string]: boolean } = {};
  appointments: Appointment[] = [];
  selectedDate: string | null = null;
  selectedSlot: string | null = null;
  doctorDetails: Doctor = {
    id: 0,
    name: '',
    gender: '',
    qualification: '',
    email: '',
    fees: 0,
    experienceStart: '',
    specialization: '',
    leaveStart: '',
    leaveEnd: '',
    rating: 0,
    status: '',
    userName: '',
    password: '',
    profileImgUrl: ''
  };
  slots: string[] = [];
  dates: StringPair[] = [];
  direction: 'next' | 'previous' = 'next';
  visibleDates: StringPair[] = [];
  currentIndex: number = 0;
  feedback: { review: string; rating: number; }[] = [];
  dayNames: string[] = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
  leaveStart: string= '';
  leaveEnd: string= '';
  
  constructor(private doctorAppointmentsService: DoctorAppointmentsService, 
    private dialog: MatDialog, 
    private route: ActivatedRoute, 
    private patientAppointmentsService: PatientAppointmentsService,
    private toast: NgToastService,
    private router: Router) {

    const today = new Date();
    for (let i = 1; i < 8; i++) {
      const nextDate = new Date();
      nextDate.setDate(today.getDate() + i);


      this.dates.push([this.dateFormat(nextDate),this.dayNames[nextDate.getDay()]]);
      
    }


    this.updateVisibleDates();
  }

  getnumberofyearsofexperience(): number {
    return parseInt(this.doctorDetails.experienceStart);
  }

  tooltipText(): string {
    return "my tooltip";
  }
  ngOnInit(): void {
    let doctorId: Number;
    this.route.params.subscribe(params => {
      doctorId = params['id'];



      this.doctorAppointmentsService.getParticularDoctor(doctorId).subscribe((response) => {
        response.experienceStart = parseInt(response.experienceStart);
        this.doctorDetails=response;
        
        if(this.doctorDetails?.leaveEnd != null)
        {
        this.leaveEnd = this.doctorDetails?.leaveEnd[0].toString() + '-' +
        this.doctorDetails?.leaveEnd[1].toString().padStart(2, '0') + '-' +
        this.doctorDetails?.leaveEnd[2].toString().padStart(2, '0');
        this.leaveStart = this.doctorDetails?.leaveStart[0].toString() + '-' +
        this.doctorDetails?.leaveStart[1].toString().padStart(2, '0') + '-' +
        this.doctorDetails?.leaveStart[2].toString().padStart(2, '0');
        }
        
          this.loadReviews();
          
      },
      (error) => {
        
        
        this.toast.error({detail:"ERROR",summary:'No such doctor available',duration:5000});
        this.router.navigate(['**']);

      });
      this.patientAppointmentsService.searchAppointments(localStorage.getItem("patientId"),0,100,"PENDING","ASC").subscribe(
        (response) => {
          response.cookies.content.forEach((appointment: { appDate: any[]; slot: string; doctorId: number; }) => {
            const key = new Date(Number(appointment.appDate[0]), (Number(appointment.appDate[1]) - 1), Number(appointment.appDate[2]) + 1).toISOString().substring(0, 10) + '-' + appointment.slot;
            if (appointment.doctorId == doctorId) {
              this.currentDoctorAppointmentMap[key] = true;
            }
            else {
              this.slotMap[key] = true;
            }
            



          });
        },
        (error) => {
          console.error('Error fetching appointments:', error);
        });

      this.doctorAppointmentsService.getAppointmentsOfDoctor(doctorId).subscribe(
        (response) => {

          this.appointments = response.cookies.content;
          
          this.appointments.forEach(appointment => {
            const key = new Date(Number(appointment.appDate[0]), (Number(appointment.appDate[1]) - 1), Number(appointment.appDate[2]) + 1).toISOString().substring(0, 10) + '-' + appointment.slot;

            
            let tempId: string | null = "";
            tempId = localStorage.getItem("patientId");
            if (tempId !== null) {
              const patientId = tempId;
              if (parseInt(patientId) !== appointment.patientId)
                this.appointmentMap[key] = true;
            }
          });



        },
        (error) => {
          console.error('Error fetching appointments:', error);
        })
     
    });



  }


  
  public unavailableDate(date: StringPair): boolean {
    if(date[1] == 'Sun')
    {
    
    return true;
    }
    if(this.leaveEnd == '' && this.leaveEnd == '')
    return false;
    
    const leaveStartDate = new Date(this.leaveStart);
    const leaveEndDate = new Date(this.leaveEnd);
    if(new Date(date[0]).getTime()>=leaveStartDate.getTime() && new Date(date[0]).getTime()<=leaveEndDate.getTime())
    return true;
    return false;
  }
  private dateFormat(date: Date): string {
    const day = date && date.getDate() || -1;
    const dayWithZero = day.toString().length > 1 ? day : '0' + day;
    const month = date && date.getMonth() + 1 || -1;
    const monthWithZero = month.toString().length > 1 ? month : '0' + month;
    const year = date && date.getFullYear() || -1;
    return `${year}-${monthWithZero}-${dayWithZero}`;
  }

  getStarArray(rating: number | undefined): number[] {
    if (rating === undefined)
      return Array(5).fill(0)
    const roundedRating = Math.round(rating);
    return Array(roundedRating).fill(0);
  }


  onDateClick(date: StringPair) {
    this.selectedDate = date[0];
    this.selectedSlot = null;
    
  }




  onSlotClick(slot: string) {
    this.selectedSlot = slot;
    
  }




  updateVisibleDates() {
    this.visibleDates = this.dates.slice(this.currentIndex, this.currentIndex + 4);
  }




  onNextClick() {
    if (this.currentIndex + 3 < this.dates.length) {
      this.currentIndex += 3;
      this.updateVisibleDates();
    }
  }


  onPrevClick() {
    if (this.currentIndex - 3 >= 0) {
      this.currentIndex -= 3;
      this.updateVisibleDates();
    }
  }




  isSlotBooked(date: string | null, slot: number): boolean {
    if (!date || !slot) {
      return false;
    }
    const key = date + '-' + (slot);

    return this.appointmentMap.hasOwnProperty(key);
  }


  isSlotBookedWithOtherDoctor(date: string | null, slot: number): boolean {
    if (!date || !slot) {
      return false; 
    }
    const key = date + '-' + (slot);
    
    return this.slotMap.hasOwnProperty(key);
  }

  isSlotBookedWithThisDoctor(date: string | null, slot: number): boolean {
    if (!date || !slot) {
      return false; 
    }
    const key = date + '-' + (slot);
    
    return this.currentDoctorAppointmentMap.hasOwnProperty(key);
  }


  onBookClick(): void {
    
    const dialogRef = this.dialog.open(BookModalComponent, {
      width: '400px', 
      data: {
        isType : 1,
        message: "Booking slot",
        selectedDate: this.selectedDate,
        selectedSlot: this.selectedSlot
      }
    });



    dialogRef.afterClosed().subscribe((result: any) => {
      

      if (this.selectedSlot !== null) {
       let index = this.timeMap.indexOf(this.selectedSlot)+1;
      
      if(result.result==true)
      {
        
        this.doctorAppointmentsService.bookDoctorAppointment(this.doctorDetails?.id,localStorage.getItem("patientId"),this.selectedDate,index)
        .subscribe((response) => {
            
          const dialogRef = this.dialog.open(BookModalComponent, {
            width: '400px', 
            data: {
              isType : 2,
              message : "Successfully Booked",
              selectedDate : this.selectedDate,
              selectedSlot : this.selectedSlot
            }
          })
          
        },
        (error) => {
          
          const dialogRef = this.dialog.open(BookModalComponent, {
            width: '400px',
            data: {
              isType : 3,
              message : error.error.message,
              selectedDate : this.selectedDate,
              selectedSlot : this.selectedSlot
            }
          });
        })
    }
  }
    });


  }
  isButtonDisabled(): boolean {
    return (this.selectedDate == null || this.selectedSlot == null);
  }

  page: number = 0;
  pages: Array<number> = [];
  isPreviousDisabled: boolean = true;
  isNextDisabled: boolean = false;
  

  updateButtonState() {
    this.isPreviousDisabled = this.page === 0; 
    this.isNextDisabled = this.page === this.pages.length - 1; 
  }

  getCurrentYear(): number {
    return new Date().getFullYear();
  }

  loadReviews() {
    

  
  this.doctorAppointmentsService.getReviewsOfDoctor(this.doctorDetails?.id, this.page, 1).subscribe((response) => {
    this.feedback=[];
    response.cookies.content.forEach((feedbacks: { review: string; rating: number;}) => {
      this.feedback.push({review: "\""+feedbacks.review+"\"",
    rating: feedbacks.rating});
    
    this.pages = new Array(response.cookies.totalPages);
      
      
    })
},
(error) => {
  
});
      }
    
  

  

  setPage(i: any, event: any) {
    event.preventDefault();
    if (i > this.page) {
      this.direction = 'next'; 
    } else {
      this.direction = 'previous'; 
    }
    this.page = i;
    this.updateButtonState(); 
    this.loadReviews();
  }

  pageIncrement() {
    if (this.page < this.pages.length) {
      this.page++;
      this.direction = 'next'; 
      this.updateButtonState(); 
      this.loadReviews();
    }
  }

  pageDecrement() {
    if (this.page > 0) {
      this.page--;
      this.direction = 'previous'; 
      this.updateButtonState(); 
      this.loadReviews();
    }
  }

}

