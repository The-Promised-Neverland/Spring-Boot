package com.ecommerce.ecommerce.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class  ExpiredSessionException extends RuntimeException {

    public ExpiredSessionException() {
        super("Your session has expired. Please log in again.");
    }
}
