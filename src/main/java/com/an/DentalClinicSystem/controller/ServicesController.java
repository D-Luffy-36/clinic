package com.an.DentalClinicSystem.controller;

import com.an.DentalClinicSystem.entity.Services;
import com.an.DentalClinicSystem.service.TreatmentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services") // 👉 đặt path ở đây, KHÔNG đặt trong @RestController
@CrossOrigin(origins = "*") // Cho phép frontend gọi API
@AllArgsConstructor
public class ServicesController {

    private final TreatmentService services;

    @GetMapping
    public List<Services> getAllServices() {
        return services.getAllTreatmentServices();
    }
}
