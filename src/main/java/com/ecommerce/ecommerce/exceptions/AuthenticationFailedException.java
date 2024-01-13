package com.ecommerce.ecommerce.exceptions;

import javax.naming.AuthenticationException;

public class AuthenticationFailedException extends RuntimeException {
    public AuthenticationFailedException(Throwable cause) {
        super("Authentication failed due to server error. Please try again or after some time.",cause);
    }
}
