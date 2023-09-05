package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.DTO.userDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<userDTO,String> {
    userDTO findByEmail(String email); // finds user by email

}
