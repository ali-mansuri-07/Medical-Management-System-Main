package com.nrifintech.medicalmanagementsystem.event;

import org.springframework.context.ApplicationEvent;

import lombok.Data;

@Data
public class DoctorRatingUpdationEvent extends ApplicationEvent {
    


    private Integer newRating;
    private Long appointmentId;
    

     public DoctorRatingUpdationEvent(Object source, Integer newRating,Long appointmentId) {
         super(source);
         this.appointmentId = appointmentId;
         this.newRating = newRating;
     }
}
