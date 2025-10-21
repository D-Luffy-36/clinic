package com.an.DentalClinicSystem.service;


import com.an.DentalClinicSystem.entity.Services;
import com.an.DentalClinicSystem.repository.ServicesRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TreatmentService{
    private final ServicesRepository treatmentServiceRepository;
    public List<Services> getAllTreatmentServices() {
        return treatmentServiceRepository.findAll();
    }
}
