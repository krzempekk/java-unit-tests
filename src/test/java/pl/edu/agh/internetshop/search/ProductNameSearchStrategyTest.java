package pl.edu.agh.internetshop.search;

import org.junit.jupiter.api.Test;
import pl.edu.agh.internetshop.Address;
import pl.edu.agh.internetshop.Order;
import pl.edu.agh.internetshop.Product;
import pl.edu.agh.internetshop.Shipment;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;

public class ProductNameSearchStrategyTest {
    @Test
    public void testMatchingOrder() {
        // given
        String name = "Tormentor 3000";
        Order order = mock(Order.class);
        Product product1 = mock(Product.class);
        Product product2 = mock(Product.class);
        given(order.getProducts()).willReturn(Arrays.asList(product1, product2));
        given(product1.getName()).willReturn("Endless Pain");
        given(product2.getName()).willReturn("Tormentor 3000");
        ProductNameSearchStrategy strategy = new ProductNameSearchStrategy(name);

        // when then
        assertTrue(strategy.filter(order));
    }

    @Test
    public void testNonMatchingOrder() {
        // given
        String name = "Tormentor 3000";
        Order order = mock(Order.class);
        Product product1 = mock(Product.class);
        Product product2 = mock(Product.class);
        given(order.getProducts()).willReturn(Arrays.asList(product1, product2));
        given(product1.getName()).willReturn("Endless Pain");
        given(product2.getName()).willReturn("Endorama");
        ProductNameSearchStrategy strategy = new ProductNameSearchStrategy(name);

        // when then
        assertFalse(strategy.filter(order));
    }
}
