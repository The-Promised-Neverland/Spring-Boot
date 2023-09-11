package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.exceptions.ExpiredSessionException;
import com.ecommerce.ecommerce.exceptions.UnauthorizedAccessException;
import com.ecommerce.ecommerce.models.Users.Requests.loginRequestDTO;
import com.ecommerce.ecommerce.models.Users.Requests.registerRequestDTO;
import com.ecommerce.ecommerce.models.Users.Responses.authResponseDTO;
import com.ecommerce.ecommerce.models.Users.customUserDetails;
import com.ecommerce.ecommerce.security.JWT.JWT_Utils;
import com.ecommerce.ecommerce.services.UserDetailsService.customUserDetailsService;
import com.ecommerce.ecommerce.services.UserServices.userService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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
    private customUserDetailsService customUserDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWT_Utils jwtUtils;

    @Autowired
    private userService userService;

    /**
     * @POST
     * @Body - email,password
     * @Response - id,name,email,isAdmin,token
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody loginRequestDTO request, HttpServletResponse response) {
        try {
            UsernamePasswordAuthenticationToken authtoken=new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
            Authentication authUser= authenticationManager.authenticate(authtoken); // authenticated user

            if(authUser!=null && authUser.isAuthenticated() && authUser.getPrincipal() instanceof customUserDetails){
                customUserDetails userDetails= (customUserDetails) authUser.getPrincipal();
                String token = jwtUtils.generateToken(userDetails);
                authResponseDTO loggedUser = new authResponseDTO(userDetails.get_id(),
                                                               userDetails.getName(),
                                                               userDetails.getEmail(),
                                                               userDetails.getIsAdmin());

                Cookie cookie = new Cookie("jwt", token); // Create a new cookie
                cookie.setHttpOnly(true); // Set the HTTP-only flag for security
                cookie.setMaxAge(3600 * 5); // Set the expiration time for the cookie in seconds (adjust as needed)
                cookie.setPath("/");
                response.addCookie(cookie);

                return new ResponseEntity<>(loggedUser, HttpStatus.ACCEPTED);
            }
            else{
                return new ResponseEntity<>("Unknown error occured...Please try logging again",HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
            }
        } catch (BadCredentialsException e) {
            // Authentication failed due to bad credentials (incorrect username or password)
            return new ResponseEntity<>("Incorrect Credentials", HttpStatus.UNAUTHORIZED);
        }
    }


    /**
     * @GET
     * @Response - Current authenticated user details
     */
    @GetMapping("/auth/user")
    public ResponseEntity<?> userProfile() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof customUserDetails) {
                // Cast the principal to your CustomUserDetails class
                customUserDetails customUserDetails = (customUserDetails) authentication.getPrincipal();

                // Access custom fields
                authResponseDTO authUser = new authResponseDTO();
                authUser.set_id(customUserDetails.get_id());
                authUser.setName(customUserDetails.getName());
                authUser.setEmail(customUserDetails.getEmail());
                authUser.setIsAdmin(customUserDetails.getIsAdmin());

                return new ResponseEntity<>(authUser, HttpStatus.ACCEPTED);
            }
            else{
                return new ResponseEntity<>("Unknown error occured...Please login again",HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
            }
        } catch (ExpiredSessionException e) {
            // Handle JWT token expiration exception here
            return ResponseEntity.status(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED).build();
        } catch (UnauthorizedAccessException e) {
            // Handle other custom exceptions here if needed
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    /**
     * @POST
     * @Body - name,email,password
     * @Response - id,name,email,isAdmin,token
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody registerRequestDTO request, HttpServletResponse response) {
        try {
            authResponseDTO user=userService.createNewUser(request);
            UsernamePasswordAuthenticationToken authtoken=new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
            authenticationManager.authenticate(authtoken);
            customUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(request.getEmail());
            String token = jwtUtils.generateToken(customUserDetails);
            authResponseDTO createdUser = new authResponseDTO(customUserDetails.get_id(),
                    customUserDetails.getName(),
                    customUserDetails.getEmail(),
                    customUserDetails.getIsAdmin());

            Cookie cookie = new Cookie("jwt", token); // Create a new cookie
            cookie.setHttpOnly(true); // Set the HTTP-only flag for security
            cookie.setMaxAge(3600 * 5); // Set the expiration time for the cookie in seconds (adjust as needed)
            cookie.setPath("/");
            response.addCookie(cookie);

            return new ResponseEntity<>(createdUser, HttpStatus.ACCEPTED);
        } catch (BadCredentialsException e) {
            // Authentication failed due to bad credentials (incorrect username or password)
            return new ResponseEntity<>("Incorrect Credentials", HttpStatus.UNAUTHORIZED);
        }
    }



}
