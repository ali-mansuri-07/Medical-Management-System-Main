import { Component, ElementRef, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { NgToastService } from 'ng-angular-popup';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { LoginService } from 'src/app/services/login.service';
import { PatientAppointmentsService } from 'src/app/services/patient-appointments.service';
import { PatientService } from 'src/app/services/patient.service';

@Component({
  selector: 'app-completed-appointments',
  templateUrl: './completed-appointments.component.html',
  styleUrls: ['./completed-appointments.component.css'],
})
export class CompletedAppointmentsComponentPatient {
  

  completedAppointments: any[] = [];
  completedAppointmentsPage: number = 1;
  itemsPerPage: number = 4;
  feedbackFormData: any = {};
  isFormSubmitted: boolean = false;
  currentAppointment: any;
  rating?: number;
  feedback: string = '';
  pId?: number;
  totalElements = 0;

  slotMappings: { [key: number]: string } = {
    1: '10:00 AM - 10:15 AM',
    2: '10:15 AM - 10:30 AM',
    3: '10:30 AM - 10:45 AM',
    4: '10:45 AM - 11:00 AM',
    5: '11:00 AM - 11:15 AM',
    6: '11:15 AM - 11:30 AM',
    7: '11:30 AM - 11:45 AM',
    8: '11:45 AM - 12:00 PM',
    9: '12:00 PM- 12:15 PM',
    10: '12:15 PM - 12:30 PM',
    11: '12:30 PM - 12:45 PM',
    12: '12:45 PM - 01:00 PM',
    13: '02:00 PM - 02:15 PM',
    14: '02:15 PM - 02:30 PM',
    15: '02:30 PM - 02:45 PM',
    16: '02:45 PM - 03:00 PM',
    17: '03:00 PM - 03:15 PM',
    18: '03:15 PM - 03:30 PM',
    19: '03:30 PM - 03:45 PM',
    20: '03:45 PM - 03:00 PM',
    21: '04:00 PM - 04:15 PM',
    22: '04:15 PM - 04:30 PM',
    23: '04:30 PM - 04:45 PM',
    24: '04:45 PM - 05:00 PM',
  };

  constructor(
    private appointmentService: PatientAppointmentsService,
    private patientService: PatientService,
    private loginService: LoginService,
    private toast: NgToastService
  ) { }

  ngOnInit(): void {
    this.fetchAppointments(this.completedAppointmentsPage, this.itemsPerPage);
  }

  fetchAppointments(pageNumber: number, itemsPerPage: number) {
    const stringPatientId = this.loginService.getPatientId();
    if (stringPatientId !== null) {
      this.pId = parseInt(stringPatientId);
    }
    const offset = pageNumber - 1;
    if (this.pId) {
      this.appointmentService.searchAppointments(this.pId, offset, itemsPerPage, "COMPLETED", "DESC").subscribe({
        next: (response) => {
          // console.log('Appointments fetched successfully:', response);
          this.completedAppointments = response.cookies.content;
          this.totalElements = response.cookies.totalElements;
          this.fetchFeedbacks(this.completedAppointments);
        },
        error: (error) => {
          console.error('Error fetching appointments:', error);
        }
      });
    } else {
      console.error('Patient ID is required');
    }
  }

  mapSlotToTime(slot: number): string {
    return this.slotMappings[slot] || 'Unknown';
  }

  onCompletedAppointmentsPageChange(completedpage: number): void {
    this.completedAppointmentsPage = completedpage;
    this.fetchAppointments(this.completedAppointmentsPage, this.itemsPerPage);
  }

  setCurrentAppointment(appointment: any) {
    this.currentAppointment = appointment;
  }

  submitFeedbackForm(feedbackForm: NgForm) {
    // console.log('Current appointment:', this.currentAppointment);
    if (this.currentAppointment && this.currentAppointment.appId) {
      const feedbackData = {
        rating: this.rating,
        review: this.feedback,
        appointment_id: this.currentAppointment.appId
       
      };

      this.patientService.submitFeedback(feedbackData).subscribe({
        next: (response) => {
          // console.log('Feedback submitted successfully:', response);
          this.toast.success({
            detail: 'SUCCESS',
            summary: 'Feedback Submitted',
            duration: 3000,
          });
          this.fetchFeedbacks(this.completedAppointments);
        },
        error: (error) => {
          console.error('Error submitting feedback:', error);
          this.toast.error({
            detail: 'ERROR',
            summary: `${error.error.message}`,
            duration: 3000,
          });
        }
      });

      feedbackForm.resetForm();
      this.rating = 0;
      this.feedback = '';
    } else {
      console.error('Current appointment is invalid:', this.currentAppointment);
    }
  }

  fetchFeedbacks(appointments: any[]): void {
    this.appointmentService.getFeedbackByPatientId(this.pId).subscribe({
      next: (response) => {
        const feedbackList = response.cookies;
        appointments.forEach(appointment => {
          const appointmentIdsWithFeedback = feedbackList.map((feedback: any) => feedback.appointment_id);
          appointment.feedbackSubmitted = appointmentIdsWithFeedback.includes(appointment.appId);
        });
      },
      error: (error) => {
        console.error('Error fetching feedback:', error);
      }
    });
  }


}

