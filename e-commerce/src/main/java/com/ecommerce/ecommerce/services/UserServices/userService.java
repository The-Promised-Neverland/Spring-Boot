package com.ecommerce.ecommerce.services.UserServices;

import com.ecommerce.ecommerce.DTO.userDTO;
import com.ecommerce.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class userService {

    private UserRepository userRepository;

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

}
