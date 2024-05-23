package com.nrifintech.medicalmanagementsystem.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.management.InvalidAttributeValueException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.nrifintech.medicalmanagementsystem.dto.FeedbackDTO;
import com.nrifintech.medicalmanagementsystem.event.DoctorRatingUpdationEvent;
import com.nrifintech.medicalmanagementsystem.model.Appointment;
import com.nrifintech.medicalmanagementsystem.model.Doctor;
import com.nrifintech.medicalmanagementsystem.model.Feedback;
import com.nrifintech.medicalmanagementsystem.repository.DoctorRepository;
import com.nrifintech.medicalmanagementsystem.repository.FeedbackRepository;
import com.nrifintech.medicalmanagementsystem.mapper.FeedbackMapper;


@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private FeedbackMapper feedbackMapper;

        @Autowired
    ApplicationEventPublisher eventPublisher;

    @Override
    public FeedbackDTO giveFeedback(FeedbackDTO feedback) {
        Feedback feedback1 = new Feedback();

        feedback1.setRating(feedback.getRating());
        feedback1.setReview(feedback.getReview());

        Appointment appointment = new Appointment();
        try {
            appointment = appointmentService.getAppointmentById(feedback.getAppointment_id());
            feedback1.setAppointment(appointment);
        } catch (InvalidAttributeValueException e) {
            e.printStackTrace();
        }

        // System.out.println("Feedback : " + feedback1);
        
        feedbackRepository.save(feedback1);

        FeedbackDTO feedback2 = new FeedbackDTO();
        feedback2.setAppointment_id(feedback.getAppointment_id());
        feedback2.setRating(feedback.getRating());
        feedback2.setReview(feedback.getReview());

       
        DoctorRatingUpdationEvent updateEvent=new DoctorRatingUpdationEvent(this, feedback.getRating(),feedback.getAppointment_id());
        eventPublisher.publishEvent(updateEvent);
        return feedback2;
        
    }

    

    public Page<FeedbackDTO> getFeedbackByDoctorId(Long id,int page,int size)
    {
           
        Pageable pageable = PageRequest.of(page, size);        
        Page<FeedbackDTO> feedbackPage = feedbackRepository.findAllByDoctorId(id, pageable);
        // System.out.println(feedbackPage.getContent());
        
         return feedbackPage;
    }

    public List<FeedbackDTO> getFeedbackByPatientId(Long id)
    {
        List<FeedbackDTO> feedbackPage = feedbackRepository.findAllByPatientId(id);

        
        return feedbackPage;
    }


   

    public List<FeedbackDTO> getDoctorFeedback(Long doctorId) {
        List<Feedback> feedbackList = feedbackRepository.findByDoctorId(doctorId);
        return feedbackList.stream()
                .map(feedbackMapper::convertToFeedbackDTO)
                .collect(Collectors.toList());
    }

}


