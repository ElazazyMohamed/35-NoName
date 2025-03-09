package com.example.controller;

import com.example.model.Order;
import com.example.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.UUID;

@RestController
@RequestMapping("/order")
public class OrderController {
    //The Dependency Injection Variables
    //The Constructor with the required variables mapping the Dependency Injection.
    OrderService orderService;
    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/")
    public void addOrder(@RequestBody Order order) {
        // ✅ Manual validation for empty request
        if (order == null || (order.getId() == null && order.getUserId() == null)) {
            throw new IllegalArgumentException("Invalid request: Order ID and User ID cannot be empty.");
        }

        orderService.addOrder(order);
    }
    //  Exception Handler to return 400 Bad Request with the error message
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleIllegalArgumentException(IllegalArgumentException ex) {
        return ex.getMessage();  // Return error message in response body
    }

    @GetMapping("/{orderId}")
    public Order getOrderById(@PathVariable UUID orderId){
        Order order = orderService.getOrderById(orderId);

        if (order == null) {
            throw new IllegalArgumentException("Order not found with ID: " + orderId); // Throw exception instead of returning null
        }

        return order;
    }

    @GetMapping("/")
    public ArrayList<Order> getOrders(){
        return orderService.getOrders();
    }

    @DeleteMapping("/delete/{orderId}")
    public String deleteOrderById(@PathVariable UUID orderId){
        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            return "Order not found";
        }
        orderService.deleteOrderById(orderId);
        return "Order deleted successfully";
    }



}
