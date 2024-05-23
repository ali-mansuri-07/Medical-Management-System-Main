import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpcomingAppointmentsComponentPatient } from './upcoming-appointments.component';

describe('UpcomingAppointmentsComponentPatient', () => {
  let component: UpcomingAppointmentsComponentPatient;
  let fixture: ComponentFixture<UpcomingAppointmentsComponentPatient>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UpcomingAppointmentsComponentPatient]
    });
    fixture = TestBed.createComponent(UpcomingAppointmentsComponentPatient);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
