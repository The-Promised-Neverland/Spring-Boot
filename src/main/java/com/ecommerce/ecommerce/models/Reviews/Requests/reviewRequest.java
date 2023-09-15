package com.ecommerce.ecommerce.models.Reviews.Requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class reviewRequest {
    private double rating;
    private String comment;
}
