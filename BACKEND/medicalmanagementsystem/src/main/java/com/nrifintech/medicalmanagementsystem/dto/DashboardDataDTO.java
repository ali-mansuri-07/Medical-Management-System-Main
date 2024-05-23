package com.nrifintech.medicalmanagementsystem.dto;


import java.util.LinkedHashMap;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDataDTO {
    
    private int pendingAppointmentCount;
    private int completedAppointmentCount;
    private int totalPatientCount;
    private int totalDoctorCount;
    private LinkedHashMap<String,Integer> dailyCompletedAppointments;

}
