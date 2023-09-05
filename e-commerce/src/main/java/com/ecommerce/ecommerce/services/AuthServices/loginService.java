package com.ecommerce.ecommerce.services.AuthServices;

import com.ecommerce.ecommerce.DTO.jwtResponseDTO;
import com.ecommerce.ecommerce.DTO.authRequestDTO;
import com.ecommerce.ecommerce.repository.UserRepository;
import com.ecommerce.ecommerce.security.JWT.JWT_Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


@Service
public class loginService{

    @Autowired
    public UserDetailsService userDetailsService;

    @Autowired
    public AuthenticationManager authenticationManager;

    @Autowired
    public JWT_Helper helper;

    private UserRepository userRepository;


    public jwtResponseDTO login(authRequestDTO request){
        String email=request.getEmail();
        String password=request.getPassword();

        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email,password));
        } catch (Exception e){
            throw new RuntimeException("Invalid username or password");
        }
        UserDetails user=userDetailsService.loadUserByUsername(email);
        String token=helper.generateToken(user);

        jwtResponseDTO responseDTO= new jwtResponseDTO(user.getUsername(),token);
        return responseDTO;
    }
}
