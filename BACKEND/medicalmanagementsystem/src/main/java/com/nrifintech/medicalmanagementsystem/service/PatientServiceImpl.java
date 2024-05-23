package com.nrifintech.medicalmanagementsystem.service;

import java.util.Optional;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.nrifintech.medicalmanagementsystem.model.Patient;
import com.nrifintech.medicalmanagementsystem.repository.PatientRepository;

import jakarta.transaction.Transactional;

import java.util.regex.Pattern;

import javax.management.InvalidAttributeValueException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nrifintech.medicalmanagementsystem.dto.PatientDTO;
import com.nrifintech.medicalmanagementsystem.model.Patient;
import com.nrifintech.medicalmanagementsystem.model.User;
import com.nrifintech.medicalmanagementsystem.repository.PatientRepository;
import com.nrifintech.medicalmanagementsystem.repository.UserRepository;
import com.nrifintech.medicalmanagementsystem.utility.enums.BloodGroup;
import com.nrifintech.medicalmanagementsystem.utility.enums.Gender;

import jakarta.transaction.Transactional;

@Service
public class PatientServiceImpl implements PatientService{

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    UserRepository userRepository;
    
    @Transactional
        public Optional<Patient> getPatientById(Long patientId) {
            Optional<Patient> patient=patientRepository.findById(patientId);
            // System.out.println(patient.get());
            
            return patient;
        }

    @Override
    public Patient create(PatientDTO patientDTO) throws InvalidAttributeValueException {            

        User user = validateUserId(patientDTO.getUserId());
        validatePatientDetails(patientDTO);

        Patient patient = new Patient();
        patient.setName(patientDTO.getName());
        patient.setGender(patientDTO.getGender());
        patient.setBirthYear(patientDTO.getBirthYear());
        patient.setBloodGroup(patientDTO.getBloodGroup());
        patient.setEmail(patientDTO.getEmail());
        patient.setContactNumber(patientDTO.getContactNumber());
        patient.setUser(user);
        
        return patientRepository.save(patient);
    }
    
    @Override
    public Patient read(Long id) {
        return validatePatientId(id);
    }

    @Override
    public Patient update(Long id,PatientDTO patientDTO) throws InvalidAttributeValueException {    

        Patient patient = validatePatientId(id);
        validatePatientDetails(patientDTO);

        patient.setEmail(patientDTO.getEmail());
        patient.setContactNumber(patientDTO.getContactNumber());
    
        return patientRepository.save(patient);
    }

    @Override
    public Patient validatePatientId(Long id){
        return patientRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("Patient Id Invalid"));
    }

    @Override
    public User validateUserId(Long id){
        return userRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("User Id Invalid"));
    }

    @Override
    public void validatePatientDetails(PatientDTO patientDTO) throws InvalidAttributeValueException{

        if (patientDTO.getName() == null || patientDTO.getName().isEmpty()) {
            throw new InvalidAttributeValueException("Invalid Name");
        }

        if (patientDTO.getGender() == null || !validateGender(patientDTO.getGender())) {
            throw new InvalidAttributeValueException("Invalid Gender");
        }

        if (patientDTO.getEmail() == null || !validateEmail(patientDTO.getEmail())) {
            throw new InvalidAttributeValueException("Invalid Email");
        }

        if (patientDTO.getBirthYear() == null || !validateBirthYear(patientDTO.getBirthYear())) {
            throw new InvalidAttributeValueException("Invalid Birth Year");
        }

        if (patientDTO.getBloodGroup() == null || !validateBloodGroup(patientDTO.getBloodGroup())) {
            throw new InvalidAttributeValueException("Invalid Blood Group");
        }

        if (patientDTO.getContactNumber() == null || !validateContactNumber(patientDTO.getContactNumber())) {
            throw new InvalidAttributeValueException("Invalid Contact Number");
        }
    }

    @Override
    public boolean validateBirthYear(String birthYear) {
        // Ensure birthYear consists of exactly four digits
        return Pattern.matches("\\d{4}",birthYear);
    }

    @Override
    public boolean validateGender(Gender gender) {
        // Check if the gender is one of the allowed values ('M', 'F', 'O')
        return Pattern.matches("[MFO]", gender.toString());
    }

    @Override
    public boolean validateBloodGroup(BloodGroup bloodGroup) {
        // Check if the blood group is one of the allowed values
        return Pattern.matches("^(Ap|An|Bp|Bn|Op|On|ABp|ABn|U)$", bloodGroup.toString());
    }

    @Override
    public boolean validateEmail(String email) {
        // Define the pattern for the email address
        // Pattern: [a-zA-Z0-9._%-]+@[a-zA-Z0-9._%-]+\.[a-zA-Z]{2,4}
        String regex = "[a-zA-Z0-9._%-]+@[a-zA-Z0-9._%-]+\\.[a-zA-Z]{2,4}";
        return Pattern.matches(regex, email);
    }

    @Override
    public boolean validateContactNumber(String contactNumber) {
        // Define the pattern for the contact number
        // Pattern: optional '+' followed by 1 to 3 digits, mandatory space, followed by 8 to 10 digits
        String regex = "^(\\+\\d{1,3})?\\s\\d{8,10}$";
        return Pattern.matches(regex, contactNumber);
    }
}

