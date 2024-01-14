package com.ecommerce.ecommerce.openAPI;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@OpenAPIDefinition
@Configuration
public class openAPIConfig {
    @Bean
    public OpenAPI customOpenAPI(){
        Contact dev=new Contact();
        dev.setEmail("abhijitroycoc@gmail.com");
        dev.setName("Abhijit Roy");
        dev.setUrl("https://github.com/The-Promised-Neverland");

        return new OpenAPI()
                .info(new Info()
                        .title("Ecommerce API - Spring Boot")
                        .description("CRUD operations for a ecommerce API. Authorization handled by JWT. Stripe and its webhook integrated as Payment Gateway. MongoDB as database.")
                        .contact(dev)
                        .version("1.0.0")
                );
    }
}