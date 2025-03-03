package com.example.controller;

import com.example.model.User;
import com.example.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

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
        this.userService.addUser(user);
    }

    @GetMappping("/")
    public ArrayList<User> getUsers(){
        this.userService.getUsers();
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable UUID userId){
        this.userService.getUserById(userId);
    }

    @GetMapping("/{userId}/orders")
    public List<Order> getOrderByUserId(@PathVariable UUID userId){
        this.userService.getOrderByUserId(userId);
    }

    @PostMapping("/{userId}/checkout")
    public String addOrderToUser(@PathVariable UUID userId){
        this.userService.addOrderToUser(userId);
    }

    @PostMapping("/{userId}/removeOrder")
    public String removeOrderFromUser(@PathVariable UUID userId, @RequestParam UUID orderId){
        this.userService.removeOrderFromUer(userId, orderId);
    }

    @DeleteMapping("/{userId}/emptyCart")
    public String emptyCart(@PathVariable UUID userId){
        this.userService.emptyCart(userId);
    }
    @PutMapping("/addProductToCart")
    public String addProductToCart(@RequestParam UUID userId, @RequestParam UUID productId){
        this.userService.addOrderToUser(userId);
    }

    @PutMapping("/deleteProductFromCart")
    public String deleteProductFromCart(@RequestParam UUID userId, @RequestParam UUID productId){
        this.userService.();
    }

    @DeleteMapping("/delete/{UserId}")
    public String deleteUserById(@PathVariable UUID userId){
        this.userService.deleteUserByUserId(userId);
    }
}
