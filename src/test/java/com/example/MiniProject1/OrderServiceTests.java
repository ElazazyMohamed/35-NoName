package com.example.MiniProject1;

import com.example.model.User;
import com.example.repository.OrderRepository;
import com.example.repository.UserRepository;
import com.example.service.CartService;
import com.example.service.OrderService;
import com.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)

public class OrderServiceTests {

    @Mock
    private OrderRepository orderRepository;

    private OrderService underTest;

    @BeforeEach
    void setUp() {
        underTest = new OrderService(orderRepository);
    }

    @Test
    void addOrder1() {
    }

    @Test
    void addOrder2() {
    }

    @Test
    void addOrder3() {
    }

    @Test
    void getOrders1() {
    }

    @Test
    void getOrders2() {
    }

    @Test
    void getOrders3() {
    }

    @Test
    void getOrderById1() {
    }

    @Test
    void getOrderById2() {
    }

    @Test
    void getOrderById3() {
    }

    @Test
    void deleteOrderById1() {
    }

    @Test
    void deleteOrderById2() {
    }

    @Test
    void deleteOrderById3() {
    }
}
