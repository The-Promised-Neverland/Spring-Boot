package com.ecommerce.ecommerce.exceptions;

public class UnauthorizedAccessException extends RuntimeException {

    public UnauthorizedAccessException() {
        super("Authenticated Users only.");
    }
}
