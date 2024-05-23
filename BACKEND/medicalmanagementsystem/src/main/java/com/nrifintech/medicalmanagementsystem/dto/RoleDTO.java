package com.nrifintech.medicalmanagementsystem.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Data
public class RoleDTO {

    @Positive(message = "Role ID must be a positive value")
    private Long id;

    @NotBlank(message = "Role name is required")
    private String roleName;

}
