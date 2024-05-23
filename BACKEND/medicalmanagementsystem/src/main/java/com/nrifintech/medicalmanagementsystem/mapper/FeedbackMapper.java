package com.nrifintech.medicalmanagementsystem.mapper;

import org.springframework.stereotype.Component;


import com.nrifintech.medicalmanagementsystem.dto.FeedbackDTO;
import com.nrifintech.medicalmanagementsystem.model.Feedback;

@Component
public class FeedbackMapper {
    public FeedbackDTO convertToFeedbackDTO(Feedback feedback) {
        FeedbackDTO feedbackDTO = new FeedbackDTO();
        feedbackDTO.setAppointment_id(feedback.getAppointment().getId());
        feedbackDTO.setRating(feedback.getRating());
        feedbackDTO.setReview(feedback.getReview());
       
        return feedbackDTO;
    }
}
