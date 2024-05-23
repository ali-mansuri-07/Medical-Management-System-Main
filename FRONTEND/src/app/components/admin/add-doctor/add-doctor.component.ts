import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { DoctorService } from 'src/app/services/doctor.service';
import { HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { AbstractControl, FormBuilder, FormGroup, NgForm, Validators } from '@angular/forms';
import { Doctor } from 'src/app/models/doctor.model';
import { MatRadioModule } from '@angular/material/radio';
import { AdminService } from 'src/app/services/admin.service';
import { NgToastService } from 'ng-angular-popup';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { ImgService } from 'src/app/services/ImgService.service';
import { specializationDTO } from '../models/specializationDTO';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-add-doctor',
  templateUrl: './add-doctor.component.html',
  styleUrls: ['./add-doctor.component.css'],
})
export class AddDoctorComponent implements OnInit {
<<<<<<< HEAD
  //@ViewChild('specForm') specForm!: NgForm;
  // @ViewChild('doctorForm') doctorForm!: NgForm;
  @ViewChild('passwordField') passwordField!: ElementRef;
  passwordPattern = /^(?=[^A-Z]*[A-Z])(?=[^a-z]*[a-z])(?=\D*\d).{8,}$/;
=======
>>>>>>> 70b59cb0aa72c6777055208dbed254f531d57d10

  fileSizeError: boolean[] = [];
  isSubmitDisabled: boolean[] = [];

  initializeArrays(): void {
    this.fileSizeError = [false, false];
    this.isSubmitDisabled = [false, false];
  }

  onFileSelected(event: any, index: number): void {
    const file: File = event.target.files[0];
    const fileSizeInMB: number = file.size / (1024 * 1024 * 2);
    // console.log(file.size)
    if (fileSizeInMB > 1) {
      this.fileSizeError[index] = true;
      this.isSubmitDisabled[index] = true;
      event.target.value = '';
    } else {
      this.fileSizeError[index] = false;
      this.isSubmitDisabled[index] = false;
    }
  }

  addDoctorForm:   FormGroup = new FormGroup({});
  specForm: FormGroup = new FormGroup({});
  doctor: Doctor = {
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
    rating: 5,
    status: 'ACTIVE',
    userName: '',
    password: '',
    profileImgUrl: '',
  };

  specialization: specializationDTO = {
    imageUrl: '',
    description: '',
    name: '',
    id: 0,
  };

  specializations: Array<specializationDTO> = [];

  constructor(
    private doctorService: DoctorService,
    private adminService: AdminService,
    private imgService: ImgService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private ngLoader: NgxUiLoaderService,
    private toast: NgToastService,
    private userService: UserService,
    private formBuilder: FormBuilder
  ) {}

