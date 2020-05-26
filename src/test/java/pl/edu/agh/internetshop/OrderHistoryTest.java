package pl.edu.agh.internetshop;

import org.junit.jupiter.api.Test;
import org.mockito.Matchers;
import pl.edu.agh.internetshop.search.SearchStrategy;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Matchers.any;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;


public class OrderHistoryTest {
    @Test
    public void addMultipleOrders() {
        // given
        Order order1 = mock(Order.class);
        Order order2 = mock(Order.class);
        OrderHistory orderHistory = new OrderHistory();
        orderHistory.addOrder(order1);
        orderHistory.addOrder(order2);

        // when
        List<Order> orders = orderHistory.getOrders();

        // then
        assertSame(order1, orders.get(0));
        assertSame(order2, orders.get(1));
    }

    @Test
    public void addNullOrder() {
        // given
        OrderHistory orderHistory = new OrderHistory();

        // when then
        assertThrows(NullPointerException.class, () -> orderHistory.addOrder(null));
    }

    @Test
    public void getSearchingResultsWithAllMatchingOrders() {
        // given
        Order order1 = mock(Order.class);
        Order order2 = mock(Order.class);
        OrderHistory orderHistory = new OrderHistory();
        orderHistory.addOrder(order1);
        orderHistory.addOrder(order2);
        SearchStrategy strategy = mock(SearchStrategy.class);
        given(strategy.filter(any(Order.class))).willReturn(true);

        // when
        List<Order> orders = orderHistory.searchOrders(strategy);

        // then
        assertEquals(2, orders.size());
        assertSame(order1, orders.get(0));
        assertSame(order2, orders.get(1));
    }

    @Test
    public void getSearchingResultsWithSomeMatchingOrders() {
        // given
        Order order1 = mock(Order.class);
        Order order2 = mock(Order.class);
        OrderHistory orderHistory = new OrderHistory();
        orderHistory.addOrder(order1);
        orderHistory.addOrder(order2);
        SearchStrategy strategy = mock(SearchStrategy.class);
        given(strategy.filter(order1)).willReturn(true);
        given(strategy.filter(order2)).willReturn(false);

        // when
        List<Order> orders = orderHistory.searchOrders(strategy);

        // then
        assertEquals(1, orders.size());
        assertSame(order1, orders.get(0));
    }

    @Test
    public void getSearchingResultsWithNoneMatchingOrders() {
        // given
        Order order1 = mock(Order.class);
        Order order2 = mock(Order.class);
        OrderHistory orderHistory = new OrderHistory();
        orderHistory.addOrder(order1);
        orderHistory.addOrder(order2);
        SearchStrategy strategy = mock(SearchStrategy.class);
        given(strategy.filter(any(Order.class))).willReturn(false);

        // when
        List<Order> orders = orderHistory.searchOrders(strategy);

        // then
        assertEquals(0, orders.size());
    }
}
