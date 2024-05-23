import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CompletedAppointmentsComponentPatient } from './completed-appointments.component';

describe('CompletedAppointmentsComponentPatient', () => {
  let component: CompletedAppointmentsComponentPatient;
  let fixture: ComponentFixture<CompletedAppointmentsComponentPatient>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CompletedAppointmentsComponentPatient]
    });
    fixture = TestBed.createComponent(CompletedAppointmentsComponentPatient);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
