package com.ecommerce.ecommerce.models.Orders;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class orderItemDTO {
    private String name;
    private int qty;
    private String image;
    private double price;
    private String product;
}
