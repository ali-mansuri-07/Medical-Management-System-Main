package com.nrifintech.medicalmanagementsystem.dto;

import java.time.LocalDate;

import com.nrifintech.medicalmanagementsystem.utility.enums.Gender;
import com.nrifintech.medicalmanagementsystem.utility.enums.Status;

import lombok.Data;

@Data
public class DoctorDTO {

    private Long id;
    
    private String name;

    private Gender gender;

    private String qualification;

    private String email;

    private Integer fees;

    private String experienceStart;

    private LocalDate leaveStart;

    private LocalDate leaveEnd;

    private Integer rating;

    private Status status;

    private String userName;
    private String password;

    private String specialization;

    private String profileImgUrl;

}

