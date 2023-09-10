package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.models.Users.userDTO;
import com.ecommerce.ecommerce.services.UserServices.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class user_controller {

    @Autowired
    private userService userService;

    @GetMapping("/auth/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<userDTO>> getUsers(){
        List<userDTO> users = userService.getAllusers(); // Fetch users (employees) from your service
        return ResponseEntity.ok(users);
    }

    @PostMapping("/auth/email")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<userDTO> getUsersByEmail(@RequestBody userDTO userDTO){
        userDTO users = userService.getEmail(userDTO.getEmail()); // Fetch users (employees) from your service
        return ResponseEntity.ok(users);
    }
}
