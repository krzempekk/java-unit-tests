package pl.edu.agh.internetshop.search;

import pl.edu.agh.internetshop.Order;

import java.util.Arrays;
import java.util.List;

public class CompositeSearchStrategy implements SearchStrategy {
    List<SearchStrategy> strategies;

    public CompositeSearchStrategy(SearchStrategy... strategies) {
        this.strategies = Arrays.asList(strategies);
    }

    @Override
    public boolean filter(Order order) {
        return strategies.stream().allMatch(strategy -> strategy.filter(order));
    }
}
