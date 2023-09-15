package com.ecommerce.ecommerce.models.Orders;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShippingAddressDTO {
    private String address;
    private String city;
    private String postalCode;
    private String country;
}
