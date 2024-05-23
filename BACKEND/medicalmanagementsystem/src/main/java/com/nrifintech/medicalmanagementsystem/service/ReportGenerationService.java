package com.nrifintech.medicalmanagementsystem.service;

import java.time.LocalDate;
import java.util.List;

import com.nrifintech.medicalmanagementsystem.dto.AppointmentStatusDistribution;
import com.nrifintech.medicalmanagementsystem.dto.AppointmentStatusDistributionWithSpecialization;
import com.nrifintech.medicalmanagementsystem.dto.DoctorDistribution;
import com.nrifintech.medicalmanagementsystem.dto.PatientDistribution;
import com.nrifintech.medicalmanagementsystem.dto.TopDepartmentAndDoctor;
import com.nrifintech.medicalmanagementsystem.dto.TopTimeSlotsWithHighestNumberOfAppointments;

public interface ReportGenerationService {
    public List<AppointmentStatusDistribution> getAppointmentStatusDistribution(LocalDate startDate, LocalDate endDate);
    public List<AppointmentStatusDistributionWithSpecialization> getAppointmentStatusDistributionWithSpecialization(LocalDate startDate, LocalDate endDate);
     public List<TopTimeSlotsWithHighestNumberOfAppointments> getTopTimeSlotsWithHighestNumberOfAppointments(LocalDate startDate, LocalDate endDate);
    public List<TopDepartmentAndDoctor> getTopDepartmentAndDoctor(LocalDate startDate, LocalDate endDate);
    public List<PatientDistribution> getPatientDistribution(LocalDate startDate, LocalDate endDate);
    
     public List<DoctorDistribution> getDoctorDistribution(LocalDate startDate, LocalDate endDate);
}