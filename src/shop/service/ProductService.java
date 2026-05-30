package shop.service;

import shop.exceptions.OutOfStockException;
import shop.exceptions.ProductNotFoundException;
import shop.model.Product;
import shop.repository.Repository;

import java.util.UUID;

public class ProductService {
    private final Repository<Product> productRepository;

    public ProductService(Repository<Product> productRepository) {
        this.productRepository = productRepository;
    }

    public Product getProduct(UUID id) throws ProductNotFoundException {
        Product result = productRepository.findById(id);
        if (result == null) {
            throw new ProductNotFoundException("Cannot find Product by id=" + id);
        }

        return result;
    }

    public void reduceStock(UUID productId, int quantity) throws ProductNotFoundException, OutOfStockException {
        Product product = this.getProduct(productId);

        int currStock = product.getStock();
        if (product.getStock() < quantity) {
            throw new OutOfStockException("Not enough stock to reduce, curr stock: " + product.getStock() + ", wanna: " + quantity);
        } else {
            product.setStock(currStock - quantity);
        }
    }
}
