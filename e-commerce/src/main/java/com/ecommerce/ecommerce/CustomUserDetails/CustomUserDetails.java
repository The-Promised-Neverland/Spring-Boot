package com.ecommerce.ecommerce.CustomUserDetails;

import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
@ToString
public class CustomUserDetails implements UserDetails {

    private final String id;
    private final String name;

    private final String email;
    private final String password;
    private final Boolean isAdmin;

    public CustomUserDetails(String id, String name, String email, String password, Boolean isAdmin) {
        this.id = id;
        this.name=name;
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = isAdmin ? "ADMIN" : "USER";
        return Collections.singleton(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}