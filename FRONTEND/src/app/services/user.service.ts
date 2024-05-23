import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { baseUrl } from './helper';
// import {baseUrl} from helper.ts

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  getSpecializations(){
    return this.http.get(`${baseUrl}/search/specializations`);
  }

  searchDoctors(searchQuery: string, page: number, sort: string) {
    // console.log(searchQuery + " " + sort)
    return this.http.get(`${baseUrl}/search/doctors?query=${searchQuery}&page=${page}&sort=${sort}`);
  }

  viewDoctors(specialization: string, page: any) {
    return this.http.get(`${baseUrl}/search/doctorsBySpecialization?specialization=${specialization}&page=${page}`);
  }
}