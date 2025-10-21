package com.an.DentalClinicSystem.service;


import com.an.DentalClinicSystem.dto.request.UpdateUserRequest;
import com.an.DentalClinicSystem.dto.response.UserResponse;
import com.an.DentalClinicSystem.entity.User;
import com.an.DentalClinicSystem.repository.UserRepository;
import com.an.DentalClinicSystem.utils.JwtUtil;
import com.an.DentalClinicSystem.utils.UpdateUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
public class UserService {

    private final UpdateUtils updateUtils;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            throw new RuntimeException("Authorization header is missing or invalid");
        }
        return bearerToken.substring(7);
    }

    public UserResponse getUserInfo(HttpServletRequest request) {
        // Step 1: Lấy token từ header Authorization
        String jwtToken = getTokenFromRequest(request);

        // Step 2: Giải mã token để lấy username
        String username = jwtUtil.extractUsername(jwtToken);

        // Step 3: Tìm user từ repository
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Step 4: Trả về DTO
        return UserResponse.toUserResponse(user);
    }

    @Transactional
    public UserResponse updateUser(HttpServletRequest request, UpdateUserRequest updateUserRequest) {
        String token = getTokenFromRequest(request);
        String currentUsername = jwtUtil.extractUsername(token);
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Bước 3: Cập nhật thông tin từ request
        UpdateUtils.updateIfChanged(updateUserRequest.getFullName(), user.getFullName(), user::setFullName);
        UpdateUtils.updateIfChanged(updateUserRequest.getPhone(), user.getPhone(), user::setPhone);
        UpdateUtils.updateIfChanged(updateUserRequest.getEmail(), user.getEmail(), user::setEmail);

        // Xử lý đổi username nếu có
        String newUsername = updateUserRequest.getUsername();
        if (newUsername != null && !newUsername.isBlank() && !newUsername.equals(user.getUsername())) {
            user.setUsername(newUsername);
        }
        // Bước 4: Lưu lại
        userRepository.save(user);

        // Bước 5: Trả về DTO
        return UserResponse.toUserResponse(user);

    };
//
//    public UserResponse deleteUser(User user) {
//
//    }
//
//    public UserResponse updateAvatar(User user) {
//
//    }




}
