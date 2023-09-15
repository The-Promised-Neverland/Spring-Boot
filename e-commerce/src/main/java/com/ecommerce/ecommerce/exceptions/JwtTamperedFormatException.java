package com.ecommerce.ecommerce.exceptions;

public class JwtTamperedFormatException extends RuntimeException{
    public JwtTamperedFormatException() {
        super("JWT has been tampered!!!");
    }
}
