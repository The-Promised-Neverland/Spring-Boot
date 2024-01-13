package com.ecommerce.ecommerce.restControllerAdvice;


import com.ecommerce.ecommerce.models.Error.errorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserRestAdviceController {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<errorResponse> handleIncorrectCredentials(Exception ex) {
        String errorMessage = ex.getMessage();
        String errorCause = ex.getCause() != null ? ex.getCause().toString() : null;
        // Construct your error response
        errorResponse response = new errorResponse(errorMessage, errorCause);

        return new ResponseEntity<errorResponse>(response,HttpStatus.BAD_REQUEST);
    }

}
