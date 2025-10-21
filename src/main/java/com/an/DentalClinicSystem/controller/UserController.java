package com.an.DentalClinicSystem.controller;

import com.an.DentalClinicSystem.dto.request.UpdateUserRequest;
import com.an.DentalClinicSystem.dto.response.UserResponse;
import com.an.DentalClinicSystem.service.UserService;
import com.an.DentalClinicSystem.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor

public class UserController {

    private final UserService userService;

    @GetMapping("/info")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public ResponseEntity<UserResponse> getUserInfo(HttpServletRequest request) {
        UserResponse response = userService.getUserInfo(request);
        return ResponseEntity.ok(response);
    }


/* <<<<<<<<<<<<<<  ✨ Windsurf Command ⭐ >>>>>>>>>>>>>>>> */
    /**
     * Cập nhật thông tin của người dùng hiện tại
     *
     * @param updateUserRequest thông tin người dùng mới
     * @param request HTTP request
     * @return UserResponse thông tin người dùng sau khi cập nhật
     */
/* <<<<<<<<<<  f89d4c46-d982-4e56-9a45-512944f44597  >>>>>>>>>>> */
    @PutMapping("/update-me")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserRequest updateUserRequest, HttpServletRequest request) {
        UserResponse userResponse = userService.updateUser(request, updateUserRequest);
        return ResponseEntity.ok(userResponse);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser() {
        // TODO: Implement logic
        return ResponseEntity.ok("User deleted");
    }

//    @PutMapping("/avatar")
//    public ResponseEntity<?> updateAvatar(@RequestBody AvatarDto avatarDto) {
//        // TODO: Implement logic
//        return ResponseEntity.ok("Avatar updated");
//    }
}
