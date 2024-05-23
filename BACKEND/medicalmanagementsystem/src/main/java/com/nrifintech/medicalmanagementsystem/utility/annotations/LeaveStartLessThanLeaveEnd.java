package com.nrifintech.medicalmanagementsystem.utility.annotations;

import java.lang.annotation.Target;

import com.nrifintech.medicalmanagementsystem.utility.validators.LeaveStartLessThanLeaveEndValidation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Target( ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { LeaveStartLessThanLeaveEndValidation.class })
public @interface LeaveStartLessThanLeaveEnd {
    String message() default "Start date must be before end date";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
