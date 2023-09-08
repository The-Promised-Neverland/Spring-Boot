package com.ecommerce.ecommerce.services.JWTServices;

import com.ecommerce.ecommerce.CustomUserDetails.CustomUserDetails;
import com.ecommerce.ecommerce.DTO.userDTO;
import com.ecommerce.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class jwtService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        userDTO user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        CustomUserDetails loadUser=new CustomUserDetails(user.getId(),user.getName(),user.getEmail(),user.getPassword(),user.getIsAdmin());
        return loadUser;
    }
}
