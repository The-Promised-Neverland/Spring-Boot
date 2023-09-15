package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.models.Orders.orderDTO;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrderRepository extends MongoRepository<orderDTO, String> {

    List<orderDTO> findByUser(ObjectId user);
}
