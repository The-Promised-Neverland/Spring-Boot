package com.ecommerce.ecommerce.controller.publicRoutes;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Hidden
public class healthCheck_controller_PUBLIC {

    @RequestMapping("/")
    public String index() {
        return "index";
    }
}
