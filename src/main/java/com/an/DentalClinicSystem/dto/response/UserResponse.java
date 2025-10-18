package com.an.DentalClinicSystem.dto.response;

import com.an.DentalClinicSystem.entity.User;
import com.an.DentalClinicSystem.utils.FormatUtils;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String phone;
    private Boolean isActive;
    private String roleName;

    public static UserResponse toUserResponse(User user) {
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
