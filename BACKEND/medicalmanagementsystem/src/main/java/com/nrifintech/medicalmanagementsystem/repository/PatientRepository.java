package com.nrifintech.medicalmanagementsystem.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.nrifintech.medicalmanagementsystem.model.Patient;
import com.nrifintech.medicalmanagementsystem.model.User;

import jakarta.persistence.LockModeType;
import java.util.List;




public interface PatientRepository extends JpaRepository<Patient, Long>{
    
   Patient findByUser(User user);
   @Query(value = "SELECT count(*) FROM patient", nativeQuery = true)
   Integer countPatient();
}
