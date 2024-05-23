package com.nrifintech.medicalmanagementsystem.repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.eclipse.angus.mail.imap.protocol.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.nrifintech.medicalmanagementsystem.dto.AppointmentSearchResponseDTO;
import com.nrifintech.medicalmanagementsystem.dto.AppointmentStatusDistribution;
import com.nrifintech.medicalmanagementsystem.dto.AppointmentStatusDistributionWithSpecialization;
import com.nrifintech.medicalmanagementsystem.dto.DoctorDistribution;
import com.nrifintech.medicalmanagementsystem.dto.PatientDistribution;
import com.nrifintech.medicalmanagementsystem.dto.TopDepartmentAndDoctor;
import com.nrifintech.medicalmanagementsystem.dto.TopTimeSlotsWithHighestNumberOfAppointments;

import jakarta.persistence.LockModeType;
import com.nrifintech.medicalmanagementsystem.model.Appointment;
import com.nrifintech.medicalmanagementsystem.utility.enums.AppointmentStatus;
import com.nrifintech.medicalmanagementsystem.utility.enums.Slot;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

       
        @Query("SELECT DISTINCT NEW com.nrifintech.medicalmanagementsystem.dto.DoctorDistribution(" +
        "d.name, " +
        "s.name, " +
        "MAX(d.fees) OVER (PARTITION BY s.name), " +
        "FIRST_VALUE(d.name) OVER (PARTITION BY s.name ORDER BY d.fees DESC ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING), " +
        "MIN(d.fees) OVER (PARTITION BY s.name), " +
        "LAST_VALUE(d.name) OVER (PARTITION BY s.name ORDER BY d.fees DESC ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING), " +
        "AVG(d.fees) OVER (PARTITION BY s.name)) " +
    "FROM Appointment a " +
    "JOIN a.doctor d " +
    "JOIN d.specialization s " +
    "JOIN a.patient p " +
    "WHERE a.appDate BETWEEN :startDate AND :endDate ORDER BY s.name")
