import { Component, OnInit } from '@angular/core';
import { DoctorAppointmentsService } from 'src/app/services/doctor-appointments.service';
import { LoginService } from 'src/app/services/login.service';

@Component({
  selector: 'app-upcoming-appointments-doctor',
  templateUrl: './upcoming-appointments.component.html',
  styleUrls: ['./upcoming-appointments.component.css']
})
export class UpcomingAppointmentsComponent implements OnInit {
  upcomingAppointments: any[] = [];
  dId: number | undefined;

  upcomingAppointmentsPage: number = 1;

  itemsPerPage: number = 4;

  constructor(private appointmentService: DoctorAppointmentsService,
    private loginService: LoginService ) {}

  ngOnInit(): void {
    this.fetchAppointments();
  }

  fetchAppointments() {
    const stringDoctorId = this.loginService.getDoctor();
      if(stringDoctorId !== null) {
        this.dId = parseInt(stringDoctorId);
      }
    if (this.dId) {
      this.appointmentService.getUpcomingAppointments(this.dId).subscribe({
        next: (response: any) => {
          // console.log('Appointments fetched successfully:', response.cookies);
          this.upcomingAppointments = response.cookies.content.filter((appointment:any) => appointment.appStatus === 'PENDING');
          // console.log("upcommingAppointments : "+ this.upcomingAppointments)
          },
        error: (error) => {
          console.error('Error fetching appointments:', error);
        }
      });
    } else {
      console.error('Doctor ID is required');
    }
  }

  onUpcomingAppointmentsPageChange(upcomingpage: number): void {
    this.upcomingAppointmentsPage = upcomingpage;
    // console.log("this.upcomingAppointmentsPage = "+ this.upcomingAppointmentsPage);
   
  }
  getSlotTime(slot: number): string {
    // Define your slot time mappings here
    const slotTimes: { [key: number]: string } = {
      1: '10:00 AM - 10:15 AM',
      2: '10:15 AM - 10:30 AM',
      3: '10:30 AM - 10:45 AM',
      4: '10:45 AM - 11:00 AM',
      5: '11:00 AM - 11:15 AM',
      6: '11:15 AM - 11:30 AM',
      7: '11:30 AM - 11:45 AM',
      8: '11:45 AM - 12:00 PM',
      9: '12:00 PM - 12:15 PM',
      10: '12:15 PM - 12:30 PM',
      11: '12:30 PM - 12:45 PM',
      12: '12:45 PM - 1:00 PM',
      13: '2:00 PM - 2:15 PM',
      14: '2:15 PM - 2:30 PM',
      15: '2:30 PM - 2:45 PM',
      16: '2:45 PM - 3:00 PM',
      17: '3:00 PM - 3:15 PM',
      18: '3:15 PM - 3:30 PM',
      19: '3:30 PM - 3:45 PM',
      20: '3:45 PM - 4:00 PM',
      21: '4:00 PM - 4:15 PM',
      22: '4:15 PM - 4:30 PM',
      23: '4:30 PM - 4:45 PM',
      24: '4:45 PM - 5:00 PM'

      // Add more slot time mappings as needed
    };

    // Return the slot time corresponding to the slot number, or 'Unknown' if not found
    return slotTimes[slot] || 'Unknown';
  }

  }

 

