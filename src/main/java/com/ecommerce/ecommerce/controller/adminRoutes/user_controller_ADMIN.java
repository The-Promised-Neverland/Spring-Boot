package com.ecommerce.ecommerce.controller.adminRoutes;


import com.ecommerce.ecommerce.models.Users.userDTO;
import com.ecommerce.ecommerce.services.UserServices.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/user")
public class user_controller_ADMIN {
    @Autowired
    private userService userService;

    /**
     * @GET
     * @Param - email
     * @Response - User
     */
    @GetMapping("/{email}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<userDTO> getUserByEmail(@PathVariable("email") String email){
        userDTO user = userService.getUserByEmail(email); // Fetch users (employees) from your service
        return ResponseEntity.ok(user);
    }

    /**
     * @GET
     * @Response - ALL USERS
     */
    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<userDTO>> getUsers(){
        List<userDTO> users = userService.getAllusers(); // Fetch users (employees) from your service
        return ResponseEntity.ok(users);
    }
}
