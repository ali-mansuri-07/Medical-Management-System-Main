import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DoctorSlotBookingScreenComponent } from './doctor-slot-booking-screen.component';

describe('DoctorSlotBookingScreenComponent', () => {
  let component: DoctorSlotBookingScreenComponent;
  let fixture: ComponentFixture<DoctorSlotBookingScreenComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DoctorSlotBookingScreenComponent]
    });
    fixture = TestBed.createComponent(DoctorSlotBookingScreenComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
