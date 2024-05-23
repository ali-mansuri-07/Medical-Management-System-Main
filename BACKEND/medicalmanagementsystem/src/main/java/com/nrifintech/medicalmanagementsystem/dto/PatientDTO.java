package com.nrifintech.medicalmanagementsystem.dto;

import com.nrifintech.medicalmanagementsystem.utility.enums.BloodGroup;
import com.nrifintech.medicalmanagementsystem.utility.enums.Gender;

public class PatientDTO {
    private String name;
    private Gender gender;
    private String email;
    private String birthYear;
    private BloodGroup bloodGroup;
    private String contactNumber;
    private Long userId;

    public PatientDTO() {
    }

    public PatientDTO(String name, Gender gender, String email, String birthYear, BloodGroup bloodGroup,
            String contactNumber, Long userId) {
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.birthYear = birthYear;
        this.bloodGroup = bloodGroup;
        this.contactNumber = contactNumber;
        this.userId = userId;
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Gender getGender() {
        return gender;
    }
    public void setGender(Gender gender) {
        this.gender = gender;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getBirthYear() {
        return birthYear;
    }
    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }
    public BloodGroup getBloodGroup() {
        return bloodGroup;
    }
    public void setBloodGroup(BloodGroup bloodGroup) {
        this.bloodGroup = bloodGroup;
    }
    public String getContactNumber() {
        return contactNumber;
    }
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

}
