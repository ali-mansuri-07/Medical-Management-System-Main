import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { NgxPaginationModule } from 'ngx-pagination';

import { CommonModule, DatePipe } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule } from '@angular/router';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AddDoctorComponent } from './components/admin/add-doctor/add-doctor.component';
import { AdminHomeComponent } from './components/admin/admin-home/admin-home.component';
import { ReportComponent } from './components/admin/report/report.component';
import { SidebarComponent } from './components/admin/sidebar/sidebar.component';
import { ViewDoctorComponent } from './components/admin/view-doctor/view-doctor.component';
import { ProfileComponentPatient } from './components/patient/profile/profile.component';
import { AdminDashboardComponent } from './pages/admin-dashboard/admin-dashboard.component';
import { HomeScreenComponent } from './pages/home-screen/home-screen.component';
import { PatientDashboardComponent } from './pages/patient-dashboard/patient-dashboard.component';
import { FooterComponent } from './shared-lib/footer/footer.component';
import { NavbarComponent } from './shared-lib/navbar/navbar.component';

import { MatTooltipModule } from '@angular/material/tooltip';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';

import { NgxUiLoaderModule } from "ngx-ui-loader";
import { DoctorDashboardComponent } from './pages/doctor-dashboard/doctor-dashboard.component';
import { SignupComponent } from './pages/signup/signup.component';
import { AuthInterceptor } from './services/auth.interceptor';

import { ProfileComponent } from './components/doctor/profile/profile.component';
import { UpcomingAppointmentsComponent } from './components/doctor/upcoming-appointments/upcoming-appointments.component';

import { NgxUiLoaderConfig, NgxUiLoaderRouterModule, PB_DIRECTION, POSITION, SPINNER } from "ngx-ui-loader";
import { CarouselComponent } from './components/home/carousel/carousel.component';

import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NgToastModule } from 'ng-angular-popup';
import { CarouselModule } from 'ngx-owl-carousel-o';
import { AppointmentComponentGlobal } from './pages/appointment/appointment.component';
import { LoginComponent } from './pages/login/login.component';
import { Screen1Component } from './shared-lib/screen1/screen1.component';


const ngxUiLoaderConfig: NgxUiLoaderConfig = {
  bgsColor: '#ffffff', // Background color
  bgsOpacity: 0.5, // Background opacity
  bgsPosition: POSITION.bottomRight, // Background position
  bgsSize: 60, // Background size
  fgsType: SPINNER.ballSpin, // Foreground spinner type
  fgsPosition: POSITION.bottomRight,
  pbColor: '#000000', // Progress bar color
  pbDirection: PB_DIRECTION.leftToRight, // Progress bar direction
  pbThickness: 5, // Progress bar thickness
  fgsColor: '#000000' // Foreground spinner color
};

import { CompletedAppointmentsComponentPatient } from './components/patient/completed-appointments/completed-appointments.component';
import { UpcomingAppointmentsComponentPatient } from './components/patient/upcoming-appointments/upcoming-appointments.component';
import { UpdateProfileComponentPatient } from './components/patient/update-profile/update-profile.component';

import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatDialogModule } from '@angular/material/dialog';
import * as pdfMake from 'pdfmake/build/pdfmake';
import * as pdfFonts from 'pdfmake/build/vfs_fonts';
import { EditProfileComponentPatient } from './components/patient/edit-profile/edit-profile.component';
import { BookModalComponent } from './modals/book-modal/book-modal.component';
import { CancelModalComponent } from './modals/cancel-modal/cancel-modal.component';
import { UserConfirmationComponent } from './modals/user-confirmation/user-confirmation.component';
import { DoctorSlotBookingScreenComponent } from './pages/doctor-slot-booking-screen/doctor-slot-booking-screen.component';

import { DonutChartComponent } from './components/admin/donut-chart/donut-chart.component';
import { PatientReviewsComponent } from './components/doctor/patient-reviews/patient-reviews.component';
import { PagenotfoundComponent } from './components/pagenotfound/pagenotfound.component';
import {
  NgxAwesomePopupModule,
  DialogConfigModule,
  ConfirmBoxConfigModule,
  ToastNotificationConfigModule
} from '@costlydeveloper/ngx-awesome-popup';
(pdfMake as any).vfs = pdfFonts.pdfMake.vfs;

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    FooterComponent,
    Screen1Component,
    HomeScreenComponent,
    AdminDashboardComponent,
    SidebarComponent,
    ReportComponent,
    ViewDoctorComponent,
    AddDoctorComponent,
    AdminHomeComponent,
    ProfileComponentPatient,
    LoginComponent,
    SignupComponent,
    PatientDashboardComponent,
    DoctorDashboardComponent,
    AppointmentComponentGlobal,
    UpcomingAppointmentsComponent,
    DoctorSlotBookingScreenComponent,
    BookModalComponent,
    ProfileComponent,
    CarouselComponent,
    UpdateProfileComponentPatient,
    UpcomingAppointmentsComponentPatient,
    CompletedAppointmentsComponentPatient,
    EditProfileComponentPatient,
    UserConfirmationComponent,
    CancelModalComponent,
    DonutChartComponent,
    PagenotfoundComponent,
    PatientReviewsComponent,
  ],
  imports: [
    MatTooltipModule,
    FormsModule,
    ReactiveFormsModule,
    CommonModule,
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    RouterModule,
    HttpClientModule,
    MatDatepickerModule,
    NgxPaginationModule,
    NgxUiLoaderModule,
    NgToastModule,
    MatDialogModule,
    // MatRadioButton,
    NgxPaginationModule,
    BrowserAnimationsModule,
    CarouselModule,
    NgxAwesomePopupModule.forRoot({
      colorList: {
        success: '#3caea3', // optional
        info: '#2f8ee5', // optional
        warning: '#198754', // optional
        danger: '#e46464', // optional
        customOne: '#3ebb1a', // optional
        customTwo: '#bd47fa', // optional (up to custom five)
      },
    }),
    ConfirmBoxConfigModule.forRoot(),
    ConfirmBoxConfigModule,
    NgxUiLoaderRouterModule.forRoot({
      loaderId: "loader-01"
    }),
    NgbModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    },
    DatePipe
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
