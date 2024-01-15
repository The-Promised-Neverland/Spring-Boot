package com.ecommerce.ecommerce.controller.adminRoutes;

import com.ecommerce.ecommerce.utils.determineContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/admin")
public class file_controller_ADMIN {
    @Autowired
    private determineContentType determineContentType;

    private String uploadDirectory;

    public file_controller_ADMIN(@Value("${upload.directory}") String uploadDirectory) {
        this.uploadDirectory=uploadDirectory;

    }
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
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
            String contentType = determineContentType.determine(fileName);
            // Return the file as a ResponseEntity
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
        } else {
            // Handle the case when the file does not exist
            return ResponseEntity.notFound().build();
        }
    }
}
