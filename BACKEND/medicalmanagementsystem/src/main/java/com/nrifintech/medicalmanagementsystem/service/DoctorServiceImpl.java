package com.nrifintech.medicalmanagementsystem.service;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.print.Doc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nrifintech.medicalmanagementsystem.Exception.DoctorNotCreated;
import com.nrifintech.medicalmanagementsystem.Exception.DoctorNotFoundException;
import com.nrifintech.medicalmanagementsystem.Exception.specializationAlreadyExistsException;
import com.nrifintech.medicalmanagementsystem.dto.DashboardDataDTO;
import com.nrifintech.medicalmanagementsystem.dto.DoctorDTO;
import com.nrifintech.medicalmanagementsystem.dto.PatientDTO;
import com.nrifintech.medicalmanagementsystem.dto.SpecializationDTO;
import com.nrifintech.medicalmanagementsystem.event.DoctorAdditionEmailEvent;
import com.nrifintech.medicalmanagementsystem.event.EmailSendingEvent;
import com.nrifintech.medicalmanagementsystem.mapper.DoctorMapper;
import com.nrifintech.medicalmanagementsystem.mapper.PatientMapper;
import com.nrifintech.medicalmanagementsystem.model.Appointment;
import com.nrifintech.medicalmanagementsystem.model.Doctor;
import com.nrifintech.medicalmanagementsystem.model.Patient;
import com.nrifintech.medicalmanagementsystem.model.Role;
import com.nrifintech.medicalmanagementsystem.model.Specialization;
import com.nrifintech.medicalmanagementsystem.model.User;
import com.nrifintech.medicalmanagementsystem.repository.AppointmentRepository;
import com.nrifintech.medicalmanagementsystem.repository.DoctorRepository;
import com.nrifintech.medicalmanagementsystem.repository.FeedbackRepository;
import com.nrifintech.medicalmanagementsystem.repository.PatientRepository;
import com.nrifintech.medicalmanagementsystem.repository.RoleRepository;
import com.nrifintech.medicalmanagementsystem.repository.SpecializationRepository;
import com.nrifintech.medicalmanagementsystem.repository.UserRepository;
import com.nrifintech.medicalmanagementsystem.utility.enums.AppointmentStatus;
import com.nrifintech.medicalmanagementsystem.utility.enums.Status;

import jakarta.mail.MessagingException;


