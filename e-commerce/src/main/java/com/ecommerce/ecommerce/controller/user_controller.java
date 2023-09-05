package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.DTO.userDTO;
import com.ecommerce.ecommerce.services.UserServices.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class user_controller {

    @Autowired
    private userService userService;

    @GetMapping("/all")
    public ResponseEntity<List<userDTO>> getUsers(){
        List<userDTO> users = userService.getAllusers(); // Fetch users (employees) from your service
        return ResponseEntity.ok(users);
    }

    @PostMapping("/all/email")
    public ResponseEntity<userDTO> getUsersByEmail(@RequestBody userDTO userDTO){
        userDTO users = userService.getEmail(userDTO.getEmail()); // Fetch users (employees) from your service
        return ResponseEntity.ok(users);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<userDTO> getUserById(@PathVariable("id") String userID){
        userDTO user = userService.getUserbyID(userID);
        return ResponseEntity.ok(user);
    }
}
