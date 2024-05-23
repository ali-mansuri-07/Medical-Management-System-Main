package com.nrifintech.medicalmanagementsystem.controller;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nrifintech.medicalmanagementsystem.Exception.UsernameAlreadyExistsException;
import com.nrifintech.medicalmanagementsystem.config.JwtGeneratorValidator;
import com.nrifintech.medicalmanagementsystem.dto.UserDTO;
import com.nrifintech.medicalmanagementsystem.model.User;
import com.nrifintech.medicalmanagementsystem.repository.UserRepository;
import com.nrifintech.medicalmanagementsystem.service.DefaultUserService;
import com.nrifintech.medicalmanagementsystem.service.GenerateResponseService;
import com.nrifintech.medicalmanagementsystem.service.RegisterLoginService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class AuthorizationController {

	@Autowired
	UserRepository userRepo;

	@Autowired
	AuthenticationManager authManager;

	@Autowired
	JwtGeneratorValidator jwtGenVal;
	
	@Autowired
	BCryptPasswordEncoder bcCryptPasswordEncoder;
	
	@Autowired
	RegisterLoginService registerLoginService;

	@Autowired
	GenerateResponseService generateResponseService;

	@Autowired
	DefaultUserService defaultUserService;

	@PostMapping("/auth/registration")
	public ResponseEntity<Object> registerUser(@RequestBody UserDTO userDto) {
		if (userRepo.existsByUserName(userDto.getUserName())) {
			throw new UsernameAlreadyExistsException("Username already exists: " + userDto.getUserName());
        }
		
		User users =  registerLoginService.save(userDto);
		
		if (users.equals(null))
			return generateResponseService.generateResponse("Not able to save user ", HttpStatus.BAD_REQUEST, userDto);
		else
			return generateResponseService.generateResponse("User saved successfully : " + users.getId(), HttpStatus.OK, users);
	}

	@PostMapping("/auth/login")
	public ResponseEntity<Object> generateJwtToken(@RequestBody UserDTO userDto) throws Exception{
		return generateResponseService.generateResponse("Login Successful!", HttpStatus.OK, registerLoginService.login(userDto));
		
	}

	@PostMapping("/auth/refresh")
	public ResponseEntity<Object> refreshJwtToken(@RequestBody String refToken) throws Exception{
		return generateResponseService.generateResponse("Refresh token !", HttpStatus.OK,(registerLoginService.refreshToken(refToken)));
	}

	
	
	@GetMapping("/current-user")
	public ResponseEntity<String> getCurrentUser(HttpServletRequest req) {
        String currentUser = req.getUserPrincipal().getName();
        String jsonCurrentUser = "{\"currentUser\": \"" + currentUser + "\"}";
        return ResponseEntity.ok(jsonCurrentUser);
    }


	@GetMapping("/auth/welcomeAdmin")
	public String welcome(Principal p) {
		return "Welcome Admin "+p.getName();
	}

	@GetMapping("/auth/welcomeDoctor")
	public String welcomeDoctor() {
		return "Welcome Doctor ";
	}

	@GetMapping("/welcomePatient")
	public String welcomePatient(Principal p) {
		return "Welcome Patient "+p.getName();
	}

}
