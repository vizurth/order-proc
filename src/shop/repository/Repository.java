package shop.repository;

import shop.interfaces.Identifiable;

import java.util.List;
import java.util.UUID;

public class Repository<T extends Identifiable> {
    private final List<T> items;

    public Repository(List<T> items) {
        this.items = items;
    }

    public void add(T item) {
        items.add(item);
    }

    public T findById(UUID id) {
        for (T item : items) {
            if (item.getId().equals(id)) {
                return item;
            }
        }

        return null;
    }

    public List<T> findAll() {
        return this.items;
    }

    public void remove(UUID id) {
        items.removeIf(item -> item.getId().equals(id));
    }
}