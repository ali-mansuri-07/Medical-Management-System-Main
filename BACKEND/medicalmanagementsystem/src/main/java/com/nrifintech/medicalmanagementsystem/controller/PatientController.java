package com.nrifintech.medicalmanagementsystem.controller;

import javax.management.InvalidAttributeValueException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nrifintech.medicalmanagementsystem.dto.DoctorDTO;
import com.nrifintech.medicalmanagementsystem.dto.FeedbackDTO;
import com.nrifintech.medicalmanagementsystem.dto.PatientDTO;
import com.nrifintech.medicalmanagementsystem.mapper.DoctorMapper;
import com.nrifintech.medicalmanagementsystem.model.Patient;
import com.nrifintech.medicalmanagementsystem.service.DoctorService;
import com.nrifintech.medicalmanagementsystem.service.FeedbackService;
import com.nrifintech.medicalmanagementsystem.service.GenerateResponseService;
import com.nrifintech.medicalmanagementsystem.service.PatientService;

@RestController
@RequestMapping("patient")
public class PatientController {

	@Autowired
	private PatientService patientService;

	@Autowired
	private GenerateResponseService generateResponseService;

	@Autowired
	private FeedbackService feedbackService;

	@Autowired
	private DoctorService doctorService;
	@Autowired
	private DoctorMapper doctorMapper;

	@PostMapping("/add")
	public ResponseEntity<Object> addPatient(@RequestBody PatientDTO patientDTO) throws InvalidAttributeValueException {

		Patient patient = patientService.create(patientDTO);
		return generateResponseService.generateResponse("Patient saved successfully : " + patient.getId(),
				HttpStatus.CREATED, patient);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<Object> updatePatient(@PathVariable Long id, @RequestBody PatientDTO patientDTO)
			throws InvalidAttributeValueException {

		Patient patient = patientService.update(id, patientDTO);
		return generateResponseService.generateResponse("Patient updated successfully : " + patient.getId(),
				HttpStatus.OK, patient);
	}

	@GetMapping("/view/{id}") 
	public ResponseEntity<Object> viewPatient(@PathVariable Long id) {

		Patient patient = patientService.read(id);
		return generateResponseService.generateResponse("Patient view successfully : " + patient.getId(), HttpStatus.OK,
				patient);
	}

	@PostMapping("/give-feedback")
	public ResponseEntity<Object> giveFeeback(@RequestBody FeedbackDTO feedbackDTO) {
		FeedbackDTO feedback = feedbackService.giveFeedback(feedbackDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(feedback);
	}

	@GetMapping("/get-feedback")
	public ResponseEntity<Object> getFeedback(@RequestParam Long id,
	@RequestParam(required = false, defaultValue = "0") Integer page,
	@RequestParam(required = false, defaultValue = "10") Integer size)
	{
		return generateResponseService.generateResponse("Feedback fetched successfully : ", HttpStatus.OK,
		feedbackService.getFeedbackByDoctorId(id,page,size));
	}

	@GetMapping("/fetch-feedback")
	public ResponseEntity<Object> getFeedbackUsingPatientId(@RequestParam Long id)
	{
		return generateResponseService.generateResponse("Feedback fetched successfully : ", HttpStatus.OK,
		feedbackService.getFeedbackByPatientId(id));
	}

	 @GetMapping("/get-particular-doctor/{id}")
    public DoctorDTO getParticularDoctor(@PathVariable Long id) {
        return doctorMapper.convertToDoctorDTO(doctorService.getActiveDoctorById(id).get());
    }

}
