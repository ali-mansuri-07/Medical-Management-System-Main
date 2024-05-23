    
    
package com.nrifintech.medicalmanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopTimeSlotsWithHighestNumberOfAppointments {    

    String day_of_week;

    Integer appointment_time;

    String specialization;

    Long appointment_count;

    Double average_doctor_rating;

} 

    
  
