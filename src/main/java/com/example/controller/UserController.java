package com.example.controller;

import com.example.model.Cart;
import com.example.model.Order;
import com.example.model.Product;
import com.example.model.User;
import com.example.service.CartService;
import com.example.service.ProductService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final CartService cartService;
    private final ProductService productService;

    @Autowired
    public UserController(UserService userService, CartService cartService, ProductService productService) {
        this.userService = userService;
        this.cartService = cartService;
        this.productService = productService;
    }

    @PostMapping("/")
    public User addUser(@RequestBody User user){
        if (user == null || (user.getId() == null && user.getName() == null )) {
            throw new IllegalArgumentException("Invalid request: User ID and Name cannot be empty.");
        }
        return userService.addUser(user);
    }

    @GetMapping("/")
    public ArrayList<User> getUsers(){
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable UUID userId){
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }
        return user;
    }

    @GetMapping("/{userId}/orders")
    public List<Order> getOrderByUserId(@PathVariable UUID userId){
        return userService.getOrderByUserId(userId);
    }

    @PostMapping("/{userId}/checkout")
    public String addOrderToUser(@PathVariable UUID userId){
        this.userService.addOrderToUser(userId);
        return "Order added successfully";
    }

    @PostMapping("/{userId}/removeOrder")
    public String removeOrderFromUser(@PathVariable UUID userId, @RequestParam UUID orderId){
        this.userService.removeOrderFromUser(userId, orderId);
        return "Order removed successfully";
    }

    @DeleteMapping("/{userId}/emptyCart")
    public String emptyCart(@PathVariable UUID userId){
        this.userService.emptyCart(userId);
        return "Cart emptied successfully";
    }
    @PutMapping("/addProductToCart")
    public String addProductToCart(@RequestParam UUID userId, @RequestParam UUID productId){
        Cart cart = cartService.getCartByUserId(userId);
        // If none exists, create a new one
        if (cart == null) {
            cart = new Cart(UUID.randomUUID(), userId, new ArrayList<>());
            cartService.addCart(cart);
        }
        // Get product using product service
        Product product = productService.getProductById(productId);
        if (product != null) {
            cart.getProducts().add(product);
            cartService.addCart(cart);
        }
        return "Product added to cart";
    }

    @PutMapping("/deleteProductFromCart")
    public String deleteProductFromCart(@RequestParam UUID userId, @RequestParam UUID productId){
        Cart cart = cartService.getCartByUserId(userId);
        if (cart == null) {
            return "Cart is empty";
        }
        Product product = productService.getProductById(productId);
        if (product != null) {
            cartService.deleteProductFromCart(cart.getId(), product);
        }
        return "Product deleted from cart";
    }

    @DeleteMapping("/delete/{userId}")
    public String deleteUserById(@PathVariable UUID userId){

        if (this.userService.getUserById(userId) == null) {
            return "User not found";
        }
        this.userService.deleteUserById(userId);

        return "User deleted successfully";
    }


    // Exception Handler to return 400 Bad Request with the error message
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleIllegalArgumentException(IllegalArgumentException ex) {
        return ex.getMessage(); // Return error message in response body
    }
}
