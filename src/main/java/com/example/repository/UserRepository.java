package com.example.repository;

import com.example.model.Order;
import com.example.model.Product;
import com.example.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@SuppressWarnings("rawtypes")
public class UserRepository extends MainRepository<User> {

    private static final String DATA_PATH = "src/main/java/com/example/data/users.json";

    public UserRepository() {

    }

    @Override
    protected String getDataPath() {
        return DATA_PATH;
    }

    @Override
    protected Class<User[]> getArrayType() {
        return User[].class;
    }

    public ArrayList<User> getUsers() {
        return this.findAll();
    }

    public User getUserById(UUID userId) {
        ArrayList<User> users = this.getUsers();
        for (User user : users) {
            if (user.getId().equals(userId)) {
                return user;
            }
        }
        return null;
    }

    public User addUser(User user) {
        ArrayList<User> users = this.getUsers();
        users.add(user);
        this.saveAll(users);
        return user;
    }

    public List<Order> getOrdersByUserId(UUID userId) {
        User user = this.getUserById(userId);
        if (user != null) {
            return user.getOrders();
        }
        return null;
    }

    public void addOrderToUser(UUID userId, Order order) {
        User user = this.getUserById(userId);
        if (user != null) {
            List<Order> orders = user.getOrders();
            orders.add(order);
            user.setOrders(orders);
            this.save(user);
        }
    }

    public void removeOrderFromUser(UUID userId, UUID orderId) {
        User user = this.getUserById(userId);
        if (user != null) {
            List<Order> orders = user.getOrders();
            orders.removeIf(order -> order.getId().equals(orderId));
            user.setOrders(orders);
            this.save(user);
        }
    }

    public void deleteUserById(UUID userId){
        ArrayList<User> users = this.getUsers();
        users.removeIf(user -> user.getId().equals(userId));
        this.saveAll(users);
    }
}
