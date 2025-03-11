package com.example.MiniProject1;

import com.example.exception.DatabaseException;
import com.example.exception.ResourceNotFoundException;
import com.example.exception.ServiceException;
import com.example.model.Order;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.service.CartService;
import com.example.service.OrderService;
import com.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)

public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartService cartService;

    @Mock
    private OrderService orderService;

    private UserService underTest;

    @BeforeEach
    void setUp() {
        underTest = new UserService(userRepository, cartService, orderService);
    }

    // addUser Tests
    @Test
    void givenNewUser_addUser_thenUserIsAdded() {
        User user = new User(UUID.randomUUID(), "John Doe");
        when(userRepository.addUser(any(User.class))).thenReturn(user);
        User addedUser = underTest.addUser(user);

        when(userRepository.getUserById(user.getId())).thenReturn(user);
        User retrievedUser = underTest.getUserById(user.getId());

        assertNotNull(retrievedUser);
        assertEquals(addedUser, retrievedUser);
    }

    @Test
    void givenNull_addUser_ShouldThrowException() {
        User newUser = null;
        ServiceException thrown = assertThrows(
                ServiceException.class,
                () -> underTest.addUser(newUser),
                "Expected addUser to throw ServiceException, but it didn't"
        );
        assertEquals("User details cannot be null!", thrown.getMessage());
        verify(userRepository, never()).addUser(any(User.class));
    }

    @Test
    void WhenDatabaseFails_addUser_ShouldThrowException() {
        User user = new User(UUID.randomUUID(), "John Doe");
        when(userRepository.addUser(any(User.class))).thenThrow(DatabaseException.class);

        DatabaseException  thrown = assertThrows(
                DatabaseException.class,
                () -> underTest.addUser(user),
                "Expected addUser to throw DatabaseException, but it didn't"
        );

        assertEquals("Database error occurred while adding user", thrown.getMessage());
        verify(userRepository, times(1)).addUser(any(User.class));
    }

    // getUsers Tests
    @Test
    void WhenUsersExist_getUsers_ShouldReturnUsers() {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User(UUID.randomUUID(), "John Doe"));
        users.add(new User(UUID.randomUUID(), "Jane Doe"));
        when(userRepository.getUsers()).thenReturn(users);

        ArrayList<User> result = underTest.getUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(users, result);
    }

    @Test
    void WhenNoUsersExist_getUsers_ShouldReturnEmptyList() {
        when(userRepository.getUsers()).thenReturn(new ArrayList<>());

        ArrayList<User> result = underTest.getUsers();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void WhenDatabaseFails_getUsers_ShouldThrowException() {
        when(userRepository.getUsers()).thenThrow(DatabaseException.class);

        DatabaseException  thrown = assertThrows(
                DatabaseException.class,
                () -> underTest.getUsers(),
                "Expected getUsers to throw RuntimeException, but it didn't"
        );

        assertEquals("Database error occurred while getting users", thrown.getMessage());
        verify(userRepository, times(1)).getUsers();
    }

    // getUserById Tests
    @Test
    void WhenUserOrderExist_getUserById_ShouldReturnUser() {
        UUID userId = UUID.randomUUID();
        User expectedUser = new User(userId, "John Doe");
        when(userRepository.getUserById(userId)).thenReturn(expectedUser);

        User result = underTest.getUserById(userId);

        assertNotNull(result);
        assertEquals(expectedUser, result);
    }

    @Test
    void WhenUserNotFound_getUserById_ShouldThrowException() {
        UUID userId = UUID.randomUUID();
        when(userRepository.getUserById(userId)).thenReturn(null);

        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class,
                () -> underTest.getUserById(userId),
                "Expected getUserById to throw ResourceNotFoundException, but it didn't"
        );
        assertEquals("User not found!", thrown.getMessage());
        verify(userRepository, never()).addUser(any(User.class));
    }

    @Test
    void WhenDatabaseFails_getUserById_ShouldThrowException() {
        UUID userId = UUID.randomUUID();
        when(userRepository.getUserById(userId)).thenThrow(DatabaseException.class);

        DatabaseException thrown = assertThrows(
                DatabaseException.class,
                () -> underTest.getUserById(userId),
                "Expected getUserById to throw DatabaseException, but it didn't"
        );

        assertEquals("Database error occurred while getting user", thrown.getMessage());
        verify(userRepository, times(1)).getUserById(userId);
    }

    // getOrderByUserId Tests
    @Test
    void WhenUserOrderExists_getOrderByUserId_ReturnOrderList() {
        UUID userId = UUID.randomUUID();
        Order order1 = new Order(UUID.randomUUID(), userId, 100.0);
        Order order2 = new Order(UUID.randomUUID(), userId, 200.0);
        List<Order> orders = List.of(order1, order2);
        when(userRepository.getOrdersByUserId(userId)).thenReturn(orders);

        List<Order> result = underTest.getOrderByUserId(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(orders, result);
    }

    @Test
    void WhenUserNotFound_getOrderByUserId_ShouldThrowException() {
        UUID userId = UUID.randomUUID();
        when(userRepository.getOrdersByUserId(userId)).thenReturn(null);

        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class,
                () -> underTest.getOrderByUserId(userId),
                "Expected getOrderByUserId to throw ResourceNotFoundException, but it didn't"
        );
        assertEquals("User not found!", thrown.getMessage());
        verify(userRepository, never()).addUser(any(User.class));
    }

    @Test
    void WhenUserExistOrderDoesNotExist_getOrderByUserId_ShouldReturnEmptyList() {
        UUID userId = UUID.randomUUID();
        when(userRepository.getOrdersByUserId(userId)).thenReturn(new ArrayList<>());

        List<Order> result = underTest.getOrderByUserId(userId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void WhenCartHasProducts_addOrderToUser_ShouldAddOrderSuccessfully() {
        UUID userId = UUID.randomUUID();
        Order order = new Order(UUID.randomUUID(), userId, 100.0);
        when(cartService.checkoutCart(userId)).thenReturn(order);

        assertDoesNotThrow(() -> underTest.addOrderToUser(userId));

        verify(userRepository, times(1)).addOrderToUser(userId, order);
        verify(orderService, times(1)).addOrder(order);
    }

    @Test
    void WhenCartIsEmptyOrMissing_addOrderToUser_ShouldThrowIllegalStateException() {
        UUID userId = UUID.randomUUID();
        when(cartService.checkoutCart(userId)).thenThrow(new IllegalStateException("Cart is Empty. Cannot create new order"));

        IllegalStateException thrown = assertThrows(
                IllegalStateException.class,
                () -> underTest.addOrderToUser(userId),
                "Expected addOrderToUser to throw IllegalStateException, but it didn't"
        );

        assertEquals("Cart is Empty. Cannot create new order", thrown.getMessage());
        verify(userRepository, never()).addOrderToUser(any(UUID.class), any(Order.class));
        verify(orderService, never()).addOrder(any(Order.class));
    }

    @Test
    void WhenDatabaseFails_addOrderToUser_ShouldThrowDatabaseException() {
        UUID userId = UUID.randomUUID();
        Order order = new Order(UUID.randomUUID(), userId, 100.0);
        when(cartService.checkoutCart(userId)).thenReturn(order);
        doThrow(DatabaseException.class).when(userRepository).addOrderToUser(userId, order);

        DatabaseException thrown = assertThrows(
                DatabaseException.class,
                () -> underTest.addOrderToUser(userId),
                "Expected addOrderToUser to throw DatabaseException, but it didn't"
        );

        assertEquals("Database error occurred while adding order to user", thrown.getMessage());
        verify(orderService, never()).addOrder(any(Order.class));
    }

    @Test
    void WhenCartExists_emptyCart_ShouldSucceed() {
        UUID userId = UUID.randomUUID();
        doNothing().when(cartService).deleteCartById(userId);
        assertDoesNotThrow(() -> underTest.emptyCart(userId));
        verify(cartService, times(1)).deleteCartById(userId);
    }

    @Test
    void WhenCartDoesNotExist_emptyCart_ShouldStillSucceed() {
        UUID userId = UUID.randomUUID();
        doNothing().when(cartService).deleteCartById(userId);

        assertDoesNotThrow(() -> underTest.emptyCart(userId));

        verify(cartService, times(1)).deleteCartById(userId);
    }

    @Test
    void WhenDatabaseFails_emptyCart_ShouldThrowRuntimeException() {
        UUID userId = UUID.randomUUID();
        doThrow(new RuntimeException("Database error occurred while emptying cart")).when(cartService).deleteCartById(userId);

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> underTest.emptyCart(userId),
                "Expected emptyCart to throw DatabaseException, but it didn't"
        );

        assertEquals("Database error occurred while emptying cart", thrown.getMessage());
        verify(cartService, times(1)).deleteCartById(userId);
    }

    @Test
    void WhenOrderExists_removeOrderFromUser_ShouldSucceed() {
        UUID userId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        Order order = new Order(UUID.randomUUID(), userId, 100.0);
        Order order2 = new Order(orderId, userId, 200.0);
        List<Order> orders = List.of(order, order2);

        when(userRepository.getOrdersByUserId(userId)).thenReturn(orders);
        doNothing().when(userRepository).removeOrderFromUser(userId, orderId);
        doNothing().when(orderService).deleteOrderById(orderId);

        assertDoesNotThrow(() -> underTest.removeOrderFromUser(userId, orderId));

        verify(userRepository, times(1)).getOrdersByUserId(userId);
        verify(userRepository, times(1)).removeOrderFromUser(userId, orderId);
        verify(orderService, times(1)).deleteOrderById(orderId);
    }

    @Test
    void WhenOrderDoesNotExist_removeOrderFromUser_ShouldThrowResourceNotFoundException() {
        UUID userId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        Order order = new Order(UUID.randomUUID(), userId, 100.0);
        List<Order> orders = List.of(order);

        when(userRepository.getOrdersByUserId(userId)).thenReturn(orders);

        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class,
                () -> underTest.removeOrderFromUser(userId, orderId),
                "Expected removeOrderFromUser to throw ResourceNotFoundException, but it didn't"
        );

        assertEquals("Order not found!", thrown.getMessage());
        verify(userRepository, times(1)).getOrdersByUserId(userId);
        verify(userRepository, never()).removeOrderFromUser(userId, orderId);
        verify(orderService, never()).deleteOrderById(orderId);
    }

    @Test
    void WhenDatabaseFails_removeOrderFromUser_ShouldThrowDatabaseException() {
        UUID userId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        Order order = new Order(orderId, userId, 100.0);
        List<Order> orders = List.of(order);

        when(userRepository.getOrdersByUserId(userId)).thenReturn(orders);
        doThrow(new DatabaseException("Database error occurred while removing order from user")).when(userRepository).removeOrderFromUser(userId, orderId);

        DatabaseException thrown = assertThrows(
                DatabaseException.class,
                () -> underTest.removeOrderFromUser(userId, orderId),
                "Expected removeOrderFromUser to throw DatabaseException, but it didn't"
        );

        assertEquals("Database error occurred while removing order from user", thrown.getMessage());
        verify(userRepository, times(1)).getOrdersByUserId(userId);
        verify(orderService, never()).deleteOrderById(orderId);
    }

    @Test
    void WhenUserExists_deleteUserById_ShouldDeleteUser() {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "John Doe");
        when(userRepository.getUserById(userId)).thenReturn(user);
        doNothing().when(userRepository).deleteUserById(userId);

        assertDoesNotThrow(() -> underTest.deleteUserById(userId));

        verify(userRepository, times(1)).getUserById(userId);
        verify(userRepository, times(1)).deleteUserById(userId);
    }

    @Test
    void WhenUserDoesNotExist_deleteUserById_ShouldThrowResourceNotFoundException() {
        UUID userId = UUID.randomUUID();

        doThrow(new ResourceNotFoundException("User not found!")).when(userRepository).getUserById(userId);

        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class,
                () -> underTest.deleteUserById(userId),
                "Expected deleteUserById to throw ResourceNotFoundException, but it didn't"
        );

        assertEquals("User not found!", thrown.getMessage());

        verify(userRepository, times(1)).getUserById(userId);
        verify(userRepository, never()).deleteUserById(userId);
    }

    @Test
    void WhenDatabaseFails_deleteUserById_ShouldThrowDatabaseException() {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "John Doe");
        when(userRepository.getUserById(userId)).thenReturn(user);

        doThrow(new DatabaseException("Database error occurred while deleting user")).when(userRepository).deleteUserById(userId);

        DatabaseException thrown = assertThrows(
                DatabaseException.class,
                () -> underTest.deleteUserById(userId),
                "Expected deleteUserById to throw DatabaseException, but it didn't"
        );

        assertEquals("Database error occurred while deleting user", thrown.getMessage());

        verify(userRepository, times(1)).getUserById(userId);
        verify(userRepository, times(1)).deleteUserById(userId);
    }
}
