package observers;

import models.Task;
import models.User;

/**
 * Observer interface for task events
 * Implements Observer Design Pattern - similar to BookingEventObserver
 */
public interface TaskEventObserver {
    void onTaskCreated(Task task);
    void onTaskStarted(Task task);
    void onTaskCompleted(Task task);
    void onTaskCancelled(Task task);
    void onTaskOverdue(Task task);
    void onUserAssigned(User user, Task task);
}