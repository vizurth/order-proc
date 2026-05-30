package shop.service;

import shop.exceptions.*;
import shop.model.Order;
import shop.model.OrderItem;
import shop.model.OrderStatus;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class OrderService {
    private final ProductService productService;

    public OrderService(ProductService productService) {
        this.productService = productService;
    }

    public Order createOrder(List<OrderItem> items) throws AppException {
        if (items.isEmpty()) {
            throw new EmptyOrderException("Items list is empty.");
        }

        for (OrderItem item : items) {
            productService.reduceStock(item.getProduct().getId(), item.getQuantity());
        }

        return new Order(UUID.randomUUID(), items, OrderStatus.PENDING);
    }

    public void processPayment(Order order, double availableFunds) throws PaymentException {
        try {
            if (order.totalPrice() >  availableFunds) {
                throw new InsufficientFundsException("Not enough funds");
            }
        } catch (InsufficientFundsException e) {
            throw new PaymentException("Payment failed", e);
        }

        // симулируем Timeout
        Random random = new Random();
        if (random.nextInt(100) < 10) {
            throw new PaymentTimeoutException("Payment timeout....");
        }
    }


}
