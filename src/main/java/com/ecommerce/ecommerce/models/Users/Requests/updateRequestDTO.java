package com.ecommerce.ecommerce.models.Users.Requests;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class updateRequestDTO {
    private String name=null;
    private String email=null;
    private String password=null;
}
