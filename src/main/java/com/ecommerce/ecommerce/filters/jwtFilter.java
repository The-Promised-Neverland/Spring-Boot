package com.ecommerce.ecommerce.filters;

import com.ecommerce.ecommerce.utils.ExtractCookieFromRequests;
import com.ecommerce.ecommerce.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


@Component
public class jwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = ExtractCookieFromRequests.extractJwtFromCookies(request);

        //jwt will always contain email,role,
        if(token!=null && !jwtUtils.isTokenExpired(token)){
            String email= jwtUtils.getEmailFromToken(token);
            String role=jwtUtils.getRoleFromToken(token);
            String name=jwtUtils.getNameFromToken(token);

            // Create a map to store name and email
            Map<String, String> userDetailsMap = new HashMap<>();
            userDetailsMap.put("name", name);
            userDetailsMap.put("email", email);

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(userDetailsMap,null, Collections.singleton(new SimpleGrantedAuthority(role)));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }

        filterChain.doFilter(request, response);
    }
}
