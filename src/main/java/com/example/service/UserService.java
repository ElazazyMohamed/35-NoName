package com.example.service;

import com.example.model.Order;
import com.example.model.User;
import com.example.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@SuppressWarnings("rawtypes")
public class UserService extends MainService<User> {
    private UserRepository userRepository;
    private CartService cartService;

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
        return this.userRepository.getUserById(userId);
    }
    public List<Order> getOrderByUserID(UUID userId) {
        return this.userRepository.getOrdersByUserId(userId);
    }

    public void addOrderToUser(UUID userId) {
        Order order = this.cartService.checkoutCart(userId);
        this.userRepository.addOrderToUser(userId, order);
    }
    public void emptyCart(UUID userId){
        this.cartService.deleteCartById(userId);
    }
    public void removeOrderFromUer(UUID userId, UUID orderId) {
        this.userRepository.removeOrderFromUser(userId, orderId);
    }
    public void deleteUserByUserId(UUID userId) {
        this.userRepository.deleteUserById(userId);
    }
}
