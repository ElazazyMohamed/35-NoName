package com.example.MiniProject1;

import com.example.model.User;
import com.example.repository.ProductRepository;
import com.example.repository.UserRepository;
import com.example.service.CartService;
import com.example.service.ProductService;
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

public class ProductServiceTests {

    @Mock
    private ProductRepository productRepository;

    private ProductService underTest;

    @BeforeEach
    void setUp() {
        underTest = new ProductService(productRepository);
    }

    @Test
    void addProduct1() {
    }

    @Test
    void addProduct2() {
    }

    @Test
    void addProduct3() {
    }

    @Test
    void getProducts1() {
    }

    @Test
    void getProducts2() {
    }

    @Test
    void getProducts3() {
    }

    @Test
    void getProductById1() {
    }

    @Test
    void getProductById2() {
    }

    @Test
    void getProductById3() {
    }

    @Test
    void updateProduct1() {
    }

    @Test
    void updateProduct2() {
    }

    @Test
    void updateProduct3() {
    }

    @Test
    void applyDiscount1() {
    }

    @Test
    void applyDiscount2() {
    }

    @Test
    void applyDiscount3() {
    }

    @Test
    void deleteProductById1() {
    }

    @Test
    void deleteProductById2() {
    }

    @Test
    void deleteProductById3() {
    }
}
