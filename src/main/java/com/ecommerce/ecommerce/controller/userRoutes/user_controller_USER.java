package com.ecommerce.ecommerce.controller.userRoutes;

import com.ecommerce.ecommerce.models.Users.Requests.updateRequestDTO;
import com.ecommerce.ecommerce.models.Users.Responses.authResponseDTO;
import com.ecommerce.ecommerce.models.Users.UserDetails;
import com.ecommerce.ecommerce.services.UserServices.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth/user")
public class user_controller_USER {
    @Autowired
    private userService userService;

    /**
     * @GET
     * @Response - 200 ok
     */
    @GetMapping
    public ResponseEntity<?> getLoggedUser(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        UserDetails authUser= (UserDetails) authentication.getPrincipal();
        authResponseDTO response=new authResponseDTO(authUser.getName(),authUser.getEmail(),authUser.getIsAdmin());
        return ResponseEntity.ok(response);
    }

    /**
     * @PUT
     * @Body(Optional) - name,email,password
     * @Response - 200 ok
     */
    @PutMapping("/updateUser")
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
