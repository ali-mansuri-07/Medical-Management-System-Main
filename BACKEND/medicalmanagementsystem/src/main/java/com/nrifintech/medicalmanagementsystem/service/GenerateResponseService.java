package com.nrifintech.medicalmanagementsystem.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class GenerateResponseService {

    public ResponseEntity<Object> generateResponse(String message, HttpStatus st, Object responseobj) {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", st.value());
		map.put("message", message);
		// map.put("data", responseobj);
		map.put("cookies", responseobj);
		return new ResponseEntity<Object>(map, st);
	}

	public ResponseEntity<Object> generateResponse(String message, HttpStatusCode st){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", st.value());
		map.put("message", message);
		return new ResponseEntity<Object>(map, st);
	}

    
}
