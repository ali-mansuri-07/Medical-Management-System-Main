package com.nrifintech.medicalmanagementsystem.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nrifintech.medicalmanagementsystem.dto.FeedbackDTO;
import com.nrifintech.medicalmanagementsystem.model.Doctor;
import com.nrifintech.medicalmanagementsystem.model.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, Long>{

    List<Feedback> findAllByAppointment_Doctor(Doctor doctor);

    @Query("SELECT NEW com.nrifintech.medicalmanagementsystem.dto.FeedbackDTO(f.rating,f.review,a.id) FROM Feedback f JOIN f.appointment a JOIN a.doctor d WHERE d.id=:id AND f.review IS NOT NULL ORDER BY f.rating DESC ")
    Page<FeedbackDTO> findAllByDoctorId(Long id, Pageable pageable);

    @Query("SELECT NEW com.nrifintech.medicalmanagementsystem.dto.FeedbackDTO(f.rating,f.review,a.id) FROM Feedback f JOIN f.appointment a JOIN a.patient p WHERE p.id=:id AND f.review IS NOT NULL ORDER BY f.rating ")
    List<FeedbackDTO> findAllByPatientId(Long id);
    @Query("SELECT COUNT(f.id) FROM Feedback f JOIN f.appointment a JOIN a.doctor d WHERE d.id = :doctorId")
    Integer countFeedbackForDoctor(@Param("doctorId") Long doctorId);
    
 

    @Query(value = "SELECT f.id, f.appointment_id, f.rating, f.review FROM Feedback f " +
    "JOIN Appointment a ON f.appointment_id = a.id " +
    "JOIN Doctor d ON a.doctor_id = d.id " +
    "WHERE d.id = :doctorId ORDER BY f.rating DESC", nativeQuery = true)
    List<Feedback> findByDoctorId(@Param("doctorId") Long doctorId);

}
