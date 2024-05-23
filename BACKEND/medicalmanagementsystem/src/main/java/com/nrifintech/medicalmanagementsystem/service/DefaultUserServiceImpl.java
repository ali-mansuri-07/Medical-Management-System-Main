package com.nrifintech.medicalmanagementsystem.service;


import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.BadCredentialsException;
// import org.springframework.security.authentication.BadCredentialsException;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
// import org.springframework.web.bind.annotation.ExceptionHandler;
// import org.springframework.web.bind.annotation.RequestBody;

import com.nrifintech.medicalmanagementsystem.model.Role;
import com.nrifintech.medicalmanagementsystem.model.User;
import com.nrifintech.medicalmanagementsystem.repository.DoctorRepository;
import com.nrifintech.medicalmanagementsystem.repository.RoleRepository;
import com.nrifintech.medicalmanagementsystem.repository.UserRepository;


@Service
public class DefaultUserServiceImpl implements DefaultUserService{ // Custom UserService implementation

	@Autowired
	UserRepository userRepo;
	
	@Autowired
	RoleRepository roleRepo;

	@Autowired
	DoctorRepository doctorRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		 User user = userRepo.findByUserName(username);
         Set<Role> roleSet=new HashSet<>();
         roleSet.add(user.getRole());
	     return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), mapRolesToAuthorities(roleSet));
	}
	
	public Collection<? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles){
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toList());
	}

	public String getStatus(String username){
        String status = doctorRepository.findStatusFromUsername(username).toString();
        return status;
    }

	public String getUserRole(String userName){
		User user = userRepo.findByUserName(userName);
		String userRole = user.getRole().getRoleName();
		return userRole;
	}
	
}
