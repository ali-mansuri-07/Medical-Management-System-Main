package com.nrifintech.medicalmanagementsystem.service;

import java.util.Optional;

// <<<<<<< HEAD
// import java.util.Optional;

// import com.nrifintech.medicalmanagementsystem.model.Patient;

// public interface PatientService {
//     public Patient addPatient(Patient patient);
//      public Optional<Patient> getPatientById(Long patientId);
// }

import javax.management.InvalidAttributeValueException;

import com.nrifintech.medicalmanagementsystem.dto.PatientDTO;
import com.nrifintech.medicalmanagementsystem.model.Appointment;
import com.nrifintech.medicalmanagementsystem.model.Patient;
import com.nrifintech.medicalmanagementsystem.model.User;
import com.nrifintech.medicalmanagementsystem.utility.enums.BloodGroup;
import com.nrifintech.medicalmanagementsystem.utility.enums.Gender;


public interface PatientService {
    Patient create(PatientDTO patientDTO) throws InvalidAttributeValueException;
    Patient read(Long id);
    Patient update(Long id,PatientDTO patientDTO) throws InvalidAttributeValueException;

    Patient validatePatientId(Long id);
    User validateUserId(Long id);
    void validatePatientDetails(PatientDTO patientDTO) throws InvalidAttributeValueException;

    boolean validateBirthYear(String birthYear);
    boolean validateGender(Gender gender);
    boolean validateBloodGroup(BloodGroup bloodGroup);
    boolean validateEmail(String email);
    boolean validateContactNumber(String contactNumber);
    Optional<Patient> getPatientById(Long patientId);
}

