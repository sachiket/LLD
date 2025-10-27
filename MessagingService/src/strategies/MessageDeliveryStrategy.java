package strategies;

import models.Message;

/**
 * Strategy pattern for different message delivery mechanisms
 * Demonstrates Open/Closed Principle - open for extension, closed for modification
 */
public interface MessageDeliveryStrategy {
    boolean deliverMessage(Message message);
    String getDeliveryType();
}