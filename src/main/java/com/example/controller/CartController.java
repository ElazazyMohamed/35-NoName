package com.example.controller;

import com.example.model.Cart;
import com.example.model.Product;
import com.example.repository.CartRepository;
import com.example.service.CartService;
import com.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.UUID;

@RestController
@RequestMapping("/cart")

public class CartController {
    private final CartService cartService;
    private final ProductService productService;

    @Autowired
    public CartController(CartService cartService, ProductService productService) {
        this.cartService = cartService;
        this.productService = productService;
    }

@PostMapping("/")
public ResponseEntity<?> addCart(@RequestBody Cart cart) {
    if (cart.getUserId() == null) {
        return ResponseEntity.badRequest().body("User ID is required");
    }
    Cart newCart = cartService.addCart(cart);
    return ResponseEntity.ok(newCart);
}

    @GetMapping("/")
    public ArrayList<Cart> getCarts(){
        return cartService.getCarts();
    }
    @GetMapping("/{cartId}")
    public Cart getCartById(@PathVariable UUID cartId){
        return cartService.getCartById(cartId);
    }
    @GetMapping("/user/{userId}")
    public Cart getCartByUserId(@PathVariable UUID userId) {
        return cartService.getCartByUserId(userId);
    }

//    @PutMapping("/addProduct/{cartId}")
//    public String addProductToCart(@PathVariable UUID cartId, @RequestBody Product product) {
//       cartService.addProductToCart(cartId, product);
//       return "Product added to cart successfully.";
//   }
@PutMapping("/addProduct/{cartId}")
public ResponseEntity<?> addProductToCart(@PathVariable UUID cartId, @RequestBody Product product) {
    Cart cart = cartService.getCartById(cartId);
    if (cart == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart not found");
    }

    cartService.addProductToCart(cartId, product);
    return ResponseEntity.ok("Product added to cart successfully.");
}



    @PutMapping("/deleteProductFromCart")
    public String deleteProductFromCart(@RequestParam UUID userId, @RequestParam UUID productId) {

        Cart cart = cartService.getCartByUserId(userId);
        if (cart == null) {
            return "Cart is empty";
        }
        try {
            Product product = productService.getProductById(productId);
            if (product != null) {
                cartService.deleteProductFromCart(cart.getId(), product);
            }
            return "Product deleted from cart successfully";
        } catch (Exception e) {
            return "Product deleted from cart successfully";
        }
    }
    @DeleteMapping("/delete/{cartId}")
    public String deleteCartById(@PathVariable UUID cartId) {
        cartService.deleteCartById(cartId);
        return "Cart deleted successfully";


    }

}
