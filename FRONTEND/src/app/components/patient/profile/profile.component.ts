import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { LoginService } from 'src/app/services/login.service';
import { PatientService } from 'src/app/services/patient.service';
import { ProfileUpdateService } from 'src/app/services/profile-update.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponentPatient implements OnInit {
  
  currentUser: any = localStorage?.getItem('username');

  pId?: number;
  id?: number;
  name: string = this.currentUser;
  gender: string = '';
  email: string = '';
  birthYear: string = '';
  bloodGroup: string = '';
  contactNumber: string = '';

  bloodTypeMappings: { [key: string]: string } = {
    'Ap': 'A+',
    'An': 'A-',
    'Bp': 'B+',
    'Bn': 'B-',
    'ABp': 'AB+',
    'ABn': 'AB-',
    'Op': 'O+',
    'On': 'O-'
  };

  constructor(private route: ActivatedRoute,
    private router: Router,
    private patientService: PatientService,
    public loginService: LoginService,
    private profileUpdateService: ProfileUpdateService) {
    profileUpdateService.publish({ data: '' });
  }

  mapBloodType(bloodType: string): string {
    return this.bloodTypeMappings[bloodType] || '';
  }

  ngOnInit(): void {
    const stringPatientId = this.loginService.getPatientId();
    if (stringPatientId !== null) {
      this.pId = parseInt(stringPatientId);
    }
    if (this.pId) {
      this.patientService.getPatient(this.pId).subscribe({
        next: (data: any) => {
          // console.log('Patient details fetched successfully:', data);

          this.name = data.cookies.name;
          this.gender = data.cookies.gender;
          this.email = data.cookies.email;
          this.birthYear = data.cookies.birthYear;
          this.bloodGroup = data.cookies.bloodGroup;
          this.contactNumber = data.cookies.contactNumber;
        },
        error: (error: any) => {
          console.error('Error fetching patient data:', error);
        }
      });
    }
    this.router.navigate(['/patient/upcoming-appointments']);
  }



  redirectToUpdateProfile(): void {
    this.router.navigate(['/update-profile'])
  }

  redirectToEditProfile(): void {
    this.router.navigate(['/edit-profile'])
  }
}
