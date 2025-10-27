package observers;

import models.Message;
import models.User;

/**
 * Concrete observer for console notifications
 * Demonstrates concrete implementation of Observer pattern
 */
public class ConsoleNotificationObserver implements MessageEventObserver {
    
    @Override
    public void onMessageSent(Message message) {
        System.out.println("NOTIFICATION: Message sent from " + message.getSenderId() + 
                          " to " + message.getReceiverId());
    }
    
    @Override
    public void onMessageDelivered(Message message) {
        System.out.println("NOTIFICATION: Message delivered to " + message.getReceiverId());
    }
    
    @Override
    public void onMessageRead(Message message) {
        System.out.println("NOTIFICATION: Message read by " + message.getReceiverId());
    }
    
    @Override
    public void onUserStatusChanged(User user) {
        System.out.println("NOTIFICATION: User " + user.getName() + 
                          " is now " + user.getStatus().getDisplayName());
    }
}