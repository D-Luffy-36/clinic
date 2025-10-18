package com.an.DentalClinicSystem.common;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
public class CustomErrorResponse {
    private int status;
    private String error;
    private String message;
    private String path;
    private LocalDateTime timestamp;
}
