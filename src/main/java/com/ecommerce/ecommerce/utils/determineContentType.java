package com.ecommerce.ecommerce.utils;


import org.springframework.stereotype.Component;

@Component
public class determineContentType {
    public String determine(String fileName) {
        // Determine and return the content type based on the file extension
        // You can extend this logic to support various file types
        if (fileName.endsWith(".pdf")) {
            return "application/pdf";
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (fileName.endsWith(".png")) {
            return "image/png";
        } else {
            // Default content type for other file types
            return "application/octet-stream";
        }
    }
}
