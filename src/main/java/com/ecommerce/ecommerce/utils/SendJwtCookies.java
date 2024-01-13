package com.ecommerce.ecommerce.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class SendJwtCookies {
    public void sendCookies(HttpServletResponse response, String jwt){
        Cookie cookie = new Cookie("jwt", jwt); // Create a new cookie
        cookie.setHttpOnly(true); // Set the HTTP-only flag for security
        cookie.setMaxAge(3600 * 5); // Set the expiration time for the cookie in seconds (adjust as needed)
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
