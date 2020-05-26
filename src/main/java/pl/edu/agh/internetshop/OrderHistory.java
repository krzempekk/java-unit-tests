package pl.edu.agh.internetshop;

import pl.edu.agh.internetshop.search.SearchStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrderHistory {
    private List<Order> orders;

    public OrderHistory() {
        this.orders = new ArrayList<>();
    }

    public void addOrder(Order order) {
        this.orders.add(Objects.requireNonNull(order));
    }

    public List<Order> getOrders() {
        return orders;
    }

    public List<Order> searchOrders(SearchStrategy strategy) {
        return orders.stream().filter(strategy::filter).collect(Collectors.toList());
    }
}
