package pl.edu.agh.internetshop.search;

import pl.edu.agh.internetshop.Order;

public interface SearchStrategy {
    boolean filter(Order order);
}
