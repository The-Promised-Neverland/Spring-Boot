package com.ecommerce.ecommerce.exceptions;

public class UserUpdateRequestFailed extends RuntimeException{

    public UserUpdateRequestFailed(String email, Throwable cause) {
        super("Update failed for user " + email,cause);
    }
}
