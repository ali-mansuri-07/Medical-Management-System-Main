package com.nrifintech.medicalmanagementsystem.dto;

import java.time.LocalDate;

import com.nrifintech.medicalmanagementsystem.utility.enums.BloodGroup;
import com.nrifintech.medicalmanagementsystem.utility.enums.Gender;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientDistribution {
    private Long total_appointments;
    private String name;
    private Gender gender;
    private BloodGroup bloodgroup;
    private LocalDate firstAppointment;
    private LocalDate lastAppointment;

}
