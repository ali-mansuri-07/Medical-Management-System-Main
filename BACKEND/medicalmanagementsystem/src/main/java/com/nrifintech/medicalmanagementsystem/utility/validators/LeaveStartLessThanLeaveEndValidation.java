package com.nrifintech.medicalmanagementsystem.utility.validators;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Year;

import org.springframework.beans.factory.annotation.Autowired;

import com.nrifintech.medicalmanagementsystem.model.Doctor;
import com.nrifintech.medicalmanagementsystem.utility.annotations.LeaveStartLessThanLeaveEnd;
import com.nrifintech.medicalmanagementsystem.utility.annotations.PastDate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LeaveStartLessThanLeaveEndValidation  implements ConstraintValidator<LeaveStartLessThanLeaveEnd, Doctor>{
    
    @Autowired
    private LocalDate startDate;


    @Override
    public void initialize(LeaveStartLessThanLeaveEnd constraintAnnotation)
    {
        
    }

    @Override
    public boolean isValid(Doctor value, ConstraintValidatorContext context) {
        if( value.getLeaveEnd().isBefore(value.getLeaveStart()))
        return false;
        return true;
    }

   
   

}