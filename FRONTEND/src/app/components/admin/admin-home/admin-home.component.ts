import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { AdminService } from 'src/app/services/admin.service';
import { DashboardDataDTO } from '../models/dashboardDataDTO';
import { HttpBackend, HttpErrorResponse } from '@angular/common/http';
import { Chart, ChartOptions } from 'chart.js';

@Component({
  selector: 'app-admin-home',
  templateUrl: './admin-home.component.html',
  styleUrls: ['./admin-home.component.css']
})
export class AdminHomeComponent implements OnInit {

  @ViewChild('lineChart') private chartRef!: ElementRef;
  chart!: Chart;

  dashboardData!: DashboardDataDTO;
  constructor(private adminService: AdminService) { }
  ngOnInit(): void {
    this.getDashboardData();
  }

  getDashboardData() {
    this.adminService.getDashboardData().subscribe({
      next: (data: DashboardDataDTO) => {
        this.dashboardData = data;
        // console.log(this.dashboardData)
        this.createLineChart();
      },
      error: (err: HttpErrorResponse) => {
        // console.log("error", err);
      }
    })
  }

  createLineChart(): void {
    const chartData = {
      labels: Object.keys(this.dashboardData.dailyCompletedAppointments),

      datasets: [{
        label: 'Patient Visit Data For Last 7 Days',
        data: Object.values(this.dashboardData.dailyCompletedAppointments),
        backgroundColor: '#41BD81',
        fill: false,
      }]
    };

    const chartOptions: ChartOptions = {
      responsive: false,
      scales: {
        y: {
          title: {
            display: true,
            text: 'Number of Appointments',
            font: {
              size: 14,
            },
            padding: { bottom: 15 }
          },
          beginAtZero: true,
          grid: {
            display: true,
            color: 'rgba(0, 0, 0, 0.05)',
          },
          ticks: { 
            display: true,
            color: 'black',
          }
        },
        x: {
          title: {
            display: true,
            text: 'Days ',
            font: {
              size: 14,
            },
            padding: { top: 15 }
          },
          grid: {
            display: false,
            color: 'rgba(0, 0, 0, 0.05)',
          },
          ticks: { 
            display: true,
            color: 'black',
          }
        }
      },
      elements: {
      }
    };

    this.chart = new Chart(this.chartRef.nativeElement, {
      type: 'bar',
      data: chartData,
      options: chartOptions
    });
  }
}
