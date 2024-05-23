package com.nrifintech.medicalmanagementsystem.utility.validators;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Year;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.nrifintech.medicalmanagementsystem.utility.annotations.PastDate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public class PastDateValidation implements ConstraintValidator<PastDate, Object>{
    
    @Autowired
    private LocalDate startDate;


    @Override
    public void initialize(PastDate constraintAnnotation)
    {
        
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if(value == null)
            return true;

        if(value instanceof String)
            return Integer.parseInt(((String) value))<=Year.now().getValue();
            //return startDate.getYear()<=value && value<=Year.now().getValue();

        if(value instanceof LocalDate)
        {
            //Integer year=Integer.parseInt(((String) value).substring(0,4));
            //Integer year=((LocalDate)value).isBefore(startDate);
            //return startDate.getYear()<=year && year<=Year.now().getValue();
            return ((LocalDate)value).isAfter(startDate);
        }
        return false;
    }

   
   

    
}
