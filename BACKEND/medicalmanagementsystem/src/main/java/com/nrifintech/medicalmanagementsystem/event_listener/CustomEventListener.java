package com.nrifintech.medicalmanagementsystem.event_listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.nrifintech.medicalmanagementsystem.event.DoctorAdditionEmailEvent;
import com.nrifintech.medicalmanagementsystem.event.DoctorRatingUpdationEvent;
import com.nrifintech.medicalmanagementsystem.event.EmailSendingEvent;
import com.nrifintech.medicalmanagementsystem.service.DoctorService;
import com.nrifintech.medicalmanagementsystem.service.EmailSendingServiceImpl;

import jakarta.mail.MessagingException;

@Component
public class CustomEventListener {

    @Autowired
    EmailSendingServiceImpl emailSendingService;

    @Autowired
    DoctorService doctorService;
    
    @EventListener(EmailSendingEvent.class)
    @Async("asyncTaskExecutor")
    public void handleEmailEvent(EmailSendingEvent event) throws InterruptedException, MessagingException {
        
        switch (event.getType()) {
            case "BOOK":
            emailSendingService.sendBookingConfirmationMail(event.getRecipient(),event.getDate(),event.getTime(),event.getName());
                break;
        
            case "CANCELLED":
            emailSendingService.sendBookingCancelledMail(event.getRecipient(),event.getDate(),event.getTime(),event.getName());
                break;
        
            default:
                break;
        }
       
    }
    @EventListener(DoctorAdditionEmailEvent.class)
    @Async("asyncTaskExecutor")
    public void handleDoctorAdditionEmailEvent(DoctorAdditionEmailEvent event) throws InterruptedException, MessagingException {
        
        emailSendingService.sendDoctorCredentials(event.getEmail(), event.getUsername(), event.getPassword());
        
    }
    @EventListener(DoctorRatingUpdationEvent.class)
    @Async("asyncTaskExecutor")
    public void handleDoctorRatingUpdationEvent(DoctorRatingUpdationEvent event) throws InterruptedException, MessagingException {
        doctorService.updateDoctorRating(event.getNewRating(),event.getAppointmentId());
    }
}
