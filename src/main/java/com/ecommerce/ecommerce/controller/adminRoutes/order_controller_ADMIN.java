package com.ecommerce.ecommerce.controller.adminRoutes;

import com.ecommerce.ecommerce.models.Orders.orderDTO;
import com.ecommerce.ecommerce.services.OrderServices.orderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/admin/orders")
public class order_controller_ADMIN {
    @Autowired
    private orderService orderService;

    @PutMapping("/{orderID}/delivered")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> orderDelivered(@PathVariable("orderID") String orderID){
        orderService.updateorderDelivered(orderID);
        return new ResponseEntity<>("OrderID: " + orderID + " " + "Order Delivered", HttpStatus.OK);
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
