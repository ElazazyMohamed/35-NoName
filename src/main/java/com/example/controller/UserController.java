package com.example.controller;

import com.example.model.Order;
import com.example.model.User;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/")
    public User addUser(@RequestBody User user){
       return  this.userService.addUser(user);
    }

    @GetMapping("/")
    public ArrayList<User> getUsers(){
        return this.userService.getUsers();
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable UUID userId){
        return this.userService.getUserById(userId);
    }

    @GetMapping("/{userId}/orders")
    public List<Order> getOrderByUserId(@PathVariable UUID userId){
        return this.userService.getOrderByUserId(userId);
    }

    @PostMapping("/{userId}/checkout")
    public String addOrderToUser(@PathVariable UUID userId){
        this.userService.addOrderToUser(userId);
        return "Order added to user";
    }

    @PostMapping("/{userId}/removeOrder")
    public String removeOrderFromUser(@PathVariable UUID userId, @RequestParam UUID orderId){
        this.userService.removeOrderFromUer(userId, orderId);
        return "Order removed from user";
    }

    @DeleteMapping("/{userId}/emptyCart")
    public String emptyCart(@PathVariable UUID userId){
        this.userService.emptyCart(userId);
        return "Cart emptied";
    }
    @PutMapping("/addProductToCart")
    public String addProductToCart(@RequestParam UUID userId, @RequestParam UUID productId){
        this.userService.addOrderToUser(userId);
        return "Product added to cart";
    }

    @PutMapping("/deleteProductFromCart")
    public String deleteProductFromCart(@RequestParam UUID userId, @RequestParam UUID productId){
        this.userService.removeOrderFromUer(userId, productId);
        return "Product removed from cart";
    }

    @DeleteMapping("/delete/{UserId}")
    public String deleteUserById(@PathVariable UUID userId){
        this.userService.deleteUserByUserId(userId);
        return "User deleted";
    }
}
