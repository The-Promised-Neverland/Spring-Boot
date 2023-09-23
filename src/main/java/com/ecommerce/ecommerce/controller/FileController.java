package com.ecommerce.ecommerce.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api")
public class FileController {
    private String uploadDirectory;

    public FileController(@Value("${upload.directory}") String uploadDirectory) {
        this.uploadDirectory=uploadDirectory;

    }
    @PostMapping("/upload")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // Check if the file is empty
            if (file.isEmpty()) {
                return new ResponseEntity<>("File is empty", HttpStatus.OK) ;
            }
            // Get the file's original name
            String originalFilename = file.getOriginalFilename();
            // Specify the path where you want to store the file
            String filePath = uploadDirectory + "/" + originalFilename;

            // Save the file to the specified path
            file.transferTo(new File(filePath));
            return new ResponseEntity<>("File uploaded successfully!", HttpStatus.OK) ;
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("File upload failed: " + e.getMessage(), HttpStatus.OK) ;
        }
    }


    @GetMapping("/download/{fileName}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Resource> downloadFile(@PathVariable("fileName") String fileName) {
        // Construct the full path to the file in the Docker volume
        String filePath = uploadDirectory + "/" + fileName;
        // Create a Resource object from the file
        Resource resource = new FileSystemResource(filePath);

        // Check if the file exists
        if (resource.exists()) {
            // Define the content type based on file type
            String contentType = determineContentType(fileName);
            // Return the file as a ResponseEntity
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
        } else {
            // Handle the case when the file does not exist
            return ResponseEntity.notFound().build();
        }
    }

    private String determineContentType(String fileName) {
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
