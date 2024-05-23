package com.nrifintech.medicalmanagementsystem.service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import javax.management.InvalidAttributeValueException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;

import com.nrifintech.medicalmanagementsystem.Exception.AppointmentAlreadyBookedException;
import com.nrifintech.medicalmanagementsystem.Exception.AppointmentAlreadyCancelledException;
import com.nrifintech.medicalmanagementsystem.dto.AppointmentBookingRequestDTO;
import com.nrifintech.medicalmanagementsystem.dto.AppointmentBookingResponseDTO;
import com.nrifintech.medicalmanagementsystem.dto.AppointmentSearchRequestDTO;
import com.nrifintech.medicalmanagementsystem.dto.AppointmentSearchResponseDTO;
import com.nrifintech.medicalmanagementsystem.dto.DashboardDataDTO;
import com.nrifintech.medicalmanagementsystem.model.Appointment;

import jakarta.persistence.PessimisticLockException;
import jakarta.validation.ConstraintViolationException;

public interface AppointmentService {
    public AppointmentBookingResponseDTO bookAppointment(AppointmentBookingRequestDTO appointmentBookingRequestDTO) 
    throws AppointmentAlreadyBookedException, ConstraintViolationException, NoSuchElementException, DataIntegrityViolationException;

    public String cancelAppointment(Long id) throws AppointmentAlreadyCancelledException,InvalidAttributeValueException;

    public Appointment getAppointmentById(Long appointment_id) throws InvalidAttributeValueException; 
    public Page<AppointmentSearchResponseDTO> searchAppointments(AppointmentSearchRequestDTO appointmentSearchRequestDTO);
    public void updateAppointmentStatus();
    
}