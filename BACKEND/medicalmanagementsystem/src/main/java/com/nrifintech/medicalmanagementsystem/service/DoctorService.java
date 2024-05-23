package com.nrifintech.medicalmanagementsystem.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nrifintech.medicalmanagementsystem.Exception.DoctorNotFoundException;
import com.nrifintech.medicalmanagementsystem.dto.DashboardDataDTO;
import com.nrifintech.medicalmanagementsystem.dto.DoctorDTO;
import com.nrifintech.medicalmanagementsystem.dto.PatientDTO;
import com.nrifintech.medicalmanagementsystem.dto.SpecializationDTO;
import com.nrifintech.medicalmanagementsystem.model.Doctor;
import com.nrifintech.medicalmanagementsystem.model.Specialization;
import com.nrifintech.medicalmanagementsystem.utility.enums.Status;

import jakarta.mail.MessagingException;

/**
 * Service class responsible for handling operations related to doctors.
 * This includes adding new doctors and updating existing ones.
 */

public interface DoctorService {

    Optional<Doctor> getActiveDoctorById(Long doctorId);

    Doctor addDoctor(DoctorDTO doctor)throws MessagingException;

    Doctor updateDoctor(Long id, DoctorDTO updatedDoctorDto);

    Page<DoctorDTO> searchDoctors(String query, Pageable pageable, String sortBy, String sortDir);

    Page<DoctorDTO> getAllDoctors(Pageable pageable);

    Page<DoctorDTO> findByStatus(Status status, Pageable pageable);

    

    Page<DoctorDTO> findDoctorsBySpecialization(String specialization, Pageable pageable);

    public void updateDoctorStatus();

    public void updateDoctorRating(Integer newRating, Long appointmentId);

    Page<DoctorDTO> searchAndFilterDoctors(String searchQuery, Status status, Pageable pageable);
    Page<DoctorDTO> searchDoctorsAdmin(String query, Pageable pageable);
    Doctor getDoctorById(Long id) throws DoctorNotFoundException;
    

    Specialization addSpecialization(SpecializationDTO specializationDTO);
    Specialization updateSpecialization(Long id, SpecializationDTO specializationDTO);
    Map<String, Long> getDoctorSpecializationCounts();
    DashboardDataDTO getDashboardData();

    Page<PatientDTO> getAllPatients(Pageable pageable);
}
