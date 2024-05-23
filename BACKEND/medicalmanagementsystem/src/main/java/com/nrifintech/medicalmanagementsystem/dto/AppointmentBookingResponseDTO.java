package com.nrifintech.medicalmanagementsystem.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class AppointmentBookingResponseDTO {
    
    private Long doctorId;

    private Long patientId;

    private LocalDate appDate;

    private Integer slot;

    
}