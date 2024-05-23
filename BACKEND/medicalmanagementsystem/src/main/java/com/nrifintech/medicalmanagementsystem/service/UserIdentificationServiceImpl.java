package com.nrifintech.medicalmanagementsystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nrifintech.medicalmanagementsystem.Exception.UnauthorizedloginException;
import com.nrifintech.medicalmanagementsystem.dto.UserDTO;
import com.nrifintech.medicalmanagementsystem.dto.UserIdentificationDTO;
import com.nrifintech.medicalmanagementsystem.model.Doctor;
import com.nrifintech.medicalmanagementsystem.model.Patient;
import com.nrifintech.medicalmanagementsystem.model.Role;
import com.nrifintech.medicalmanagementsystem.model.User;
import com.nrifintech.medicalmanagementsystem.repository.DoctorRepository;
import com.nrifintech.medicalmanagementsystem.repository.PatientRepository;
import com.nrifintech.medicalmanagementsystem.repository.RoleRepository;
import com.nrifintech.medicalmanagementsystem.repository.UserRepository;

@Service
public class UserIdentificationServiceImpl implements UserIdentificationService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    DoctorRepository doctorRepository;

    @Override
    public UserIdentificationDTO convertUserDTOtoUserIdentificationDTO(UserDTO userDTO) {

        User user = userRepository.findByUserName(userDTO.getUserName());
        Role role = user.getRole();


        UserIdentificationDTO userIdentificationDTO = new UserIdentificationDTO();
        userIdentificationDTO.setUserId(user.getId());
        userIdentificationDTO.setUserType(role.getRoleName());

        if(role.getRoleName().equals("ROLE_PATIENT"))
        {
            Patient patient = patientRepository.findByUser(user);

            if(patient == null)       // user is not yet patient
                userIdentificationDTO.setUserTypeId(null);
            else                        
                userIdentificationDTO.setUserTypeId(patient.getId());
        }
        else if(role.getRoleName().equals("ROLE_DOCTOR"))
        {
            // doctor is always in doctor, user table
            Doctor doctor = doctorRepository.findByUser(user);
            // String doctor_status = doctorRepository.getDoctorStatus(doctor.getId());

            // if(doctor == null){
            //     userIdentificationDTO.setUserTypeId(null);
            //     userIdentificationDTO.setDoctorStatus(null);
            // }                        
                userIdentificationDTO.setUserTypeId(doctor.getId());
                    String doctorStatus = doctor.getStatus().toString();
                    if(doctorStatus == "INACTIVE")
                    {
                        throw new UnauthorizedloginException("You are not authorized to login",972);
                    }
                
        }
        else        // for admin user type id is same as user id
            userIdentificationDTO.setUserTypeId(user.getId());
        
        return userIdentificationDTO;
    }
    
}
