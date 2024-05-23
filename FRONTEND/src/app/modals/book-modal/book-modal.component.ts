import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatDialogModule } from '@angular/material/dialog';

@Component({
  selector: 'app-book-modal',
  templateUrl: './book-modal.component.html',
  styleUrls: ['./book-modal.component.css']
})
export class BookModalComponent {
  
  constructor(
    public dialogRef: MatDialogRef<BookModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) { }
  closeModal(commit : boolean): void {
    // console.log(commit);
    this.dialogRef.close({ result: commit });
  }

  closeSuccessModal(commit : boolean): void {
    window.location.reload();
    this.dialogRef.close({ result: commit });
  }
}
