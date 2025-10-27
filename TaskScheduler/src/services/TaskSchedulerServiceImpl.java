package services;

import models.Task;
import models.User;
import enums.TaskStatus;
import enums.TaskPriority;
import factories.TaskFactory;
import managers.TaskManager;
import observers.TaskEventObserver;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Implementation of TaskSchedulerService
 * Similar to BookMyShowServiceImpl in the reference implementation
 */
public class TaskSchedulerServiceImpl implements TaskSchedulerService {
    
    private final TaskManager taskManager;
    private final List<TaskEventObserver> observers;

    public TaskSchedulerServiceImpl() {
        this.taskManager = TaskManager.getInstance();
        this.observers = new ArrayList<>();
    }

    // Observer pattern methods
    public void addObserver(TaskEventObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(TaskEventObserver observer) {
        observers.remove(observer);
    }

    private void notifyTaskCreated(Task task) {
        for (TaskEventObserver observer : observers) {
            observer.onTaskCreated(task);
        }
    }

    private void notifyTaskStarted(Task task) {
        for (TaskEventObserver observer : observers) {
            observer.onTaskStarted(task);
        }
    }

    private void notifyTaskCompleted(Task task) {
        for (TaskEventObserver observer : observers) {
            observer.onTaskCompleted(task);
        }
    }

    private void notifyTaskCancelled(Task task) {
        for (TaskEventObserver observer : observers) {
            observer.onTaskCancelled(task);
        }
    }

    private void notifyTaskOverdue(Task task) {
        for (TaskEventObserver observer : observers) {
            observer.onTaskOverdue(task);
        }
    }

    // Task operations
    @Override
    public Task createTask(String title, String description, String userId, 
                          LocalDateTime dueDate, TaskPriority priority) {
        User user = taskManager.getUser(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + userId);
        }

        Task task = TaskFactory.createTask(title, description, user, dueDate, priority);
        taskManager.addTask(task);
        notifyTaskCreated(task);
        
        return task;
    }

    @Override
    public boolean startTask(String taskId) {
        Task task = taskManager.getTask(taskId);
        if (task == null || task.getStatus() != TaskStatus.PENDING) {
            return false;
        }

        task.start();
        notifyTaskStarted(task);
        return true;
    }

    @Override
    public boolean completeTask(String taskId) {
        Task task = taskManager.getTask(taskId);
        if (task == null || task.getStatus() != TaskStatus.IN_PROGRESS) {
            return false;
        }

        task.complete();
        notifyTaskCompleted(task);
        return true;
    }

    @Override
    public boolean cancelTask(String taskId) {
        Task task = taskManager.getTask(taskId);
        if (task == null || task.getStatus() == TaskStatus.COMPLETED) {
            return false;
        }

        task.cancel();
        notifyTaskCancelled(task);
        return true;
    }

    @Override
    public Task getTaskById(String taskId) {
        return taskManager.getTask(taskId);
    }

    // User operations
    @Override
    public void addUser(User user) {
        taskManager.addUser(user);
    }

    @Override
    public User getUserById(String userId) {
        return taskManager.getUser(userId);
    }

    @Override
    public List<Task> getTasksForUser(String userId) {
        return taskManager.getTasksForUser(userId);
    }

    // Query operations
    @Override
    public List<Task> getAllTasks() {
        return taskManager.getAllTasks();
    }

    @Override
    public List<Task> getTasksByStatus(TaskStatus status) {
        return taskManager.getTasksByStatus(status);
    }

    @Override
    public List<Task> getTasksByPriority(TaskPriority priority) {
        return taskManager.getTasksByPriority(priority);
    }

    @Override
    public List<Task> getOverdueTasks() {
        List<Task> overdueTasks = taskManager.getAllTasks().stream()
                .filter(Task::isOverdue)
                .collect(Collectors.toList());
        
        // Mark them as overdue and notify
        for (Task task : overdueTasks) {
            if (task.getStatus() != TaskStatus.OVERDUE) {
                task.markOverdue();
                notifyTaskOverdue(task);
            }
        }
        
        return overdueTasks;
    }

    @Override
    public List<Task> getTasksDueToday() {
        LocalDateTime today = LocalDateTime.now();
        return taskManager.getAllTasks().stream()
                .filter(task -> task.getDueDate().toLocalDate().equals(today.toLocalDate()))
                .filter(task -> task.getStatus() != TaskStatus.COMPLETED)
                .collect(Collectors.toList());
    }

    // Display operations
    @Override
    public void displayAllTasks() {
        List<Task> tasks = getAllTasks();
        System.out.println("\n[TASKS] All Tasks:");
        System.out.println("=" + "=".repeat(60));
        
        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }
        
        for (Task task : tasks) {
            displayTaskSummary(task);
        }
    }

    @Override
    public void displayTasksForUser(String userId) {
        User user = getUserById(userId);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }
        
        List<Task> userTasks = getTasksForUser(userId);
        System.out.println("\n[TASKS] Tasks for: " + user.getName());
        System.out.println("=" + "=".repeat(60));
        
        if (userTasks.isEmpty()) {
            System.out.println("No tasks assigned to this user.");
            return;
        }
        
        for (Task task : userTasks) {
            displayTaskSummary(task);
        }
    }

    @Override
    public void displayTaskDetails(String taskId) {
        Task task = getTaskById(taskId);
        if (task == null) {
            System.out.println("Task not found.");
            return;
        }

        System.out.println("\n[TASK] Task Details");
        System.out.println("=" + "=".repeat(60));
        System.out.println("Task ID: " + task.getTaskId());
        System.out.println("Title: " + task.getTitle());
        System.out.println("Description: " + task.getDescription());
        System.out.println("Assigned User: " + task.getAssignedUser().getName());
        System.out.println("Priority: " + task.getPriority());
        System.out.println("Status: " + task.getStatus());
        System.out.println("Created At: " + task.getCreatedAt());
        System.out.println("Due Date: " + task.getDueDate());
        if (task.getCompletedAt() != null) {
            System.out.println("Completed At: " + task.getCompletedAt());
        }
        System.out.println("Overdue: " + (task.isOverdue() ? "Yes" : "No"));
    }

    @Override
    public void displayOverdueTasks() {
        List<Task> overdueTasks = getOverdueTasks();
        System.out.println("\n[OVERDUE] Overdue Tasks:");
        System.out.println("=" + "=".repeat(60));
        
        if (overdueTasks.isEmpty()) {
            System.out.println("No overdue tasks found.");
            return;
        }
        
        for (Task task : overdueTasks) {
            displayTaskSummary(task);
        }
    }

    private void displayTaskSummary(Task task) {
        System.out.println("[TASK] " + task.getTitle() + " (" + task.getPriority() + ")");
        System.out.println("    Assigned to: " + task.getAssignedUser().getName());
        System.out.println("    Status: " + task.getStatus() + " | Due: " + task.getDueDate());
        if (task.isOverdue()) {
            System.out.println("    [!] OVERDUE");
        }
        System.out.println();
    }
}