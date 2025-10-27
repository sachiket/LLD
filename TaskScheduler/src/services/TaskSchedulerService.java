package services;

import models.Task;
import models.User;
import enums.TaskStatus;
import enums.TaskPriority;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Main service interface for Task Scheduler system
 * Similar to BookMyShowService in the reference implementation
 */
public interface TaskSchedulerService {
    
    // Task operations
    Task createTask(String title, String description, String userId, 
                   LocalDateTime dueDate, TaskPriority priority);
    boolean startTask(String taskId);
    boolean completeTask(String taskId);
    boolean cancelTask(String taskId);
    Task getTaskById(String taskId);
    
    // User operations
    void addUser(User user);
    User getUserById(String userId);
    List<Task> getTasksForUser(String userId);
    
    // Query operations
    List<Task> getAllTasks();
    List<Task> getTasksByStatus(TaskStatus status);
    List<Task> getTasksByPriority(TaskPriority priority);
    List<Task> getOverdueTasks();
    List<Task> getTasksDueToday();
    
    // Display operations
    void displayAllTasks();
    void displayTasksForUser(String userId);
    void displayTaskDetails(String taskId);
    void displayOverdueTasks();
}