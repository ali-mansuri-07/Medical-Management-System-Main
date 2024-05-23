import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-cancel-modal',
  templateUrl: './cancel-modal.component.html',
  styleUrls: ['./cancel-modal.component.css']
})
export class CancelModalComponent {
  constructor(
    public dialogRef: MatDialogRef<CancelModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) { }
  closeModal(commit : boolean): void {
    // console.log(commit);
    this.dialogRef.close({ result: commit });
  }
}
