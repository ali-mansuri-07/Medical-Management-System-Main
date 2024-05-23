import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserConfirmationComponent } from './user-confirmation.component';

describe('UserConfirmationComponent', () => {
  let component: UserConfirmationComponent;
  let fixture: ComponentFixture<UserConfirmationComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UserConfirmationComponent]
    });
    fixture = TestBed.createComponent(UserConfirmationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
