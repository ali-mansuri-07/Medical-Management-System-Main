package com.nrifintech.medicalmanagementsystem.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;

import java.util.Set;

import java.util.stream.Collectors;

import javax.management.InvalidAttributeValueException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.nrifintech.medicalmanagementsystem.mapper.SlotTimeMapper;
import com.nrifintech.medicalmanagementsystem.Exception.AppointmentAlreadyBookedException;
import com.nrifintech.medicalmanagementsystem.Exception.AppointmentAlreadyCancelledException;
import com.nrifintech.medicalmanagementsystem.Exception.NoConsecutiveBookingsException;
import com.nrifintech.medicalmanagementsystem.Exception.NoSameBookingsWithSameDoctorByOnePatient;
import com.nrifintech.medicalmanagementsystem.dto.AppointmentBookingRequestDTO;
import com.nrifintech.medicalmanagementsystem.dto.AppointmentBookingResponseDTO;
import com.nrifintech.medicalmanagementsystem.dto.AppointmentSearchRequestDTO;
import com.nrifintech.medicalmanagementsystem.dto.AppointmentSearchResponseDTO;
import com.nrifintech.medicalmanagementsystem.dto.DoctorDTO;
import com.nrifintech.medicalmanagementsystem.event.EmailSendingEvent;
import com.nrifintech.medicalmanagementsystem.mapper.AppointmentMapper;
import com.nrifintech.medicalmanagementsystem.model.Appointment;
import com.nrifintech.medicalmanagementsystem.model.Doctor;
import com.nrifintech.medicalmanagementsystem.model.Patient;
import com.nrifintech.medicalmanagementsystem.repository.AppointmentRepository;
import com.nrifintech.medicalmanagementsystem.utility.enums.AppointmentStatus;
import com.nrifintech.medicalmanagementsystem.utility.enums.Slot;

import jakarta.persistence.PessimisticLockException;

import jakarta.validation.ConstraintViolationException;

// import jakarta.transaction.Transactional;

@Service
public class AppointmentServiceImpl implements AppointmentService {

        @Autowired
        DoctorService doctorService;

        @Autowired
        PatientService patientService;

        
        @Autowired
        ApplicationEventPublisher eventPublisher;

        @Autowired
        AppointmentRepository appointmentRepository;

        @Autowired
        SlotTimeMapper slotTimeMapper;

