import { Component } from '@angular/core';
import { NgToastService } from 'ng-angular-popup';
import { LoginService } from 'src/app/services/login.service';
import { AppearanceAnimation, ConfirmBoxEvokeService, ConfirmBoxInitializer, DialogLayoutDisplay, DisappearanceAnimation } from '@costlydeveloper/ngx-awesome-popup';
@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {

  constructor(public loginService: LoginService,
    private toast: NgToastService,
    private confirmLogout: ConfirmBoxEvokeService){
  }

  currentUser: any = localStorage?.getItem('username');

  confirm(){
    const newConfirmBox = new ConfirmBoxInitializer();

    newConfirmBox.setTitle('Logout');
    newConfirmBox.setMessage('Are you sure you want to logout?');


    newConfirmBox.setConfig({
    layoutType: DialogLayoutDisplay.WARNING, 
    animationIn: AppearanceAnimation.BOUNCE_IN, 
    animationOut: DisappearanceAnimation.BOUNCE_OUT, 
    buttonPosition: 'center',
    height: '200px', 
    width: '400px', 
    });

    newConfirmBox.setButtonLabels('Yes', 'No');
    newConfirmBox.openConfirmBox$().subscribe(resp => {
      if(resp.clickedButtonID == "yes"){
      this.logout();
      }
    });
  }

  logout(){
    this.loginService.logout();
    this.toast.success({detail:"Log out successfull",summary:'success',duration:3000});
  }
}
