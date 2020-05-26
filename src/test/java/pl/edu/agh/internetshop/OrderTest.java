package pl.edu.agh.internetshop;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static pl.edu.agh.internetshop.util.CustomAssertions.assertBigDecimalCompareValue;

public class OrderTest {

	private Order getOrderWithMockedProduct() {
		Product product = mock(Product.class);
		return new Order(Collections.singletonList(product));
	}

	@Test
	public void createOfferWithNullList() {
		// when then
		assertThrows(NullPointerException.class, () -> new Order(null));
	}

	@Test
	public void createOfferWithNoProducts() {
		// when then
		assertThrows(IllegalArgumentException.class, () -> new Order(Collections.emptyList()));
	}

	@Test
	public void createOfferWithListContainingNull() {
		//given
		List<Product> products = Arrays.asList(mock(Product.class), null);

		// when then
		assertThrows(IllegalArgumentException.class, () -> new Order(products));
	}

	@Test
	public void testGetProductThroughOrder() {
		// given
		Product expectedProduct = mock(Product.class);
		Order order = new Order(Collections.singletonList(expectedProduct));

		// when
		List<Product> actualProduct = order.getProducts();

		// then
		assertSame(expectedProduct, actualProduct.get(0));
	}

	@Test
	public void getMultipleProductsThroughOrder() {
		// given
		Product expectedProduct1 = mock(Product.class);
		Product expectedProduct2 = mock(Product.class);
		Order order = new Order(Arrays.asList(expectedProduct1, expectedProduct2));

		// when
		List<Product> products = order.getProducts();

		// then
		assertEquals(2, products.size());
		assertSame(expectedProduct1, products.get(0));
		assertSame(expectedProduct2, products.get(1));
	}

	@Test
	public void testSetShipment() throws Exception {
		// given
		Order order = getOrderWithMockedProduct();
		Shipment expectedShipment = mock(Shipment.class);

		// when
		order.setShipment(expectedShipment);

		// then
		assertSame(expectedShipment, order.getShipment());
	}

	@Test
	public void testShipmentWithoutSetting() throws Exception {
		// given
		Order order = getOrderWithMockedProduct();

		// when

		// then
		assertNull(order.getShipment());
	}

	private List<Product> getProductsWithMockedPrices(BigDecimal... prices) {
		List<Product> products = new ArrayList<>();
		for(BigDecimal price: prices) {
			Product product = mock(Product.class);
			given(product.getPrice()).willReturn(price);
			products.add(product);
		}
		return products;
	}

	@Test
	public void testGetPrice() throws Exception {
		// given
		BigDecimal expectedProductPrice = BigDecimal.valueOf(1000);
		List<Product> products = getProductsWithMockedPrices(expectedProductPrice);
		Order order = new Order(products);

		// when
		BigDecimal actualProductPrice = order.getPrice();

		// then
		assertBigDecimalCompareValue(expectedProductPrice, actualProductPrice);
	}

	@Test
	public void getPriceWhenMultipleProducts() {
		// given
		BigDecimal expectedProductsPrice = BigDecimal.valueOf(3000);
		List<Product> products = getProductsWithMockedPrices(BigDecimal.valueOf(1000), BigDecimal.valueOf(2000));
		Order order = new Order(products);

		// when
		BigDecimal actualProductsPrice = order.getPrice();

		// then
		assertBigDecimalCompareValue(expectedProductsPrice, actualProductsPrice);
	}

	private Order getOrderWithCertainProductPrice(double productPriceValue) {
		BigDecimal productPrice = BigDecimal.valueOf(productPriceValue);
		Product product = mock(Product.class);
		given(product.getPrice()).willReturn(productPrice);
		return new Order(Collections.singletonList(product));
	}

	@Test
	public void testPriceWithTaxesWithoutRoundUp() {
		// given

		// when
		Order order = getOrderWithCertainProductPrice(2); // 2 PLN

		// then
		assertBigDecimalCompareValue(order.getPriceWithTaxes(), BigDecimal.valueOf(2.46)); // 2.46 PLN
	}

