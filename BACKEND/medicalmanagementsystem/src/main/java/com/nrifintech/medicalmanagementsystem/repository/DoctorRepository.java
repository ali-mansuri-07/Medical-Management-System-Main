package com.nrifintech.medicalmanagementsystem.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nrifintech.medicalmanagementsystem.model.Doctor;
import com.nrifintech.medicalmanagementsystem.model.User;
import com.nrifintech.medicalmanagementsystem.utility.enums.Status;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    @Query(value = "SELECT * FROM DOCTOR d WHERE (d.status LIKE 'ACTIVE' OR d.status LIKE 'LEAVE') AND d.id= :doctorId", nativeQuery = true)
    Optional<Doctor> findActiveDoctorById(Long doctorId);

    Page<Doctor> findByNameContainingIgnoreCase(@Param("name") String name, Pageable pageable);

    List<Doctor> findByNameContainingIgnoreCaseAndStatus(@Param("name") String name, Status status);
    List<Doctor> findBySpecializationNameContainingIgnoreCaseAndStatus(@Param("name") String name, Status status);
    Page<Doctor> findByStatus(@Param("status") Status status, Pageable pageable);

    @Query("SELECT d.status FROM Doctor d JOIN d.user u WHERE u.userName= :username")
    Status findStatusFromUsername(@Param("username") String username);

    List<Doctor> findByNameContainingIgnoreCase(String name);

    Doctor findByUser(User user);

    List<Doctor> findBySpecializationNameContainingIgnoreCase(String specialization);
    
    List<Doctor> findBySpecializationNameIgnoreCase(String specialization);
   
   

    Optional<Doctor> findById(Long id);

    @Modifying
    @Query("UPDATE Doctor d SET d.status = 'ACTIVE', d.leaveStart = NULL , d.leaveEnd = NULL WHERE d.leaveStart IS NOT NULL AND  d.leaveEnd IS NOT NULL AND d.leaveEnd < :currentDate ")
    void updateDoctorStatusToActive(@Param("currentDate") LocalDate currentDate);
    @Modifying
    @Query("UPDATE Doctor d SET d.status = 'LEAVE' WHERE d.leaveStart IS NOT NULL AND  d.leaveEnd IS NOT NULL AND d.leaveStart <= :currentDate AND d.leaveEnd > :currentDate")
    void updateDoctorStatusToLeave(@Param("currentDate") LocalDate currentDate);

    // @Query(value = "SELECT s.name AS specialization, COUNT(d.id) AS count FROM Doctor d JOIN Specialization s ON d.specialization_id = s.id GROUP BY s.name", nativeQuery = true)
    // List<Object[]> countDoctorsPerSpecializations();

    @Query(value = "SELECT specialization.name AS specialization, COUNT(doctor.id) AS count FROM Doctor doctor JOIN doctor.specialization specialization GROUP BY specialization")
    List<Object[]> countDoctorsPerSpecialization();

    @Query(value = "SELECT count(*) FROM doctor WHERE status IN('ACTIVE','LEAVE')", nativeQuery = true)
    Integer countDoctor();

    // @Query("SELECT d.leaveStart,d.leaveEnd FROM Doctor d where d.id = :id")
    // List<LocalDate> findUnavailableDoctorDatesById(@Param("id") Long id);

    

}
