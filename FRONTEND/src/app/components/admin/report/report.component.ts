import * as pdfMake from 'pdfmake/build/pdfmake';
import * as pdfFonts from 'pdfmake/build/vfs_fonts';
(pdfMake as any).vfs = pdfFonts.pdfMake.vfs;
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ReportService } from 'src/app/services/report.service';
import { AppointmentStatusDistribution } from '../models/appointment-status-distribution.model';
import { AppointmentStatusDistributionWithSpecialization } from '../models/appointment-status-distribution-with-specialization.model';
import { TopDepartmentAndDoctor } from '../models/TopDepartmentAndDoctor';
import { TopTimeSlotsWithHighestNumberOfAppointments } from '../models/TopTimeSlotsWithHighestNumberOfAppointments';
import { PatientDistribution } from '../models/PatientDistribution';
import { DoctorDistribution } from '../models/DoctorDistribution';

@Component({
  selector: 'app-report',
  templateUrl: './report.component.html',
  styleUrls: ['./report.component.css']
})
export class ReportComponent implements OnInit {
  reportForm!: FormGroup;
  responseData: any;
  download_status: string = "null";

  appointmentStatusList: AppointmentStatusDistribution[] = [];
  appointmentStatusListWithSpecialization: AppointmentStatusDistributionWithSpecialization[] = [];
  topDepartmentAndDoctor: TopDepartmentAndDoctor[] = [];
  topTimeSlotsWithHighestNumberOfAppointments: TopTimeSlotsWithHighestNumberOfAppointments[] = [];
  patientDistribution: PatientDistribution[] = [];
  doctorDistribution: DoctorDistribution[] = [];
  filename!: string;

  constructor(private formBuilder: FormBuilder, private reportService: ReportService) { }

  ngOnInit() {
    this.reportForm = this.formBuilder.group({
      start: ['', Validators.required],
      end: ['', Validators.required],
    });
  }

  async submitForm() {
    if (this.reportForm.valid) {

      const formData = this.reportForm.value;
      // console.log('Start Date :', formData.start);
      // console.log('End Date :', formData.end);

      // Check if the start date is greater than the end date
      if (formData.start >= formData.end) {
        // If the start date is greater than or equal to the end date, display an error message
        // console.log('Error: Start date must be less than end date');

        this.download_status = "invalid";
        await new Promise(r => setTimeout(r, 2000));
        this.download_status = "null";

        return;
      }

      this.download_status = "start";

      this.reportService.fetchAppointmentStatusDistribution(formData.start, formData.end).subscribe(
        (response) => {
          this.responseData = response;

          this.appointmentStatusList = this.responseData.body.cookies;

          this.reportService.fetchAppointmentStatusDistributionWithSpecializations(formData.start, formData.end).subscribe(
            (response) => {
              this.responseData = response;

              this.appointmentStatusListWithSpecialization = this.responseData.body.cookies;

              this.reportService.fetchTopTimeSlotsWithHighestNumberOfAppointments(formData.start, formData.end).subscribe(
                (response) => {
                  this.responseData = response;

                  this.topTimeSlotsWithHighestNumberOfAppointments = this.responseData.body.cookies;

                  this.reportService.fetchTopDepartmentAndDoctor(formData.start, formData.end).subscribe(
                    (response) => {
                      this.responseData = response;

                      this.topDepartmentAndDoctor = this.responseData.body.cookies;

                      this.reportService.fetchPatientDistribution(formData.start, formData.end).subscribe(
                        (response) => {
                          this.responseData = response;

                          this.patientDistribution = this.responseData.body.cookies;

                          this.reportService.fetchDoctorDistribution(formData.start, formData.end).subscribe(
                            async (response) => {
                              this.responseData = response;

                              this.doctorDistribution = this.responseData.body.cookies;

                              await new Promise(r => setTimeout(r, 2000));

                              this.generateReport(this.formatDate(formData.start), this.formatDate(formData.end));

                              this.download_status = "end";

                              await new Promise(r => setTimeout(r, 5000));

                              this.download_status = "null";

                            },
                            (error) => {
                              console.error('Error fetching data:', error);
                              this.download_status = "null";
                            }
                          );

                        },
                        (error) => {
                          console.error('Error fetching data:', error);
                          this.download_status = "null";
                        }
                      );

                    },
                    (error) => {
                      console.error('Error fetching data:', error);
                      this.download_status = "null";
                    }
                  );
                },
                (error) => {
                  console.error('Error fetching data:', error);
                  this.download_status = "null";
                }
              );

            },
            (error) => {
              console.error('Error fetching data:', error);
              this.download_status = "null";
            }
          );

        },
        (error) => {
          console.error('Error fetching data:', error);
          this.download_status = "null";
        }
      );
    }
  }

