import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PatientService {

  private baseUrl = 'http://localhost:8083/patient';

  constructor(private http: HttpClient) { }

  addPatient(patientData: any, userId: number): Observable<any> {
    // Add userId to the patientData object
    const patientDataWithUserId = {
      ...patientData,
      userId: userId
    };

    return this.http.post<any>(`${this.baseUrl}/add`, patientDataWithUserId).pipe(
      catchError(this.handleError)
    );
  }

  getPatient(id: number): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/view/${id}`).pipe(
      catchError(this.handleError)
    );
  }


  updatePatient(id: number, patientData: any): Observable<any> {
    const url = `${this.baseUrl}/update/${id}`;
    return this.http.put(url, patientData);
  }

  submitFeedback(feedbackData: any): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/give-feedback`, feedbackData);
  }

  private handleError(error: any): Promise<any> {
    console.error('An error occurred', error);
    return Promise.reject(error.message || error);
  }
}
