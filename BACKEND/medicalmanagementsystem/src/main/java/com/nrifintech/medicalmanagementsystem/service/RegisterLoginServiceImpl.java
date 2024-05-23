package com.nrifintech.medicalmanagementsystem.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nrifintech.medicalmanagementsystem.config.JwtGeneratorValidator;
import com.nrifintech.medicalmanagementsystem.dto.JwtAuthenticationResponse;
import com.nrifintech.medicalmanagementsystem.dto.RefreshTokenRequest;
import com.nrifintech.medicalmanagementsystem.dto.UserDTO;
import com.nrifintech.medicalmanagementsystem.dto.UserIdentificationDTO;
import com.nrifintech.medicalmanagementsystem.model.Role;
import com.nrifintech.medicalmanagementsystem.model.User;
import com.nrifintech.medicalmanagementsystem.repository.RoleRepository;
import com.nrifintech.medicalmanagementsystem.repository.UserRepository;

@Service
public class RegisterLoginServiceImpl implements RegisterLoginService{
    
@Autowired
	UserRepository userRepo;
	
	@Autowired
	RoleRepository roleRepo;

	@Autowired
	AuthenticationManager authManager;

	@Autowired
	JwtGeneratorValidator jwtGeneratorValidator;

    @Autowired
    DefaultUserServiceImpl userDetailService;

	@Autowired
	UserIdentificationService userIdentificationService;
	
	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
	public User save(UserDTO userRegisteredDTO) {						// register admin and user using DTO
		Role role = new Role();
		if(userRegisteredDTO.getRole().equals("PATIENT"))
		  role = roleRepo.findByRoleName("ROLE_PATIENT");
		else if(userRegisteredDTO.getRole().equals("DOCTOR"))
		  role = roleRepo.findByRoleName("ROLE_DOCTOR");
		else if(userRegisteredDTO.getRole().equals("ADMIN"))
		 role = roleRepo.findByRoleName("ROLE_ADMIN");
		User user = new User();
		//user.setEmail(userRegisteredDTO.getEmail());
		user.setUserName(userRegisteredDTO.getUserName());
		user.setPassword(passwordEncoder.encode(userRegisteredDTO.getPassword()));
		user.setRole(role);
		
		return userRepo.save(user);
	}


	public JwtAuthenticationResponse login(@RequestBody UserDTO userDto) throws Exception{

		
		try{
			Authentication authentication = authManager.authenticate(
					new UsernamePasswordAuthenticationToken(userDto.getUserName(), userDto.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
            // System.out.println(authentication.getName());
			String jwt = jwtGeneratorValidator.generateToken(authentication);
            
			String jwtRefresh = jwtGeneratorValidator.generateRefreshToken(authentication);
            
			JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
			jwtAuthenticationResponse.setToken(jwt);
			jwtAuthenticationResponse.setRefreshToken(jwtRefresh);

			UserIdentificationDTO userIdentificationDTO = userIdentificationService.convertUserDTOtoUserIdentificationDTO(userDto);
			jwtAuthenticationResponse.setUserIdentificationDTO(userIdentificationDTO);
    
			return jwtAuthenticationResponse;
		} catch(BadCredentialsException e){
			throw new BadCredentialsException(" Invalid Username or Password  !!");
		}
        catch(Exception e)
        {
           //e.printStackTrace();
            throw e;
        }
	}

	


    public JwtAuthenticationResponse refreshToken(String refToken) {
        // String refreshToken = refreshTokenRequest.getToken();
        String username = jwtGeneratorValidator.extractUsername(refToken);
            UserDetails userDetails = userDetailService.loadUserByUsername(username);
        if (jwtGeneratorValidator.validateToken(refToken,userDetails)) {
            
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtGeneratorValidator.generateToken(authentication);
            JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
            jwtAuthenticationResponse.setToken(jwt);
            return jwtAuthenticationResponse;
        } else {
            throw new BadCredentialsException("Please Login again !!");
        }
    }


}