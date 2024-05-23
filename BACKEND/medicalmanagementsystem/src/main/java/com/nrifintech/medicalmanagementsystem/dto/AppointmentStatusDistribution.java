    
    
package com.nrifintech.medicalmanagementsystem.dto;

import java.time.LocalDate;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentStatusDistribution {    

    LocalDate time_period;

    Long pending;

    Long cancelled;

    Long completed;


} 

    
  
