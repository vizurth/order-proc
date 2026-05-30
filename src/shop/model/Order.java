package shop.model;

import shop.interfaces.Identifiable;

import java.util.List;
import java.util.UUID;

public class Order implements Identifiable {
    private final UUID id;
    private final List<OrderItem> items;
    private OrderStatus status;

    public Order(UUID id, List<OrderItem> items, OrderStatus status) {
        this.id = id;
        this.items = items;
        this.status = status;
    }

    @Override
    public UUID getId() {
        return id;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public double totalPrice() {
        double result = 0;
        for (OrderItem item : items) {
            result += item.totalPrice();
        }
        return result;
    }
}
