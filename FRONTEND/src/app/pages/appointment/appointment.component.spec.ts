import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AppointmentComponentGlobal } from './appointment.component';

describe('AppointmentComponentGlobal', () => {
  let component: AppointmentComponentGlobal;
  let fixture: ComponentFixture<AppointmentComponentGlobal>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AppointmentComponentGlobal]
    });
    fixture = TestBed.createComponent(AppointmentComponentGlobal);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
