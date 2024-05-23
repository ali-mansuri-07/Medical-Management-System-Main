import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { UserService } from 'src/app/services/user.service';
import { MatDialog } from '@angular/material/dialog';
import {
  trigger,
  state,
  style,
  animate,
  transition,
} from '@angular/animations';
import { Router } from '@angular/router';
import { Doctor } from 'src/app/models/doctor';
import { LoginService } from 'src/app/services/login.service';
import { UserConfirmationComponent } from 'src/app/modals/user-confirmation/user-confirmation.component';
import { IToast, NgToastService } from 'ng-angular-popup';

@Component({
  selector: 'app-screen1',
  templateUrl: './screen1.component.html',
  styleUrls: ['./screen1.component.css'],
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
})
export class Screen1Component {
  doctors: any[] = [];
  searchQuery: string = '';
  page: number = 0;
  pages: Array<number> = [];
  isPreviousDisabled: boolean = true; // Initialize as true since user starts at page 0
  isNextDisabled: boolean = false;
  direction: 'next' | 'previous' = 'next'; // 'next' for next direction, 'previous' for previous direction
  sortField: string = '';

  constructor(
    private userService: UserService,
    private loginService: LoginService,
    private router: Router,
    private dialog:MatDialog,
    private toast: NgToastService
  ) {}

  setPage(i: any, event: any) {
    event.preventDefault();
    if (i > this.page) {
      this.direction = 'next'; // Set direction to next
    } else {
      this.direction = 'previous'; // Set direction to previous
    }
    this.page = i;
    this.updateButtonState(); // Update button state
    this.loadData(this.sortField);
  }

  loadData(sort: string) {
    this.sortField = sort;
    if (this.searchQuery.trim() !== '') {
      this.userService.searchDoctors(this.searchQuery, this.page ,sort).subscribe({
        next: (data: any) => {
          this.doctors = data.content;
          // console.log(data)
          // this.doctors = data.content.filter((doctor: any) => doctor.status === 'ACTIVE');
          this.pages = new Array(data.totalPages);
          // console.log(this.doctors)
        },
        error: (err: HttpErrorResponse) => {
          // console.log("error", err);
        }
      });
    }
  }

  goToDoctorSlotBookingScreen(doctor: any) {
    if(localStorage.length==0){
    // this.toast.error({detail:"ERROR",summary:'Error in fetching details',duration:3000});
    const dialogRef = this.dialog.open(UserConfirmationComponent, {
      width: '400px', // Set the width of the modal
      data: {
        isError : 1,
        message : "You must be logged in",
      }
    })
    
    }
    else if (localStorage.getItem('userRole') != 'ROLE_PATIENT') {
      // console.log('Admin cannot book appointment');
      const dialogRef = this.dialog.open(UserConfirmationComponent, {
        width: '400px', // Set the width of the modal
        data: {
          isError : 2,
          message : "A user must register as a patient to book appointments",
        }
      })
    } else {
      if (localStorage.getItem('userTypeId') == 'null') {
        // console.log('First Update profile');
        const dialogRef = this.dialog.open(UserConfirmationComponent, {
          width: '400px', // Set the width of the modal
          data: {
            isError : 2,
            message : "Create a profile to book appointments",
          }
        })
      } else {
        // console.log(doctor);
        this.router.navigate(['/app-doctor-slot-booking-screen', doctor.id]);
      }
    }
  }
  getCurrentYear(): number {
    return new Date().getFullYear();
  }
  pageIncrement() {
    if (this.page < this.pages.length - 1) {
      this.page++;
      this.direction = 'next'; // Set direction to previous
      this.updateButtonState(); // Update button state
      this.loadData(this.sortField);
    }
  }

  pageDecrement() {
    if(this.page > 0){
      this.page--;
      this.direction = 'previous'; // Set direction to next
      this.updateButtonState(); // Update button state
      this.loadData(this.sortField);
    }
  }

  updateButtonState() {
    this.isPreviousDisabled = this.page === 0; // Disable Previous button on first page
    this.isNextDisabled = this.page === this.pages.length - 1; // Disable Next button on last page
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
          this.router.navigate(['/patient/book', doctor.id]);
        }
      } else {
        // User does not have a patient profile, redirect to profile creation page
        this.router.navigate(['/patient/update-profile']);
      }
    } else {
      // User is not logged in, redirect to login page
      this.router.navigate(['/login']);
    }
  }

  scrollToCarousel() {
    const element = document.getElementById('carousel-section');
    if (element) {
      const targetY = element.getBoundingClientRect().top + window.pageYOffset;
      const initialY = window.pageYOffset;
      const distance = targetY - initialY;
      const duration = 1000;
      let start: number;

      function step(timestamp: number) {
        if (!start) start = timestamp;
        const elapsed = timestamp - start;
        const progress = Math.min(elapsed / duration, 1);
        window.scrollTo(0, initialY + distance * ease(progress));

        if (elapsed < duration) {
          requestAnimationFrame(step);
        }
      }

      function ease(t: number): number {
        return t < 0.5 ? 2 * t * t : -1 + (4 - 2 * t) * t;
      }
      requestAnimationFrame(step);
    }
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
