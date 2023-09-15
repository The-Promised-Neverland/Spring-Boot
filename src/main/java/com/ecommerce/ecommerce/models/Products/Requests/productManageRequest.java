package com.ecommerce.ecommerce.models.Products.Requests;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class productManageRequest {
    private String name;
    private double price;
    private String image;
    private String brand;
    private String category;
    private int countInStock;
    private String description;
}
