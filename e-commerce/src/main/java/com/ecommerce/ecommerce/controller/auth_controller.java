package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.CustomUserDetails.CustomUserDetails;
import com.ecommerce.ecommerce.DTO.*;
import com.ecommerce.ecommerce.security.JWT.JWT_Helper;
import com.ecommerce.ecommerce.services.JWTServices.jwtService;
import com.ecommerce.ecommerce.services.UserServices.userService;
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

    @Autowired
    private userService userService;

    /**
     * @POST
     * @Body - email,password
     * @Response - id,name,email,isAdmin,token
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody loginRequestDTO request) {
        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            if (auth.isAuthenticated()) {
                CustomUserDetails customUserDetails = jwtService.loadUserByUsername(request.getEmail());
                String token = jwtUtils.generateToken(customUserDetails);

                authResponseDTO response = new authResponseDTO(customUserDetails.getId(),
                                                               customUserDetails.getName(),
                                                               customUserDetails.getEmail(),
                                                               customUserDetails.getIsAdmin(),
                                                               token);
                return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
            } else {
                // Authentication failed (incorrect username or password)
                return new ResponseEntity<>("Incorrect username or password", HttpStatus.UNAUTHORIZED);
            }
        } catch (BadCredentialsException e) {
            // Authentication failed due to bad credentials (incorrect username or password)
            return new ResponseEntity<>("Incorrect username or password", HttpStatus.UNAUTHORIZED);
        }
    }


    /**
     * @GET
     * @Response - Current authenticated user details
     */
    @GetMapping("/auth/user")
    public ResponseEntity<?> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof CustomUserDetails) {
            // Cast the principal to your CustomUserDetails class
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

            // Access custom fields
            userDTO authuser = new userDTO();
            authuser.setId(customUserDetails.getId());
            authuser.setName(customUserDetails.getName());
            authuser.setEmail(customUserDetails.getEmail());
            authuser.setIsAdmin(customUserDetails.getIsAdmin());

            return new ResponseEntity<>(authuser, HttpStatus.ACCEPTED);
        }

        // Handle the case when the principal is not a CustomUserDetails object or authentication failed
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }



    /**
     * @POST
     * @Body - name,email,password
     * @Response - id,name,email,isAdmin,token
     */
    @PostMapping("/register")
    public ResponseEntity<authResponseDTO> register(@RequestBody registerRequestDTO request) {
        authResponseDTO user=userService.createNewUser(request);
        return new ResponseEntity<>(user,HttpStatus.CREATED);
    }
}
