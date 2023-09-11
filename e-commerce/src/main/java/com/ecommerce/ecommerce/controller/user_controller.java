package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.exceptions.ExpiredSessionException;
import com.ecommerce.ecommerce.exceptions.UnauthorizedAccessException;
import com.ecommerce.ecommerce.models.Users.Requests.updateRequestDTO;
import com.ecommerce.ecommerce.models.Users.customUserDetails;
import com.ecommerce.ecommerce.models.Users.userDTO;
import com.ecommerce.ecommerce.repository.UserRepository;
import com.ecommerce.ecommerce.services.UserServices.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class user_controller {

    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private userService userService;

    @Autowired
    private UserRepository userRepository;

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
        userDTO user = userService.getByEmail(email); // Fetch users (employees) from your service
        return ResponseEntity.ok(user);
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
            if(authentication!=null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof customUserDetails){
                String userID= ((customUserDetails) authentication.getPrincipal()).get_id();

                userDTO updateUser=userRepository.findById(userID).get();

                if(request.getName()!=null){
                    updateUser.setName(request.getName());
                }
                if(request.getEmail()!=null){
                    updateUser.setEmail(request.getEmail());
                }
                if(request.getPassword()!=null){
                    String encodedPassword = encoder.encode(request.getPassword());
                    updateUser.setPassword(encodedPassword);
                }
                userRepository.save(updateUser);
                return new ResponseEntity<>("User updated successfully", HttpStatus.OK);
            }
            else{
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (ExpiredSessionException e) {
            // Handle JWT token expiration exception here
            return ResponseEntity.status(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED).build();
        } catch (UnauthorizedAccessException e) {
            // Handle other custom exceptions here if needed
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
