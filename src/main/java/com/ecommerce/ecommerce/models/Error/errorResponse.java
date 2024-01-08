package com.ecommerce.ecommerce.models.Error;


import lombok.AllArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
public class errorResponse {
    private String message;
    private String error;
}
