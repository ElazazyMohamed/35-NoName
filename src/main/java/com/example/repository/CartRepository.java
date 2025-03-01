package com.example.repository;

import com.example.model.Cart;
import com.example.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.UUID;


@Repository
@SuppressWarnings("rawtypes")
public class CartRepository extends MainRepository<Cart> {

    @Override
    protected String getDataPath() {
        return "src/main/java/com/example/data/carts.json"; // Ensure this path is correct
    }

    @Override
    protected Class<Cart[]> getArrayType() {
        return Cart[].class;
    }

    public CartRepository() {
    }
    //adding a new cart
    public Cart addCart(Cart cart) {
        save(cart);
        return cart;
    }
    //Get All Carts
    public ArrayList<Cart> getCarts() {
        return findAll();
    }
    //Finding carts by its cartID
    public Cart getCartById(UUID cartId) {
        return findAll().stream()
                .filter(cart -> cart.getId().equals(cartId))
                .findFirst()
                .orElse(null);
    }
    //get a user's cart by the user's ID
    public Cart getCartByUserId(UUID userId) {
        return findAll().stream()
                .filter(cart -> cart.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }
    //Adding product to CART
    public void addProductToCart(UUID cartId, Product product) {
        ArrayList<Cart> carts = findAll();
        for (Cart cart : carts) {
            if (cart.getId().equals(cartId)) {
                cart.getProducts().add(product);
                saveAll(carts);
                return;
            }
        }
    }
    //Delete a product from Cart
    public void deleteProductFromCart(UUID cartId, Product product) {
        ArrayList<Cart> carts = findAll();
        for (Cart cart : carts) {
            if (cart.getId().equals(cartId)) {
                cart.getProducts().removeIf(p -> p.getId().equals(product.getId()));
                saveAll(carts);
                return;
            }
        }
    }
    //Delete cart by ID
    public void deleteCartById(UUID cartId) {
        ArrayList<Cart> carts = findAll();
        carts.removeIf(cart -> cart.getId().equals(cartId));
        saveAll(carts);
    }



}
