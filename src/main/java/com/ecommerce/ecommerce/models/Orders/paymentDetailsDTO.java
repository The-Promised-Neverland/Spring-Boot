package com.ecommerce.ecommerce.models.Orders;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class paymentDetailsDTO {
    private String id; // the transaction id
    private String status;
    private Date update_time;
    private String email_address;
}

