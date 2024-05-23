    
    
package com.nrifintech.medicalmanagementsystem.dto;

import java.time.LocalDate;

import com.nrifintech.medicalmanagementsystem.utility.enums.AppointmentStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentSearchResponseDTO {    
    
    private Long appId;


    private Long doctorId;
    private String doctorName;
    private Long patientId;
    private String patientName;

    private LocalDate appDate;

    
    private Integer slot;

    
    private AppointmentStatus appStatus;

    private String specialization;

    
}
