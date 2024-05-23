package com.nrifintech.medicalmanagementsystem.utility.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.nrifintech.medicalmanagementsystem.utility.validators.PastDateValidation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target( {ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { PastDateValidation.class })
public @interface PastDate {
    String message() default "Invalid date";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
