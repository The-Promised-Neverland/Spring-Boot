package com.ecommerce.ecommerce.models.Orders;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class orderItemDTO {
    private String name;
    private int qty;
    private String image;
    private double price;
    private String product;
}
