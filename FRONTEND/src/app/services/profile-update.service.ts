import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProfileUpdateService {

  constructor() { }

  public profileUpdatedSource = new Subject<void>();

  public profileUpdated$ = this.profileUpdatedSource.asObservable();

  public emitProfileUpdated(): void {
    this.profileUpdatedSource.next();
  }

  public publish(data: any){
    this.profileUpdatedSource.next(data);
  }
}
