package observers;

import models.Message;
import models.User;

/**
 * Observer pattern interface for message events
 * Demonstrates Observer pattern for loose coupling
 */
public interface MessageEventObserver {
    void onMessageSent(Message message);
    void onMessageDelivered(Message message);
    void onMessageRead(Message message);
    void onUserStatusChanged(User user);
}