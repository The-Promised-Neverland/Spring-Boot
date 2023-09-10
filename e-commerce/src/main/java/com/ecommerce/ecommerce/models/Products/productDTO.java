package com.ecommerce.ecommerce.models.Products;

import com.ecommerce.ecommerce.models.Reviews.reviewDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document("products")
public class productDTO {
    @Id
    private String id;

    private String user; // Reference to the user who created the product

    private String name;
    private String image;
    private String brand;
    private String category;
    private String description;
    private double price;

    private double rating;
    private int numReviews;
    private int countInStock;
    private List<reviewDTO> reviews;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;
}
