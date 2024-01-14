package com.ecommerce.ecommerce.controller.userRoutes;

import com.ecommerce.ecommerce.models.Orders.Requests.orderCreationRequest;
import com.ecommerce.ecommerce.models.Orders.orderDTO;
import com.ecommerce.ecommerce.models.Users.UserDetails;
import com.ecommerce.ecommerce.services.OrderServices.orderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/auth/orders")
public class order_controller_USER {
    @Autowired
    private orderService orderService;


    @PutMapping("/createOrder")
    public ResponseEntity<?> createOrder(@RequestBody orderCreationRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = ((UserDetails) authentication.getPrincipal()).get_id();
        orderService.createOrder(user,request);
        return new ResponseEntity<>("ORDER CREATED SUCCESSFULLY", HttpStatus.CREATED);
    }


    @GetMapping("/{orderID}")
    public ResponseEntity<orderDTO> retrieveOrderByID(@PathVariable("orderID") String orderID){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = ((UserDetails) authentication.getPrincipal()).get_id();
        orderDTO order=orderService.showOrder(user,orderID);
        return new ResponseEntity<orderDTO>(order,HttpStatus.OK);
    }
}
