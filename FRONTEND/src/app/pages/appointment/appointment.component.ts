import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { CarouselModule, OwlOptions } from 'ngx-owl-carousel-o';

@Component({
  selector: 'app-appointment',
  templateUrl: './appointment.component.html',
  styleUrls: ['./appointment.component.css']
})
export class AppointmentComponentGlobal implements OnInit{

  constructor(private datePipe: DatePipe){}

  dates: Date[] = [];
  currentDate = new Date();
  slotsData: number[] = new Array(24);
  // isoDates: string[] = this.dates.map(date => date.toISOString());
  convertedDates: string[] = [];

 customOptions: OwlOptions = {
    loop: false,
    mouseDrag: true,
    touchDrag: true,
    pullDrag: true,
    dots: false,
    navSpeed: 700,
    navText: ['<i class="bi bi-caret-left-fill"></i>', '<i class="bi bi-caret-right-fill"></i>'],
    responsive: {
      0: {
        items: 1
      },
      400: {
        items: 2
      },
      740: {
        items: 3
      },
      940: {
        items: 4
      }
    },
    nav: true
  }

  ngOnInit(): void {
    this.generateDates();
    this.formatDates();
  }

  generateDates() {
    this.dates = [];
    for (let i = 0; i < 15; i++) {
      const date = new Date();
      date.setDate(this.currentDate.getDate() + i);
      this.dates.push(date);
    }
  }

  getAppointmentData(){

  }

 book(slot: number){
  // console.log(this.convertedDates);
 }
  
 formatDates(){
  this.convertedDates = this.dates.map(date => this.datePipe.transform(date, 'yyyy-MM-dd') || '');
 }
}
