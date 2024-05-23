package com.nrifintech.medicalmanagementsystem.controller;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.management.InvalidAttributeValueException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nrifintech.medicalmanagementsystem.dto.AppointmentBookingRequestDTO;
import com.nrifintech.medicalmanagementsystem.dto.AppointmentBookingResponseDTO;
import com.nrifintech.medicalmanagementsystem.service.AppointmentService;
import com.nrifintech.medicalmanagementsystem.service.GenerateResponseService;
import com.nrifintech.medicalmanagementsystem.service.ReportGenerationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("appointment")
public class AppointmentController {
    @Autowired
    private Semaphore semaphore;

    @Autowired
    AppointmentService appointmentService;

    @Autowired
    GenerateResponseService generateResponseService;

    @Autowired
    ReportGenerationService reportGenerationService;

    @PostMapping("/book")
    public ResponseEntity<Object> bookAppointment(
            @Valid @RequestBody AppointmentBookingRequestDTO appointmentBookingRequestDTO) throws InterruptedException {
                try {
                    if (semaphore.tryAcquire(2000, TimeUnit.MILLISECONDS)) {
                        AppointmentBookingResponseDTO res = appointmentService.bookAppointment(appointmentBookingRequestDTO);
                        return generateResponseService.generateResponse(
                                "Appointment successfully booked",
                                HttpStatus.OK, res);
                    } else {
                        return generateResponseService.generateResponse("Servers are busy, please try again", HttpStatusCode.valueOf(409));
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return generateResponseService.generateResponse("Servers are busy, please try again", HttpStatusCode.valueOf(409));
                } finally {
                    semaphore.release();
                }
                

    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<Object> cancelAppointment(@PathVariable Long id) throws InvalidAttributeValueException {
        return generateResponseService.generateResponse(
                "Appointment successfully cancelled",
                HttpStatus.OK,
                appointmentService.cancelAppointment(id));
    }

}
