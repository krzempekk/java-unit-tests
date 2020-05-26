package pl.edu.agh.internetshop.search;

import pl.edu.agh.internetshop.Order;

import java.math.BigDecimal;
import java.util.Objects;

public class PriceSearchStrategy implements SearchStrategy {
    private BigDecimal price;

    public PriceSearchStrategy(BigDecimal price) {
        this.price = price;
    }

    @Override
    public boolean filter(Order order) {
        return order.getTotalPrice().equals(price);
    }
}
