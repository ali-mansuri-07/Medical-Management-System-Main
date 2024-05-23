import { HttpErrorResponse } from '@angular/common/http';
import { Component, ElementRef, OnInit, ViewChild, ViewEncapsulation } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Modal } from 'bootstrap';
import { NgToastService } from 'ng-angular-popup';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { Doctor } from 'src/app/models/doctor.model';
import { ImgService } from 'src/app/services/ImgService.service';
import { AdminService } from 'src/app/services/admin.service';

// declare var $: any

@Component({
  selector: 'app-view-doctor',
  templateUrl: './view-doctor.component.html',
  styleUrls: ['./view-doctor.component.css'],
  styles: [
    `
    .my-custom-class .tooltip-inner {
      background-color: #0B3E25;
      margin-bottom: 0.3rem;
    }
    .my-custom-class .tooltip-arrow{
      margin-bottom: 0.3rem;
    }
    .my-custom-class.bs-tooltip-end .tooltip-arrow::before {
      border-right-color: #0B3E25;
    }
    .my-custom-class.bs-tooltip-start .tooltip-arrow::before {
      border-left-color: #0B3E25;
    }
    .my-custom-class.bs-tooltip-top .tooltip-arrow::before {
      border-top-color: #0B3E25;
    }
    .my-custom-class.bs-tooltip-bottom .tooltip-arrow::before {
      border-bottom-color: #0B3E25;
      margin: 1rem;
    
    }`

  ]
})
export class ViewDoctorComponent implements OnInit {
  @ViewChild('exampleModal') modal: any;

  fileSizeError: boolean = false;

  onFileSelected(event: any): void {
    const file: File = event.target.files[0];
    const fileSizeInMB: number = file.size / (1024 * 1024 * 2);
    // console.log(fileSizeInMB);
    if (fileSizeInMB > 2) {
      this.fileSizeError = true;
      event.target.value = '';
      // console.log("file size greater than 2mb")
    } else {
      this.fileSizeError = false;
    }
  }
  
