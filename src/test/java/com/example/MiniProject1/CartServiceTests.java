package com.example.MiniProject1;

import com.example.model.User;
import com.example.repository.CartRepository;
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

public class CartServiceTests {

    @Mock
    private CartRepository cartRepository;

    private CartService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CartService(cartRepository);
    }

    @Test
    void addCart1() {
    }

    @Test
    void addCart2() {
    }

    @Test
    void addCart3() {
    }

    @Test
    void getCarts1() {
    }

    @Test
    void getCarts2() {
    }

    @Test
    void getCarts3() {
    }

    @Test
    void getCartById1() {
    }

    @Test
    void getCartById2() {
    }

    @Test
    void getCartById3() {
    }

    @Test
    void getCartByUserId1() {
    }

    @Test
    void getCartByUserId2() {
    }

    @Test
    void getCartByUserId3() {
    }

    @Test
    void addProductToCart1() {
    }

    @Test
    void addProductToCart2() {
    }

    @Test
    void addProductToCart3() {
    }

    @Test
    void deleteProductFromCart1() {
    }

    @Test
    void deleteProductFromCart2() {
    }

    @Test
    void deleteProductFromCart3() {
    }

    @Test
    void deleteCartById1() {
    }

    @Test
    void deleteCartById2() {
    }

    @Test
    void deleteCartById3() {
    }

    @Test
    void checkoutCart1() {
    }

    @Test
    void checkoutCart2() {
    }

    @Test
    void checkoutCart3() {
    }
}
