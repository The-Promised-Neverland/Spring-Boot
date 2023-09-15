package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.models.Users.userDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<userDTO,String> {

    @Query("{ 'email' : { '$regex': ?0, '$options': 'i' } }")
    userDTO findByEmail(String email); // finds user by email

}
