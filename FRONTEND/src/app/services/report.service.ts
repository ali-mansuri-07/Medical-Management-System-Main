import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReportService {
  private baseUrl = 'http://localhost:8083/admin'; 

  constructor(private http: HttpClient) {}

  fetchAppointmentStatusDistribution(start: string, end: string): Observable<HttpResponse<Object>> {
    
    const url = `${this.baseUrl}/getAppointmentStatusDistribution?startDate=${start}&endDate=${end}`;
    return this.http.get<Object>(url, { observe: 'response' });
  }

  fetchAppointmentStatusDistributionWithSpecializations(start: string, end: string): Observable<HttpResponse<Object>> {
    
    const url = `${this.baseUrl}/getAppointmentStatusDistributionWithSpecialization?startDate=${start}&endDate=${end}`;
    return this.http.get<Object>(url, { observe: 'response' });
  }

  fetchTopTimeSlotsWithHighestNumberOfAppointments(start: string, end: string): Observable<HttpResponse<Object>> {
    
    const url = `${this.baseUrl}/getTopTimeSlotsWithHighestNumberOfAppointments?startDate=${start}&endDate=${end}`;
    return this.http.get<Object>(url, { observe: 'response' });
  }

  fetchTopDepartmentAndDoctor(start: string, end: string): Observable<HttpResponse<Object>> {
    
    const url = `${this.baseUrl}/getTopDepartmentAndDoctor?startDate=${start}&endDate=${end}`;
    return this.http.get<Object>(url, { observe: 'response' });
  }

  fetchPatientDistribution(start: string, end: string): Observable<HttpResponse<Object>> {
    
    const url = `${this.baseUrl}/getPatientDistribution?startDate=${start}&endDate=${end}`;
    return this.http.get<Object>(url, { observe: 'response' });
  }

  fetchDoctorDistribution(start: string, end: string): Observable<HttpResponse<Object>> {
    
    const url = `${this.baseUrl}/getDoctorDistribution?startDate=${start}&endDate=${end}`;
    return this.http.get<Object>(url, { observe: 'response' });
  }
}