  minLeaveStartDate!: Date;
  minLeaveEndDate!: Date;
  searchQuery = "";
  filterQuery = "";
  selectedDoctor: Doctor = {
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
  page: number = 0;
  doctorData: Array<Doctor> = []
  pages: Array<number> = []
  isPreviousDisabled: boolean = false;
  isNextDisabled: boolean = false;
  calculateSlNo(i: number) {
    return (this.page * 8) + i;
  }

  setPage(i: number, event: Event) {
    event.preventDefault();
    this.page = i;
    if (this.searchQuery === '' && this.filterQuery === '') {
      this.loadData();
    }
    else if (this.searchQuery === '' && this.filterQuery != '') {
      this.filter(this.filterQuery);
    }
    else if (this.searchQuery !== '' && this.filterQuery != '') {
      this.searchAndFilter();
    }
    else {
      this.search();
    }
  }

  constructor(private adminService: AdminService,
    private ngxService: NgxUiLoaderService,
    private toast: NgToastService,
    private imgService: ImgService,
  ) {
  }

  ngOnInit(): void {
    this.loadData();
    const currentDate = new Date();
    this.minLeaveStartDate = new Date(currentDate.setDate(currentDate.getDate() + 8));
    this.minLeaveEndDate = new Date(currentDate.setDate(currentDate.getDate())); // End date should be at least 1 day later than start date
  }

  loadData() {
    this.ngxService.startLoader("loader-01");
    this.adminService.getDoctorData(this.page).subscribe({
      next: (data: any) => {
        // console.log(data.content);
        this.doctorData = data.content;
        this.pages = new Array(data.totalPages);
        this.ngxService.stopLoader("loader-01");
      },
      error: (error: HttpErrorResponse) => {
        // console.log("something went wrong", error);
        this.ngxService.stopLoader("loader-01");
      }
    });

  }

  updateSubmit() {
    const input = document.getElementById('profile-img') as HTMLInputElement;
    const file: File = input.files![0];

    if (file == null) {
      this.selectedDoctor.profileImgUrl = this.selectedDoctor.profileImgUrl;
      this.ngxService.startLoader("master");
      // console.log(this.selectedDoctor);
      this.adminService.updateDoctor(this.selectedDoctor).subscribe({
        next: (data: any) => {
          this.ngxService.stopLoader("master");
          this.toast.success({ detail: "SUCCESS", summary: "Doctor details updated successfully", duration: 2000 });
          document.getElementById('close')?.click();
          this.loadData();
        },
        error: (err: HttpErrorResponse) => {
          this.toast.error({ detail: "ERROR", summary: 'Error while updating details', duration: 3000 })
          // console.log("Something went wrong", err);
          this.ngxService.stopLoader("master");
        }
      })
    }else {
      this.ngxService.startLoader("master");
      // console.log(this.selectedDoctor);
      this.imgService.upload(file).subscribe({
        next: (url) => {
          this.selectedDoctor.profileImgUrl = url;
          this.adminService.updateDoctor(this.selectedDoctor).subscribe({
            next: (data: any) => {
              this.ngxService.stopLoader("master");
              document.getElementById('close')?.click();
              this.clearInput();
              this.toast.success({ detail: "SUCCESS", summary: "Doctor details updated successfully", duration: 2000 });
              this.loadData();
            },
            error: (err: HttpErrorResponse) => {
              this.toast.error({ detail: "ERROR", summary: 'Error while updating details', duration: 3000 })
              // console.log("Something went wrong", err);
              this.ngxService.stopLoader("master");
            }
          })
        },
        error: (err) => {
          if (err.status == 400 && err.error.error.message == "Unavailable image format") {
            this.toast.error({ detail: "ERROR", summary: 'Image format not supported', duration: 3000 })
            this.ngxService.stopLoader("master");
          } else {
            // console.log("Error in uploading image", err)
            this.ngxService.stopLoader("master");
          }
        }
      })
    }
  }

  search() {
    if (this.searchQuery == "") {
      return;
    }
    else if (this.filterQuery != "") {
      return this.searchAndFilter();
    }
    this.ngxService.startLoader("loader-01");
    this.adminService.searchDoctor(this.searchQuery, this.page, 8).subscribe({
      next: (data: any) => {
        this.doctorData = data.content;
        this.pages = new Array(data.totalPages);
        this.ngxService.stopLoader("loader-01");
      },
      error: (err: HttpErrorResponse) => {
        // console.log("error", err);
        this.ngxService.stopLoader("loader-01");
      }
    })
  }

  filter(filterData: string) {
    this.filterQuery = filterData;
    if (this.searchQuery !== "") {
      return this.searchAndFilter();
    }
    this.ngxService.startLoader("loader-01");
    this.adminService.filterByStatus(filterData, this.page, 8).subscribe({
      next: (data: any) => {
        this.doctorData = data.content;
        this.pages = new Array(data.totalPages);
        this.ngxService.stopLoader("loader-01");
      },
      error: (err: HttpErrorResponse) => {
        // console.log("error", err);
        this.ngxService.stopLoader("loader-01");
      }
    })
  }

  searchAndFilter() {
    // console.log("inside search and filter")
    this.adminService.searchAndFilter(this.searchQuery, this.filterQuery, this.page, 8).subscribe({
      next: (data: any) => {
        this.doctorData = data.content;
        this.pages = new Array(data.totalPages);
      },
      error: (err: HttpErrorResponse) => {
        // console.log("error", err);
      }
    })
  }

  clearFilters() {
    this.searchQuery = "";
    this.filterQuery = "";
    this.loadData();
  }

  pageIncrement() {
    this.page++;
    if (this.searchQuery === '' && this.filterQuery === '') {
      this.loadData();
    }
    else if (this.searchQuery === '' && this.filterQuery != '') {
      this.filter(this.filterQuery);
    }
    else if (this.searchQuery !== '' && this.filterQuery != '') {
      this.searchAndFilter();
    }
    else {
      this.search();
    }
  }

  pageDecrement() {
    this.page--;
    if (this.searchQuery === '' && this.filterQuery === '') {
      this.loadData();
    }
    else if (this.searchQuery === '' && this.filterQuery != '') {
      this.filter(this.filterQuery);
    }
    else if (this.searchQuery !== '' && this.filterQuery != '') {
      this.searchAndFilter();
    }
    else {
      this.search();
    }
  }

  showDetails(doctor: Doctor) {
    this.selectedDoctor = { ...doctor };
  }

  clearInput(){
    const inputFile = document.getElementById('profile-img') as HTMLInputElement;
    if (inputFile) {
      inputFile.value = '';
    }
  }
 
}
