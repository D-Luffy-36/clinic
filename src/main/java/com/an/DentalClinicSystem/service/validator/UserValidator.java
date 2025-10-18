package com.an.DentalClinicSystem.service.validator;

import com.an.DentalClinicSystem.dto.request.LoginRequest;
import com.an.DentalClinicSystem.dto.request.RegisterRequest;
import com.an.DentalClinicSystem.entity.User;
import com.an.DentalClinicSystem.exception.AlreadyExistsException;

import com.an.DentalClinicSystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // inject từ PasswordConfig

    public void validateRegister(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AlreadyExistsException("username đã tồn tại");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AlreadyExistsException("email đã tồn tại");
        }
    }

    public void validateLogin(LoginRequest request, User user) {
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Sai thông tin đăng nhập");
        }
    }
}
