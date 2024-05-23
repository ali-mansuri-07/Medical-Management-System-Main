import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PatientReviewsComponent } from './patient-reviews.component';

describe('PatientReviewsComponent', () => {
  let component: PatientReviewsComponent;
  let fixture: ComponentFixture<PatientReviewsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PatientReviewsComponent]
    });
    fixture = TestBed.createComponent(PatientReviewsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
