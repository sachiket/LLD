package factories;

import models.Task;
import models.User;
import enums.TaskPriority;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Factory class for creating Task instances
 * Implements Factory Design Pattern - similar to BookingFactory
 */
public class TaskFactory {
    
    /**
     * Creates a new task with auto-generated ID
     */
    public static Task createTask(String title, String description, User assignedUser, 
                                 LocalDateTime dueDate, TaskPriority priority) {
        String taskId = generateTaskId();
        return new Task(taskId, title, description, assignedUser, dueDate, priority);
    }
    
    /**
     * Creates a task with custom ID (for testing purposes)
     */
    public static Task createTask(String taskId, String title, String description, 
                                 User assignedUser, LocalDateTime dueDate, TaskPriority priority) {
        return new Task(taskId, title, description, assignedUser, dueDate, priority);
    }
    
    private static String generateTaskId() {
        return "TASK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}