package com.nrifintech.medicalmanagementsystem.mapper;

import com.nrifintech.medicalmanagementsystem.dto.PatientDTO;
import com.nrifintech.medicalmanagementsystem.model.Patient;

public class PatientMapper {

    public static PatientDTO convertToPatientDTO(Patient patient) {
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setName(patient.getName());
        patientDTO.setGender(patient.getGender());
        patientDTO.setEmail(patient.getEmail());
        patientDTO.setBirthYear(patient.getBirthYear());
        patientDTO.setBloodGroup(patient.getBloodGroup());
        patientDTO.setContactNumber(patient.getContactNumber());
        if(patient.getUser() != null) {
            patientDTO.setUserId(patient.getUser().getId());
        }
        return patientDTO;
    }
}
