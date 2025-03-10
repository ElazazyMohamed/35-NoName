package com.example.MiniProject1;

import com.example.model.Order;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.service.CartService;
import com.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTests {

    private UserService userService;
    private UserRepository userRepository;
    private CartService cartService;

    private UUID userId;
    private User testUser;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        cartService = mock(CartService.class);
        userService = new UserService(userRepository, cartService);

        userId = UUID.randomUUID();
        testUser = new User();
        testUser.setId(userId);
        testUser.setName("Test User");
    }

    // ------------------ addUser() Tests ------------------

    @Test
    void testAddUser_Success() {
        when(userRepository.addUser(any(User.class))).thenReturn(testUser);

        User result = userService.addUser(testUser);

        assertNotNull(result, "User should not be null");
        assertEquals(testUser.getId(), result.getId(), "Returned user ID should match");
        assertEquals(testUser.getName(), result.getName(), "User name should match");
    }

    @Test
    void testAddUser_NullUser() {
        User result = userService.addUser(null);

        assertNull(result, "Adding null user should return null");
    }

    @Test
    void testAddUser_RepositoryFailure() {
        doThrow(new RuntimeException("Database error")).when(userRepository).addUser(any(User.class));

        assertThrows(RuntimeException.class, () -> userService.addUser(testUser), "Should throw exception on DB failure");
    }

    // ------------------ getUsers() Tests ------------------

    @Test
    void testGetUsers_Success() {
        ArrayList<User> users = new ArrayList<>();
        users.add(testUser);
        when(userRepository.getUsers()).thenReturn(users);

        List<User> result = userService.getUsers();

        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.size(), "Should return exactly one user");
    }

    @Test
    void testGetUsers_EmptyList() {
        when(userRepository.getUsers()).thenReturn(new ArrayList<>());

        List<User> result = userService.getUsers();

        assertNotNull(result, "Result should not be null");
        assertTrue(result.isEmpty(), "Should return an empty list");
    }

    @Test
    void testGetUsers_MultipleUsers() {
        // Create multiple test users
         User user1 = new User();
         user1.setId(UUID.randomUUID());
         user1.setName("Alice");
         User user2 = new User();
         user2.setId(UUID.randomUUID());
         user2.setName("Bob");
         User user3 = new User();
         user3.setId(UUID.randomUUID());
         user3.setName("Charlie");


        List<User> mockUsers = List.of(user1, user2, user3,testUser);

        // Mock repository response to return multiple users
        when(userRepository.getUsers()).thenReturn(new ArrayList<>(mockUsers));

        // Call the service method
        List<User> result = userService.getUsers();

        // Assertions
        assertNotNull(result, "Result should not be null");
        assertEquals(4, result.size(), "Should return exactly 4 users");
        assertTrue(result.contains(user1), "User list should contain Alice");
        assertTrue(result.contains(user2), "User list should contain Bob");
        assertTrue(result.contains(user3), "User list should contain Charlie");
    }

    // ------------------ getUserById() Tests ------------------

    @Test
    void testGetUserById_Success() {
        when(userRepository.getUserById(userId)).thenReturn(testUser);

        User result = userService.getUserById(userId);

        assertNotNull(result, "User should not be null");
        assertEquals(userId, result.getId(), "User ID should match");
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.getUserById(userId)).thenReturn(null);

        User result = userService.getUserById(userId);

        assertNull(result, "User should be null if not found");
    }

    @Test
    void testGetUserById_RepositoryFailure() {
        when(userRepository.getUserById(userId)).thenThrow(new RuntimeException("DB error"));

        assertThrows(RuntimeException.class, () -> userService.getUserById(userId), "Should throw exception on DB failure");
    }

    // ------------------ deleteUserById() Tests ------------------

    @Test
    void testDeleteUserById_Success() {
        doNothing().when(userRepository).deleteUserById(userId);

        assertDoesNotThrow(() -> userService.deleteUserById(userId), "Should delete user without exception");
    }

    @Test
    void testDeleteUserById_NotFound() {
        doThrow(new RuntimeException("User not found")).when(userRepository).deleteUserById(userId);

        assertThrows(RuntimeException.class, () -> userService.deleteUserById(userId), "Should throw exception if user not found");
    }

    @Test
    void testDeleteUserById_RepositoryFailure() {
        doThrow(new RuntimeException("DB error")).when(userRepository).deleteUserById(userId);

        assertThrows(RuntimeException.class, () -> userService.deleteUserById(userId), "Should throw an exception on DB failure");
    }

    // ------------------ getOrderByUserId() Tests ------------------

    @Test
    void testGetOrderByUserId_Success() {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(UUID.randomUUID(), userId, 100.0));
        orders.add(new Order(UUID.randomUUID(), userId, 50.0));

        when(userRepository.getOrdersByUserId(userId)).thenReturn(orders);

        List<Order> result = userService.getOrderByUserId(userId);

        assertNotNull(result, "Result should not be null");
        assertEquals(2, result.size(), "Should return exactly 2 orders");
    }

    @Test
    void testGetOrderByUserId_NoOrders() {
        when(userRepository.getOrdersByUserId(userId)).thenReturn(new ArrayList<>());

        List<Order> result = userService.getOrderByUserId(userId);

        assertNotNull(result, "Result should not be null");
        assertTrue(result.isEmpty(), "Should return an empty list if no orders exist");
    }

    @Test
    void testGetOrderByUserId_RepositoryFailure() {
        when(userRepository.getOrdersByUserId(userId)).thenThrow(new RuntimeException("DB error"));

        assertThrows(RuntimeException.class, () -> userService.getOrderByUserId(userId), "Should throw exception on DB failure");
    }

    // ------------------ addOrderToUser() Tests ------------------

    @Test
    void testAddOrderToUser_Success() {
        Order order = new Order(UUID.randomUUID(), userId, 100.0);
        when(cartService.checkoutCart(userId)).thenReturn(order);
        doNothing().when(userRepository).addOrderToUser(userId, order);

        assertDoesNotThrow(() -> userService.addOrderToUser(userId), "Should add order to user successfully");
    }

    @Test
    void testAddOrderToUser_EmptyCart() {
        // Simulate an empty cart where checkout returns an order with zero total price
        Order emptyOrder = new Order(UUID.randomUUID(), userId, 0.0);

        // Mock checkoutCart to return an empty order
        when(cartService.checkoutCart(userId)).thenReturn(emptyOrder);

        // Execute the service method
        userService.addOrderToUser(userId);

        // Verify that the order was still added to the user, even if it's empty
        verify(userRepository, times(1)).addOrderToUser(userId, emptyOrder);
    }

    @Test
    void testAddOrderToUser_RepositoryFailure() {
        Order order = new Order(UUID.randomUUID(), userId, 100.0);
        when(cartService.checkoutCart(userId)).thenReturn(order);
        doThrow(new RuntimeException("DB error")).when(userRepository).addOrderToUser(userId, order);

        assertThrows(RuntimeException.class, () -> userService.addOrderToUser(userId), "Should throw exception if repository fails");
    }

    // ------------------ emptyCart() Tests ------------------

    @Test
    void testEmptyCart_Success() {
        doNothing().when(cartService).deleteCartById(userId);

        assertDoesNotThrow(() -> userService.emptyCart(userId), "Should empty cart successfully");
    }

    @Test
    void testEmptyCart_NonExistingCart() {
        doThrow(new RuntimeException("Cart not found")).when(cartService).deleteCartById(userId);

        assertThrows(RuntimeException.class, () -> userService.emptyCart(userId), "Should throw exception if cart does not exist");
    }

    @Test
    void testEmptyCart_RepositoryFailure() {
        doThrow(new RuntimeException("DB error")).when(cartService).deleteCartById(userId);

        assertThrows(RuntimeException.class, () -> userService.emptyCart(userId), "Should throw exception if repository fails");
    }

    // ------------------ removeOrderFromUser() Tests ------------------

    @Test
    void testRemoveOrderFromUser_Success() {
        UUID orderId = UUID.randomUUID();
        doNothing().when(userRepository).removeOrderFromUser(userId, orderId);

        assertDoesNotThrow(() -> userService.removeOrderFromUser(userId, orderId), "Should remove order successfully");
    }

    @Test
    void testRemoveOrderFromUser_NonExistingOrder() {
        UUID orderId = UUID.randomUUID();
        doThrow(new RuntimeException("Order not found")).when(userRepository).removeOrderFromUser(userId, orderId);

        assertThrows(RuntimeException.class, () -> userService.removeOrderFromUser(userId, orderId), "Should throw exception if order does not exist");
    }

    @Test
    void testRemoveOrderFromUser_RepositoryFailure() {
        UUID orderId = UUID.randomUUID();
        doThrow(new RuntimeException("DB error")).when(userRepository).removeOrderFromUser(userId, orderId);

        assertThrows(RuntimeException.class, () -> userService.removeOrderFromUser(userId, orderId), "Should throw exception if repository fails");
    }
}
