package pl.edu.agh.internetshop.search;

import pl.edu.agh.internetshop.Order;

public class RecipientSearchStrategy implements SearchStrategy {
    private String recipientName;

    public RecipientSearchStrategy(String recipientName) {
        this.recipientName = recipientName;
    }

    @Override
    public boolean filter(Order order) {
        return order.getRecipientName().equals(recipientName);
    }
}
