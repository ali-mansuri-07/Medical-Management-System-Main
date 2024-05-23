import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { baseUrl } from './helper';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { Token } from '@angular/compiler';
import { JsonPipe } from '@angular/common';


@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor(private http: HttpClient, private router: Router) { }

  isLoggedIn(){
    let tokenStr = localStorage.getItem('token')
     if(tokenStr==''||tokenStr==undefined||tokenStr===null)
     {
       return false;
     }
     else{
       return true;
     }
  }

  loginUser(loginData: any){

    localStorage.clear();
    return this.http.post(`${baseUrl}/auth/login`, loginData);
  }

  getCurrentUser(){
    return this.http.get(`${baseUrl}/current-user`);
  } 

  refreshToken(refToken:any){
    return this.http.post(`${baseUrl}/auth/refresh`, refToken);
  }

  setToken(token: any) {
    localStorage.setItem('token', token);
  }

  setUserId(userid: any){
    localStorage.setItem('userId',userid);
  }

  getUserId() {
    return localStorage.getItem('userId');
  }

  setUserTypeId(userTypeid: any){
    localStorage.setItem('userTypeId',userTypeid);
  }

  getUserTypeId() {
    return localStorage.getItem('userTypeId');
  }

  getToken() {
    return localStorage.getItem('token')
  }

  setRefreshToken(refreshToken: any){
    return localStorage.setItem('refreshToken',refreshToken);
  }

  getRefreshToken(){
    return localStorage.getItem('refreshToken');
  }
  // setUser(user: any) {
  //   localStorage.setItem('user', JSON.stringify(user));
  // }

  setUser(username: any) {
    localStorage.setItem('username', username);
  }

  setDoctor(doctorid: any){
    localStorage.setItem('doctorId',doctorid);
  }

  getDoctor(){
    return localStorage.getItem('doctorId');
  }
  getUser() {
    localStorage?.getItem("username");
  }

  logout(){
    localStorage.clear();
    this.router.navigate(['']);
    if (!this.router.getCurrentNavigation()) {
      window.location.reload();
    }
  }

  public setUserRole(userRole: string){
    localStorage.setItem('userRole', userRole);
  }
  public getUserRole() {
    return localStorage.getItem('userRole');
  }

  setPatientId(userTypeId:any){
    localStorage.setItem('patientId',userTypeId);
  }

  getPatientId(){
    return localStorage.getItem('patientId');
  }
}

