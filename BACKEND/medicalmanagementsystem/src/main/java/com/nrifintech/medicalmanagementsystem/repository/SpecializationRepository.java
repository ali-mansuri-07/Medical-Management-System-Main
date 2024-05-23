package com.nrifintech.medicalmanagementsystem.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nrifintech.medicalmanagementsystem.dto.SpecializationDTO;
import com.nrifintech.medicalmanagementsystem.model.Doctor;
import com.nrifintech.medicalmanagementsystem.model.Specialization;

public interface SpecializationRepository extends JpaRepository<Specialization, Long> {

  

    // @Query(value = "SELECT doctor.* FROM Doctor doctor JOIN Specialization
    // specialization ON doctor.specialization_id = specialization.id WHERE
    // LOWER(specialization.name) LIKE LOWER('%:name%')", nativeQuery = true)
    // Page<Doctor> findBySpecializationName(@Param("name") String name, Pageable
    // pageable);



   



    
    @Query(value="SELECT doctor.* FROM Doctor doctor JOIN Specialization specialization ON doctor.specialization_id = specialization.id WHERE LOWER(specialization.name) LIKE LOWER('%:name%') AND d.status = 'ACTIVE'",nativeQuery = true)
   Page<Doctor> findBySpecializationName(@Param("name") String name, Pageable pageable); 
    


    Specialization findByNameIgnoreCase(String name);
    //Specialization findById(Long id);

}
