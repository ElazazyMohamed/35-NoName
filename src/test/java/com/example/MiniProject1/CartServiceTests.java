package com.example.MiniProject1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.model.Cart;
import com.example.model.Order;
import com.example.model.Product;
import com.example.model.User;
import com.example.repository.CartRepository;
import com.example.repository.OrderRepository;
import com.example.repository.ProductRepository;
import com.example.repository.UserRepository;
import com.example.service.CartService;
import com.example.service.OrderService;
import com.example.service.ProductService;
import com.example.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
@ComponentScan(basePackages = "com.example.*")
@WebMvcTest
class CartServiceTests {

    @Value("${spring.application.userDataPath}")
    private String userDataPath;

    @Value("${spring.application.productDataPath}")
    private String productDataPath;

    @Value("${spring.application.orderDataPath}")
    private String orderDataPath;

    @Value("${spring.application.cartDataPath}")
    private String cartDataPath;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    public void overRideAll() {
        try {
            objectMapper.writeValue(new File(userDataPath), new ArrayList<User>());
            objectMapper.writeValue(new File(productDataPath), new ArrayList<Product>());
            objectMapper.writeValue(new File(orderDataPath), new ArrayList<Order>());
            objectMapper.writeValue(new File(cartDataPath), new ArrayList<Cart>());
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to JSON file", e);
        }
    }

    public Object find(String typeString, Object toFind) {
        switch (typeString) {
            case "User":
                ArrayList<User> users = getUsers();

                for (User user : users) {
                    if (user.getId().equals(((User) toFind).getId())) {
                        return user;
                    }
                }
                break;
            case "Product":
                ArrayList<Product> products = getProducts();
                for (Product product : products) {
                    if (product.getId().equals(((Product) toFind).getId())) {
                        return product;
                    }
                }
                break;
            case "Order":
                ArrayList<Order> orders = getOrders();
                for (Order order : orders) {
                    if (order.getId().equals(((Order) toFind).getId())) {
                        return order;
                    }
                }
                break;
            case "Cart":
                ArrayList<Cart> carts = getCarts();
                for (Cart cart : carts) {
                    if (cart.getId().equals(((Cart) toFind).getId())) {
                        return cart;
                    }
                }
                break;
        }
        return null;
    }

    public Product addProduct(Product product) {
        try {
            File file = new File(productDataPath);
            ArrayList<Product> products;
            if (!file.exists()) {
                products = new ArrayList<>();
            } else {
                products = new ArrayList<>(Arrays.asList(objectMapper.readValue(file, Product[].class)));
            }
            products.add(product);
            objectMapper.writeValue(file, products);
            return product;
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to JSON file", e);
        }
    }

    public ArrayList<Product> getProducts() {
        try {
            File file = new File(productDataPath);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            return new ArrayList<Product>(Arrays.asList(objectMapper.readValue(file, Product[].class)));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read from JSON file", e);
        }
    }

    public User addUser(User user) {
        try {
            File file = new File(userDataPath);
            ArrayList<User> users;
            if (!file.exists()) {
                users = new ArrayList<>();
            } else {
                users = new ArrayList<>(Arrays.asList(objectMapper.readValue(file, User[].class)));
            }
            users.add(user);
            objectMapper.writeValue(file, users);
            return user;
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to JSON file", e);
        }
    }

    public ArrayList<User> getUsers() {
        try {
            File file = new File(userDataPath);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            return new ArrayList<User>(Arrays.asList(objectMapper.readValue(file, User[].class)));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read from JSON file", e);
        }
    }

    public Cart addCart(Cart cart) {
        try {
            File file = new File(cartDataPath);
            ArrayList<Cart> carts;
            if (!file.exists()) {
                carts = new ArrayList<>();
            } else {
                carts = new ArrayList<>(Arrays.asList(objectMapper.readValue(file, Cart[].class)));
            }
            carts.add(cart);
            objectMapper.writeValue(file, carts);
            return cart;
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to JSON file", e);
        }
    }

    public ArrayList<Cart> getCarts() {
        try {
            File file = new File(cartDataPath);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            return new ArrayList<Cart>(Arrays.asList(objectMapper.readValue(file, Cart[].class)));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read from JSON file", e);
        }
    }

    public Order addOrder(Order order) {
        try {
            File file = new File(orderDataPath);
            ArrayList<Order> orders;
            if (!file.exists()) {
                orders = new ArrayList<>();
            } else {
                orders = new ArrayList<>(Arrays.asList(objectMapper.readValue(file, Order[].class)));
            }
            orders.add(order);
            objectMapper.writeValue(file, orders);
            return order;
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to JSON file", e);
        }
    }

