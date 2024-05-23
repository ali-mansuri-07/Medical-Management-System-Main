import { Component } from '@angular/core';
import { trigger, style, animate, transition } from '@angular/animations';
import { TitleCasePipe } from '@angular/common';
import { UserService } from 'src/app/services/user.service';
import { ActivatedRoute, Router } from '@angular/router';
import { LoginService } from 'src/app/services/login.service';
import { UserConfirmationComponent } from 'src/app/modals/user-confirmation/user-confirmation.component';
import { MatDialog } from '@angular/material/dialog';
import { specializationDTO } from '../../admin/models/specializationDTO';

@Component({
  selector: 'app-carousel',
  templateUrl: './carousel.component.html',
  styleUrls: ['./carousel.component.css'],
  animations: [
    trigger('slideInOut', [
      transition('* => next', [
        style({ transform: 'translateX(-100%)', opacity: 0 }),
        animate('0.8s ease', style({ transform: 'translateX(0)', opacity: 1 })),
      ]),
      transition('* => previous', [
        style({ transform: 'translateX(100%)', opacity: 0 }),
        animate('0.8s ease', style({ transform: 'translateX(0)', opacity: 1 })),
      ]),
    ]),
  ],
  providers: [TitleCasePipe],
})
export class CarouselComponent {
  specializations: Array<specializationDTO> = [];
  specializationsChunks: specializationDTO[][] = [];
  doctors: any[] = [];
  specialization: string = '';
  page: number = 0;
  pages: Array<number> = [];
  isPreviousDisabled: boolean = true;
  isNextDisabled: boolean = false;
  direction: 'next' | 'previous' = 'next';

  // specializationArray: Array<specializationDTO> = [];

  constructor(
    private userService: UserService,
    private loginService: LoginService,
    private router: Router,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.loadSpecializations();
  }
  goToDoctorSlotBookingScreen(doctor: any) {
    if (localStorage.length == 0) {
      const dialogRef = this.dialog.open(UserConfirmationComponent, {
        width: '400px', // Set the width of the modal
        data: {
          isError: 1,
          message: 'Click OK to Login',
        },
      });
    } else if (localStorage.getItem('userRole') != 'ROLE_PATIENT') {
      // console.log('Admin cannot book appointment');
      const dialogRef = this.dialog.open(UserConfirmationComponent, {
        width: '400px', // Set the width of the modal
        data: {
          isError: 2,
          message: 'A user must register as a patient to book appointments',
        },
      });
    } else {
      if (localStorage.getItem('userTypeId') == 'null') {
        // console.log('First Update profile');
        const dialogRef = this.dialog.open(UserConfirmationComponent, {
          width: '400px', // Set the width of the modal
          data: {
            isError: 2,
            message: 'Create a profile to book appointments',
          },
        });
      } else {
        // console.log(doctor);
        this.router.navigate(['/app-doctor-slot-booking-screen', doctor.id]);
      }
    }
  }
  loadSpecializations() {
    this.userService.getSpecializations().subscribe({
      next: (data: any) => {
        this.specializations = data;
        this.splitSpecializationsIntoChunks();
        // this.specializationArray = data;
      },
      error: (error: any) => {
        console.error('Error fetching specializations:', error);
      },
    });
  }

  splitSpecializationsIntoChunks(): void {
    const chunkSize = 4;
    for (let i = 0; i < this.specializations.length; i += chunkSize) {
      this.specializationsChunks.push(
        this.specializations.slice(i, i + chunkSize)
      );
    }
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
    this.loadData(this.specialization);
    this.loadSpecializations();
  }

  loadData(specializationQuery: string) {
    this.specialization = specializationQuery;
    this.userService.viewDoctors(this.specialization, this.page).subscribe({
      next: (data: any) => {
        this.doctors = data.content;
        // this.doctors = data.content.filter((doctor: any) => doctor.status === 'ACTIVE');
        this.pages = new Array(data.totalPages);
      },
      error: (error: any) => {
        console.error('Error fetching doctors:', error);
      },
    });
  }

  pageIncrement() {
    if (this.page < this.pages.length - 1) {
      this.page++;
      this.direction = 'next';
      this.updateButtonState();
      this.loadData(this.specialization);
    }
  }

  pageDecrement() {
    if(this.page > 0){
      this.page--;
      this.direction = 'previous';
      this.updateButtonState();
      this.loadData(this.specialization);
    }
  }

  updateButtonState() {
    this.isPreviousDisabled = this.page === 0;
    this.isNextDisabled = this.page === this.pages.length - 1;
  }

  getStarArray(rating: number): number[] {
    const roundedRating = Math.round(rating);
    return Array(roundedRating).fill(0);
  }

  bookAppointment(doctor: any) {
    if (this.loginService.isLoggedIn()) {
      // User is logged in
      const userTypeId = localStorage.getItem('userTypeId');
      const userId = localStorage.getItem('userId');
      // console.log('userId : ' + userId);
      // console.log('userTypeId : ' + userTypeId);
      if (userTypeId != 'null') {
        // console.log('User has profile patient');
        const userType = localStorage.getItem('userRole');
        // console.log('userType : ' + userType);
        if (userType == 'ROLE_PATIENT') {
          // User has a patient profile, redirect to appointment booking page
          this.router.navigate(['/appointment/book', doctor.id]);
        }
      } else {
        // User does not have a patient profile, redirect to profile creation page
        this.router.navigate(['/patient/update']);
      }
    } else {
      // User is not logged in, redirect to login page
      this.router.navigate(['/login']);
    }
  }
  getCurrentYear(): number {
    return new Date().getFullYear();
  }

  clickedIndex: number | null = null;
  clickedCardIndex: number | null = null;

  handleCardClick(index: number, cardIndex: number) {
    // console.log("handleCardClick called");
    this.clickedIndex = index;
    this.clickedCardIndex = cardIndex;
    // console.log(this.clickedIndex, this.clickedCardIndex)
  }

  visiblePages(): number[] {
    const pageCount = this.pages.length;
    const visiblePageCount = Math.min(pageCount, 5);
    let firstVisiblePage = Math.max(0, this.page - 2);
    // Adjust the starting visible page if it exceeds the maximum possible value
    if (pageCount - firstVisiblePage < visiblePageCount) {
      firstVisiblePage = Math.max(0, pageCount - visiblePageCount);
    }  
    return Array.from({ length: visiblePageCount }, (_, i) => firstVisiblePage + i);
  }
}