  generateReport(start: string, end: string) {

    const currentDate: Date = new Date();

    // Get the weekday, month, day, year, hour, and minute
    const weekday: string = currentDate.toLocaleString('en', { weekday: 'short' });
    const month: string = currentDate.toLocaleString('en', { month: 'short' });
    const day: string = currentDate.getDate().toString().padStart(2, '0');
    const year: string = currentDate.getFullYear().toString();
    const hour: string = currentDate.getHours().toString().padStart(2, '0');
    const minute: string = currentDate.getMinutes().toString().padStart(2, '0');

    // Combine the parts into the desired format
    const formattedDate: string = `${weekday} ${day}-${month}-${year} ${hour}:${minute}`;


    this.filename = 'report_' + start + '_' + end + '.pdf';
    var row: any[] = [];

    if (
      this.appointmentStatusList.length === 0 &&
      this.appointmentStatusListWithSpecialization.length === 0 &&
      this.topTimeSlotsWithHighestNumberOfAppointments.length === 0 &&
      this.topDepartmentAndDoctor.length === 0 &&
      this.patientDistribution.length === 0 &&
      this.doctorDistribution.length === 0) {

      const emptyDefinition = {

        content: [
          {
            text: 'No data recorded for ' + start + ' to ' + end,
            style: {
              fontSize: 15,
              textColor: '#333333',
            }
          },
        ]

      }
      pdfMake.createPdf(emptyDefinition).download(this.filename);
      return;
    }

    const table1_rows: any[][] = [];
    table1_rows.push(['Date', 'Pending', 'Cancelled', 'Completed']);
    this.appointmentStatusList.forEach(jsonData => {
      row = Object.values(jsonData);
      row[0] = this.formatDate(row[0]);
      table1_rows.push(row);
    });

    const table2_rows: any[][] = [];
    table2_rows.push(['Specializations', 'Pending', 'Cancelled', 'Completed']);
    this.appointmentStatusListWithSpecialization.forEach(jsonData => {
      table2_rows.push(Object.values(jsonData));
    });

    const table3_rows: any[][] = [];
    table3_rows.push(['Day of Week', 'Appointment Time', 'Specialization', 'Appointment Count', 'Average Doctor Rating']);
    this.topTimeSlotsWithHighestNumberOfAppointments.forEach(jsonData => {
      row = Object.values(jsonData);
      row[1] = this.formatTime(row[1]);
      table3_rows.push(row);
    });

    const table4_rows: any[][] = [];
    table4_rows.push(['Specialization', 'Total Appointments', 'Avg Rating', 'Top Rated Doctor', 'Most Appointed Doctor']);
    this.topDepartmentAndDoctor.forEach(jsonData => {
      row = Object.values(jsonData);
      row[2] = parseFloat(row[2].toFixed(2));
      table4_rows.push(row);
    });

    const table5_rows: any[][] = [];
    table5_rows.push(['Total Appointments', 'Name', 'Gender', 'First Appointment', 'Last Appointment']);
    this.patientDistribution.forEach(jsonData => {
      row = Object.values(jsonData);
      row = [row[0], row[1], row[2], row[4], row[5]];  //blood group removed
      row[2] = this.formatGender(row[2]);
      row[3] = this.formatDate(row[3]);
      row[4] = this.formatDate(row[4]);
      table5_rows.push(row);
    });

    const table6_rows: any[][] = [];
    table6_rows.push(['Doctor Name', 'Specialization', 'Highest Fees In Specialization', 'Highest Fees Doctor In Specialization', 'Lowest Fees In Specialization', 'Lowest Fees Doctor In Specialization', 'Average Fees Per Specialization']);
    this.doctorDistribution.forEach(jsonData => {
      row = Object.values(jsonData);
      row[6] = parseFloat(row[6].toFixed(2));
      table6_rows.push(row);
    });


    const docDefinition = {

      content: [
        {
          text: 'Report for ' + start + ' to ' + end,
          style: {
            fontSize: 15,
            textColor: '#333333',
          }
        },
        "\nThis document contains data for Medical Mangement System\nReport generated on " + formattedDate,
        {
          text:
            "\n\nAppointment Status for every day\n\n",
          style: {
            fontSize: 12,
          }
        },
        {
          table: {
            headerRows: 1,
            widths: ['auto', 'auto', 'auto', 'auto'],
            body: table1_rows,
          },
          layout: 'lightHorizontalLines',
          style: {
            fontSize: 10,
          }
        },
        {
          text:
            "\n\nAppointment Status for every specialization\n\n",
          style: {
            fontSize: 12,
          }
        },
        {
          table: {
            headerRows: 1,
            widths: ['auto', 'auto', 'auto', 'auto'],
            body: table2_rows,
          },
          layout: 'lightHorizontalLines',
          style: {
            fontSize: 10
          }
        },
        {
          text:
            "\n\nTop Time Slots With Highest Number of Appointments\n\n",
          style: {
            fontSize: 12,
          }
        },
        {
          table: {
            headerRows: 1,
            widths: ['auto', 'auto', 'auto', 'auto', 'auto'],
            body: table3_rows,
          },
          layout: 'lightHorizontalLines',
          style: {
            fontSize: 10
          }
        },
        {
          text:
            "\n\nTop Doctors in respective Specialization\n\n",
          style: {
            fontSize: 12,
          }
        },
        {
          table: {
            headerRows: 1,
            widths: ['auto', 'auto', 'auto', 'auto', 'auto'],
            body: table4_rows,
          },
          layout: 'lightHorizontalLines',
          style: {
            fontSize: 10
          }
        },
        {
          text:
            "\n\nPatient Distribution\n\n",
          style: {
            fontSize: 12,
          }
        },
        {
          table: {
            headerRows: 1,
            widths: ['auto', 'auto', 'auto', 'auto', 'auto'],
            body: table5_rows,
          },
          layout: 'lightHorizontalLines',
          style: {
            fontSize: 10
          }
        },
        {
          text:
            "\n\nDoctor Cost Distribution\n\n",
          style: "subheader"
        },
        {
          table: {
            headerRows: 1,
            widths: ['auto', 'auto', 'auto', 'auto', 'auto', 'auto', 'auto'],
            body: table6_rows,
          },
          layout: 'lightHorizontalLines',
          style: {
            fontSize: 10
          }
        },
      ],
    };


    // pdfMake.createPdf(docDefinition).open();
    pdfMake.createPdf(docDefinition).download(this.filename);


  }