  ngOnInit(): void {
    this.addDoctorForm = this.formBuilder.group({
      name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50),Validators.pattern('[a-zA-Z ]*')]],
      email: ['', [Validators.required, Validators.email]],
      qualification: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(30)]],
      experience: ['', Validators.required],
      specialization: ['', Validators.required],
      username: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(20)]],
      password: ['', [Validators.required,Validators.pattern(this.passwordPattern)]],
      gender: ['', Validators.required],
      fee: ['', Validators.required],
    });

    this.specForm = this.formBuilder.group({
      specName: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(20)]],
      desc: ['', [Validators.required, Validators.minLength(30)]],
    });
    this.userService.getSpecializations().subscribe({
      next: (data: any) => {
        // console.log(data);
        this.specializations = data;
      },
      error: (err: HttpErrorResponse) => {
        // console.log('something went wrong', err);
      },
    });
  }

  checkGender(gender: string) {
    return this.doctor.gender != null && this.doctor.gender == gender;
  }

  saveDoctor(): void {
    const input = document.getElementById('profile-img') as HTMLInputElement;
    const file: File = input.files![0];

    if (file == null) {
      this.doctor.profileImgUrl = '';
      this.doctor.experienceStart = this.extractYearFromDate(
        this.doctor.experienceStart
      );
      this.ngLoader.startLoader('master');
      this.doctorService.saveDoctor(this.doctor).subscribe({
        next: (data) => {
          // console.log(data);
          this.ngLoader.stopLoader('master');
          this.toast.success({
            detail: 'SUCCESS',
            summary: 'Doctor added successfully',
            duration: 3000,
          });
          this.addDoctorForm.reset();
        },
        error: (err) => {
          if (
            err.status == 400 &&
            err.error.message == 'There is already an element for this'
          ) {
            this.toast.error({
              detail: 'ERROR',
              summary: 'Username already exists',
              duration: 3000,
            });
            this.ngLoader.stopLoader('master');
          } else {
            // console.log('Something went wrong, Try again');
            this.ngLoader.stopLoader('master');
            // console.log(err);
          }
        },
      });
    } else {
      this.ngLoader.startLoader('master');
      this.imgService.upload(file).subscribe({
        next: (url) => {
          this.doctor.profileImgUrl = url;
          this.doctor.experienceStart = this.extractYearFromDate(
            this.doctor.experienceStart
          );
          this.doctorService.saveDoctor(this.doctor).subscribe({
            next: (data) => {
              // console.log('file is not null');
              this.ngLoader.stopLoader('master');
              this.toast.success({
                detail: 'SUCCESS',
                summary: 'Doctor added successfully',
                duration: 3000,
              });
             this.addDoctorForm.reset();
              this.clearInput();
              // this.ngLoader.stopLoader("master");
            },
            error: (err) => {
              if (
                err.status == 400 &&
                err.error.message == 'There is already an element for this'
              ) {
                this.toast.error({
                  detail: 'ERROR',
                  summary: 'Username already exists',
                  duration: 3000,
                });
                this.ngLoader.stopLoader('master');
              } else {
                this.toast.error({
                  detail: 'ERROR',
                  summary: `${err.error.message}`,
                  duration: 3000,
                });
                // console.log('Something went wrong, Try again');
                this.ngLoader.stopLoader('master');
                // console.log(err);
              }
            },
          });
        },
        error: (err) => {
          if (
            err.status == 400 &&
            err.error.error.message == 'Unavailable image format'
          ) {
            this.toast.error({
              detail: 'ERROR',
              summary: 'Image format not supported',
              duration: 3000,
            });
            this.ngLoader.stopLoader('master');
          } else {
            this.toast.error({
              detail: 'ERROR',
              summary: `${err.error.message}`,
              duration: 3000,
            });
            // console.log('Error in uploading image', err);
            this.ngLoader.stopLoader('master');
          }
        },
      });
    }
  }

  saveSpecialization() {
    const input = document.getElementById('spec-img') as HTMLInputElement;
    const file: File = input.files![0];

    if (!file) {
      this.specialization.imageUrl = '';
      // console.log("no file")
      this.adminService.saveSpecialization(this.specialization).subscribe({
        next: (data: any) => {
          // console.log(data);
          document.getElementById('close')?.click();
          this.specForm.reset();
          this.ngLoader.stopLoader('master');
        },
        error: (err: HttpErrorResponse) => {
          // console.log('something went wrong', err);
          this.ngLoader.stopLoader('master');
        },
      });
    } else {
      this.ngLoader.startLoader('master');
      // console.log("yes file")
      this.imgService.upload(file).subscribe({
        next: (data) => {
          this.specialization.imageUrl = data;
          this.adminService.saveSpecialization(this.specialization).subscribe({
            next: (data: any) => {
              // console.log(data);
              document.getElementById('close')?.click();
              this.specForm.reset();
              this.ngLoader.stopLoader('master');
            },
            error: (err: HttpErrorResponse) => {
              // console.log('something went wrong', err);
              this.ngLoader.stopLoader('master');
            },
          });
        },
        error: (err: HttpErrorResponse) => {
          // console.log('something went wrong', err);
          this.ngLoader.stopLoader('master');
        },
      });
    }
    this.ngLoader.startLoader("master");
    this.imgService.upload(file).subscribe({
      next: (data) => {
        this.specialization.imageUrl = data;
        this.adminService.saveSpecialization(this.specialization).subscribe({
          next: (data: any) => {
            // console.log(data);
            document.getElementById('close')?.click();
            this.specForm.reset();
            this.ngLoader.stopLoader("master");
            window.location.reload();
          },
          error: (err: HttpErrorResponse) => {
            // console.log("something went wrong", err);
            this.ngLoader.stopLoader("master");
          }
        })
      },
      error: (err: HttpErrorResponse) => {
        // console.log("something went wrong", err);
        this.ngLoader.stopLoader("master");
      }
    })

  }
  extractYearFromDate(date: string): string {
    const year = new Date(date).getFullYear();
    return year.toString();
  }


  getCurrentDate(): string {
    const today = new Date();
    const year = today.getFullYear();
    const month = today.getMonth() + 1; 
    const day = today.getDate();
    return `${year}-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}`;
  }

  selectGender(gender: string): void {
    this.doctor.gender = gender;
  }

  clearInput() {
    const inputFile = document.getElementById(
      'profile-img'
    ) as HTMLInputElement;
    if (inputFile) {
      inputFile.value = '';
    }
  }

  togglePasswordVisibility(passwordField: HTMLInputElement) {
    passwordField.type = passwordField.type === 'password' ? 'text' : 'password';
  }
}
