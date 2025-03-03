package com.example.controller;


import com.example.model.Product;
import com.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/product")
public class ProductController {


    private final ProductService productService;


    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }



    @PostMapping("/")
    public Product addProduct(@RequestBody Product product) {


        return productService.addProduct(product);
    }

    @GetMapping("/")
    public ArrayList<Product> getProducts() {
        return productService.getProducts();
    }

    @GetMapping("/{productId}")
    public Product getProductById(@PathVariable UUID productId) {
        return productService.getProductById(productId);
    }

    @PutMapping("/update/{productId}")
    // java
    public Product updateProduct(@PathVariable UUID productId, @RequestBody Map<String, Object> body) {
        // Retrieve name and price from the request body
        String name = (String) body.get("name");
        Object priceObj = body.get("price");

        // Check if name or price is missing
        if (name == null || priceObj == null) {
            throw new IllegalArgumentException("Missing 'name' or 'price' in the request body");
        }

        // Convert priceObj to a double. This handles cases where priceObj might be an instance of Number.
        double price;
        if (priceObj instanceof Number) {
            price = ((Number) priceObj).doubleValue();
        } else {
            throw new IllegalArgumentException("'price' value is not a valid number");
        }

        return productService.updateProduct(productId, name, price);
    }

    @PutMapping("/applyDiscount")
    public String applyDiscount(@RequestParam double discount,@RequestBody ArrayList<UUID>
            productIds) {
        productService.applyDiscount(discount, productIds);

        return "Discount applied successfully";
    }

    @DeleteMapping("/delete/{productId}")
    public String deleteProductById(@PathVariable UUID productId) {
        productService.deleteProductById(productId);
        return "Product deleted successfully";
    }




}
