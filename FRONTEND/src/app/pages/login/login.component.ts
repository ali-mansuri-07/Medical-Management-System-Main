import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgToastComponent, NgToastService } from 'ng-angular-popup';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { LoginService } from 'src/app/services/login.service';
// import { LocalStorageService } from 'angular-web-storage';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {
  loginData = {
    userName: '',
    password: '',
  };

  constructor(private loginService: LoginService, 
    private router: Router,
    private ngxLoader: NgxUiLoaderService,
    private toast: NgToastService
    ) {
  }

  login() {
    this.ngxLoader.startLoader("master");
    this.loginService.loginUser(this.loginData).subscribe({
      next: (data: any) => {
        this.loginService.setToken(data.cookies.token);
        this.loginService.setRefreshToken(data.cookies.refreshToken);
        // console.log(data);
        this.loginService.setUserId(data.cookies.userIdentificationDTO.userId);
        this.loginService.setUserTypeId(data.cookies.userIdentificationDTO.userTypeId);
        this.loginService.setUserRole(data.cookies.userIdentificationDTO.userType);
        if (data.cookies.userIdentificationDTO.userTypeId && data.cookies.userIdentificationDTO.userType == 'ROLE_PATIENT') {
          this.loginService.setPatientId(data.cookies.userIdentificationDTO.userTypeId);
        }
        this.loginService.getCurrentUser().subscribe({
          next: (userData: any) => {
            // console.log(userData);
            this.loginService.setUser(userData.currentUser);
            let role = this.loginService.getUserRole();
            if (role == 'ROLE_ADMIN') {
              this.router.navigate(['admin/dashboard']);
              this.toast.success({detail:"Login successful",summary:'Success',duration:3000});
            } else if (role == 'ROLE_PATIENT') {
              this.router.navigate(['patient']);
              this.toast.success({detail:"Login successful",summary:'Success',duration:3000});
            } else if (role == 'ROLE_DOCTOR') {
              this.loginService.setDoctor(data.cookies.userIdentificationDTO.userTypeId);
              this.router.navigate(['doctor/upcoming-appointments']);
              this.toast.success({detail:"Login successful",summary:'Success',duration:3000});
            }
            this.ngxLoader.stopLoader("master");
          },
          error: (err) => {
            // console.log(err);
            this.toast.error({detail:"ERROR",summary:'Error in fetching details',duration:3000});
          }
        });
      },
      error: (err) => {
        // console.log(err)
        // console.log(err.error.message)
        if(err.error.message === 'Invalid Username or Password !!' || err.error.message === ' Invalid Username or Password  !!'  ){
          this.toast.error({detail:"ERROR",summary:'Invalid credentials',duration:3000});
        }
        else if(err.error.message === 'You are not authorized to login'){
          this.toast.error({detail:"Invalid credentials",summary:'You are not authorized to login!',duration:3000});
        }
        
        else{
          this.toast.error({detail:"ERROR",summary:'Something went wrong',duration:3000});
        }
       
        this.ngxLoader.stopLoader("master");
      }
    });
  }
}