@Service
public class DoctorServiceImpl implements DoctorService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SpecializationRepository specializationRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorMapper doctorMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailSendingService emailSendingService;


    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Transactional
    public Optional<Doctor> getActiveDoctorById(Long doctorId) {
        // System.out.println(doctorRepository.findActiveDoctorById(doctorId).get());
        return doctorRepository.findActiveDoctorById(doctorId);
    }

    // add doctor
    public Doctor addDoctor(DoctorDTO doctor) throws MessagingException {

        Role optionalRole = roleRepository.findByRoleName("ROLE_DOCTOR");
        if (optionalRole == null) { // if role doesn't exist
            Role role = new Role();
            role.setRoleName("ROLE_DOCTOR");
            optionalRole = roleRepository.save(role);
        }

        User user = new User();
        user.setUserName(doctor.getUserName());
        user.setPassword(passwordEncoder.encode(doctor.getPassword()));
        user.setRole(optionalRole);

        this.userRepository.save(user);

        Specialization optionalSpecialization = specializationRepository
                .findByNameIgnoreCase(doctor.getSpecialization());

        Doctor doctor1 = new Doctor();
        doctor1.setEmail(doctor.getEmail());
        doctor1.setExperienceStart(doctor.getExperienceStart());
        doctor1.setName(doctor.getName());
        doctor1.setQualification(doctor.getQualification());
        doctor1.setFees(doctor.getFees());
        doctor1.setLeaveStart(doctor.getLeaveStart());
        doctor1.setLeaveEnd(doctor.getLeaveEnd());
        doctor1.setRating(doctor.getRating());
        doctor1.setStatus(doctor.getStatus());
        doctor1.setUser(user);
        doctor1.setGender(doctor.getGender());
        doctor1.setSpecialization(optionalSpecialization);
        doctor1.setProfileImgUrl(doctor.getProfileImgUrl());

        Doctor savedDoctor = doctorRepository.save(doctor1);
        if (savedDoctor == null) {
            throw new DoctorNotCreated("Failed to create doctor");
        }
        DoctorAdditionEmailEvent event = new DoctorAdditionEmailEvent(this, doctor.getUserName(), doctor.getPassword(), doctor.getEmail());

                // event.setRecipient(appointment.getPatient().getEmail())
                // event.setSubject("APPOINTMENT BOOKING CONFIRMATION")
                // event.setMessage(emailMessage);
                eventPublisher.publishEvent(event);
        
        return savedDoctor;
    }

   

    // update doctor
    public Doctor updateDoctor(Long id, DoctorDTO updatedDoctorDto) {
        try {
            Optional<Doctor> optionalDoctor = doctorRepository.findById(id);
            if (optionalDoctor.isEmpty()) {
                throw new IllegalArgumentException("Doctor not found with ID: " + id);
            }

            Doctor existingDoctor = optionalDoctor.get();

            // existingDoctor.setName(updatedDoctorDto.getName());
            // existingDoctor.setGender(updatedDoctorDto.getGender());
            existingDoctor.setQualification(updatedDoctorDto.getQualification());
            existingDoctor.setEmail(updatedDoctorDto.getEmail());
            existingDoctor.setFees(updatedDoctorDto.getFees());
            // existingDoctor.setExperienceStart(updatedDoctorDto.getExperienceStart());
            existingDoctor.setLeaveStart(updatedDoctorDto.getLeaveStart());
            existingDoctor.setLeaveEnd(updatedDoctorDto.getLeaveEnd());
            existingDoctor.setRating(updatedDoctorDto.getRating());
            existingDoctor.setStatus(updatedDoctorDto.getStatus());
            existingDoctor.setProfileImgUrl(updatedDoctorDto.getProfileImgUrl());

            // if (!existingDoctor.getSpecialization().getName()
            // .equalsIgnoreCase(updatedDoctorDto.getSpecialization().toString())) {
            // Specialization specialization = specializationRepository
            // .findByName(updatedDoctorDto.getSpecialization().toString());
            // if (specialization == null) {
            // specialization = new Specialization();
            // specialization.setName(updatedDoctorDto.getSpecialization().toString().toLowerCase());
            // specialization = specializationRepository.save(specialization);
            // }
            // existingDoctor.setSpecialization(specialization);
            // }

            return doctorRepository.save(existingDoctor);
        } catch (DataAccessException e) {
            throw new RuntimeException("Unable to update doctor with ID :" + id, e);
        }
    }

    @Override
    public Page<DoctorDTO> searchDoctors(String query, Pageable pageable, String sortBy, String sortDir) {
        // Fetch doctors by name containing the query string
        List<Doctor> doctorsByName = doctorRepository.findByNameContainingIgnoreCase(query);

        // Fetch doctors by specialization name containing the query string
        List<Doctor> doctorsBySpecialization = doctorRepository.findBySpecializationNameContainingIgnoreCase(query);

        // Combine the results from both queries
        Set<Doctor> uniqueDoctors = new HashSet<>();
        uniqueDoctors.addAll(doctorsByName);
        uniqueDoctors.addAll(doctorsBySpecialization);

        // Filter out inactive doctors
        List<Doctor> result = uniqueDoctors.stream()
                .filter(doctor -> doctor.getStatus().equals(Status.ACTIVE) || doctor.getStatus().equals(Status.LEAVE))
                .collect(Collectors.toList());

        // Sort the combined result based on the provided criteria
        result.sort((doc1, doc2) -> {
            if (sortBy.equals("fees")) {
                return sortDir.equals("asc") ? doc1.getFees().compareTo(doc2.getFees())
                        : doc2.getFees().compareTo(doc1.getFees());
            } else if (sortBy.equals("rating")) {
                return sortDir.equals("asc") ? doc1.getRating().compareTo(doc2.getRating())
                        : doc2.getRating().compareTo(doc1.getRating());
            }
            // Handle other sorting criteria if needed
            return 0;
        });

        // Pagination logic
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), result.size());
        List<DoctorDTO> doctorDTOs = result.subList(start, end).stream()
                .map(doctorMapper::convertToDoctorDTO)
                .collect(Collectors.toList());

        // Create a Page object containing the paginated and sorted list of doctors
        return new PageImpl<>(doctorDTOs, pageable, result.size());
    }

    // fetch doctors by specialization
    @Override
    public Page<DoctorDTO> findDoctorsBySpecialization(String specialization, Pageable pageable) {
        List<Doctor> uniqueDoctors = doctorRepository.findBySpecializationNameIgnoreCase(specialization);

        List<Doctor> doctors = uniqueDoctors.stream()
                .filter(doctor -> doctor.getStatus().equals(Status.ACTIVE))
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), doctors.size());
        List<Doctor> paginatedDoctors = doctors.subList(start, end);

        List<DoctorDTO> doctorDTOs = paginatedDoctors.stream()
                .map(doctorMapper::convertToDoctorDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(doctorDTOs, pageable, doctors.size());
    }

    // fetch doctors list
    @Override
    public Page<DoctorDTO> getAllDoctors(Pageable pageable) {
        Page<Doctor> doctorsPage = doctorRepository.findAll(pageable);
        return doctorsPage.map(doctorMapper::convertToDoctorDTO);
    }

    // find doctors by status
    @Override
    public Page<DoctorDTO> findByStatus(Status status, Pageable pageable) {
        Page<Doctor> doctorsPage = doctorRepository.findByStatus(status, pageable);
        List<DoctorDTO> doctorDTOs = doctorsPage.getContent().stream()
                .map(doctorMapper::convertToDoctorDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(doctorDTOs, pageable, doctorsPage.getTotalElements());
    }

    @Transactional
    public void updateDoctorStatus() {
        doctorRepository.updateDoctorStatusToActive(LocalDate.now());
        doctorRepository.updateDoctorStatusToLeave(LocalDate.now());
    }

    @Transactional
    public void updateDoctorRating(Integer newRating, Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).get();
        Long doctorId = appointment.getDoctor().getId();
        Integer count = feedbackRepository.countFeedbackForDoctor(doctorId);
       
        Integer totalRating = ((appointment.getDoctor().getRating()) * (count - 1)) + newRating;

        Integer updatedRating = totalRating / count;
        // System.out.println(updatedRating);
        Doctor updatedDoctor = appointment.getDoctor();
        updatedDoctor.setRating(updatedRating);
        doctorRepository.save(updatedDoctor);
    }

    @Override
    public Page<DoctorDTO> searchAndFilterDoctors(String searchQuery, Status status, Pageable pageable) {
        
        List<Doctor> doctorsByName = doctorRepository.findByNameContainingIgnoreCaseAndStatus(searchQuery, status);
        List<Doctor> doctorsBySpecialization = doctorRepository.findBySpecializationNameContainingIgnoreCaseAndStatus(searchQuery, status);
        // System.out.println(doctorsByName);
        // System.out.println("specializatin" + doctorsBySpecialization);
      
        Set<Doctor> uniqueDoctors = doctorsByName.stream()
                .collect(Collectors.toSet());
        uniqueDoctors.addAll(doctorsBySpecialization);

     
        List<Doctor> result = uniqueDoctors.stream()
                .filter(doctor -> doctor.getStatus().equals(status))
                .collect(Collectors.toList());

       
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), result.size());
        List<DoctorDTO> doctorDTOs = result.subList(start, end).stream()
                .map(doctorMapper::convertToDoctorDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(doctorDTOs, pageable, result.size());
    }

    // Method to fetch count of doctors per specialization
    public Map<String, Long> getDoctorSpecializationCounts() {
        List<Object[]> specializationCounts = doctorRepository.countDoctorsPerSpecialization();
        Map<String, Long> specializationMap = new HashMap<>();
        for (Object[] row : specializationCounts) {
            String specialization = (String) row[0];
            Long count = (Long) row[1];
            specializationMap.put(specialization, count);
        }
        return specializationMap;
    }

    

    @Override
    public Page<DoctorDTO> searchDoctorsAdmin(String query, Pageable pageable) {
        List<Doctor> doctorsByName = doctorRepository.findByNameContainingIgnoreCase(query);
        List<Doctor> doctorsBySpecialization = doctorRepository.findBySpecializationNameContainingIgnoreCase(query);

        Set<Doctor> uniqueDoctorsSet = Stream.concat(doctorsByName.stream(), doctorsBySpecialization.stream())
        .collect(Collectors.toSet());

        List<Doctor> result = new ArrayList<>(uniqueDoctorsSet);

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), result.size());
        List<DoctorDTO> doctorDTOs = result.subList(start, end).stream()
                .map(doctorMapper::convertToDoctorDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(doctorDTOs, pageable, result.size());
    }
    public Doctor getDoctorById(Long id) throws DoctorNotFoundException {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(DoctorNotFoundException::new);
        return doctor;
    }

    public Specialization addSpecialization(SpecializationDTO specializationDTO) {
        Specialization specialization1 = specializationRepository.findByNameIgnoreCase(specializationDTO.getName());
        // System.out.println(specialization1);

        if (specialization1 != null) {
            throw new specializationAlreadyExistsException(
                    "Specialization already exists with name" + specializationDTO.getName());
        }

        Specialization specialization = new Specialization();
        specialization.setName(specializationDTO.getName());
        specialization.setDescription(specializationDTO.getDescription());
        specialization.setImageUrl(specializationDTO.getImageUrl());

        return specializationRepository.save(specialization);
    }

    public Specialization updateSpecialization(Long id, SpecializationDTO specializationDTO) {
        Specialization specialization = specializationRepository.findById(id).get();

        if (specialization == null) {
            throw new IllegalArgumentException(
                    "Specialization not found with name and id " + specializationDTO.getName() + ", " + id);
        }

        specialization.setName(specializationDTO.getName());
        specialization.setDescription(specializationDTO.getDescription());
        specialization.setImageUrl(specializationDTO.getImageUrl());

        return specializationRepository.save(specialization);
    }

    public DashboardDataDTO getDashboardData() {
        Integer countPendingAppointment = appointmentRepository.countPendingAppointments();
        Integer countCompletedAppointment = appointmentRepository.countCompletedAppointments();
        Integer countDoctors = doctorRepository.countDoctor();
        Integer countPatients = patientRepository.countPatient();

        LocalDate today = LocalDate.now();
        LocalDate oneWeekAgo = today.minusDays(7);
        List<Appointment> completedAppointments = appointmentRepository
                .findByAppStatusAndAppDateBetweenOrderByAppDateAsc(AppointmentStatus.COMPLETED, oneWeekAgo, today);
        Map<LocalDate, Long> dailyCompletedAppointments = completedAppointments.stream()
                .collect(Collectors.groupingBy(Appointment::getAppDate, Collectors.counting()));

          LinkedHashMap<String, Integer> dailyCompletedAppointmentsMap = new LinkedHashMap<>();
        dailyCompletedAppointments.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .forEachOrdered(entry -> {
                String dayOfWeek = entry.getKey().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
                dailyCompletedAppointmentsMap.put(dayOfWeek, entry.getValue().intValue());
            });
            

        DashboardDataDTO dashboardDataDTO = new DashboardDataDTO();
        dashboardDataDTO.setCompletedAppointmentCount(countCompletedAppointment);
        dashboardDataDTO.setPendingAppointmentCount(countPendingAppointment);
        dashboardDataDTO.setTotalDoctorCount(countDoctors);
        dashboardDataDTO.setTotalPatientCount(countPatients);
        dashboardDataDTO.setDailyCompletedAppointments(dailyCompletedAppointmentsMap);

        return dashboardDataDTO;
    }

    // public List<LocalDate> getUnavailableDoctorDatesById(Long id) throws DoctorNotFoundException {
    //     List<LocalDate> unavailableDates= doctorRepository.findUnavailableDoctorDatesById(id);
    //     return unavailableDates;
    // } 

    @Override
    public Page<PatientDTO> getAllPatients(Pageable pageable) {
        Page<Patient> patients = patientRepository.findAll(pageable);
        return patients.map(PatientMapper::convertToPatientDTO);
    }
  
}
