package shop.model;

import shop.interfaces.Identifiable;

import java.util.UUID;

public class Product implements Identifiable {
    private final UUID id;
    private final String name;
    private double price;
    private int stock;

    public Product(UUID id, String name, int price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    @Override
    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
