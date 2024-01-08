package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.models.Users.Requests.loginRequestDTO;
import com.ecommerce.ecommerce.models.Users.Requests.registerRequestDTO;
import com.ecommerce.ecommerce.models.Users.Responses.authResponseDTO;
import com.ecommerce.ecommerce.models.Users.UserDetails;
import com.ecommerce.ecommerce.services.UserServices.userService;
import com.ecommerce.ecommerce.utils.JwtUtils;
import com.ecommerce.ecommerce.utils.SendJwtCookies;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class auth_controller {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private userService userService;

    private SendJwtCookies sendJwtCookies;

    Logger logger= LoggerFactory.getLogger(auth_controller.class);

    /**
     * @POST
     * @Body - email,password
     * @Response - id,name,email,isAdmin,token
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody loginRequestDTO request, HttpServletResponse response) {
        try {
            UsernamePasswordAuthenticationToken authtoken = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
            Authentication authUser = authenticationManager.authenticate(authtoken);

//            UserDetails userDetails = (UserDetails) authUser.getPrincipal();
//            String token = jwtUtils.generateToken(userDetails);
//            authResponseDTO loggedUser = new authResponseDTO(userDetails.getName(),
//                    userDetails.getEmail(),
//                    userDetails.getIsAdmin());
//
//            sendJwtCookies.sendCookies(response, token);
//            System.out.println(token);
//
//            return new ResponseEntity<>(loggedUser, HttpStatus.ACCEPTED);
            return new ResponseEntity<>(authtoken,HttpStatus.OK);
        } catch (Exception e) {
            logger.info(e.toString());
            throw new RuntimeException();
        }
    }

    /**
     * @POST
     * @Body - name,email,password
     * @Response - id,name,email,isAdmin,token
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody registerRequestDTO request, HttpServletResponse response) {
            userService.createNewUser(request);
            UserDetails userDetails=new UserDetails(null, request.getName(), request.getEmail(),null,false);
            String token = jwtUtils.generateToken(userDetails);
            authResponseDTO createdUser = new authResponseDTO(userDetails.getName(),
                                                              userDetails.getEmail(),
                                                              userDetails.getIsAdmin());

            sendJwtCookies.sendCookies(response, token);

            return new ResponseEntity<>(createdUser, HttpStatus.ACCEPTED);
    }

}