        @Transactional(isolation = Isolation.READ_COMMITTED)
        public AppointmentBookingResponseDTO bookAppointment(AppointmentBookingRequestDTO appointmentBookingRequestDTO)
                        throws AppointmentAlreadyBookedException, ConstraintViolationException, NoSuchElementException,
                        DataIntegrityViolationException {
               
                AppointmentSearchRequestDTO appointmentSearchRequestDTO;
                appointmentSearchRequestDTO = new AppointmentSearchRequestDTO();
                appointmentSearchRequestDTO.setStartDate(appointmentBookingRequestDTO.getAppDate());
                appointmentSearchRequestDTO.setEndDate(appointmentBookingRequestDTO.getAppDate());
                appointmentSearchRequestDTO.setDoctorId(appointmentBookingRequestDTO.getDoctorId());
                appointmentSearchRequestDTO.setSlot(appointmentBookingRequestDTO.getSlot());
                appointmentSearchRequestDTO.setAppointmentStatus("PENDING");

                List<AppointmentSearchResponseDTO> sameAppointment = searchAppointments(appointmentSearchRequestDTO)
                                .getContent();

                if (sameAppointment.size() > 0) {
                        throw new AppointmentAlreadyBookedException("This appointment has already been booked");
                }
                

                if (appointmentBookingRequestDTO.getSlot() + 1 <= 24) {
                        appointmentSearchRequestDTO = new AppointmentSearchRequestDTO();
                        appointmentSearchRequestDTO.setStartDate(appointmentBookingRequestDTO.getAppDate());
                        appointmentSearchRequestDTO.setEndDate(appointmentBookingRequestDTO.getAppDate());
                        appointmentSearchRequestDTO.setDoctorId(appointmentBookingRequestDTO.getDoctorId());
                        appointmentSearchRequestDTO.setPatientId(appointmentBookingRequestDTO.getPatientId());
                        appointmentSearchRequestDTO.setSlot(appointmentBookingRequestDTO.getSlot() + 1);
                        appointmentSearchRequestDTO.setAppointmentStatus("PENDING");

                        List<AppointmentSearchResponseDTO> sameDayBookings = searchAppointments(
                                        appointmentSearchRequestDTO).getContent();

                        if (sameDayBookings.size() > 0) {
                                throw new NoConsecutiveBookingsException(
                                                "Cannot book consecutive appointments with this doctor on this day");
                        }
                }

                if (appointmentBookingRequestDTO.getSlot() - 1 >= 1) {
                        appointmentSearchRequestDTO = new AppointmentSearchRequestDTO();
                        appointmentSearchRequestDTO.setStartDate(appointmentBookingRequestDTO.getAppDate());
                        appointmentSearchRequestDTO.setEndDate(appointmentBookingRequestDTO.getAppDate());
                        appointmentSearchRequestDTO.setDoctorId(appointmentBookingRequestDTO.getDoctorId());
                        appointmentSearchRequestDTO.setPatientId(appointmentBookingRequestDTO.getPatientId());
                        appointmentSearchRequestDTO.setSlot(appointmentBookingRequestDTO.getSlot() - 1);
                        appointmentSearchRequestDTO.setAppointmentStatus("PENDING");

                        List<AppointmentSearchResponseDTO> sameDayBookings = (searchAppointments(
                                        appointmentSearchRequestDTO)).getContent();

                        if (sameDayBookings.size() > 0) {
                                throw new NoConsecutiveBookingsException(
                                                "Cannot book consecutive appointments with this doctor on this day");
                        }
                }
                
                        appointmentSearchRequestDTO = new AppointmentSearchRequestDTO();
                        appointmentSearchRequestDTO.setStartDate(appointmentBookingRequestDTO.getAppDate());
                        appointmentSearchRequestDTO.setEndDate(appointmentBookingRequestDTO.getAppDate());
                        appointmentSearchRequestDTO.setPatientId(appointmentBookingRequestDTO.getPatientId());
                        appointmentSearchRequestDTO.setSlot(appointmentBookingRequestDTO.getSlot());
                        appointmentSearchRequestDTO.setAppointmentStatus("PENDING");

                        List<AppointmentSearchResponseDTO> samePatientBookings = (searchAppointments(
                                        appointmentSearchRequestDTO)).getContent();

                        if (samePatientBookings.size() > 0) {
                                throw new NoSameBookingsWithSameDoctorByOnePatient(
                                                "Cannot book appointments with multiple doctors on same day and on same slot");
                        }
                
                Appointment appointment;
                appointment = new Appointment();
                Doctor doctor = (doctorService.getActiveDoctorById(appointmentBookingRequestDTO.getDoctorId())).get();

                

                appointment.setDoctor(doctor);
                Patient patient = patientService.getPatientById(appointmentBookingRequestDTO.getPatientId()).get();
                appointment.setPatient(patient);
                appointment.setAppStatus(AppointmentStatus.valueOf("PENDING"));
                appointment.setAppDate(appointmentBookingRequestDTO.getAppDate());
                appointment.setSlot(appointmentBookingRequestDTO.getSlot());
                
                AppointmentBookingResponseDTO appointmentBookingResponseDTO = new AppointmentBookingResponseDTO();
                appointment = appointmentRepository.save(appointment);
                appointmentBookingResponseDTO.setAppDate(appointment.getAppDate());
                appointmentBookingResponseDTO.setSlot(appointment.getSlot());
                appointmentBookingResponseDTO.setDoctorId(appointment.getDoctor().getId());
                appointmentBookingResponseDTO.setPatientId(appointment.getPatient().getId());

           

                                EmailSendingEvent event = new EmailSendingEvent(this, appointment.getPatient().getEmail(),
                                appointment.getAppDate().toString(),slotTimeMapper.getTimeFromSlot(appointment.getSlot()),appointment.getPatient().getName());

               
                eventPublisher.publishEvent(event);
                return appointmentBookingResponseDTO;

        }