	@Test
	public void testPriceWithTaxesWithRoundDown() {
		// given

		// when
		Order order = getOrderWithCertainProductPrice(0.01); // 0.01 PLN

		// then
		assertBigDecimalCompareValue(order.getPriceWithTaxes(), BigDecimal.valueOf(0.01)); // 0.01 PLN
																							
	}

	@Test
	public void testPriceWithTaxesWithRoundUp() {
		// given

		// when
		Order order = getOrderWithCertainProductPrice(0.03); // 0.03 PLN

		// then
		assertBigDecimalCompareValue(order.getPriceWithTaxes(), BigDecimal.valueOf(0.04)); // 0.04 PLN
																							
	}

	@Test
	public void testSetShipmentMethod() {
		// given
		Order order = getOrderWithMockedProduct();
		ShipmentMethod surface = mock(SurfaceMailBus.class);

		// when
		order.setShipmentMethod(surface);

		// then
		assertSame(surface, order.getShipmentMethod());
	}

	@Test
	public void testSending() {
		// given
		Order order = getOrderWithMockedProduct();
		SurfaceMailBus surface = mock(SurfaceMailBus.class);
		Shipment shipment = mock(Shipment.class);
		given(shipment.isShipped()).willReturn(true);

		// when
		order.setShipmentMethod(surface);
		order.setShipment(shipment);
		order.send();

		// then
		assertTrue(order.isSent());
	}

	@Test
	public void testIsSentWithoutSending() {
		// given
		Order order = getOrderWithMockedProduct();
		Shipment shipment = mock(Shipment.class);
		given(shipment.isShipped()).willReturn(true);

		// when

		// then
		assertFalse(order.isSent());
	}

	@Test
	public void testWhetherIdExists() throws Exception {
		// given
		Order order = getOrderWithMockedProduct();

		// when

		// then
		assertNotNull(order.getId());
	}

	@Test
	public void testSetPaymentMethod() throws Exception {
		// given
		Order order = getOrderWithMockedProduct();
		PaymentMethod paymentMethod = mock(MoneyTransferPaymentTransaction.class);

		// when
		order.setPaymentMethod(paymentMethod);

		// then
		assertSame(paymentMethod, order.getPaymentMethod());
	}

	@Test
	public void testPaying() throws Exception {
		// given
		Order order = getOrderWithMockedProduct();
		PaymentMethod paymentMethod = mock(MoneyTransferPaymentTransaction.class);
		given(paymentMethod.commit(any(MoneyTransfer.class))).willReturn(true);
		MoneyTransfer moneyTransfer = mock(MoneyTransfer.class);
		given(moneyTransfer.isCommitted()).willReturn(true);

		// when
		order.setPaymentMethod(paymentMethod);
		order.pay(moneyTransfer);

		// then
		assertTrue(order.isPaid());
	}

	@Test
	public void testIsPaidWithoutPaying() throws Exception {
		// given
		Order order = getOrderWithMockedProduct();

		// when

		// then
		assertFalse(order.isPaid());
	}

	@Test
	public void setGeneralDiscount() {
		// given
		Order order = getOrderWithMockedProduct();

		// when
		BigDecimal expectedDiscount = BigDecimal.valueOf(0.5);
		order.setGeneralDiscount(expectedDiscount);

		// then
		assertBigDecimalCompareValue(expectedDiscount, order.getGeneralDiscount());
	}

	@Test
	public void setTooBigGeneralDiscount() {
		// given
		Order order = getOrderWithMockedProduct();

		// when then
		assertThrows(IllegalArgumentException.class, () -> order.setGeneralDiscount(BigDecimal.valueOf(1.5)));
	}

	@Test
	public void setTooSmallGeneralDiscount() {
		// given
		Order order = getOrderWithMockedProduct();

		// when then
		assertThrows(IllegalArgumentException.class, () -> order.setGeneralDiscount(BigDecimal.valueOf(-0.5)));
	}

