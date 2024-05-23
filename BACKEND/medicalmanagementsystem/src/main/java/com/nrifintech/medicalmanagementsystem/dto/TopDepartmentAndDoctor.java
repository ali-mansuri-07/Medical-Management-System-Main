package com.nrifintech.medicalmanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopDepartmentAndDoctor {
    private String specialization;
    private Long total_appointments;
    private Double avg_rating;
    private String top_rating_doctor;
    private String top_appointment_doctor;

}

    
  
