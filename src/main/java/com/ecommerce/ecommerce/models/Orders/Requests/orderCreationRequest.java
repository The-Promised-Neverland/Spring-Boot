package com.ecommerce.ecommerce.models.Orders.Requests;

import com.ecommerce.ecommerce.models.Orders.ShippingAddressDTO;
import com.ecommerce.ecommerce.models.Orders.orderItemDTO;
import com.ecommerce.ecommerce.models.Orders.paymentDetailsDTO;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class orderCreationRequest {
    List<orderItemDTO> orderItems;
    private ShippingAddressDTO shippingAddress;
    private String paymentMethod;

    private paymentDetailsDTO paymentResult;

    private double itemsPrice;
    private double taxPrice;
    private double shippingPrice;
    private double totalPrice;
    private String successURL;
    private String cancelURL;
}
