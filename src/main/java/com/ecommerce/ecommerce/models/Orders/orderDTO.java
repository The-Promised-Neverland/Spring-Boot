package com.ecommerce.ecommerce.models.Orders;


import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document("orders")
public class orderDTO {

    @Id
    private String _id;

    private ObjectId user;
    private List<orderItemDTO> orderItems;
    private ShippingAddressDTO shippingAddress;
    private String paymentMethod;
    private paymentDetailsDTO paymentResult;
    private double itemsPrice;
    private double taxPrice;
    private double shippingPrice;
    private double totalPrice;
    private boolean isPaid;
    private Date paidAt;
    private boolean isDelivered;
    private Date deliveredAt;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;


    // Method to get the formatted paidAt for display
    public String setPaidAt() {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss dd/MM/yy");
        return (paidAt != null) ? sdf.format(paidAt) : "";
    }
}
