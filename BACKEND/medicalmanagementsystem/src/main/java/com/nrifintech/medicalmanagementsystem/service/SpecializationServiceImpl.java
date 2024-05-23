package com.nrifintech.medicalmanagementsystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nrifintech.medicalmanagementsystem.model.Specialization;
import com.nrifintech.medicalmanagementsystem.repository.SpecializationRepository;


@Service
public class SpecializationServiceImpl implements SpecializationService{

    @Autowired
    private SpecializationRepository specializationRepository;

    @Override
    public List<Specialization> getSpecializations() {        
        List<Specialization> specializations = specializationRepository.findAll();
        return specializations;
    }
}
