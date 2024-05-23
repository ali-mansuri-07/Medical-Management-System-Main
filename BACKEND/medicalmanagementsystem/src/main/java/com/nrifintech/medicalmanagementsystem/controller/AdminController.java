package com.nrifintech.medicalmanagementsystem.controller;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

import com.nrifintech.medicalmanagementsystem.Exception.DoctorNotCreated;
import com.nrifintech.medicalmanagementsystem.dto.DashboardDataDTO;
import com.nrifintech.medicalmanagementsystem.dto.DoctorDTO;
import com.nrifintech.medicalmanagementsystem.dto.PatientDTO;
import com.nrifintech.medicalmanagementsystem.dto.SpecializationDTO;
import com.nrifintech.medicalmanagementsystem.mapper.DoctorMapper;
import com.nrifintech.medicalmanagementsystem.model.Doctor;
import com.nrifintech.medicalmanagementsystem.model.Specialization;
import com.nrifintech.medicalmanagementsystem.service.AppointmentService;
import com.nrifintech.medicalmanagementsystem.service.DoctorService;
import com.nrifintech.medicalmanagementsystem.service.GenerateResponseService;
import com.nrifintech.medicalmanagementsystem.service.ReportGenerationService;
import com.nrifintech.medicalmanagementsystem.utility.enums.Status;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping("admin")
public class AdminController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    DoctorMapper doctorMapper;

     @Autowired
    AppointmentService appointmentService;

    @Autowired
    GenerateResponseService generateResponseService;

    @Autowired
    ReportGenerationService reportGenerationService;


    @PostMapping("/add-doctor")
    public ResponseEntity<Object> addDoctor(@RequestBody DoctorDTO doctor) throws MessagingException{
        try {
            Doctor savedDoctor = doctorService.addDoctor(doctor);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedDoctor);
        } catch (DoctorNotCreated e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add doctor");
        }
    }

    @PutMapping("/update-doctor/{id}")
    public ResponseEntity<Object> updateDoctor(@PathVariable Long id, @RequestBody DoctorDTO doctor) {
        try{
        Doctor updatedDoctor = doctorService.updateDoctor(id, doctor);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedDoctor);
        }catch(DataAccessException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update doctor");
        }

    }

    @GetMapping("/get-doctors")
    public Page<DoctorDTO> getAllDoctors(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return doctorService.getAllDoctors(pageable);
    }

    @GetMapping("/get-doctors-by-status")
    public Page<DoctorDTO> getDoctorsByStatus(@RequestParam Status status, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return doctorService.findByStatus(status, pageable);
    }

    @PostMapping("/add-specialization")
    public ResponseEntity<Object> addSpecialization(@RequestBody SpecializationDTO specializationDTO) {
        Specialization specialization = doctorService.addSpecialization(specializationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(specialization);
    }

    @PostMapping("/update-specialization/{id}")
    public ResponseEntity<Object> updateSpecialization(@PathVariable("id") Long id , @RequestBody SpecializationDTO specializationDTO) {
        Specialization specialization = doctorService.updateSpecialization(id , specializationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(specialization);
    }

    @GetMapping("/doctor-specialization-counts")
    public ResponseEntity<Map<String, Long>> getDoctorSpecializationCounts() throws Exception {
        Map<String, Long> specializationCounts = doctorService.getDoctorSpecializationCounts();
        if (specializationCounts.isEmpty()) {
            throw new Exception("No specialization counts found");
        }
        return ResponseEntity.ok(specializationCounts);
    }

    @GetMapping("/getAppointmentStatusDistribution")
    public ResponseEntity<Object> getAppointmentStatusDistribution(@RequestParam LocalDate startDate,@RequestParam LocalDate endDate) {
        return generateResponseService.generateResponse(
            "Status distribution fetched",
            HttpStatus.OK,
            reportGenerationService.getAppointmentStatusDistribution(startDate, endDate));
    }
    @GetMapping("/getAppointmentStatusDistributionWithSpecialization")
    public ResponseEntity<Object> getAppointmentStatusDistributionWithSpecializations(@RequestParam LocalDate startDate,@RequestParam LocalDate endDate) {
        return generateResponseService.generateResponse(
            "Status distribution fetched",
            HttpStatus.OK,
            reportGenerationService.getAppointmentStatusDistributionWithSpecialization(startDate, endDate));
    }
    @GetMapping("/getTopTimeSlotsWithHighestNumberOfAppointments")
    public ResponseEntity<Object> getTopTimeSlotsWithHighestNumberOfAppointments(@RequestParam LocalDate startDate,@RequestParam LocalDate endDate) {
        return generateResponseService.generateResponse(
            "Status distribution fetched",
            HttpStatus.OK,
            reportGenerationService.getTopTimeSlotsWithHighestNumberOfAppointments(startDate, endDate));
    }
    @GetMapping("/getTopDepartmentAndDoctor")
    public ResponseEntity<Object> getTopDepartmentAndDoctor(@RequestParam LocalDate startDate,@RequestParam LocalDate endDate) {
        return generateResponseService.generateResponse(
            "Status distribution fetched",
            HttpStatus.OK,
            reportGenerationService.getTopDepartmentAndDoctor(startDate, endDate));
    }
    @GetMapping("/getPatientDistribution")
    public ResponseEntity<Object> getPatientDistribution(@RequestParam LocalDate startDate,@RequestParam LocalDate endDate) {
        return generateResponseService.generateResponse(
            "Status distribution fetched",
            HttpStatus.OK,
            reportGenerationService.getPatientDistribution(startDate, endDate));
    }
    @GetMapping("/getDoctorDistribution")
    public ResponseEntity<Object> getDoctorDistribution(@RequestParam LocalDate startDate,@RequestParam LocalDate endDate) {
        return generateResponseService.generateResponse(
            "Doctor distribution fetched",
            HttpStatus.OK,
            reportGenerationService.getDoctorDistribution(startDate, endDate));
    }
    @GetMapping("/get-dashboard-data")
    public ResponseEntity<DashboardDataDTO> getDashboardData(){
        DashboardDataDTO data = doctorService.getDashboardData();
        return  ResponseEntity.ok(data);
    }

    @GetMapping("/get-doctors/count")
    public long getTotalDoctorsCount() {
        return doctorService.getAllDoctors(Pageable.unpaged()).getTotalElements();
    }

    @GetMapping("/get-patients")
    public Page<PatientDTO> getAllPatients(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return doctorService.getAllPatients(pageable);
    }

}


