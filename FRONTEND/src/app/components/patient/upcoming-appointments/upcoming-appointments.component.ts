import { Component } from '@angular/core';
import { LoginService } from 'src/app/services/login.service';
import { PatientAppointmentsService } from 'src/app/services/patient-appointments.service';
import { MatDialog } from '@angular/material/dialog';
import { CancelModalComponent } from 'src/app/modals/cancel-modal/cancel-modal.component';
import { NgToastService } from 'ng-angular-popup';

@Component({
  selector: 'app-upcoming-appointments',
  templateUrl: './upcoming-appointments.component.html',
  styleUrls: ['./upcoming-appointments.component.css'],
})
export class UpcomingAppointmentsComponentPatient {
  upcomingAppointments: any[] = [];
  pId?: number;
  upcomingAppointmentsPage: number = 1;
  itemsPerPage: number = 4;
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
    20: '03:45 PM - 04:00 PM',
    21: '04:00 PM - 04:15 PM',
    22: '04:15 PM - 04:30 PM',
    23: '04:30 PM - 04:45 PM',
    24: '04:45 PM - 05:00 PM',
  };

  constructor(
    private appointmentService: PatientAppointmentsService,
    private loginService: LoginService,
    private dialog: MatDialog,
    private toast: NgToastService
  ) { }

  ngOnInit(): void {
    this.fetchAppointments(this.upcomingAppointmentsPage, this.itemsPerPage);
  }

  mapSlotToTime(slot: number): string {
    return this.slotMappings[slot] || 'Unknown';
  }

  onUpcomingAppointmentsPageChange(pageNumber: number): void {
    this.upcomingAppointmentsPage = pageNumber;
    this.fetchAppointments(this.upcomingAppointmentsPage, this.itemsPerPage);
  }

  fetchAppointments(pageNumber: number, itemsPerPage: number) {
    const stringPatientId = this.loginService.getPatientId();
    if (stringPatientId !== null) {
      this.pId = parseInt(stringPatientId);
    }
    const offset = pageNumber - 1;
    if (this.pId) {
      this.appointmentService
        .searchAppointments(this.pId, offset, itemsPerPage, 'PENDING', 'ASC')
        .subscribe({
          next: (response) => {
            // console.log('Appointments fetched successfully:', response);
            this.upcomingAppointments = response.cookies.content;
            this.totalElements = response.cookies.totalElements;
            this.upcomingAppointments = this.upcomingAppointments.map(
              (appointment) => {
                return {
                  ...appointment,
                  appDate: appointment.appDate.join('-'),
                  slot: this.mapSlotToTime(appointment.slot),
                };
              }
            );

            if (response.cookies.numberOfElements === 0 && this.totalElements!=0) {
              // console.log('No appointments on the current page. Adjusting page number...');
              this.upcomingAppointmentsPage = Math.max(this.upcomingAppointmentsPage - 1, 1); 
              // console.log('New page number:', this.upcomingAppointmentsPage);
              this.fetchAppointments(this.upcomingAppointmentsPage, this.itemsPerPage);
            }
          },
          error: (error) => {
            console.error('Error fetching appointments:', error);
          },
        });
    } else {
      console.error('Patient ID is required');
    }
  }

  cancelAppointment(appointment: any): void {

    const currentDate = new Date();
  const appointmentDate = new Date(appointment.appDate);
  
  if (currentDate.getDate() === appointmentDate.getDate() &&
      currentDate.getMonth() === appointmentDate.getMonth() &&
      currentDate.getFullYear() === appointmentDate.getFullYear()) {
    this.toast.error({
      detail: 'ERROR',
      summary: "Cannot cancel today's appointment",
      duration: 3000,
    });
    return; 
  }

    const dialogRef = this.dialog.open(CancelModalComponent, {
      width: '400px',
      data: {
        isError: 2,
        message: 'Cancel confirmation',
        selectedDate: appointment.appDate,
        selectedSlot: appointment.slot,
      },
    });

    dialogRef.afterClosed().subscribe((result: any) => {
      // console.log(result);
      if (result.result == true) {
        this.appointmentService.cancelAppointment(appointment.appId).subscribe({
          next: (response) => {
            // console.log('Appointment cancelled:', response);
            this.toast.success({
              detail: 'SUCCESS',
              summary: 'Appointment Cancelled',
              duration: 3000,
            });
            this.fetchAppointments(this.upcomingAppointmentsPage, this.itemsPerPage);
          },
          error: (error: any) => {
            console.error('Error cancelling appointment:', error);
            this.toast.error({
              detail: 'ERROR',
              summary: `${error.error.message}`,
              duration: 3000,
            });
          },
        });
      }
    });
  }
}
