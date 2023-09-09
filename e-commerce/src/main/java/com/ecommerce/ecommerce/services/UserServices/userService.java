package com.ecommerce.ecommerce.services.UserServices;

import com.ecommerce.ecommerce.DTO.authResponseDTO;
import com.ecommerce.ecommerce.DTO.registerRequestDTO;
import com.ecommerce.ecommerce.DTO.userDTO;
import com.ecommerce.ecommerce.repository.UserRepository;
import com.ecommerce.ecommerce.security.JWT.JWT_Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class userService {

    @Autowired
    private JWT_Helper jwtUtils;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    public List<userDTO> getAllusers(){
        return userRepository.findAll();
    }

    public userDTO getEmail(String email){
        return userRepository.findByEmail(email);
    }

    public userDTO getUserbyID(String userID) {
        return userRepository.findById(userID)
                .orElseThrow(() -> new NoSuchElementException("USER DOES NOT EXIST"));
    }

    public authResponseDTO createNewUser(registerRequestDTO request){
        String name=request.getName();
        String email=request.getEmail();
        String password=request.getPassword();
        userDTO existingUser = userRepository.findByEmail(email);
        if (existingUser != null) {
            throw new RuntimeException("User with this email already exists.");
        }

        userDTO newUser=new userDTO();
        newUser.setPassword(encoder.encode(password));
        newUser.setName(name);
        newUser.setEmail(email);
        userRepository.save(newUser);

        authResponseDTO responseDTO = new authResponseDTO(newUser.getId(), newUser.getName(), newUser.getEmail(), newUser.getIsAdmin(),null);

        String token=j
        return responseDTO;
    }



}
