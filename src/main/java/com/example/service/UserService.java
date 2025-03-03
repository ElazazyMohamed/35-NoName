package com.example.service;

import com.example.model.Cart;
import com.example.model.Order;
import com.example.model.Product;
import com.example.model.User;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@SuppressWarnings("rawtypes")
public class UserService extends MainService<User> {
    private final UserRepository userRepository;
    private final CartService cartService;

    @Autowired
    public UserService(UserRepository userRepository, CartService cartService) {
        this.userRepository = userRepository;
        this.cartService = cartService;
    }

    public User addUser(User user) {
        this.userRepository.addUser(user);
        return user;
    }

    public ArrayList<User> getUsers() {
        return this.userRepository.getUsers();
    }

    public User getUserById(UUID userId) {
        User user = this.userRepository.getUserById(userId);
        if (user == null) {
            throw new NoSuchElementException("User not found.");
        }
        return user;
    }

    public List<Order> getOrderByUserId(UUID userId) {
        List<Order> orders = this.userRepository.getOrdersByUserId(userId);
        if (orders == null) {
            throw new NoSuchElementException("Orders not found.");
        }
        return orders;
    }

    public void addOrderToUser(UUID userId) {
        Cart cart = this.cartService.getCartByUserId(userId);
        if (cart == null || cart.getProducts().isEmpty()) {
            throw new IllegalStateException("Cart is Empty.");
        }
        Order order = new Order(
                UUID.randomUUID(),
                userId,
                cart.getProducts().stream().mapToDouble(Product::getPrice).sum()
        );
        order.setProducts(cart.getProducts());
        this.userRepository.addOrderToUser(userId, order);
    }

    public void removeOrderFromUer(UUID userId, UUID orderId) {
        this.userRepository.removeOrderFromUser(userId, orderId);
    }

    public void emptyCart(UUID userId){
        Cart cart = this.cartService.getCartByUserId(userId);
        this.cartService.deleteCartById(cart.getId());
    }

    public void deleteUserByUserId(UUID userId) {
        this.userRepository.deleteUserById(userId);
    }
}
