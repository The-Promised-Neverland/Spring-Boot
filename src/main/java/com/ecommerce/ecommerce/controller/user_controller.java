package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.models.Users.Requests.updateRequestDTO;
import com.ecommerce.ecommerce.models.Users.UserDetails;
import com.ecommerce.ecommerce.models.Users.userDTO;
import com.ecommerce.ecommerce.services.UserServices.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class user_controller {
    @Autowired
    private userService userService;

    /**
     * @GET
     * @Response - ALL USERS
     */
    @GetMapping("/auth/allUsers")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<userDTO>> getUsers(){
        List<userDTO> users = userService.getAllusers(); // Fetch users (employees) from your service
        return ResponseEntity.ok(users);
    }

    /**
     * @GET
     * @Param - email
     * @Response - User
     */
    @GetMapping("/auth/{email}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<userDTO> getUserByEmail(@PathVariable("email") String email){
        userDTO user = userService.getUserByEmail(email); // Fetch users (employees) from your service
        return ResponseEntity.ok(user);
    }

    @GetMapping("/auth")
    public ResponseEntity<?> GETuSER(){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(authentication.getPrincipal());
    }
    /**
     * @PUT
     * @Body(Optional) - name,email,password
     * @Response - 200 ok
     */
    @PutMapping("/auth/updateUser")
    public ResponseEntity<?> updateUser(@RequestBody updateRequestDTO request){
        try{
            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            if(authentication!=null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails){
                String userID= ((UserDetails) authentication.getPrincipal()).get_id();

                userService.updateUser(userID,request);

                return new ResponseEntity<>("User updated successfully", HttpStatus.OK);
            }
            else{
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (RuntimeException e) {
            // Handle JWT token expiration exception here
            return ResponseEntity.status(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED).build();
        }
    }

}