    public ArrayList<Order> getOrders() {
        try {
            File file = new File(orderDataPath);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            return new ArrayList<Order>(Arrays.asList(objectMapper.readValue(file, Order[].class)));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read from JSON file", e);
        }
    }


    private UUID userId;
    private User testUser;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        testUser = new User();
        testUser.setId(userId);
        testUser.setName("Test User");
        overRideAll();
    }
    //Ammar's Test Cases
    @Test
    void testAddCartSuccess() throws Exception {
        Cart cart = new Cart(UUID.randomUUID(), UUID.randomUUID(), new ArrayList<>());

        mockMvc.perform(MockMvcRequestBuilders.post("/cart/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cart)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        boolean found = false;
        for (Cart c : getCarts()) {
            if (c.getId().equals(cart.getId())) {
                found = true;
                break;
            }
        }
        assertTrue(found, "Cart should be added correctly");
    }
    @Test
    void testAddCartForExistingUser() throws Exception {
        User existingUser = new User();
        existingUser.setId(UUID.randomUUID());
        existingUser.setName("Existing User");
        addUser(existingUser);

        Cart cart = new Cart(UUID.randomUUID(), existingUser.getId(), new ArrayList<>());

        mockMvc.perform(MockMvcRequestBuilders.post("/cart/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cart)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Cart retrievedCart = cartService.getCartByUserId(existingUser.getId());
        assertNotNull(retrievedCart, "Cart should be created for the existing user");
    }
    @Test
    void testAddCartWithoutUser() throws Exception {
        Cart cart = new Cart(UUID.randomUUID(), null, new ArrayList<>());

        mockMvc.perform(MockMvcRequestBuilders.post("/cart/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cart)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
    @Test
    void testGetCartsSuccess() throws Exception {
        // Add some carts to test retrieval
        Cart cart1 = new Cart(UUID.randomUUID(), UUID.randomUUID(), new ArrayList<>());
        Cart cart2 = new Cart(UUID.randomUUID(), UUID.randomUUID(), new ArrayList<>());
        cartService.addCart(cart1);
        cartService.addCart(cart2);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/cart/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        List<Cart> responseCarts = objectMapper.readValue(responseContent, new TypeReference<List<Cart>>() {});

        assertEquals(2, responseCarts.size(), "Should return exactly 2 carts");
    }
    @Test
    void testGetCartsWhenEmpty() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/cart/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        List<Cart> responseCarts = objectMapper.readValue(responseContent, new TypeReference<List<Cart>>() {});

        assertTrue(responseCarts.isEmpty(), "Should return an empty list when no carts exist");
    }
    @Test
    void testGetCartsWithOneEntry() throws Exception {
        Cart cart = new Cart(UUID.randomUUID(), UUID.randomUUID(), new ArrayList<>());
        cartService.addCart(cart);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/cart/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        List<Cart> responseCarts = objectMapper.readValue(responseContent, new TypeReference<List<Cart>>() {});

        assertEquals(1, responseCarts.size(), "Should return exactly 1 cart");
    }
    @Test
    void testGetCartByIdSuccess() throws Exception {
        // Create and add a cart
        Cart cart = new Cart(UUID.randomUUID(), UUID.randomUUID(), new ArrayList<>());
        cartService.addCart(cart);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/cart/{cartId}", cart.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        Cart responseCart = objectMapper.readValue(responseContent, Cart.class);

        assertNotNull(responseCart, "Cart should not be null");
        assertEquals(cart.getId(), responseCart.getId(), "Returned cart ID should match");
    }
    @Test
    void testGetCartByIdNotFound() throws Exception {
        UUID nonExistentCartId = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders.get("/cart/{cartId}", nonExistentCartId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(""));
    }
    @Test
    void testGetCartByIdWithEmptyProducts() throws Exception {
        // Create and add a cart with no products
        Cart cart = new Cart(UUID.randomUUID(), UUID.randomUUID(), new ArrayList<>());
        cartService.addCart(cart);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/cart/{cartId}", cart.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        Cart responseCart = objectMapper.readValue(responseContent, Cart.class);

        assertNotNull(responseCart, "Cart should not be null");
        assertEquals(cart.getId(), responseCart.getId(), "Returned cart ID should match");
        assertTrue(responseCart.getProducts().isEmpty(), "Cart should have an empty product list");
    }
    @Test
    void testGetCartByUserIdSuccess() throws Exception {
        // Create and add a cart for a specific user
        UUID userId = UUID.randomUUID();
        Cart cart = new Cart(UUID.randomUUID(), userId, new ArrayList<>());
        cartService.addCart(cart);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/cart/user/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        Cart responseCart = objectMapper.readValue(responseContent, Cart.class);

        assertNotNull(responseCart, "Cart should not be null");
        assertEquals(userId, responseCart.getUserId(), "Returned cart user ID should match");
    }
    @Test
    void testGetCartByUserIdNotFound() throws Exception {
        UUID nonExistentUserId = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders.get("/cart/user/{userId}", nonExistentUserId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(""));
    }
    @Test
    void testGetCartByUserIdWithEmptyProducts() throws Exception {
        // Create and add a cart with no products for a user
        UUID userId = UUID.randomUUID();
        Cart cart = new Cart(UUID.randomUUID(), userId, new ArrayList<>());
        cartService.addCart(cart);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/cart/user/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        Cart responseCart = objectMapper.readValue(responseContent, Cart.class);

        assertNotNull(responseCart, "Cart should not be null");
        assertEquals(userId, responseCart.getUserId(), "Returned cart user ID should match");
        assertTrue(responseCart.getProducts().isEmpty(), "Cart should have an empty product list");
    }
    //(ADD product to cart still missing)
    @Test
    void testDeleteProductFromCartSuccess() throws Exception {
        // Create and add a cart
        UUID cartId = UUID.randomUUID();
        Product product = new Product(UUID.randomUUID(), "Mouse", 30.0);
        Cart cart = new Cart(cartId, UUID.randomUUID(), new ArrayList<>(List.of(product)));
        cartService.addCart(cart);

        mockMvc.perform(MockMvcRequestBuilders.put("/cart/deleteProductFromCart")
                        .param("userId", cart.getUserId().toString())
                        .param("productId", product.getId().toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Product deleted from cart successfully"));

        // Verify the product has been removed
        Cart updatedCart = cartService.getCartById(cartId);
        assertFalse(updatedCart.getProducts().contains(product), "Product should be removed from the cart");
    }
    @Test
    void testDeleteNonExistentProductFromCart() throws Exception {
        UUID cartId = UUID.randomUUID();
        Product product = new Product(UUID.randomUUID(), "Keyboard", 50.0);
        Cart cart = new Cart(cartId, UUID.randomUUID(), new ArrayList<>());
        cartService.addCart(cart);

        mockMvc.perform(MockMvcRequestBuilders.put("/cart/deleteProductFromCart")
                        .param("userId", cart.getUserId().toString())
                        .param("productId", product.getId().toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Product deleted from cart successfully"));

        Cart updatedCart = cartService.getCartById(cartId);
        updatedCart.setProducts(new ArrayList<>());
        assertTrue(updatedCart.getProducts().isEmpty(), "Cart should still be empty");
    }
    @Test
    void testDeleteProductFromNonExistentCart() throws Exception {
        UUID nonExistentCartId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders.put("/cart/deleteProductFromCart")
                        .param("userId", nonExistentCartId.toString())
                        .param("productId", productId.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Cart is empty"));
    }
    @Test
    void testDeleteCartByIdSuccess() throws Exception {
        // Create and add a cart
        UUID cartId = UUID.randomUUID();
        Cart cart = new Cart(cartId, UUID.randomUUID(), new ArrayList<>());
        cartService.addCart(cart);

        mockMvc.perform(MockMvcRequestBuilders.delete("/cart/delete/{cartId}", cartId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Cart deleted successfully"));

        // Verify the cart has been removed
        Cart deletedCart = cartService.getCartById(cartId);
        assertNull(deletedCart, "Cart should be deleted and return null");
    }
    @Test
    void testDeleteNonExistentCart() throws Exception {
        UUID nonExistentCartId = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders.delete("/cart/delete/{cartId}", nonExistentCartId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Cart deleted successfully"));

        // Verify no error occurs
        Cart deletedCart = cartService.getCartById(nonExistentCartId);
        assertNull(deletedCart, "Deleting a non-existent cart should not cause an error");
    }
    @Test
    void testDeleteCartByIdTwice() throws Exception {
        UUID cartId = UUID.randomUUID();
        Cart cart = new Cart(cartId, UUID.randomUUID(), new ArrayList<>());
        cartService.addCart(cart);

        // First deletion
        mockMvc.perform(MockMvcRequestBuilders.delete("/cart/delete/{cartId}", cartId))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Second deletion (cart already deleted)
        mockMvc.perform(MockMvcRequestBuilders.delete("/cart/delete/{cartId}", cartId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Cart deleted successfully"));

        // Ensure cart remains deleted
        Cart deletedCart = cartService.getCartById(cartId);
        assertNull(deletedCart, "Cart should remain deleted even after multiple delete requests");
    }

}



