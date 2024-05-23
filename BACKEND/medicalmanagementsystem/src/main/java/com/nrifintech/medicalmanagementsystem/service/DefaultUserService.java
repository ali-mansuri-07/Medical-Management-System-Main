package com.nrifintech.medicalmanagementsystem.service;

import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.web.bind.annotation.RequestBody;

// import com.jwt.implementation.dto.JwtAuthenticationResponse;
// import com.jwt.implementation.model.User;
// import com.jwt.implementation.model.UserDTO;

public interface DefaultUserService extends UserDetailsService{
	public String getStatus(String username);
	public String getUserRole(String userName);
}
