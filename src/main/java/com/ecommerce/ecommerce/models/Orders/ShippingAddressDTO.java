package com.ecommerce.ecommerce.models.Orders;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ShippingAddressDTO {
    private String address;
    private String city;
    private String postalCode;
    private String country;
}
