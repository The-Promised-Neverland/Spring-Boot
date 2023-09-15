package com.ecommerce.ecommerce.security.JWT;

import com.ecommerce.ecommerce.exceptions.ExpiredSessionException;
import com.ecommerce.ecommerce.exceptions.JwtTamperedFormatException;
import com.ecommerce.ecommerce.exceptions.handleException.ErrorResponse;
import com.ecommerce.ecommerce.models.Users.customUserDetails;
import com.ecommerce.ecommerce.services.UserDetailsService.customUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWT_AuthFilter extends OncePerRequestFilter {
    private Logger logger = LoggerFactory.getLogger(OncePerRequestFilter.class);

    @Autowired
    private JWT_Utils jwt_utils;

    @Autowired
    private customUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String email = null;
        String token = null;

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) { // Replace "jwt" with the name of your cookie
                    String jwt = cookie.getValue();
                    // Now you have the JWT, you can use it for authentication or other purposes.
                    token=jwt;
                    break;
                }
            }
        }



        if (token != null) {
            try {
                email = jwt_utils.getUsernameFromToken(token);
            } catch (ExpiredJwtException e) {
                throw new ExpiredSessionException();
            } catch (MalformedJwtException e) {
                throw new JwtTamperedFormatException();
            }
        }



        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            customUserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

            Boolean validateToken = jwt_utils.validateToken(token, userDetails);
            if (validateToken) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            } else {
                logger.info("Validation fails !!");
            }
        }

        filterChain.doFilter(request, response);
    }
}
