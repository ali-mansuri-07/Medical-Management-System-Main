import { Component, EventEmitter, Output } from '@angular/core';
import { AbstractControl, FormBuilder, FormControl, FormGroup, ValidatorFn, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { LocalStorageService } from 'angular-web-storage';
import { LoginService } from 'src/app/services/login.service';
import { PatientService } from 'src/app/services/patient.service';
import { ProfileUpdateService } from 'src/app/services/profile-update.service';


@Component({
  selector: 'app-update-profile',
  templateUrl: './update-profile.component.html',
  styleUrls: ['./update-profile.component.css']
})
export class UpdateProfileComponentPatient {

  @Output() profileUpdated: EventEmitter<void> = new EventEmitter<void>();
  
  updatePatientForm: FormGroup = new FormGroup({});

  userId?: number;

  constructor(private formBuilder: FormBuilder,
    private router: Router,
    private patientService: PatientService ,private loginService: LoginService,
    private localStorage: LocalStorageService,
    private profileUpdateService: ProfileUpdateService ) {}

  ngOnInit(): void {
    this.updatePatientForm = this.formBuilder.group({
      gender: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      birthYear: ['', [Validators.required, Validators.pattern(/^\d{4}$/), maxCurrentYearValidator()]],
      bloodGroup: ['', Validators.required],
      contactNumber: ['', [Validators.required, Validators.pattern(/^(?:\+\d{1,3})?\s\d{8,10}$/)]],
      name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50),Validators.pattern('[a-zA-Z ]*')]],
    });
  }

  onSubmit(): void {
    if (this.updatePatientForm.valid) {
      // console.log('Form submitted successfully');
      // console.log('Form value:', this.updatePatientForm.value);

      const userIdString = this.loginService.getUserId();
      if (userIdString !== null) {
        const userId = parseInt(userIdString);
        
        this.patientService.addPatient(this.updatePatientForm.value, userId).subscribe({
          next: (response: any) => {
            // console.log('Patient added successfully:', response);
            
            localStorage.setItem('patientId',response.cookies.id);
            localStorage.setItem('userTypeId',response.cookies.id);

            this.router.navigate(['/patient']);

            this.profileUpdateService.emitProfileUpdated();
          },
          error: (error: any) => {
            console.error('Error adding patient:', error);
          }
        });
      } else {
        console.error('User ID is null');
      }
    } else {
      // console.log('Form is invalid');
      this.updatePatientForm.markAllAsTouched();
    }
  }
}

export function maxCurrentYearValidator(): ValidatorFn {
  return (control: AbstractControl): { [key: string]: any } | null => {
    const currentYear = new Date().getFullYear();
    const birthYear = control.value;
    
    if (birthYear && birthYear > currentYear) {
      return { 'maxCurrentYear': { value: control.value } };
    }
    
    return null;
  };
}