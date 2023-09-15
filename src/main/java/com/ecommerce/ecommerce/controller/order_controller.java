package com.ecommerce.ecommerce.controller;


import com.ecommerce.ecommerce.models.Orders.Requests.orderCreationRequest;
import com.ecommerce.ecommerce.models.Orders.orderDTO;
import com.ecommerce.ecommerce.models.Users.customUserDetails;
import com.ecommerce.ecommerce.services.OrderServices.orderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/orders/auth")
public class order_controller {

    @Autowired
    private orderService orderService;


    @PutMapping("/createOrder")
    public ResponseEntity<?> createOrder(@RequestBody orderCreationRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = ((customUserDetails) authentication.getPrincipal()).get_id();

        return new ResponseEntity<>(orderService.createOrder(user,request), HttpStatus.CREATED);
    }


    @GetMapping("/{orderID}")
    public ResponseEntity<orderDTO> retrieveOrderByID(@PathVariable("orderID") String orderID){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = ((customUserDetails) authentication.getPrincipal()).get_id();
        orderDTO order=orderService.showOrder(user,orderID);
        return new ResponseEntity<orderDTO>(order,HttpStatus.OK);
    }


    @PutMapping("/{orderID}/delivered")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> orderDelivered(@PathVariable("orderID") String orderID){
        orderService.updateorderDelivered(orderID);
        return new ResponseEntity<>("OrderID: " + orderID + " " + "Order Delivered",HttpStatus.OK);
    }


    @GetMapping("/allOrders")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getAllorders(){
        List<orderDTO> orders=orderService.getAllOrders();
        return new ResponseEntity<>(orders,HttpStatus.OK);
    }


    @GetMapping("/{userID}/getOrders")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getAllOrderofUser(@PathVariable("userID") String userID) {
        List<orderDTO> orders=orderService.getUserOrders(userID);
        return new ResponseEntity<>(orders,HttpStatus.OK);
    }



}
