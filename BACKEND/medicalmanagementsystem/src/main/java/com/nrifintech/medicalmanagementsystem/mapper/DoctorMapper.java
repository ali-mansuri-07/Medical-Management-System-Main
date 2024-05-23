package com.nrifintech.medicalmanagementsystem.mapper;

import org.springframework.stereotype.Component;

import com.nrifintech.medicalmanagementsystem.dto.DoctorDTO;
import com.nrifintech.medicalmanagementsystem.model.Doctor;


@Component
public class DoctorMapper {
    public DoctorDTO convertToDoctorDTO(Doctor doctor) {
        DoctorDTO doctorDTO = new DoctorDTO();
        doctorDTO.setId(doctor.getId());
        doctorDTO.setName(doctor.getName());
        doctorDTO.setGender(doctor.getGender());
        doctorDTO.setQualification(doctor.getQualification());
        doctorDTO.setEmail(doctor.getEmail());
        doctorDTO.setFees(doctor.getFees());
        doctorDTO.setExperienceStart(doctor.getExperienceStart());
        doctorDTO.setLeaveStart(doctor.getLeaveStart());
        doctorDTO.setLeaveEnd(doctor.getLeaveEnd());
        doctorDTO.setRating(doctor.getRating());
        doctorDTO.setStatus(doctor.getStatus());
        doctorDTO.setUserName(doctor.getUser().getUserName());
        doctorDTO.setPassword(doctor.getUser().getPassword());
        doctorDTO.setSpecialization(doctor.getSpecialization().getName());
        doctorDTO.setProfileImgUrl(doctor.getProfileImgUrl());
        return doctorDTO;
    }
}
