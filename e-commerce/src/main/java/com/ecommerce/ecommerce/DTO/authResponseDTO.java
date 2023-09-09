package com.ecommerce.ecommerce.DTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class authResponseDTO{
    private String id;
    private String name;
    private String email;
    private Boolean isAdmin=false;
    private String token;
}
