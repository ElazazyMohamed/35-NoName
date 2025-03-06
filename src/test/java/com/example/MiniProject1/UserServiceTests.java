package com.example.MiniProject1;

import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.service.CartService;
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

public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartService cartService;

    private UserService underTest;

    @BeforeEach
    void setUp() {
        underTest = new UserService(userRepository, cartService);
    }

    @Test
    void addUser1() {
    }

    @Test
    void addUser2() {
    }

    @Test
    void addUser3() {
    }

    @Test
    void getUsers1() {
    }

    @Test
    void getUsers2() {
    }

    @Test
    void getUsers3() {
    }

    @Test
    void getUserById1() {
    }

    @Test
    void getUserById2() {
    }

    @Test
    void getUserById3() {
    }

    @Test
    void getOrderByUserId1() {
    }

    @Test
    void getOrderByUserId2() {
    }

    @Test
    void getOrderByUserId3() {
    }

    @Test
    void addOrderToUser1() {
    }

    @Test
    void addOrderToUser2() {
    }

    @Test
    void addOrderToUser3() {
    }

    @Test
    void emptyCart1() {
    }

    @Test
    void emptyCart2() {
    }

    @Test
    void emptyCart3() {
    }

    @Test
    void removeOrderFromUser1() {
    }

    @Test
    void removeOrderFromUser2() {
    }

    @Test
    void removeOrderFromUser3() {
    }

    @Test
    void deleteUserById1() {
    }

    @Test
    void deleteUserById2() {
    }

    @Test
    void deleteUserById3() {
    }
}
