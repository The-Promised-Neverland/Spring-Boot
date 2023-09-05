package com.ecommerce.ecommerce.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class jwtResponseDTO {
    private String jwtToken;
    private String username;

}
