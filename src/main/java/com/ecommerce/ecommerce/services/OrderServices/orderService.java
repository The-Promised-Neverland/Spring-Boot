package com.ecommerce.ecommerce.services.OrderServices;


import com.ecommerce.ecommerce.exceptions.NotFoundException;
import com.ecommerce.ecommerce.models.Orders.Requests.orderCreationRequest;
import com.ecommerce.ecommerce.models.Orders.orderDTO;
import com.ecommerce.ecommerce.repository.OrderRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class orderService {

    @Autowired
    private OrderRepository orderRepository;

    public void createOrder(String user, orderCreationRequest request){
        orderDTO order=new orderDTO();

        if(request.getPaymentResult()!=null){
            order.setPaid(true);
            order.setPaidAt(new Date());
        }
        order.setUser(new ObjectId(user));
        order.setOrderItems(request.getOrderItems());
        order.setShippingAddress(request.getShippingAddress());
        order.setPaymentMethod(request.getPaymentMethod());
        order.setItemsPrice(request.getItemsPrice());
        order.setTaxPrice(request.getTaxPrice());
        order.setShippingPrice(request.getShippingPrice());
        order.setTotalPrice(request.getTotalPrice());
        order.setPaymentResult(request.getPaymentResult());
        orderRepository.save(order);
        return;
    }

    public orderDTO showOrder(String user, String orderID){
        orderDTO order=orderRepository.findById(orderID).orElseThrow(() -> new NotFoundException());
        if(!order.getUser().equals(new ObjectId(user))){ // if the order is not of this user
            throw new AccessDeniedException("Access denied. This order does not belong to the current user.");
        }
        return order;
    }

    public void updateorderDelivered(String orderID){
        orderDTO order=orderRepository.findById(orderID).orElseThrow(() -> new NotFoundException());
        order.setPaid(true);
        order.setDelivered(true);
        order.setDeliveredAt(new Date());
        orderRepository.save(order);
    }

    public List<orderDTO> getAllOrders(){
        List<orderDTO> orders=orderRepository.findAll();
        return orders;
    }

    public List<orderDTO> getUserOrders(String userID){
        ObjectId userId = new ObjectId(userID);
        List<orderDTO> orders = orderRepository.findByUser(userId);
        System.out.println(userID);
        return orders;
    }
}
