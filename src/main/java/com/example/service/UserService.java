package com.example.service;

import com.example.exception.DatabaseException;
import com.example.exception.ResourceNotFoundException;
import com.example.exception.ServiceException;
import com.example.model.Order;
import com.example.model.User;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@SuppressWarnings("rawtypes")
public class UserService extends MainService<User> {
    private final UserRepository userRepository;
    private final CartService cartService;
    private final OrderService orderService;

    @Autowired
    public UserService(UserRepository userRepository, CartService cartService, OrderService orderService) {
        this.userRepository = userRepository;
        this.cartService = cartService;
        this.orderService = orderService;
    }

    public User addUser(User user) {
        if (user == null) {
            throw new ServiceException("User details cannot be null!");
        }
        if (this.userRepository.getUserById(user.getId()) != null) {
            throw new ResourceNotFoundException("User already exists!");
        }
        try {
            return this.userRepository.addUser(user);
        } catch (RuntimeException e) {
            throw new DatabaseException("Database error occurred while adding user", e);
        }
    }

    public ArrayList<User> getUsers() {
        try {
            return this.userRepository.getUsers();
        } catch (RuntimeException e) {
            throw new DatabaseException("Database error occurred while getting users", e);
        }
    }

    public User getUserById(UUID userId) {
        try {
            User user = this.userRepository.getUserById(userId);
            if (user == null) {
                throw new ResourceNotFoundException("User not found!");
            }
            return user;
        } catch (DatabaseException e) {
            throw new DatabaseException("Database error occurred while getting user", e);
        }
    }

    public List<Order> getOrderByUserId(UUID userId) {
        try {
            List<Order> orders = this.userRepository.getOrdersByUserId(userId);
            if (orders == null) {
                throw new ResourceNotFoundException("User not found!");
            }
            return orders;
        } catch (DatabaseException e) {
            throw new DatabaseException("Database error occurred while getting orders", e);
        }
    }

    public void addOrderToUser(UUID userId) {
        try {
            Order order = this.cartService.checkoutCart(userId);
            this.userRepository.addOrderToUser(userId, order);
            this.orderService.addOrder(order);
        } catch (DatabaseException e) {
            throw new DatabaseException("Database error occurred while adding order to user", e);
        }
    }

    public void emptyCart(UUID userId){
        try {
            this.cartService.deleteCartById(userId);
        } catch (DatabaseException e) {
            throw new RuntimeException("Database error occurred while emptying cart", e);
        }
    }

    public void removeOrderFromUser(UUID userId, UUID orderId) {
        try{
            List<Order> orders = this.userRepository.getOrdersByUserId(userId);
            boolean orderExists = false;
            for (Order order : orders) {
                if (order.getId().equals(orderId)) {
                    orderExists = true;
                    break;
                }
            }
            if (!orderExists) {
                throw new ResourceNotFoundException("Order not found!");
            }
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Order not found!");
        }
        try {
            this.userRepository.removeOrderFromUser(userId, orderId);
            this.orderService.deleteOrderById(orderId);
        } catch (DatabaseException e) {
            throw new DatabaseException("Database error occurred while removing order from user", e);
        }
    }

    public void deleteUserById(UUID userId) {
        try {
            this.userRepository.getUserById(userId);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("User not found!");
        }
        try {
            this.userRepository.deleteUserById(userId);
        } catch (DatabaseException e) {
            throw new DatabaseException("Database error occurred while deleting user", e);
        }
    }
}
