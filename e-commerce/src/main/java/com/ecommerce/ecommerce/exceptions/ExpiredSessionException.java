package com.ecommerce.ecommerce.exceptions;

public class  ExpiredSessionException extends RuntimeException {

    public ExpiredSessionException() {
        super("Your session has expired. Please log in again.");
    }
}
