package shop;

import shop.exceptions.*;
import shop.model.Order;
import shop.model.OrderItem;
import shop.model.Product;
import shop.repository.Repository;
import shop.service.OrderService;
import shop.service.ProductService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        List<Product> productList = new ArrayList<>();
        Repository<Product> productRepository = new Repository<>(productList);

        Product macbook = new Product(UUID.randomUUID(), "Macbook", 10, 1000);
        Product iPhone = new Product(UUID.randomUUID(), "iPhone", 5, 500);
        Product iPad = new Product(UUID.randomUUID(), "iPad", 3, 300);

        productRepository.add(macbook);
        productRepository.add(iPhone);
        productRepository.add(iPad);

        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(new OrderItem(productRepository.findAll().get(0), 2));
        orderItems.add(new OrderItem(productRepository.findAll().get(1), 1));
        orderItems.add(new OrderItem(productRepository.findAll().get(2), 1));

        ProductService productService = new ProductService(productRepository);
        OrderService orderService = new OrderService(productService);

        // Сценарий 1
        System.out.println("Scenario 1: Create order and process payment");
        try {
            Order order = orderService.createOrder(orderItems);
            orderService.processPayment(order, 3000);
            System.out.println("Order created and paid successfully!");
        } catch (AppException e) {
            System.out.printf("Error: %s\n", e.getMessage());
        } catch (PaymentException e) {
            System.out.printf("Payment error: %s\n", e.getMessage());
        }
        System.out.println("");

        // Сценарий 2
        System.out.println("Scenario 2: Product not found");
        try {
            Product temp = productService.getProduct(UUID.randomUUID());
            System.out.printf("Product found: %s, stock: %d\n", temp.getName(), temp.getStock());
        } catch (ProductNotFoundException e) {
            System.out.printf("Product not found: %s\n", e.getMessage());
        }
        System.out.println("");

        // Сценарий 3
        System.out.println("Scenario 3: Out of stock");
        try {
            productService.reduceStock(macbook.getId(), 10000);
            System.out.println("Stock reduced successfully!");
        } catch (OutOfStockException e) {
            System.out.println("Out of stock: " + e.getMessage());
        } catch (ProductNotFoundException e) {
            System.out.println("Product not found: " + e.getMessage());
        }
        System.out.println("");

        // Сценарий 4
        System.out.println("Scenario 4: Empty order");
        try {
            Order emptyOrder = orderService.createOrder(new ArrayList<>());
            System.out.println("Empty order created successfully!");
        } catch (AppException e) {
            System.out.println("Empty order error: " + e.getMessage());
        }

        // Сценарий 5
        System.out.println("\nScenario 5: Multi-cathch for payment");
        try {
            productService.reduceStock(UUID.randomUUID(), 10000);
        } catch (ProductNotFoundException | OutOfStockException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Сценарий 6
        System.out.println("\nScenario 6: Chain of exceptions");
        try {
            Order order = orderService.createOrder(orderItems);
            orderService.processPayment(order, 1);
            System.out.println("Order created and paid successfully!");
        } catch (AppException e) {
            System.out.println("App error: " + e.getMessage());
        } catch (PaymentException e) {
            System.out.println("Payment error: " + e.getMessage());
            if (e.getCause() != null) {
                System.out.println("Caused by: " + e.getCause().getMessage());
            }
        }

        // Сценарий 7
        System.out.println("\nScenario 7: Payment timeout");
        try {
            Order order = orderService.createOrder(orderItems);
            orderService.processPayment(order, 3000);
            System.out.println("Order created and paid successfully!");
        } catch (AppException e) {
            System.out.println("App error: " + e.getMessage());
        } catch (PaymentTimeoutException e) {
            System.out.println("Payment timeout: " + e.getMessage());
        } finally {
            System.out.println("End of scenario 7");
        }
    }
}