package com.example.MiniProject1;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.model.Order;
import com.example.model.Product;
import com.example.service.OrderService;
import com.example.repository.OrderRepository;

@ComponentScan(basePackages = "com.example.*")
@WebMvcTest
class OrderServiceTests {

    @Value("${spring.application.orderDataPath}")
    private String orderDataPath;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    private UUID orderId1, orderId2, userId;
    private Order testOrder1, testOrder2;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        orderId1 = UUID.randomUUID();
        orderId2 = UUID.randomUUID();

        testOrder1 = new Order(orderId1, userId, 50.0);
        testOrder2 = new Order(orderId2, userId, 75.0);

        // Reset JSON data before each test
        try {
            objectMapper.writeValue(new File(orderDataPath), new ArrayList<Order>());
        } catch (IOException e) {
            throw new RuntimeException("Failed to reset order data", e);
        }
    }

    // ------------------ ✅ 1. Add Order (POST /order/) ------------------

    @Test
    void testAddOrder_Success() throws Exception {
        String orderJson = objectMapper.writeValueAsString(testOrder1);

        mockMvc.perform(MockMvcRequestBuilders.post("/order/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson))
                .andExpect(status().isOk());

        assertEquals(1, orderService.getOrders().size());
    }

    @Test
    void testAddMultipleOrders() throws Exception {
        String orderJson1 = objectMapper.writeValueAsString(testOrder1);
        String orderJson2 = objectMapper.writeValueAsString(testOrder2);

        mockMvc.perform(MockMvcRequestBuilders.post("/order/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson1))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.post("/order/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson2))
                .andExpect(status().isOk());

        assertEquals(2, orderService.getOrders().size());
    }

    @Test
    void testAddOrder_InvalidRequest() throws Exception {
        String invalidOrderJson = "{}"; // Empty JSON (invalid request)

        mockMvc.perform(MockMvcRequestBuilders.post("/order/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidOrderJson))
                .andExpect(status().isBadRequest());
    }

    // ------------------ ✅ 2. Get Order by ID (GET /order/{orderId}) ------------------

    @Test
    void testGetOrderById_Success() throws Exception {
        orderService.addOrder(testOrder1);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/order/" + orderId1))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        Order returnedOrder = objectMapper.readValue(responseJson, Order.class);

        assertNotNull(returnedOrder);
        assertEquals(orderId1, returnedOrder.getId());
    }

    @Test
    void testGetOrderById_NonExistingOrder() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/order/" + UUID.randomUUID()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetOrderById_InvalidIdFormat() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/order/invalid-uuid"))
                .andExpect(status().isBadRequest());
    }

    // ------------------ ✅ 3. Get All Orders (GET /order/) ------------------

    @Test
    void testGetAllOrders_Success() throws Exception {
        orderService.addOrder(testOrder1);
        orderService.addOrder(testOrder2);

        mockMvc.perform(MockMvcRequestBuilders.get("/order/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2)); // Expect 2 orders
    }

    @Test
    void testGetAllOrders_EmptyList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/order/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0)); // Expect 0 orders
    }

    @Test
    void testGetAllOrders_AfterDeletion() throws Exception {
        orderService.addOrder(testOrder1);
        orderService.deleteOrderById(orderId1);

        mockMvc.perform(MockMvcRequestBuilders.get("/order/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0)); // Should be empty after deletion
    }

    // ------------------ ✅ 4. Delete Order by ID (DELETE /order/delete/{orderId}) ------------------

    @Test
    void testDeleteOrderById_Success() throws Exception {
        orderService.addOrder(testOrder1);

        mockMvc.perform(MockMvcRequestBuilders.delete("/order/delete/" + orderId1))
                .andExpect(status().isOk())
                .andExpect(content().string("Order deleted successfully"));

        assertEquals(0, orderService.getOrders().size());
    }

    @Test
    void testDeleteOrderById_NonExistingOrder() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/order/delete/" + UUID.randomUUID()))
                .andExpect(content().string("Order not found"));
    }

    @Test
    void testDeleteOrderById_InvalidIdFormat() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/order/delete/invalid-uuid"))
                .andExpect(content().string("Invalid UUID string: invalid-uuid"));
    }
}
