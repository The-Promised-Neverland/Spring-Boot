package com.ecommerce.ecommerce.services.UserDetailsService;

import com.ecommerce.ecommerce.models.Users.customUserDetails;
import com.ecommerce.ecommerce.models.Users.userDTO;
import com.ecommerce.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class customUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public customUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        userDTO user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        customUserDetails loadUser=new customUserDetails(user.get_id(),
                                                         user.getName(),
                                                         user.getEmail(),
                                                         user.getPassword(),
                                                         user.getIsAdmin());
        return loadUser;
    }
}