package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.CustomUserDetails.CustomUserDetails;
import com.ecommerce.ecommerce.DTO.authRequestDTO;
import com.ecommerce.ecommerce.DTO.authResponseDTO;
import com.ecommerce.ecommerce.DTO.userDTO;
import com.ecommerce.ecommerce.security.JWT.JWT_Helper;
import com.ecommerce.ecommerce.services.JWTServices.jwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
                CustomUserDetails customUserDetails = jwtService.loadUserByUsername(request.getEmail());
                String token = jwtUtils.generateToken(customUserDetails);
                userDTO user = new userDTO();
                user.setId(customUserDetails.getId());
                user.setEmail(customUserDetails.getEmail());
                user.setIsAdmin(customUserDetails.getIsAdmin());
                authResponseDTO authResponseDTO=new authResponseDTO();
                authResponseDTO.setToken(token);
                return new ResponseEntity<>(authResponseDTO, HttpStatus.ACCEPTED);
            } else {
                // Authentication failed (incorrect username or password)
                return new ResponseEntity<>("Incorrect username or password", HttpStatus.UNAUTHORIZED);
            }
        } catch (BadCredentialsException e) {
            // Authentication failed due to bad credentials (incorrect username or password)
            return new ResponseEntity<>("Incorrect username or password", HttpStatus.UNAUTHORIZED);
        }
    }


    @GetMapping("/auth/getData")
    public ResponseEntity<?> getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof CustomUserDetails) {
            // Cast the principal to your CustomUserDetails class
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

            // Access custom fields
            String id = customUserDetails.getId();
            String email = customUserDetails.getEmail();
            Boolean isAdmin = customUserDetails.getIsAdmin();
            String name= customUserDetails.getName();

            // Create a userDTO object with the custom fields
            userDTO user = new userDTO();
            user.setId(id);
            user.setName(name);
            user.setEmail(email);
            user.setIsAdmin(isAdmin);

            // Return the userDTO in the response
            return ResponseEntity.ok(user);
        }

        // Handle the case when the principal is not a CustomUserDetails object or authentication failed
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
