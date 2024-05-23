package com.nrifintech.medicalmanagementsystem.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.nrifintech.medicalmanagementsystem.dto.FeedbackDTO;
import com.nrifintech.medicalmanagementsystem.model.Feedback;

public interface FeedbackService {

    FeedbackDTO giveFeedback(FeedbackDTO feedback);
    public Page<FeedbackDTO> getFeedbackByDoctorId(Long id,int page,int size);
    public List<FeedbackDTO> getFeedbackByPatientId(Long id);
    // List<FeedbackDTO> getFeedbackByDoctorId(Long id);
    
    List<FeedbackDTO> getDoctorFeedback(Long doctorId);
}
