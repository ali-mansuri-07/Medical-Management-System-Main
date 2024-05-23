import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditProfileComponentPatient } from './edit-profile.component';

describe('EditProfileComponentPatient', () => {
  let component: EditProfileComponentPatient;
  let fixture: ComponentFixture<EditProfileComponentPatient>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EditProfileComponentPatient]
    });
    fixture = TestBed.createComponent(EditProfileComponentPatient);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
