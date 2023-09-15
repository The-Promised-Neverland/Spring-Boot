package com.ecommerce.ecommerce.models.Users.Responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class authResponseDTO{
    private String _id;
    private String name;
    private String email;
    private Boolean isAdmin=false;
}
