package com.ecommerce.ecommerce.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class TokenNotFoundException extends RuntimeException{
    public TokenNotFoundException() {
        super("JWT token missing");
    }
}
