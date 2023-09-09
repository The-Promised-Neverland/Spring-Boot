package com.ecommerce.ecommerce.DTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class registerRequestDTO{
    private String name;
    private String email;
    private String password;
}
