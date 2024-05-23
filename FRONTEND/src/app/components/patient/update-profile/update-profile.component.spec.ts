import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateProfileComponentPatient } from './update-profile.component';

describe('UpdateProfileComponentPatient', () => {
  let component: UpdateProfileComponentPatient;
  let fixture: ComponentFixture<UpdateProfileComponentPatient>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UpdateProfileComponentPatient]
    });
    fixture = TestBed.createComponent(UpdateProfileComponentPatient);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
