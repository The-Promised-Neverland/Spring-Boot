package com.ecommerce.ecommerce.exceptions;

public class AuthenticationFailedException extends RuntimeException{
    public AuthenticationFailedException(Throwable cause) {
        super("Authentication failed. Invalid email or password.", cause);
    }
}
