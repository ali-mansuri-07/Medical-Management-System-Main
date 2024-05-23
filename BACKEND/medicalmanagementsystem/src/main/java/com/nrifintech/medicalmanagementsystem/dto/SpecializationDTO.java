package com.nrifintech.medicalmanagementsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;



@Data
public class SpecializationDTO {

    @PositiveOrZero(message = "Specialization ID must be a positive value or zero")
    private Long id;

    @NotBlank(message = "Specialization name is required")
    private String name;

    private String description;
    private String imageUrl;
}
