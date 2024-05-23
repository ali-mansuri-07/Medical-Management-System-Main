import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse
} from '@angular/common/http';
import { Observable, catchError, switchMap, throwError } from 'rxjs';
import { LoginService } from './login.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private loginService: LoginService) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let authReq = request;
    const token = this.loginService.getToken();
    const excludedUrls = ['/auth/**'];

    const isExcluded = excludedUrls.some(url => request.url.includes(url));
    
    if (isExcluded) {
      return next.handle(request);
    }

    if (token) {
      authReq = this.addTokenHeader(request, token);
    }

    
    return next.handle(authReq).pipe(
      catchError(error => {
        if (error instanceof HttpErrorResponse && error.status === 666) {
          
          return this.handle401Error(request, next);
        }
        if (error instanceof HttpErrorResponse && error.status === 612) {
          // console.log("ERROR IN DOCTOR HELP PLEASE")
          return this.handle621Error(request, next);
        }
        
        return throwError(() => error);
      })
    );
  }
  private handle621Error(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    
      this.loginService.logout(); // Logout user if no refresh token available
      return throwError(() => 'Session expired'); // Redirect to login or handle as needed
    }
  
  private addTokenHeader(request: HttpRequest<any>, token: string): HttpRequest<any> {
    return request.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }

  private handle401Error(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const refreshToken = this.loginService.getRefreshToken();
    
    if (refreshToken!=null) {
      return this.loginService.refreshToken(refreshToken).pipe(
    
        switchMap((response: any) => {
          // console.log(response)
          this.loginService.setToken(response.cookies.token);
          let authReqRepeat = this.addTokenHeader(request, response.cookies.token);
          return next.handle(authReqRepeat);
        }),
        catchError((err:any) => {
          this.loginService.logout(); // Logout user if refresh token fails
          // console.log(err);
          
          return throwError(() => 'Session expired!'); // Redirect to login or handle as needed
        })
      );
    } else {
      this.loginService.logout(); // Logout user if no refresh token available
      return throwError(() => 'Session expired'); // Redirect to login or handle as needed
    }
  }
}

