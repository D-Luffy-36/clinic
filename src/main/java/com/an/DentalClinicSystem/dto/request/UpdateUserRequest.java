package com.an.DentalClinicSystem.dto.request;

import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class UpdateUserRequest {
    private String fullName;
    private String phone;
    private String email;
    private String username;
}