	@Test
	public void getGeneralDiscountWhenNotSet() {
		// given
		Order order = getOrderWithMockedProduct();

		// when
		BigDecimal actualDiscount = order.getGeneralDiscount();

		// then
		assertBigDecimalCompareValue(BigDecimal.ZERO, actualDiscount);
	}


	@Test
	public void setDiscountOnProduct() {
		// given
		List<Product> products = getProductsWithMockedPrices(BigDecimal.valueOf(1000), BigDecimal.valueOf(2000));
		Order order = new Order(products);
		BigDecimal expectedDiscount = BigDecimal.valueOf(0.5);
		order.setDiscount(products.get(1), expectedDiscount);

		// when
		BigDecimal actualDiscount = order.getDiscount(products.get(1));

		// then
		assertBigDecimalCompareValue(expectedDiscount, actualDiscount);
	}

	@Test
	public void setTooBigDiscountOnProduct() {
		// given
		List<Product> products = getProductsWithMockedPrices(BigDecimal.valueOf(1000));
		Order order = new Order(products);

		// when then
		assertThrows(IllegalArgumentException.class, () -> order.setDiscount(products.get(0), BigDecimal.valueOf(1.5)));
	}

	@Test
	public void setTooSmallDiscountOnProduct() {
		// given
		List<Product> products = getProductsWithMockedPrices(BigDecimal.valueOf(1000));
		Order order = new Order(products);

		// when then
		assertThrows(IllegalArgumentException.class, () -> order.setDiscount(products.get(0), BigDecimal.valueOf(-0.5)));
	}

	@Test
	public void getProductDiscountWhenNotSet() {
		// given
		List<Product> products = getProductsWithMockedPrices(BigDecimal.valueOf(1000));
		Order order = new Order(products);

		// when
		BigDecimal actualDiscount = order.getDiscount(products.get(0));

		// then
		assertBigDecimalCompareValue(BigDecimal.ZERO, actualDiscount);
	}

	@Test
	public void getProductDiscountWhenSetMultipleTimes() {
		// given
		List<Product> products = getProductsWithMockedPrices(BigDecimal.valueOf(1000));
		Order order = new Order(products);
		BigDecimal expectedDiscount = BigDecimal.valueOf(0.7);
		order.setDiscount(products.get(0), BigDecimal.valueOf(0.3));
		order.setDiscount(products.get(0), BigDecimal.valueOf(0.5));
		order.setDiscount(products.get(0), expectedDiscount);

		// when
		BigDecimal actualDiscount = order.getDiscount(products.get(0));

		// then
		assertBigDecimalCompareValue(expectedDiscount, actualDiscount);
	}

	@Test
	public void getPriceOfOfferWithDiscountedProducts() {
		// given
		List<Product> products = getProductsWithMockedPrices(BigDecimal.valueOf(1000), BigDecimal.valueOf(2000));
		Order order = new Order(products);
		order.setDiscount(products.get(0), BigDecimal.valueOf(0.5));
		order.setDiscount(products.get(1), BigDecimal.valueOf(0.3));

		// when
		BigDecimal actualPrice = order.getPriceWithDiscounts();

		// then
		assertBigDecimalCompareValue(BigDecimal.valueOf(1900), actualPrice);
	}

	@Test
	public void getPriceOfOfferWithGeneralDiscount() {
		// given
		List<Product> products = getProductsWithMockedPrices(BigDecimal.valueOf(1000), BigDecimal.valueOf(2000));
		Order order = new Order(products);
		order.setDiscount(products.get(0), BigDecimal.valueOf(0.5));
		order.setDiscount(products.get(1), BigDecimal.valueOf(0.3));

		// when
		BigDecimal actualPrice = order.getPriceWithDiscounts();

		// then
		assertBigDecimalCompareValue(BigDecimal.valueOf(1900), actualPrice);
	}
}
