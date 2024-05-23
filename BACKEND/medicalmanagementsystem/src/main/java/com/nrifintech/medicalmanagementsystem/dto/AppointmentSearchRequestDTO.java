package com.nrifintech.medicalmanagementsystem.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class AppointmentSearchRequestDTO {

    
    private Long patientId;

    private Long doctorId;

    private String doctorName;
    
    private LocalDate startDate;
    private LocalDate endDate;

    private String specialization;

    private String appointmentStatus;

    @Min(value=0, message="Offset cannot be less than 0")
    private Integer offset;

    @Max(value=24, message="Slot is only upto 24")
    @Min(value=1, message="Slot is starting from 1")
    private Integer slot;
    
    private Integer pageSize;

    private String sortBy;

}