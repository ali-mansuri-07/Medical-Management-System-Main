package com.nrifintech.medicalmanagementsystem.schedulers;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.nrifintech.medicalmanagementsystem.repository.AppointmentRepository;
import com.nrifintech.medicalmanagementsystem.repository.DoctorRepository;
import com.nrifintech.medicalmanagementsystem.service.AppointmentService;
import com.nrifintech.medicalmanagementsystem.service.DoctorService;

import jakarta.transaction.Transactional;



@Service
public class scheduler {

    @Autowired
AppointmentService appointmentService;
    @Autowired
DoctorService doctorService;

    @Transactional
    @Async("taskScheduler")
    @Scheduled(cron = "0 15,30,45 10-13,14-17 * * *")
    public void completeAppointment() {
        // System.out.println(Thread.currentThread().getName() + "APPOINTMENT");
        appointmentService.updateAppointmentStatus();
        

    }

    @Transactional
    @Async("taskScheduler")
   @Scheduled(cron = "0 0 1 * * *")
    public void updateDoctorStatus() {
        // System.out.println(Thread.currentThread().getName());
        
        doctorService.updateDoctorStatus();

    }

}
