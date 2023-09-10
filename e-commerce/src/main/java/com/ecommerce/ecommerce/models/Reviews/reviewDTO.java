package com.ecommerce.ecommerce.models.Reviews;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class reviewDTO {
    @DBRef
    private String user; // Reference to the user who created the product

    private String name;
    private double rating;
    private String comment;
}

