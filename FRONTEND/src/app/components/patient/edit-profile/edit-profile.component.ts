import { Component, EventEmitter, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { NgToastService } from 'ng-angular-popup';
import { LoginService } from 'src/app/services/login.service';
import { PatientService } from 'src/app/services/patient.service';
import { ProfileUpdateService } from 'src/app/services/profile-update.service';

@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.css']
})
export class EditProfileComponentPatient {

  isDisabled: boolean = true;

  pId?: number;
  id?: number;
  name: string = '';
  gender: string = '';
  email: string = '';
  birthYear: string = '';
  bloodGroup: string = '';
  contactNumber: string = '';
  address: string = '';

  @Output() profileUpdated: EventEmitter<void> = new EventEmitter<void>();

  constructor(private formBuilder: FormBuilder,
    private router: Router,
    private patientService: PatientService ,
    private loginService: LoginService,
    private profileUpdateService: ProfileUpdateService,
    private toast: NgToastService ) {}

  editPatientForm: FormGroup = new FormGroup({});

  ngOnInit(): void {
    const stringPatientId = this.loginService.getPatientId();
      if(stringPatientId !== null) {
        this.pId = parseInt(stringPatientId);
      }
      if(this.pId){
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
    this.editPatientForm = this.formBuilder.group({
      gender: { value: this.gender, disabled: true },
      email: [this.email, [Validators.email]],
      birthYear: { value: this.birthYear, disabled: true },
      bloodGroup: { value: this.bloodGroup, disabled: true },
      contactNumber: [this.contactNumber, [Validators.pattern(/^(?:\+\d{1,3})?\s\d{8,10}$/)]],
      name:{ value: this.name, disabled: true }
    });
  }

  onSubmit(): void {
    if (this.editPatientForm.valid) {

      const formValue = {
        ...this.editPatientForm.value,
        name: this.name,
        gender: this.gender,
        birthYear: this.birthYear,
        bloodGroup: this.bloodGroup
      };

      // console.log('Form submitted successfully');
      // console.log('Form value:', formValue);
  
      const userIdString = this.loginService.getPatientId();
      if (userIdString !== null) {
        const patientId = parseInt(userIdString);
        formValue.email == "" ? formValue.email=this.email : formValue.email = formValue.email;
        formValue.contactNumber == "" ? formValue.contactNumber=this.contactNumber : formValue.contactNumber = formValue.contactNumber;
         
        
        // console.log('Form value:', formValue, "Email:", this.email);
        this.patientService.updatePatient(patientId, formValue).subscribe({
          next: (response: any) => {
            // console.log('Patient details updated successfully:', response);
            this.toast.success({
              detail: 'SUCCESS',
              summary: 'Profile updated',
              duration: 3000,
            });

            this.profileUpdateService.emitProfileUpdated();

            this.router.navigate(['/patient']);
          },
          error: (error: any) => {
            console.error('Error updating patient details:', error);
            this.toast.error({
              detail: 'ERROR',
              summary: `${error.error.message}`,
              duration: 3000,
            });
          }
        });
      } else {
        console.error('User ID is null');
      }
    } else {
      // console.log('Form is invalid');
      this.editPatientForm.markAllAsTouched();
    }
  }
  
}
