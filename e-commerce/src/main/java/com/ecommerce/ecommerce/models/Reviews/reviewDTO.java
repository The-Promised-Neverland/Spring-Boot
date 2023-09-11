package com.ecommerce.ecommerce.models.Reviews;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class reviewDTO {
    private String user; // Reference to the user who created the product
    private String name;
    private double rating;
    private String comment;
}
