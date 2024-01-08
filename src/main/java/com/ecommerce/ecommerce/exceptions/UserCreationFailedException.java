package com.ecommerce.ecommerce.exceptions;

public class UserCreationFailedException extends RuntimeException{

    public UserCreationFailedException(String email, Throwable cause) {
        super("User creation failed. Please try again later.",cause);
    }
}