        @Transactional
        public String cancelAppointment(Long id)
                        throws AppointmentAlreadyCancelledException, InvalidAttributeValueException {
                Appointment appointment = appointmentRepository.findById(id)
                                .orElseThrow(() -> new InvalidAttributeValueException("Appointment ID doesnt exits"));
                if (appointment.getAppStatus() == AppointmentStatus.CANCELLED)
                        throw new AppointmentAlreadyCancelledException("Appointment already cancelled");

                appointment.setAppStatus(AppointmentStatus.CANCELLED);
                appointment = appointmentRepository.save(appointment);

           
                EmailSendingEvent event = new EmailSendingEvent(this, appointment.getPatient().getEmail(),
                                appointment.getAppDate().toString(),slotTimeMapper.getTimeFromSlot(appointment.getSlot()),appointment.getPatient().getName());

                eventPublisher.publishEvent(event);

                return "Your Appointment with " + appointment.getDoctor().getName() + " is cancelled !!";

        }

        @Override
        public Appointment getAppointmentById(Long appointmentId) {
                return appointmentRepository.findById(appointmentId).get();
        }

        public Page<AppointmentSearchResponseDTO> searchAppointments(
                        AppointmentSearchRequestDTO appointmentSearchRequestDTO) {
                Pageable pageable;
               
                if (appointmentSearchRequestDTO.getOffset() == null)
                        appointmentSearchRequestDTO.setOffset(0);

                if (appointmentSearchRequestDTO.getPageSize() == null)
                        appointmentSearchRequestDTO.setPageSize(200);

                pageable = PageRequest.of(appointmentSearchRequestDTO.getOffset(),
                                appointmentSearchRequestDTO.getPageSize());

                List<AppointmentSearchResponseDTO> searchResponseForDoctorName = appointmentRepository.findAppointments(
                                appointmentSearchRequestDTO.getDoctorId(),
                                appointmentSearchRequestDTO.getPatientId(),
                                appointmentSearchRequestDTO.getDoctorName(),
                                appointmentSearchRequestDTO.getSlot(),
                                appointmentSearchRequestDTO.getStartDate(),
                                appointmentSearchRequestDTO.getEndDate(),
                                appointmentSearchRequestDTO.getSpecialization(),
                                appointmentSearchRequestDTO.getAppointmentStatus() != null
                                                ? AppointmentStatus.valueOf(
                                                                appointmentSearchRequestDTO.getAppointmentStatus())
                                                : null);
                List<AppointmentSearchResponseDTO> searchResponseForSpecializationName = appointmentRepository
                                .findAppointments(
                                                appointmentSearchRequestDTO.getDoctorId(),
                                                appointmentSearchRequestDTO.getPatientId(),
                                                null,
                                                appointmentSearchRequestDTO.getSlot(),
                                                appointmentSearchRequestDTO.getStartDate(),
                                                appointmentSearchRequestDTO.getEndDate(),
                                                appointmentSearchRequestDTO.getDoctorName(),
                                                appointmentSearchRequestDTO.getAppointmentStatus() != null
                                                                ? AppointmentStatus.valueOf(appointmentSearchRequestDTO
                                                                                .getAppointmentStatus())
                                                                : null);
                Set<AppointmentSearchResponseDTO> mergedSet = new HashSet<>(searchResponseForDoctorName);
                mergedSet.addAll(searchResponseForSpecializationName);

                List<AppointmentSearchResponseDTO> result = new ArrayList<>(mergedSet);

                switch(appointmentSearchRequestDTO.getSortBy() != null
                ? appointmentSearchRequestDTO.getSortBy():"ASC")
                {
                        case "DESC":
                        
                        Collections.sort(result, Comparator
                        .comparing(AppointmentSearchResponseDTO::getAppDate)
                        .thenComparing(AppointmentSearchResponseDTO::getSlot).reversed());
                        break;
                        case "ASC":
                        
                        Collections.sort(result, Comparator
                        .comparing(AppointmentSearchResponseDTO::getAppDate)
                        .thenComparing(AppointmentSearchResponseDTO::getSlot));
                        break;
                        default:
                }
                

                int start = (int) pageable.getOffset();
                int end = (start + pageable.getPageSize()) > result.size() ? result.size()
                                : (start + pageable.getPageSize());
                
                List<AppointmentSearchResponseDTO> truncatedResult = result.subList(start, end).stream()
                                .collect(Collectors.toList());

                return new PageImpl<>(truncatedResult, pageable, result.size());
        }

        @Transactional
        public void updateAppointmentStatus() {
                
                appointmentRepository.updateAppointmentStatus(LocalDate.now(),slotTimeMapper.getSlotFromTime(LocalDateTime.now().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")).toString()));
        }

       

}