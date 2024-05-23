import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AddDoctorComponent } from './components/admin/add-doctor/add-doctor.component';
import { ReportComponent } from './components/admin/report/report.component';
import { ViewDoctorComponent } from './components/admin/view-doctor/view-doctor.component';
import { HomeScreenComponent } from './pages/home-screen/home-screen.component';
import { AdminDashboardComponent } from './pages/admin-dashboard/admin-dashboard.component';
import { AdminHomeComponent } from './components/admin/admin-home/admin-home.component';
import { LoginComponent } from './pages/login/login.component';
import { SignupComponent } from './pages/signup/signup.component';
import { PatientDashboardComponent } from './pages/patient-dashboard/patient-dashboard.component';
import { DoctorDashboardComponent } from './pages/doctor-dashboard/doctor-dashboard.component';
import { adminGuard, doctorGuard, patientGuard } from './services/auth.guard';
import { AppointmentComponentGlobal } from './pages/appointment/appointment.component';
import { UpdateProfileComponentPatient } from './components/patient/update-profile/update-profile.component';
import { UpcomingAppointmentsComponentPatient } from './components/patient/upcoming-appointments/upcoming-appointments.component';
import { CompletedAppointmentsComponentPatient } from './components/patient/completed-appointments/completed-appointments.component';
import { EditProfileComponentPatient } from './components/patient/edit-profile/edit-profile.component';
import { DoctorSlotBookingScreenComponent } from './pages/doctor-slot-booking-screen/doctor-slot-booking-screen.component';
import { PagenotfoundComponent } from './components/pagenotfound/pagenotfound.component';
import { PatientReviewsComponent } from './components/doctor/patient-reviews/patient-reviews.component';
import { UpcomingAppointmentsComponent } from './components/doctor/upcoming-appointments/upcoming-appointments.component';

const routes: Routes = [
  {
    path: '',
    component: HomeScreenComponent,
    pathMatch: 'full'
  },
  {
    path: 'login',
    component: LoginComponent,
    pathMatch: 'full'
  },
  {
    path: 'signup',
    component: SignupComponent,
    pathMatch: 'full'
  },

  {
    path: 'admin',
    component: AdminDashboardComponent,
    canActivate: [adminGuard],
    children: [
      {
        path: 'dashboard',
        component: AdminHomeComponent,

      },
      {
        path: 'add-doctor',
        component: AddDoctorComponent
      },
      {
        path: 'report',
        component: ReportComponent
      },
      {
        path: 'view-doctor',
        component: ViewDoctorComponent
      },
    ]
  },
  {
    path: 'patient',
    component: PatientDashboardComponent,
    canActivate: [patientGuard],
    children: [
      {
        path: 'bookAppointment',
        component: AppointmentComponentGlobal
      },
      {
        path: 'upcoming-appointments',
        component: UpcomingAppointmentsComponentPatient
      },
      {
        path: 'completed-appointments',
        component: CompletedAppointmentsComponentPatient
      }
    ]
  },
  {
    path: 'app-doctor-slot-booking-screen/:id',
    component: DoctorSlotBookingScreenComponent,
    canActivate: [patientGuard]
  },
  {
    path: 'update-profile',
    component: UpdateProfileComponentPatient,
    canActivate: [patientGuard]
  },
  {
    path: 'edit-profile',
    component: EditProfileComponentPatient,
    canActivate: [patientGuard]
  },
  {
    path: 'doctor',
    component: DoctorDashboardComponent,
    canActivate: [doctorGuard],
    children: [
      {
        path: 'upcoming-appointments',
        component: UpcomingAppointmentsComponent
      },
      {
        path: 'patient-reviews',
        component: PatientReviewsComponent
      }
    ]
  },
  {
    path: '**',
    component: PagenotfoundComponent,
    pathMatch: 'full'
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
