package pl.edu.agh.internetshop.search;

import org.junit.jupiter.api.Test;
import pl.edu.agh.internetshop.Order;

import java.math.BigDecimal;

import static org.mockito.Mockito.mock;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;

public class PriceSearchStrategyTest {
    @Test
    public void testMatchingOrder() {
        // given
        Order order = mock(Order.class);
        given(order.getTotalPrice()).willReturn(BigDecimal.valueOf(1000));
        PriceSearchStrategy strategy = new PriceSearchStrategy(BigDecimal.valueOf(1000));

        // when then
        assertTrue(strategy.filter(order));
    }

    @Test
    public void testNonMatchingOrder() {
        // given
        Order order = mock(Order.class);
        given(order.getTotalPrice()).willReturn(BigDecimal.valueOf(2000));
        PriceSearchStrategy strategy = new PriceSearchStrategy(BigDecimal.valueOf(1000));

        // when then
        assertFalse(strategy.filter(order));
    }
}
