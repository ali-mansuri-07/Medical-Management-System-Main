import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { baseUrl } from './helper';

@Injectable({
  providedIn: 'root',
})
export class DoctorProfileService {
  constructor(private http: HttpClient) {}

  getDoctorById(id: number) {
    const data = this.http.get(`${baseUrl}/search/doctorById`, {
      params: {
        id
      }
    });
    return data;
  }
}
