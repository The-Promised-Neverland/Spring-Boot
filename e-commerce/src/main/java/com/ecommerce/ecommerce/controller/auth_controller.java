package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.DTO.jwtResponseDTO;
import com.ecommerce.ecommerce.DTO.authRequestDTO;
import com.ecommerce.ecommerce.security.JWT.JWT_Helper;
import com.ecommerce.ecommerce.services.JWTServices.jwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/authentication")
public class auth_controller {

    @Autowired
    private jwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWT_Helper jwtUtils;



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody authRequestDTO request) {
        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            if (auth.isAuthenticated()) {
                UserDetails userDetails = jwtService.loadUserByUsername(request.getEmail());
                String token = jwtUtils.generateToken(userDetails);
                jwtResponseDTO response = new jwtResponseDTO(request.getEmail(), token);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                // Authentication failed (incorrect username or password)
                return new ResponseEntity<>("Incorrect username or password", HttpStatus.UNAUTHORIZED);
            }
        } catch (BadCredentialsException e) {
            // Authentication failed due to bad credentials (incorrect username or password)
            return new ResponseEntity<>("Incorrect username or password", HttpStatus.UNAUTHORIZED);
        }
    }

}
v 