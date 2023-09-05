package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.DTO.jwtResponseDTO;
import com.ecommerce.ecommerce.DTO.authRequestDTO;
import com.ecommerce.ecommerce.security.JWT.JWT_Helper;
import com.ecommerce.ecommerce.services.AuthServices.loginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/authentication")
public class auth_controller {

    @Autowired
    private loginService loginService;

    @Autowired
    public AuthenticationManager manager;

    @Autowired
    public JWT_Helper helper;


    @PostMapping("/login")
    public ResponseEntity<jwtResponseDTO> login(@RequestBody authRequestDTO request){
        jwtResponseDTO user=loginService.login(request);

//        this.doAuthenticate(request.getEmail(),request.getPassword());
//
//        UserDetails userDetails=userDetailsService.loadUserByUsername(request.getEmail());
//        String token=helper.generateToken(userDetails);
//
//        jwtResponseDTO responseDTO= new jwtResponseDTO(userDetails.getUsername(),token);

        return new ResponseEntity<>(user, HttpStatus.OK);

    }


}
