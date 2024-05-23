import { Component } from '@angular/core';
import Chart from 'chart.js/auto';
import { AdminService } from 'src/app/services/admin.service';

@Component({
  selector: 'app-donut-chart',
  templateUrl: './donut-chart.component.html',
  styleUrls: ['./donut-chart.component.css'],
})
export class DonutChartComponent {
  title = 'ng-chart';
  chart!: Chart<'doughnut', unknown[], string>;
  //chart: any = [];
  totalCount = 20;
  constructor(private adminService: AdminService) {}

  ngOnInit() {
    this.getDoctorSpecializationCounts();
  }

  getDoctorSpecializationCounts() {
    this.adminService.getDoctorSpecializationCounts().subscribe(
      (data: any) => {
        // console.log('Specialization Counts:', data);
        this.renderChart(data);
      },
      (error) => {
        console.error('Error fetching specialization counts:', error);
      }
    );
  }

  renderChart(data: any) {
    const labels = Object.keys(data);
    const chartData = Object.values(data);

    // Generate random colors
    const randomColors = this.generateRandomColors(labels.length);

    this.chart = new Chart('canvas', {
      type: 'doughnut',
      data: {
        labels: labels,
        datasets: [
          {
            label: 'Number of Doctors',
            data: chartData,
            backgroundColor: randomColors, // Assign random colors here
            borderWidth: 5,
          },
        ],
      },
      options: {
        responsive: false,
        cutout: 80,
        plugins: {
          title: {
            display: true,
            text: 'Specialization Wise Distribution',
            position: 'bottom',
            font: {
              size: 18,
              weight: 'bold',
            },
          },
        },
        elements: {
          arc: {
            borderRadius: 8,
          },
        },
      },
    });
  }

  generateRandomColors(count: number): string[] {
    const randomColors = [];
    for (let i = 0; i < count; i++) {
      // Generate random color in hexadecimal format
      const color = '#' + Math.floor(Math.random() * 16777215).toString(16);
      randomColors.push(color);
    }
    return randomColors;
  }
}
