package com.nrifintech.medicalmanagementsystem.event;

import org.springframework.context.ApplicationEvent;

import lombok.Data;

@Data
public class DoctorAdditionEmailEvent extends ApplicationEvent {
    

    private String username;
    private String password;
    private String email;
    

     public DoctorAdditionEmailEvent(Object source, String username, String password, String email) {
         super(source);
         this.username = username;
         this.password = password;
         this.email = email;
     }

}
