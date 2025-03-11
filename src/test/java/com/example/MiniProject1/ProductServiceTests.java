package com.example.MiniProject1;

import com.example.exception.DatabaseException;
import com.example.exception.ResourceNotFoundException;
import com.example.exception.ServiceException;
import com.example.model.Product;
import com.example.repository.ProductRepository;
import com.example.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
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

    // Add Products
    @Test
    void WhenProductExisit_addProduct_AddProduct() {
        UUID productId = UUID.randomUUID();
        Product product = new Product(productId, "Laptop", 1000.0);
        when(productRepository.addProduct(product)).thenReturn(product);

        Product result = underTest.addProduct(product);

        assertEquals(product, result);
        verify(productRepository, times(1)).addProduct(product);
    }

    @Test
    void WhenNull_addProduct_ShouldThrowServiceException() {
        Product product  = null;

        ServiceException thrown = assertThrows(
                ServiceException.class,
                () -> underTest.addProduct(product),
                "Expected addProduct to throw ServiceException, but it didn't"
        );

        assertEquals("Product cannot be null", thrown.getMessage());
        verify(productRepository, never()).addProduct(any(Product.class));
    }

    @Test
    void WhenDatabaseFails_addProduct_ShouldThrowDatabaseException () {
        Product newProduct = new Product(UUID.randomUUID(), "Phone", 500.0);

        doThrow(new DatabaseException("Error adding product"))
                .when(productRepository).addProduct(newProduct);
        DatabaseException thrown = assertThrows(
                DatabaseException.class,
                () -> underTest.addProduct(newProduct),
                "Expected addProduct to throw DatabaseException, but it didn't"
        );

        assertEquals("Error adding product", thrown.getMessage());
        verify(productRepository, times(1)).addProduct(newProduct);
    }

    // getProducts
    @Test
    void WhenProductsExist_getProducts_ShouldReturnProductList() {
        ArrayList<Product> mockProducts = new ArrayList<>(List.of(
                new Product(UUID.randomUUID(), "Laptop", 1000.0),
                new Product(UUID.randomUUID(), "Phone", 500.0)
        ));
        when(productRepository.getProducts()).thenReturn(mockProducts);

        ArrayList<Product> result = underTest.getProducts();

        assertEquals(2, result.size());
        assertEquals(mockProducts, result);
        verify(productRepository, times(1)).getProducts();
    }

    @Test
    void WhenNoProductsExist_getProducts_ShouldReturnEmptyList() {
        when(productRepository.getProducts()).thenReturn(new ArrayList<>());

        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class,
                () -> underTest.getProducts(),
                "Expected getProducts to throw ResourceNotFoundException, but it didn't"
        );

        assertEquals("No products found", thrown.getMessage());
        verify(productRepository, times(1)).getProducts();
    }

    @Test
    void WhenDatabaseFails_getProducts_ShouldThrowDatabaseException() {
        doThrow(new DatabaseException("Database error occurred while getting products"))
                .when(productRepository).getProducts();

        DatabaseException thrown = assertThrows(
                DatabaseException.class,
                () -> underTest.getProducts(),
                "Expected getProducts to throw DatabaseException, but it didn't"
        );

        assertEquals("Database error occurred while getting products", thrown.getMessage());
        verify(productRepository, times(1)).getProducts();
    }

    // getProductById
    @Test
    void WhenProductExists_getProductById_ShouldReturnProduct() {
        UUID productId = UUID.randomUUID();
        Product mockProduct = new Product(productId, "Laptop", 1000.0);
        when(productRepository.getProductById(productId)).thenReturn(mockProduct);

        Product result = underTest.getProductById(productId);

        assertNotNull(result);
        assertEquals(mockProduct, result);
        verify(productRepository, times(1)).getProductById(productId);
    }

    @Test
    void WhenProductDoesNotExist_getProductById_ShouldThrowResourceNotFoundException() {
        UUID productId = UUID.randomUUID();
        when(productRepository.getProductById(productId)).thenReturn(null);

        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class,
                () -> underTest.getProductById(productId),
                "Expected getProductById to throw ResourceNotFoundException, but it didn't"
        );

        assertEquals("Product not found!", thrown.getMessage());
        verify(productRepository, times(1)).getProductById(productId);
    }

    @Test
    void WhenDatabaseFails_getProductById_ShouldThrowDatabaseException() {
        UUID productId = UUID.randomUUID();
        doThrow(new DatabaseException("Database error occurred while getting product"))
                .when(productRepository).getProductById(productId);

        DatabaseException thrown = assertThrows(
                DatabaseException.class,
                () -> underTest.getProductById(productId),
                "Expected getProductById to throw DatabaseException, but it didn't"
        );

        assertEquals("Database error occurred while getting product", thrown.getMessage());
        verify(productRepository, times(1)).getProductById(productId);
    }

    // updateProduct
    @Test
    void WhenProductExists_updateProduct_ShouldReturnUpdatedProduct() {
        UUID productId = UUID.randomUUID();
        Product updatedProduct = new Product(productId, "Updated Laptop", 1200.0);
        when(productRepository.updateProduct(productId, "Updated Laptop", 1200.0)).thenReturn(updatedProduct);

        Product result = underTest.updateProduct(productId, "Updated Laptop", 1200.0);

        assertNotNull(result);
        assertEquals(updatedProduct, result);
        verify(productRepository, times(1)).updateProduct(productId, "Updated Laptop", 1200.0);
    }

    @Test
    void WhenProductDoesNotExist_updateProduct_ShouldThrowResourceNotFoundException() {
        UUID productId = UUID.randomUUID();
        when(productRepository.updateProduct(productId, "Updated Laptop", 1200.0)).thenReturn(null);

        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class,
                () -> underTest.updateProduct(productId, "Updated Laptop", 1200.0),
                "Expected updateProduct to throw ResourceNotFoundException, but it didn't"
        );

        assertEquals("Product not found to update!", thrown.getMessage());
        verify(productRepository, times(1)).updateProduct(productId, "Updated Laptop", 1200.0);
    }

    @Test
    void WhenDatabaseFails_updateProduct_ShouldThrowDatabaseException() {
        UUID productId = UUID.randomUUID();
        doThrow(new DatabaseException("Database error occurred while updating product"))
                .when(productRepository).updateProduct(productId, "Updated Laptop", 1200.0);

        DatabaseException thrown = assertThrows(
                DatabaseException.class,
                () -> underTest.updateProduct(productId, "Updated Laptop", 1200.0),
                "Expected updateProduct to throw DatabaseException, but it didn't"
        );

        assertEquals("Database error occurred while updating product", thrown.getMessage());
        verify(productRepository, times(1)).updateProduct(productId, "Updated Laptop", 1200.0);
    }

    // applyDiscount
    @Test
    void WhenValidProductsAndDiscount_applyDiscount_ShouldApplySuccessfully() {
        UUID productId1 = UUID.randomUUID();
        UUID productId2 = UUID.randomUUID();
        ArrayList<Product> products = new ArrayList<>(List.of(
                new Product(productId1, "Laptop", 1000.0),
                new Product(productId2, "Phone", 500.0)
        ));
        ArrayList<UUID> productIds = new ArrayList<>(Arrays.asList(productId1, productId2));

        underTest.applyDiscount(10.0, productIds);

        verify(productRepository, times(1)).applyDiscount(10.0, productIds);
    }

    @Test
    void WhenProductListIsEmpty_applyDiscount_ShouldThrowServiceException() {
        ArrayList<UUID> productIds = new ArrayList<>();

        ServiceException thrown = assertThrows(
                ServiceException.class,
                () -> underTest.applyDiscount(10.0, productIds),
                "Expected applyDiscount to throw ServiceException, but it didn't"
        );

        assertEquals("No products selected to apply discount", thrown.getMessage());
        verify(productRepository, never()).applyDiscount(anyDouble(), any());
    }

    @Test
    void WhenDiscountIsInvalid_applyDiscount_ShouldThrowServiceException() {
        ArrayList<UUID> productIds = new ArrayList<>();
        productIds.add(UUID.randomUUID());

        ServiceException thrownNegative = assertThrows(
                ServiceException.class,
                () -> underTest.applyDiscount(-5.0, productIds),
                "Expected applyDiscount to throw ServiceException for negative discount, but it didn't"
        );
        assertEquals("Invalid discount percentage!", thrownNegative.getMessage());

        ServiceException thrownAbove100 = assertThrows(
                ServiceException.class,
                () -> underTest.applyDiscount(120.0, productIds),
                "Expected applyDiscount to throw ServiceException for discount above 100, but it didn't"
        );
        assertEquals("Invalid discount percentage!", thrownAbove100.getMessage());

        verify(productRepository, never()).applyDiscount(anyDouble(), any());
    }

    // deleteProductById
    @Test
    void WhenProductExists_deleteProductById_ShouldDeleteSuccessfully() {
        UUID productId = UUID.randomUUID();

        doNothing().when(productRepository).deleteProductById(productId);

        underTest.deleteProductById(productId);

        verify(productRepository, times(1)).deleteProductById(productId);
    }

    @Test
    void WhenProductDoesNotExist_deleteProductById_ShouldNotDoAnything() {
        UUID productId = UUID.randomUUID();

        doNothing().when(productRepository).deleteProductById(productId);

        assertDoesNotThrow(() -> underTest.deleteProductById(productId));

        verify(productRepository, times(1)).deleteProductById(productId);
    }

    @Test
    void WhenDatabaseFails_deleteProductById_ShouldThrowDatabaseException() {
        UUID productId = UUID.randomUUID();
        doThrow(new DatabaseException("Database error occurred while deleting product"))
                .when(productRepository).deleteProductById(productId);

        DatabaseException thrown = assertThrows(
                DatabaseException.class,
                () -> underTest.deleteProductById(productId),
                "Expected deleteProductById to throw DatabaseException, but it didn't"
        );

        assertEquals("Database error occurred while deleting product", thrown.getMessage());

        verify(productRepository, times(1)).deleteProductById(any());
    }
}
