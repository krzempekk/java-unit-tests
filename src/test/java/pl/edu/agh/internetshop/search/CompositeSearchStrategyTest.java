package pl.edu.agh.internetshop.search;

import org.junit.jupiter.api.Test;
import pl.edu.agh.internetshop.Order;

import java.math.BigDecimal;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;

public class CompositeSearchStrategyTest {
    private SearchStrategy getMockSearchStrategy(boolean returnValue) {
        SearchStrategy searchStrategy = mock(SearchStrategy.class);
        given(searchStrategy.filter(any(Order.class))).willReturn(returnValue);
        return searchStrategy;
    }

    @Test
    public void allStrategiesPass() {
        // given
        Order order = mock(Order.class);
        CompositeSearchStrategy strategy = new CompositeSearchStrategy(getMockSearchStrategy(true), getMockSearchStrategy(true));

        // when then
        assertTrue(strategy.filter(order));
    }

    @Test
    public void oneStrategyNotPass() {
        // given
        Order order = mock(Order.class);
        CompositeSearchStrategy strategy = new CompositeSearchStrategy(getMockSearchStrategy(true), getMockSearchStrategy(false));

        // when then
        assertFalse(strategy.filter(order));
    }

    @Test
    public void noStrategyPass() {
        // given
        Order order = mock(Order.class);
        CompositeSearchStrategy strategy = new CompositeSearchStrategy(getMockSearchStrategy(false), getMockSearchStrategy(false));

        // when then
        assertFalse(strategy.filter(order));
    }
}
