package com.ecommerce.ecommerce.controller.identityRoutes;

import com.ecommerce.ecommerce.exceptions.AuthenticationFailedException;
import com.ecommerce.ecommerce.exceptions.IncorrectCredentialsException;
import com.ecommerce.ecommerce.models.Users.Requests.loginRequestDTO;
import com.ecommerce.ecommerce.models.Users.Requests.registerRequestDTO;
import com.ecommerce.ecommerce.models.Users.Responses.authResponseDTO;
import com.ecommerce.ecommerce.models.Users.UserDetails;
import com.ecommerce.ecommerce.utils.JwtUtils;
import com.ecommerce.ecommerce.utils.SendJwtCookies;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class auth_controller_IDENTITY {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private com.ecommerce.ecommerce.services.UserServices.userService userService;

    @Autowired
    private SendJwtCookies sendJwtCookies;

    /**
     * @POST
     * @Body - email,password
     * @Response - id,name,email,isAdmin,token
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody loginRequestDTO request, HttpServletResponse response) throws AuthenticationFailedException {
        try {
            UsernamePasswordAuthenticationToken authtoken = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
            Authentication authUser = authenticationManager.authenticate(authtoken);
            UserDetails userDetails = (UserDetails) authUser.getPrincipal();
            String token = jwtUtils.generateToken(userDetails.getName(),
                    userDetails.getAuthorities().stream().iterator().next().getAuthority(),
                    userDetails.getUsername(),
                    userDetails.get_id());
            authResponseDTO loggedUser = new authResponseDTO(userDetails.getName(),
                    userDetails.getEmail(),
                    userDetails.getIsAdmin());

            sendJwtCookies.sendCookies(response, token);
            return new ResponseEntity<>(loggedUser, HttpStatus.ACCEPTED);
        } catch (BadCredentialsException e) {
            throw new IncorrectCredentialsException(e);
        } catch (AuthenticationException e) {
            throw new AuthenticationFailedException(e);
        }
    }

    /**
     * @POST
     * @Body - name,email,password
     * @Response - id,name,email,isAdmin,token
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody registerRequestDTO request, HttpServletResponse response) {
        String userId=userService.createNewUser(request);
        String token = jwtUtils.generateToken(request.getName(),
                "USER",
                request.getEmail(),
                userId);
        authResponseDTO createdUser = new authResponseDTO(request.getName(),
                request.getEmail(),
                false);

        sendJwtCookies.sendCookies(response, token);

        return new ResponseEntity<>(createdUser, HttpStatus.ACCEPTED);
    }

}

