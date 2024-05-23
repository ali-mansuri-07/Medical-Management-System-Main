package com.nrifintech.medicalmanagementsystem.event;

import org.springframework.context.ApplicationEvent;

import lombok.Data;

@Data
public class EmailSendingEvent extends ApplicationEvent {

    
    private String recipient;
    private String date;
    private String time;
    private String name;
    private String type;
    

     public EmailSendingEvent(Object source, String recipient, String date, String time, String type) {
         super(source);
         this.recipient = recipient;
         this.date = date;
         this.time = time;
         this.type = type;
     }
}
