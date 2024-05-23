package com.nrifintech.medicalmanagementsystem.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDistribution {
    String doctorName;
    String specializationName;
    Integer highestFeesInSpecialization;
    String highestFeesDoctorInSpecialization;
    Integer lowestFeesInSpecialization;
    String lowestFeesDoctorInSpecialization;
    Double averageFeesPerSpecialization;
}
