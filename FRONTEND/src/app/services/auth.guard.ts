import { CanActivateFn, Router } from '@angular/router';
import { LoginService } from './login.service';
import { inject } from '@angular/core';

export const adminGuard: CanActivateFn = (route, state) => {

  const loginService = inject(LoginService);
  const router = inject(Router);
  
  if(loginService.isLoggedIn() && loginService.getUserRole() == "ROLE_ADMIN") 
  {
    return true;
  }

router.navigate(['/login'])

return false;
};

export const doctorGuard: CanActivateFn = (route, state) => {

  const loginService = inject(LoginService);
  const router = inject(Router);
  
  if(loginService.isLoggedIn() && loginService.getUserRole() == "ROLE_DOCTOR") 
  {
    return true;
  }

router.navigate(['/login'])

return false;

};

export const patientGuard: CanActivateFn = (route, state) => {
  const loginService = inject(LoginService);
  const router = inject(Router);
  
  if(loginService.isLoggedIn() && (loginService.getUserRole() == "ROLE_PATIENT") )
  {
    return true;
  }

router.navigate(['/login'])

return false;
};
