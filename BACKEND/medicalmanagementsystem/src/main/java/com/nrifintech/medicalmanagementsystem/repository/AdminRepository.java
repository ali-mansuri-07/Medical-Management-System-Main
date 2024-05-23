package com.nrifintech.medicalmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nrifintech.medicalmanagementsystem.model.Doctor;

public interface AdminRepository extends JpaRepository<Doctor, Long> {

}
