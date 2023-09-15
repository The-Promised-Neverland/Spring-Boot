package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.models.Products.productDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;



@Repository
public interface ProductRepository extends MongoRepository<productDTO, String> {

    @Query("{ 'name' : { '$regex': ?0, '$options': 'i' } }")
    Page<productDTO> PaginationAndSearch(String searchKeyword, Pageable pageable);

}
