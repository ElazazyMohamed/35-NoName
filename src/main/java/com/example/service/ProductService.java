package com.example.service;

import com.example.exception.DatabaseException;
import com.example.exception.ResourceNotFoundException;
import com.example.exception.ServiceException;
import com.example.model.Product;
import com.example.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@SuppressWarnings("rawtypes")
public class ProductService extends MainService<Product> {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product addProduct(Product product) {
        if (product == null)
            throw new ServiceException("Product cannot be null");
        try {
            return productRepository.addProduct(product);
        } catch (DatabaseException e) {
            throw new DatabaseException("Error adding product");
        }
    }

    public ArrayList<Product> getProducts() {
        try {
            ArrayList<Product> products = productRepository.getProducts();
            if (products.isEmpty())
                throw new ResourceNotFoundException("No products found");
            return products;
        } catch (DatabaseException e) {
            throw new DatabaseException("Database error occurred while getting products");
        }
    }

    public Product getProductById(UUID productId) {
        try {
            Product product = productRepository.getProductById(productId);
            if (product == null)
                throw new ResourceNotFoundException("Product not found!");
            return product;
        } catch (DatabaseException e) {
            throw new DatabaseException("Database error occurred while getting product");
        }
    }

    public Product updateProduct(UUID productId, String newName, double newPrice) {
        try {
            Product product = productRepository.updateProduct(productId, newName, newPrice);
            if (product == null)
                throw new ResourceNotFoundException("Product not found to update!");
            return product;
        } catch (DatabaseException e) {
            throw new DatabaseException("Database error occurred while updating product");
        }
    }

    public void applyDiscount(double discount, ArrayList<UUID> productIds) {
        try {
            if (productIds.isEmpty())
                throw new ServiceException("No products selected to apply discount");
            if (discount < 0 || discount > 100)
                throw new ServiceException("Invalid discount percentage!");
            productRepository.applyDiscount(discount, productIds);
        } catch (DatabaseException e) {
            throw new DatabaseException("Database error occurred while applying discount");
        }
    }

    public void deleteProductById(UUID productId) {
        try {
            productRepository.deleteProductById(productId);
        } catch (DatabaseException e) {
            throw new DatabaseException("Database error occurred while deleting product");
        }
    }



}
