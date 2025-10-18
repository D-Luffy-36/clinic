package com.an.DentalClinicSystem.service;

import com.an.DentalClinicSystem.dto.request.LoginRequest;
import com.an.DentalClinicSystem.dto.request.RegisterRequest;
import com.an.DentalClinicSystem.dto.response.LoginResponse;
import com.an.DentalClinicSystem.dto.response.RegisterResponse;
import com.an.DentalClinicSystem.dto.response.UserResponse;
import com.an.DentalClinicSystem.entity.Role;
import com.an.DentalClinicSystem.entity.User;
import com.an.DentalClinicSystem.exception.AlreadyExistsException;
import com.an.DentalClinicSystem.mapper.UserMapper;
import com.an.DentalClinicSystem.repository.RoleRepository;
import com.an.DentalClinicSystem.repository.UserRepository;
import com.an.DentalClinicSystem.service.validator.UserValidator;
import com.an.DentalClinicSystem.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder; // inject từ PasswordConfig
    private final JwtUtil jwtUtil;
    private final UserValidator userValidator;
    private final UserMapper userMapper;


    public Role getDefaultRole() {
        return roleRepository.findByName("patient")
                .orElseThrow(() -> new RuntimeException("Default role not found"));
    }


    public User findUserByUserName(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException("User not found with username: " + username));
    }


    public RegisterResponse registerUser(RegisterRequest registerRequest) {
        // 1️⃣ Kiểm tra trùng username/email
        userValidator.validateRegister(registerRequest);

        // 2️⃣ Lấy role mặc định
        Role defaultRole = getDefaultRole();
        // 3️⃣ Chuyển DTO -> Entity
        User user = User.builder()
                .username(registerRequest.getUsername())
                .passwordHash(passwordEncoder.encode(registerRequest.getPassword()))
                .email(registerRequest.getEmail())
                .role(defaultRole)
                .isActive(true)
                .build();


        // 4️⃣ Lưu user
        userRepository.save(user);

        UserResponse userResponse = userMapper.toResponse(user);

        // 5️⃣ Trả response DTO
        return RegisterResponse.builder()
                .message("Register successfully")
                .user(userResponse)
                .build();
    }


    public LoginResponse login(LoginRequest loginRequest) {

        User currentUser = findUserByUserName(loginRequest.getUsername());

        userValidator.validateLogin(loginRequest, currentUser);

        CustomUserDetails currentUserDetails = CustomUserDetails
                .builder()
                .user(currentUser)
                .build();

        String token = jwtUtil.generateToken(currentUserDetails);

        return LoginResponse.builder()
                .message("Đăng nhập thành công")
                .token(token)
                .build();
    };


};






