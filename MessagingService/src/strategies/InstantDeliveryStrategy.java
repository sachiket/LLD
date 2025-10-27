package strategies;

import models.Message;

/**
 * Concrete strategy for instant message delivery
 * Simulates real-time delivery for online users
 */
public class InstantDeliveryStrategy implements MessageDeliveryStrategy {
    
    @Override
    public boolean deliverMessage(Message message) {
        // Simulate instant delivery logic
        System.out.println("Delivering message instantly: " + message.getId());
        message.markAsDelivered();
        return true;
    }
    
    @Override
    public String getDeliveryType() {
        return "INSTANT";
    }
}