package com.ecommerce.ecommerce.exceptions;


public class IncorrectCredentialsException extends RuntimeException {
    public IncorrectCredentialsException(Throwable cause) {
        super("Invalid email or password.",cause);
    }
}
