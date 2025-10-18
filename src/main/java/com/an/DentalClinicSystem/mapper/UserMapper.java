package com.an.DentalClinicSystem.mapper;

import com.an.DentalClinicSystem.dto.response.UserResponse;
import com.an.DentalClinicSystem.entity.User;
import com.an.DentalClinicSystem.utils.FormatUtils;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(FormatUtils.safe(user.getUsername()))
                .fullName(FormatUtils.safe(user.getFullName()))
                .email(FormatUtils.safe(user.getEmail()))
                .phone(FormatUtils.safe(user.getPhone()))
                .roleName(user.getRole() != null ? FormatUtils.safe(user.getRole().getName()) : "")
                .isActive(user.getIsActive())
                .build();
    }
}
