package com.ecommerce.ecommerce.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HealthCheckController {

    @RequestMapping("/")
    public String index() {
        return "index";
    }
}
