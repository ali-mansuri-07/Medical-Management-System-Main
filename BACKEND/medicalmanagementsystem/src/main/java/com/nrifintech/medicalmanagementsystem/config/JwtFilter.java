package com.nrifintech.medicalmanagementsystem.config;


import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.nrifintech.medicalmanagementsystem.Exception.UnauthorizedAccessException;
import com.nrifintech.medicalmanagementsystem.repository.UserRepository;
import com.nrifintech.medicalmanagementsystem.service.DefaultUserService;
import com.nrifintech.medicalmanagementsystem.service.GenerateResponseService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	DefaultUserService defaultUserService;

	@Autowired
	UserRepository userRepo;

	@Autowired
	@Qualifier("handlerExceptionResolver")
	private HandlerExceptionResolver exceptionResolver;

	@Autowired
	GenerateResponseService generateResponseService;

	private Logger logger = LoggerFactory.getLogger(OncePerRequestFilter.class);

	@Autowired
	JwtGeneratorValidator jwtgenVal;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authorizationHeader = request.getHeader("Authorization");

		String token = null;
		String userName = null;
try{
	// System.out.println(request.getRequestURI());
	// System.out.println(request.getRequestURI().startsWith("/auth"));
	if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ") && !request.getRequestURI().startsWith("/auth")) {
			token = authorizationHeader.substring(7);
			userName = jwtgenVal.extractUsername(token);
	}

		if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			UserDetails userDetails = defaultUserService.loadUserByUsername(userName);
		
			
			if (jwtgenVal.validateToken(token, userDetails)) {

				if(defaultUserService.getUserRole(userName).equals("ROLE_PATIENT") || defaultUserService.getUserRole(userName).equals("ROLE_ADMIN") || (defaultUserService.getUserRole(userName).equals("ROLE_DOCTOR") && !defaultUserService.getStatus(userName).equals("INACTIVE")))
				{
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = jwtgenVal.getAuthenticationToken(token, SecurityContextHolder.getContext().getAuthentication(), userDetails);
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				}
				else{
					throw new UnauthorizedAccessException("Not authorized",927);
				}
			}
			else {
                logger.info("Validation failed!!");
            }
		}
		filterChain.doFilter(request, response);
	}
	catch(ExpiredJwtException | SignatureException | UnauthorizedAccessException ex)
	{
		
		exceptionResolver.resolveException(request, response, null, ex);
	}
}
}
