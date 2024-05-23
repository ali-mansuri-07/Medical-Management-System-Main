import { Component, OnInit } from '@angular/core';
import { DoctorProfileService } from 'src/app/services/doctor-profile.service';
import { DataClass } from './data-class';
import { LoginService } from 'src/app/services/login.service';

@Component({
  selector: 'app-profile-doctor',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css'],
})
export class ProfileComponent implements OnInit {
  id: any; // Example user ID
  doctor: DataClass | undefined;
  constructor(
    private doctorProfile: DoctorProfileService,
    private loginService: LoginService
  ) {}
  ngOnInit(): void {
    this.getDoctorProfile();
  }
  getDoctorProfile() {
    this.id = this.loginService.getDoctor();

    this.doctorProfile.getDoctorById(this.id).subscribe({
      next: (data: any) => {
        // console.log(data);
        this.doctor = data;
      },
      error: (err) => {
        // console.log(err);
      },
    });
  }

  getStarArray(rating: number | undefined): number[] {
    if(rating === undefined)
    return Array(5).fill(0)
    const roundedRating = Math.round(rating);
    return Array(roundedRating).fill(0);
  }
}
