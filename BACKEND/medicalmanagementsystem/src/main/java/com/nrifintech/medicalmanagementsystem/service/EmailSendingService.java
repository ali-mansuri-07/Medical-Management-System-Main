package com.nrifintech.medicalmanagementsystem.service;

import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;


public interface EmailSendingService {

    public void sendBookingCancelledMail(String recipient,String date, String time, String name) throws MessagingException;
    public void sendBookingConfirmationMail(String recipient,String date, String time, String name) throws MessagingException;
    public void sendDoctorCredentials(String recipient, String username, String password) throws MessagingException;
}
