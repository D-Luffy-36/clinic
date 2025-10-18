package com.an.DentalClinicSystem.dto.response;
import com.an.DentalClinicSystem.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class RegisterResponse {
    private UserResponse user;
    private String message;
}
