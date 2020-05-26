package pl.edu.agh.internetshop.search;

import org.junit.jupiter.api.Test;
import pl.edu.agh.internetshop.Order;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class RecipientSearchStrategyTest {
    @Test
    public void testMatchingOrder() {
        // given
        String name = "Millie Petrozza";
        Order order = mock(Order.class);
        given(order.getRecipientName()).willReturn(name);
        RecipientSearchStrategy strategy = new RecipientSearchStrategy(name);

        // when then
        assertTrue(strategy.filter(order));
    }

    @Test
    public void testNonMatchingOrder() {
        // given
        Order order = mock(Order.class);
        given(order.getRecipientName()).willReturn("Millie Petrozza");
        RecipientSearchStrategy strategy = new RecipientSearchStrategy("Nick Holmes");

        // when then
        assertFalse(strategy.filter(order));
    }
}
