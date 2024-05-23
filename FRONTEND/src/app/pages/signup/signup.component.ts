import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { NgToastService } from 'ng-angular-popup';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { SignupService } from 'src/app/services/signup.service';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent {

  form:FormGroup
  passwordPattern = /^(?=[^A-Z]*[A-Z])(?=[^a-z]*[a-z])(?=\D*\d).{8,}$/;

  constructor(private signupService: SignupService,
    private router: Router,
    private ngxLoader: NgxUiLoaderService,
    private toast: NgToastService){

    this.form = new FormGroup({
      userName: new FormControl('', [Validators.required, Validators.minLength(3), Validators.maxLength(30)]),
      password: new FormControl('', [Validators.required, Validators.pattern(this.passwordPattern)]),
      confirmPassword: new FormControl('', [Validators.required, Validators.minLength(3)])
    },
     { validators: this.passwordMatchValidator }
    );
  }

  passwordMatchValidator(control: AbstractControl){
    return control.get('password')?.value === control.get('confirmPassword')?.value? null : {mismatch: true};
  }

  signup(){

    if (this.form.valid) {
      const signupData = {
        userName: this.form.value.userName,
        password: this.form.value.password,
        role: "PATIENT"
      };
    // // console.log(this.signupData);
    this.signupService.registerUser(signupData).subscribe({
      next: (data:any) => {
        // console.log(data);
        this.router.navigate(['/login']);
        this.ngxLoader.stopLoader('master');
       this.toast.success({detail:"Sign Up successful",summary:'Success',duration:5000});
      },
      error: (err) => {
        // console.log("something went wrong", err);
        this.ngxLoader.stopLoader('master');
        if(err.status == 409){
          this.toast.error({detail:"ERROR",summary:'Username already exists',duration:5000});
        }else{
          this.toast.error({detail:"ERROR",summary:'Something went wrong',duration:5000});
        }
        
      }
    })
  }
}
}
