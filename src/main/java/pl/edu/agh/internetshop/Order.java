package pl.edu.agh.internetshop;

import java.math.BigDecimal;
import java.util.*;

public class Order {
    private static final BigDecimal TAX_VALUE = BigDecimal.valueOf(1.23);
    private BigDecimal generalDiscount = BigDecimal.ZERO;
    private final Map<Product, BigDecimal> productsDiscounts = new HashMap<>();
	private final UUID id;
    private final List<Product> products;
    private boolean paid;
    private Shipment shipment;
    private ShipmentMethod shipmentMethod;
    private PaymentMethod paymentMethod;

    public Order(List<Product> products) {
        this.products = Objects.requireNonNull(products);
        if(products.size() == 0) throw new IllegalArgumentException("Offer must contain at least one product");
        if(products.contains(null)) throw new IllegalArgumentException("Product list cannot contain nulls");
        for(Product product: products) productsDiscounts.put(product, BigDecimal.ZERO);
        id = UUID.randomUUID();
        paid = false;
    }

    public UUID getId() {
        return id;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public boolean isSent() {
        return shipment != null && shipment.isShipped();
    }

    public boolean isPaid() { return paid; }

    public Shipment getShipment() {
        return shipment;
    }

    public BigDecimal getPrice() {
        BigDecimal price = BigDecimal.ZERO;
        for(Product product: products) {
            price = price.add(product.getPrice());
        }
        return price;
    }

    public BigDecimal getPriceWithDiscounts() {
        BigDecimal price = BigDecimal.ZERO;
        for(Product product: products) {
            BigDecimal discount = BigDecimal.ONE.subtract(productsDiscounts.get(product));
            price = price.add(product.getPrice().multiply(discount).setScale(Product.PRICE_PRECISION, Product.ROUND_STRATEGY));
        }
        BigDecimal discount = BigDecimal.ONE.subtract(this.generalDiscount);
        return price.multiply(discount).setScale(Product.PRICE_PRECISION, Product.ROUND_STRATEGY);
    }

    public BigDecimal getPriceWithTaxes() {
        return getPrice().multiply(TAX_VALUE).setScale(Product.PRICE_PRECISION, Product.ROUND_STRATEGY);
    }

    public BigDecimal getTotalPrice() {
        return getPriceWithDiscounts().multiply(TAX_VALUE).setScale(Product.PRICE_PRECISION, Product.ROUND_STRATEGY);
    }

    public List<Product> getProducts() {
        return products;
    }

    public ShipmentMethod getShipmentMethod() {
        return shipmentMethod;
    }

    public void setShipmentMethod(ShipmentMethod shipmentMethod) {
        this.shipmentMethod = shipmentMethod;
    }

    public void send() {
        boolean sentSuccesful = getShipmentMethod().send(shipment, shipment.getSenderAddress(), shipment.getRecipientAddress());
        shipment.setShipped(sentSuccesful);
    }

    public void pay(MoneyTransfer moneyTransfer) {
        moneyTransfer.setCommitted(getPaymentMethod().commit(moneyTransfer));
        paid = moneyTransfer.isCommitted();
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }

    private boolean isCorrectDiscountValue(BigDecimal discount) {
        return discount.compareTo(BigDecimal.ZERO) > 0 && discount.compareTo(BigDecimal.ONE) < 0;
    }

    public BigDecimal getGeneralDiscount() {
        return generalDiscount;
    }

    public void setGeneralDiscount(BigDecimal generalDiscount) {
        if(!isCorrectDiscountValue(generalDiscount)) throw new IllegalArgumentException("Discount must be in range 0 to 1");
        this.generalDiscount = generalDiscount;
    }

    public BigDecimal getDiscount(Product product) {
        return productsDiscounts.get(product);
    }

    public void setDiscount(Product product, BigDecimal discount) {
        if(!isCorrectDiscountValue(discount)) throw new IllegalArgumentException("Discount must be in range 0 to 1");
        productsDiscounts.put(product, discount);
    }

    public String getRecipientName() {
        return shipment.getRecipientAddress().getName();
    }
}
