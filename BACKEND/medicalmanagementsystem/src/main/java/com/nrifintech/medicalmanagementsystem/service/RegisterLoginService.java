package com.nrifintech.medicalmanagementsystem.service;

import com.nrifintech.medicalmanagementsystem.dto.JwtAuthenticationResponse;
import com.nrifintech.medicalmanagementsystem.dto.RefreshTokenRequest;
import com.nrifintech.medicalmanagementsystem.dto.UserDTO;
import com.nrifintech.medicalmanagementsystem.model.User;

public interface RegisterLoginService {
    User save(UserDTO userRegisteredDTO);
	JwtAuthenticationResponse login(UserDTO userDto) throws Exception;
    JwtAuthenticationResponse refreshToken(String refToken);
}
