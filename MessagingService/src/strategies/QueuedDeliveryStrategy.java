package strategies;

import models.Message;

/**
 * Concrete strategy for queued message delivery
 * Used when recipient is offline - stores for later delivery
 */
public class QueuedDeliveryStrategy implements MessageDeliveryStrategy {
    
    @Override
    public boolean deliverMessage(Message message) {
        // Simulate queued delivery logic
        System.out.println("Queuing message for later delivery: " + message.getId());
        // Message remains in SENT status until user comes online
        return true;
    }
    
    @Override
    public String getDeliveryType() {
        return "QUEUED";
    }
}