import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PatientAppointmentsService {
  private baseUrl = 'http://localhost:8083';

  constructor(private http: HttpClient) { }

  searchAppointments(patientId: any, offset: number, pageSize: number, appStatus: string, sortBy: string): Observable<any> {
    const requestBody = {
      patientId: patientId,
      offset: offset,
      pageSize: pageSize,
      appointmentStatus: appStatus,
      sortBy: sortBy

    };
    return this.http.post<any>(`${this.baseUrl}/search/searchAppointments`, requestBody);
  }

  getFeedbackByPatientId(patientId: any): Observable<any> {
    const params = new HttpParams().set('id', patientId);
    
    return this.http.get<any>(`${this.baseUrl}/patient/fetch-feedback`, { params });
  }

  cancelAppointment(id: number): Observable<any> {
    return this.http.put<any>(`${this.baseUrl}/appointment/cancel/${id}`, null);
  }
}
