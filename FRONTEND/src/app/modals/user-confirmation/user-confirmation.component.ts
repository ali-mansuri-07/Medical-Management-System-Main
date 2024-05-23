import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';

@Component({
  selector: 'app-user-confirmation',
  templateUrl: './user-confirmation.component.html',
  styleUrls: ['./user-confirmation.component.css']
})
export class UserConfirmationComponent {

  constructor(
    public dialogRef: MatDialogRef<UserConfirmationComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private router: Router
  ) { }
  closeModal(commit : boolean): void {
    // console.log(commit);
    this.dialogRef.close({ result: commit });
  }
  closeModalForLogin(commit : boolean): void {
    // console.log(commit);
    this.router.navigate(['/login']);
    this.dialogRef.close({ result: commit });
  }

}
