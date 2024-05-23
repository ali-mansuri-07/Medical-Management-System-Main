import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfileComponentPatient } from './profile.component';

describe('ProfileComponentPatient', () => {
  let component: ProfileComponentPatient;
  let fixture: ComponentFixture<ProfileComponentPatient>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProfileComponentPatient]
    });
    fixture = TestBed.createComponent(ProfileComponentPatient);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
