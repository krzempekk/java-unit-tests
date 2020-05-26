package pl.edu.agh.internetshop.search;

import pl.edu.agh.internetshop.Order;

public class ProductNameSearchStrategy implements SearchStrategy {
    private String productName;

    public ProductNameSearchStrategy(String productName) {
        this.productName = productName;
    }

    @Override
    public boolean filter(Order order) {
        return order.getProducts().stream().anyMatch(product -> product.getName().equals(this.productName));
    }
}
