import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { baseUrl } from './helper';
import { Doctor } from '../models/doctor.model';
import { specializationDTO } from '../components/admin/models/specializationDTO';
import { DashboardDataDTO } from '../components/admin/models/dashboardDataDTO';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  constructor(private http: HttpClient) { }

  getDoctorData(page: any) {
    return this.http.get(`${baseUrl}/admin/get-doctors?page=${page}`);
  }
  getParticularDoctorData(doctorId: number) {
    return this.http.get(`${baseUrl}/admin/get-particular-doctor?doctorId=${doctorId}`);
  }

  addDoctor(doctor: Doctor) {
    return this.http.post(`${baseUrl}/admin/add-doctor`, doctor);
  }

  updateDoctor(doctor: Doctor) {
    return this.http.put(`${baseUrl}/admin/update-doctor/${doctor.id}`, doctor);
  }

  searchDoctor(searchQuery: string, page: number, pageSize: number) {
    return this.http.get(`${baseUrl}/search/searchDoctors?query=${searchQuery}&page=${page}&size=${pageSize}`)
  }

  filterByStatus(filterQuery: string, page: number, pageSize: number) {
    return this.http.get(`${baseUrl}/admin/get-doctors-by-status?status=${filterQuery}&page=${page}&size=${pageSize}`)
  }

  searchAndFilter(searchQuery: string, filterQuery: string, page: number, pageSize: number) {
    return this.http.get(`${baseUrl}/search/searchAndFilter?searchQuery=${searchQuery}&status=${filterQuery}&page=${page}&size=${pageSize}`)
  }

  saveSpecialization(specialization: specializationDTO)
  {
    return this.http.post(`${baseUrl}/admin/add-specialization`, specialization);
  }
  
  getDoctorSpecializationCounts(): Observable<any> {
    return this.http.get(`${baseUrl}/admin/doctor-specialization-counts`);
  }

  getDashboardData():Observable<any>{
    return this.http.get(`${baseUrl}/admin/get-dashboard-data`)
  }
}
