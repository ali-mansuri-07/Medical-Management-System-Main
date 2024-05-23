package com.nrifintech.medicalmanagementsystem.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.nrifintech.medicalmanagementsystem.dto.AppointmentBookingRequestDTO;
import com.nrifintech.medicalmanagementsystem.dto.AppointmentBookingResponseDTO;
import com.nrifintech.medicalmanagementsystem.model.Appointment;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {


    AppointmentBookingResponseDTO appointmentToAppointmentBookingResponseDTO(Appointment appointment);

    @Mapping(source = "doctor.id", target = "doctorId")
    @Mapping(source = "patient.id", target = "patientId")
    AppointmentBookingResponseDTO appointmentToAppointmentBookingResponseDTOWithIds(Appointment appointment);

    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "patient", ignore = true)
    Appointment appointmentBookingRequestDTOToAppointment(AppointmentBookingRequestDTO appointmentBookingRequestDTO);
}