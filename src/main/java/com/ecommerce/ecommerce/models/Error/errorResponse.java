package com.ecommerce.ecommerce.models.Error;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
public class errorResponse {
    private String message;
    private String error;
    private LocalDateTime timestamp;

    public errorResponse(String message, String error) {
        this.message = message;
        this.error = error;
        this.timestamp = LocalDateTime.now();
    }
}
