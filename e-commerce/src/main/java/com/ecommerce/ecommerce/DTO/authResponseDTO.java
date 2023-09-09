package com.ecommerce.ecommerce.DTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class authResponseDTO extends userDTO{
    private String token;
}
