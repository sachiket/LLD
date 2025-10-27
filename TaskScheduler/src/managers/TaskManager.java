package managers;

import models.Task;
import models.User;
import enums.TaskStatus;
import enums.TaskPriority;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Singleton class to manage tasks
 * Implements Singleton Design Pattern - similar to TheaterManager
 */
public class TaskManager {
    private static TaskManager instance;
    private final Map<String, Task> tasks;
    private final Map<String, User> users;
    
    private TaskManager() {
        this.tasks = new HashMap<>();
        this.users = new HashMap<>();
    }
    
    /**
     * Gets the singleton instance of TaskManager
     * Thread-safe implementation using synchronized method
     */
    public static synchronized TaskManager getInstance() {
        if (instance == null) {
            instance = new TaskManager();
        }
        return instance;
    }
    
    /**
     * Adds a task to the manager
     */
    public void addTask(Task task) {
        tasks.put(task.getTaskId(), task);
    }
    
    /**
     * Adds a user to the manager
     */
    public void addUser(User user) {
        users.put(user.getUserId(), user);
    }
    
    /**
     * Gets a task by ID
     */
    public Task getTask(String taskId) {
        return tasks.get(taskId);
    }
    
    /**
     * Gets a user by ID
     */
    public User getUser(String userId) {
        return users.get(userId);
    }
    
    /**
     * Gets all tasks for a specific user
     */
    public List<Task> getTasksForUser(String userId) {
        return tasks.values().stream()
                .filter(task -> task.getAssignedUser().getUserId().equals(userId))
                .collect(Collectors.toList());
    }
    
    /**
     * Gets tasks by status
     */
    public List<Task> getTasksByStatus(TaskStatus status) {
        return tasks.values().stream()
                .filter(task -> task.getStatus() == status)
                .collect(Collectors.toList());
    }
    
    /**
     * Gets tasks by priority
     */
    public List<Task> getTasksByPriority(TaskPriority priority) {
        return tasks.values().stream()
                .filter(task -> task.getPriority() == priority)
                .collect(Collectors.toList());
    }
    
    /**
     * Gets all tasks
     */
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }
    
    /**
     * Gets all users
     */
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
    
    /**
     * Removes a task from the manager
     */
    public Task removeTask(String taskId) {
        return tasks.remove(taskId);
    }
    
    /**
     * Gets the total number of tasks managed
     */
    public int getTotalTasks() {
        return tasks.size();
    }
}