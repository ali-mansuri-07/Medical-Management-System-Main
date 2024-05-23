import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Doctor } from '../models/doctor.model';
import { baseUrl } from './helper';

@Injectable({
  providedIn: 'root'
})
export class DoctorService {

  constructor(private httpClient: HttpClient) { }

  api = "http://localhost:8083"

  public saveDoctor(doctor: Doctor): Observable<Doctor> {
    return this.httpClient.post<Doctor>(`${this.api}/admin/add-doctor`, doctor);
  }
  
  public getFeedback(id : number){
    return this.httpClient.get(`${baseUrl}/search/feedback?doctorId=${id}`);
  }

}