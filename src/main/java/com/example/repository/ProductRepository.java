package com.example.repository;

import com.example.model.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.UUID;

@Repository
@SuppressWarnings("rawtypes")
public class ProductRepository extends MainRepository<Product> {
    @Value("${spring.data.SPRING_DATA_PRODUCT}")
    private static final String DATA_PATH = "src/main/java/com/example/data/products.json";

    public ProductRepository() {

    }

    @Override
    protected String getDataPath() {
        return DATA_PATH;
    }

    @Override
    protected Class<Product[]> getArrayType() {
        return Product[].class;
    }

    public ArrayList<Product> getProducts() {
        return this.findAll();
    }

    public Product getProductById(UUID productId) {
        ArrayList<Product> products = this.getProducts();
        for (Product product : products) {
            if (product.getId().equals(productId)) {
                return product;
            }
        }
        return null;
    }

    public Product addProduct(Product product) {
        ArrayList<Product> products = this.getProducts();
        products.add(product);
        this.saveAll(products);
        return product;
    }

    public Product updateProduct(UUID productId, String newName, double newPrice) {
        ArrayList<Product> products = this.getProducts();
        for (Product product : products) {
            if (product.getId().equals(productId)) {
                product.setName(newName);
                product.setPrice(newPrice);
                this.saveAll(products);
                return product;
            }
        }
        return null;
    }

    public void deleteProductById(UUID productId) {
        ArrayList<Product> products = this.getProducts();
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId().equals(productId)) {
                products.remove(i);
                this.saveAll(products);
                return;
            }
        }
    }

    public void applyDiscount(double discount, ArrayList<UUID> productIds) {

        double discountPercentage = discount / 100;
        ArrayList<Product> products = this.getProducts();
        for (Product product : products) {
            if (productIds.contains(product.getId())) {
                product.setPrice(product.getPrice() - (product.getPrice() * discountPercentage));
            }
        }
        this.saveAll(products);
    }

}
