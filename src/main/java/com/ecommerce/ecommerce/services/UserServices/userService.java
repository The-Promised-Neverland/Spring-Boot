package com.ecommerce.ecommerce.services.UserServices;

import com.ecommerce.ecommerce.exceptions.UserAlreadyExistsException;
import com.ecommerce.ecommerce.exceptions.UserCreationFailedException;
import com.ecommerce.ecommerce.exceptions.UserNotFoundException;
import com.ecommerce.ecommerce.exceptions.UserUpdateRequestFailed;
import com.ecommerce.ecommerce.models.Users.Requests.updateRequestDTO;
import com.ecommerce.ecommerce.models.Users.Requests.registerRequestDTO;
import com.ecommerce.ecommerce.models.Users.userDTO;
import com.ecommerce.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class userService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    public List<userDTO> getAllusers(){
        return userRepository.findAll();
    }

    public userDTO getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }


    public String createNewUser(registerRequestDTO request) {
        String name=request.getName();
        String email=request.getEmail();
        String password=request.getPassword();
        userDTO existingUser = userRepository.findByEmail(email);
        if (existingUser != null) {
            throw new UserAlreadyExistsException(email);
        }
        try {
            userDTO newUser = new userDTO();
            newUser.setPassword(encoder.encode(password));
            newUser.setName(name);
            newUser.setEmail(email);
            return userRepository.save(newUser).get_id();
        } catch (Exception e) {
            throw new UserCreationFailedException(email,e);
        }
    }

    public void updateUser(String email, updateRequestDTO request){
        userDTO updateUser=userRepository.findByEmail(email);

        if(updateUser==null){
            throw new UserNotFoundException(email);
        }

        if(request.getName()!=null){
            updateUser.setName(request.getName());
        }
        if(request.getEmail()!=null){
            updateUser.setEmail(request.getEmail());
        }
        if(request.getPassword()!=null){
            String encodedPassword = encoder.encode(request.getPassword());
            updateUser.setPassword(encodedPassword);
        }

        try {
            userRepository.save(updateUser);
        } catch (Exception e) {
            throw new UserUpdateRequestFailed(email, e);
        }
    }


}
