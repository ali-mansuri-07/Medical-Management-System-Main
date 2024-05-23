package com.nrifintech.medicalmanagementsystem.Exception;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AppointmentAlreadyBookedException extends RuntimeException {
    public AppointmentAlreadyBookedException(String message) {
        super(message);
    }
}