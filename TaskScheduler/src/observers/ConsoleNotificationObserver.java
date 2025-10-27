package observers;

import models.Task;
import models.User;

/**
 * Console implementation of TaskEventObserver
 * Similar to ConsoleNotificationObserver in BookMyShow
 */
public class ConsoleNotificationObserver implements TaskEventObserver {
    
    @Override
    public void onTaskCreated(Task task) {
        System.out.println("[NOTIFICATION] Task Created: " + task.getTitle() + 
                          " assigned to " + task.getAssignedUser().getName());
    }
    
    @Override
    public void onTaskStarted(Task task) {
        System.out.println("[NOTIFICATION] Task Started: " + task.getTitle() + 
                          " by " + task.getAssignedUser().getName());
    }
    
    @Override
    public void onTaskCompleted(Task task) {
        System.out.println("[NOTIFICATION] Task Completed: " + task.getTitle() + 
                          " by " + task.getAssignedUser().getName());
    }
    
    @Override
    public void onTaskCancelled(Task task) {
        System.out.println("[NOTIFICATION] Task Cancelled: " + task.getTitle());
    }
    
    @Override
    public void onTaskOverdue(Task task) {
        System.out.println("[ALERT] Task Overdue: " + task.getTitle() + 
                          " assigned to " + task.getAssignedUser().getName());
    }
    
    @Override
    public void onUserAssigned(User user, Task task) {
        System.out.println("[NOTIFICATION] User " + user.getName() + 
                          " assigned to task: " + task.getTitle());
    }
}