  formatDate(timePeriod: number[] | string): string {
    const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];

    // If the input is in string format (e.g., "2024-03-01"), convert it to an array
    if (typeof timePeriod === 'string') {
      const parts = timePeriod.split('-').map(Number);
      if (parts.length !== 3 || parts.some(isNaN)) {
        return 'Invalid date';
      }
      timePeriod = parts;
    }

    // Extract year, month, and day from the timePeriod array
    const year = timePeriod[0];
    const monthIndex = timePeriod[1] - 1; // Month should be 0-indexed
    const day = timePeriod[2];

    // Get the abbreviated month name
    const month = months[monthIndex];

    // Construct the formatted date string
    const formattedDate = `${day}-${month}-${year}`;

    return formattedDate;
  }

  formatTime(time: number): string {
    // Define an array to map numbers 1 to 12 to corresponding time strings
    const timeMap: string[] = [
      '10:00 to 10:15', '10:15 to 10:30', '10:30 to 10:45', '10:45 to 11:00',
      '11:00 to 11:15', '11:15 to 11:30', '11:30 to 11:45', '11:45 to 12:00',
      '12:00 to 12:15', '12:15 to 12:30', '12:30 to 12:45', '12:45 to 13:00',
      '14:00 to 14:15', '14:15 to 14:30', '14:30 to 14:45', '14:45 to 15:00',
      '15:00 to 15:15', '15:15 to 15:30', '15:30 to 15:45', '15:45 to 16:00',
      '16:00 to 16:15', '16:15 to 16:30', '16:30 to 16:45', '16:45 to 17:00'
    ];

    // Check if the input number is within the valid range
    if (time < 1 || time > 24) {
      return 'Unknown';
    }

    // Return the corresponding time string based on the input number
    return timeMap[time - 1];
  }

  formatGender(gender: string): string {
    // Define a mapping for gender abbreviations to full names
    const genderMap: { [key: string]: string } = {
      'M': 'Male',
      'F': 'Female',
      'O': 'Other'
    };

    // Return the corresponding full name based on the input abbreviation
    return genderMap[gender] || 'Unknown';
  }

  formatBloodGroup(bloodGroup: string): string {
    // Define a mapping for blood group abbreviations to blood group types
    const bloodGroupMap: { [key: string]: string } = {
      'Ap': 'A+',
      'An': 'A-',
      'Bp': 'B+',
      'Bn': 'B-',
      'Op': 'O+',
      'On': 'O-',
      'ABp': 'AB+',
      'ABn': 'AB-',
      'U': 'Unknown'
    };

    // Return the corresponding blood group type based on the input abbreviation
    return bloodGroupMap[bloodGroup] || 'Unknown';
  }

}