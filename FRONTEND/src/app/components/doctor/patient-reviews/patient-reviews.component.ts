import { Component, OnInit } from '@angular/core';
import { DoctorAppointmentsService } from 'src/app/services/doctor-appointments.service';
import { LoginService } from 'src/app/services/login.service';

@Component({
  selector: 'app-patient-reviews',
  templateUrl: './patient-reviews.component.html',
  styleUrls: ['./patient-reviews.component.css']
})
export class PatientReviewsComponent implements OnInit {
  id: any;
  feedback: { review: string; rating: number; }[] = [];

  page: number = 0;
  pages: Array<number> = [];

  upcomingReviewsPage: number = 1;

  itemsPerPage: number = 5;
  totalElements = 0;
  constructor(private doctorAppointmentsService: DoctorAppointmentsService, private loginService: LoginService) { }

  ngOnInit() {
    this.loadReviews();
  }

  loadReviews() {
    this.id = this.loginService.getDoctor();
    this.doctorAppointmentsService.getReviews(this.id).subscribe((response) => {
      // console.log("api call hua hai review wala", response.cookies.content)
      this.feedback = [];
      response.cookies.content.forEach((feedbacks: { review: string; rating: number; }) => {
        this.feedback.push({
          review: "\"" + feedbacks.review + "\"",
          rating: feedbacks.rating
        });
        this.pages = new Array(response.cookies.totalPages);
        // // console.log(response.cookies.totalPages);

      })
    },
      (error) => {
        // console.log("Error fetching feedback: ", error);
      });

  }

  onUpcomingReviewsPageChange(pageNumber: number): void {
    this.upcomingReviewsPage = pageNumber;
    this.loadReviews();
  } 

  getStarArray(rating: number | undefined): number[] {
    if (rating === undefined)
      return Array(5).fill(0)
    const roundedRating = Math.round(rating);
    return Array(roundedRating).fill(0);
  }


}

