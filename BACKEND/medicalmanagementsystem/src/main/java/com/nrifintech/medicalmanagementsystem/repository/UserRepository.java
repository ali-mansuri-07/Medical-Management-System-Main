package com.nrifintech.medicalmanagementsystem.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nrifintech.medicalmanagementsystem.model.User;

public interface UserRepository extends JpaRepository<User, Long>{

	
	User findByUserName(String userName);
	Boolean existsByUserName(String userName);
}