List<DoctorDistribution> findDoctorDistribution(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);


        @Query("SELECT new com.nrifintech.medicalmanagementsystem.dto.PatientDistribution(" +
                        "COUNT(*), " +
                        "p.name, " +
                        "p.gender, " +
                        "p.bloodGroup, " +
                        "MIN(a.appDate), " +
                        "MAX(a.appDate)) " +
                        "FROM Appointment a " +
                        "JOIN a.patient p " +
                        "WHERE " +
                        "a.appDate BETWEEN :startDate AND :endDate " +
                        "GROUP BY p.name,p.bloodGroup, p.gender")
        List<PatientDistribution> findPatientDistribution(@Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

        @Query("WITH DepartmentRating AS ( " +
                        "SELECT " +
                        "s.name AS specialization, " +
                        "COUNT(*) AS total_appointments, " +
                        "AVG(d.rating) AS Avg_rating, " +
                        "d.name AS Doctor_name, " +
                        "FIRST_VALUE(d.name) OVER (PARTITION BY s.name ORDER BY AVG(d.rating) DESC) AS Top_rated_doctor, "
                        +
                        "FIRST_VALUE(d.name) OVER (PARTITION BY s.name ORDER BY COUNT(*) DESC) AS Top_appointment_doctor "
                        +
                        "FROM " +
                        "Appointment a " +
                        "JOIN " +
                        "a.doctor d " +
                        "JOIN " +
                        "d.specialization s " +
                        "WHERE " +
                        "a.appDate BETWEEN :startDate AND :endDate " +
                        "GROUP BY " +
                        "s.name,d.name " +
                        ") " +
                        "SELECT DISTINCT new com.nrifintech.medicalmanagementsystem.dto.TopDepartmentAndDoctor(specialization, SUM(total_appointments) OVER(PARTITION BY specialization),AVG(Avg_rating) OVER(PARTITION BY specialization), Top_rated_doctor, Top_appointment_doctor) "
                        +
                        "FROM " +
                        "DepartmentRating ")
        List<TopDepartmentAndDoctor> findTopDepartmentAndDoctor(@Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

        @Query("WITH RankedAppointments AS ( " +
                        "SELECT " +
                        "TO_CHAR(a.appDate, 'Day') AS dayOfWeek, " +
                        "a.slot AS appointmentTime, " +
                        "s.name AS specialization, " +
                        "DENSE_RANK() OVER (PARTITION BY TO_CHAR(a.appDate, 'Day') ORDER BY COUNT(*) DESC) AS ranked, "
                        +
                        "COUNT(*) AS appointmentCount, " +
                        "AVG(d.rating) AS averageDoctorRating " +
                        "FROM " +
                        "Appointment a " +
                        "JOIN " +
                        "a.doctor d " +
                        "JOIN " +
                        "d.specialization s " +
                        "WHERE " +
                        "a.appDate BETWEEN :startDate AND :endDate " +
                        "GROUP BY " +
                        "TO_CHAR(a.appDate, 'Day'), a.slot, s.name " +
                        ") " +
                        "SELECT new com.nrifintech.medicalmanagementsystem.dto.TopTimeSlotsWithHighestNumberOfAppointments(dayOfWeek, appointmentTime, specialization, appointmentCount, averageDoctorRating) "
                        +
                        "FROM " +
                        "RankedAppointments " +
                        "WHERE " +
                        "appointmentCount > 1 AND " +
                        "ranked = 1 " +
                        "ORDER BY " +
                        "appointmentCount DESC, dayOfWeek, appointmentTime, specialization")
        List<TopTimeSlotsWithHighestNumberOfAppointments> findTopTimeSlotsWithHighestNumberOfAppointments(
                        @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

        

        @Query("SELECT new com.nrifintech.medicalmanagementsystem.dto.AppointmentStatusDistribution(a.appDate, " +
                        "COUNT(CASE WHEN a.appStatus LIKE 'PENDING' THEN 1 END), " +
                        "COUNT(CASE WHEN a.appStatus LIKE 'CANCELLED' THEN 1 END), " +
                        "COUNT(CASE WHEN a.appStatus LIKE 'COMPLETED' THEN 1 END)) " +
                        "FROM Appointment a " +
                        "WHERE " +
                        "a.appDate BETWEEN :startDate AND :endDate " +
                        "GROUP BY " +
                        "a.appDate " +
                        "ORDER BY " +
                        "a.appDate")
        List<AppointmentStatusDistribution> findAppointmentStatusDistribution(@Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);
        // Appointment save(Appointment appointment);

        @Query("SELECT new com.nrifintech.medicalmanagementsystem.dto.AppointmentStatusDistributionWithSpecialization(s.name, "
                        +
                        "COUNT(CASE WHEN a.appStatus LIKE 'PENDING' THEN 1 END), " +
                        "COUNT(CASE WHEN a.appStatus LIKE 'CANCELLED' THEN 1 END), " +
                        "COUNT(CASE WHEN a.appStatus LIKE 'COMPLETED' THEN 1 END)) " +
                        "FROM Appointment a " +
                        "JOIN a.doctor d " +
                        "JOIN d.specialization s " +
                        "WHERE " +
                        "a.appDate BETWEEN :startDate AND :endDate " +
                        "GROUP BY " +
                        "s.name " +
                        "ORDER BY " +
                        "s.name")
        List<AppointmentStatusDistributionWithSpecialization> findAppointmentStatusDistributionWithSpecialization(
                        @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

        @Query(value = "SELECT * FROM APPOINTMENT a WHERE a.doctor_id = :doctorId AND a.slot = :slot AND a.app_date LIKE :date AND a.app_status LIKE 'PENDING'", nativeQuery = true)
        Optional<Appointment> findPendingAppointmentsByDoctorIdAndSlotAndDate(Long doctorId, Integer slot,
                        LocalDate date);
        
        @Query("SELECT new com.nrifintech.medicalmanagementsystem.dto.AppointmentSearchResponseDTO(a.id, d.id, d.name, p.id, p.name, a.appDate, a.slot, a.appStatus, s.name) "
                        +
                        "FROM Appointment a " +
                        "JOIN a.doctor d " +
                        "JOIN a.patient p " +
                        "JOIN d.specialization s " +
                        "WHERE (:doctorId IS NULL OR a.doctor.id = :doctorId) " +
                        "AND (:patientId IS NULL OR a.patient.id = :patientId) " +
                        "AND (:doctorName IS NULL OR LOWER(d.name) LIKE LOWER(CONCAT('%', :doctorName, '%'))) " +
                        "AND (:slot IS NULL OR a.slot = :slot) " +
                        "AND (:startDate IS NULL OR a.appDate >= :startDate) " +
                        "AND (:endDate IS NULL OR a.appDate <= :endDate) " +
                        "AND (:specialization IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :specialization, '%'))) "
                        +
                        "AND (:appointmentStatus IS NULL OR a.appStatus = :appointmentStatus) " +
                        "ORDER BY a.appDate DESC, a.slot DESC")
        List<AppointmentSearchResponseDTO> findAppointments(@Param("doctorId") Long doctorId,
                        @Param("patientId") Long patientId,
                        @Param("doctorName") String doctorName,
                        @Param("slot") Integer slot,
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate,
                        @Param("specialization") String specialization,
                        @Param("appointmentStatus") AppointmentStatus appointmentStatus);


        @Modifying
    @Query("UPDATE Appointment a SET a.appStatus = 'COMPLETED' WHERE a.appStatus = 'PENDING' AND (a.appDate < :currentDate OR (a.appDate = :currentDate AND a.slot <= :currentTime))")
    void updateAppointmentStatus(@Param("currentDate") LocalDate currentDate, @Param("currentTime") Integer currentTime);

    @Query(value = "SELECT count(*) FROM appointment WHERE app_status = 'COMPLETED'", nativeQuery = true)
    Integer countCompletedAppointments();

    @Query(value = "SELECT count(*) FROM appointment WHERE app_status = 'PENDING'", nativeQuery = true)
    Integer countPendingAppointments();

    List<Appointment> findByAppStatusAndAppDateBetweenOrderByAppDateAsc(AppointmentStatus status, LocalDate oneWeekAgo, LocalDate today);

   
}

