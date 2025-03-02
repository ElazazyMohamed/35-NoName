//package com.example.repository;
//
//import java.util.ArrayList;
//import java.util.UUID;
//
//import com.example.model.Cart;
//import com.example.model.Product;
//
//public class CartRepositoryManualTest {
//    public static void main(String[] args) {
//        CartRepository cartRepository = new CartRepository();
//
////        // Create and add a new Cart
////        UUID cartId = UUID.randomUUID();
////        UUID userId = UUID.randomUUID();
////        Cart cart = new Cart(cartId, userId);
////        cartRepository.addCart(cart);
////        System.out.println("Cart added!");
////
////        // Fetch and print all carts
////        System.out.println("All Carts: " + cartRepository.getCarts());
////
////        // Add a product to the cart
////        Product product = new Product(UUID.randomUUID(), "Ammar", 3700);
////        cartRepository.addProductToCart(cartId, product);
////        System.out.println("Product added!");
////
////        // Fetch and print updated cart
////        System.out.println("Updated Cart: " + cartRepository.getCartById(cartId));
////
////        // Delete product from cart
////        //cartRepository.deleteProductFromCart(cartId, product);
////        //System.out.println("Product deleted!");
////
////        // Fetch and print after deletion
////        System.out.println("Updated Cart After Deletion: " + cartRepository.getCartById(cartId));
////
////        // Delete the cart
////       // cartRepository.deleteCartById(cartId);
////       // System.out.println("Cart deleted!");
////
////        // Check final state
////        System.out.println("Final Carts: " + cartRepository.getCarts());
////        cartRepository.deleteCartById(UUID.fromString("fe23189d-c7e1-41c9-b07b-424e34fb4035"));
////        cartRepository.deleteCartById(UUID.fromString("dc57a581-ed02-423f-8b09-cd1f21f5db4e"));
//
//    }
//}
