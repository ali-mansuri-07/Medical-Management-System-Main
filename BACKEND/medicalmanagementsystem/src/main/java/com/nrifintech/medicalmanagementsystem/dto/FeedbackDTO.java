package com.nrifintech.medicalmanagementsystem.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackDTO {
    private Integer rating;
    private String review;
    private Long appointment_id;
}
