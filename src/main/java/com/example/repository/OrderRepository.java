package com.example.repository;

import com.example.model.Order;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.UUID;

@Repository
@SuppressWarnings("rawtypes")
public class OrderRepository extends MainRepository<Order> {

    @Value("${spring.data.SPRING_DATA_ORDERS}")
    private String DATA_PATH = "src/main/java/com/example/data/orders.json";

    @Override
    protected String getDataPath() {
        return DATA_PATH;
    }

    @Override
    protected Class<Order[]> getArrayType() {
        return Order[].class;
    }

    public OrderRepository() {
    }

    public void addOrder(Order order){
        this.save(order);
    }

    public ArrayList<Order> getOrders(){
        return this.findAll();
    }
    public Order getOrderById(UUID orderId){
        ArrayList<Order> orders = this.getOrders();
        for (Order order : orders) {
            if (order.getId().equals(orderId)) {
                return order;
            }
        }
        return null;
    }

    public void deleteOrderById(UUID orderId){
        ArrayList<Order> orders = this.getOrders();
        for (Order order : orders) {
            if (order.getId().equals(orderId)) {
                orders.remove(order);
                this.saveAll(orders);
                return;
            }
        }
    }


}
