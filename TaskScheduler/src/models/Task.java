package models;

import enums.TaskStatus;
import enums.TaskPriority;
import java.time.LocalDateTime;

/**
 * Represents a task in the Task Scheduler system
 * Similar to Booking in BookMyShow implementation
 */
public class Task {
    private final String taskId;
    private final String title;
    private final String description;
    private final User assignedUser;
    private final LocalDateTime createdAt;
    private final LocalDateTime dueDate;
    private final TaskPriority priority;
    private TaskStatus status;
    private LocalDateTime completedAt;

    public Task(String taskId, String title, String description, User assignedUser, 
                LocalDateTime dueDate, TaskPriority priority) {
        this.taskId = taskId;
        this.title = title;
        this.description = description;
        this.assignedUser = assignedUser;
        this.createdAt = LocalDateTime.now();
        this.dueDate = dueDate;
        this.priority = priority;
        this.status = TaskStatus.PENDING;
    }

    /**
     * Starts the task
     */
    public void start() {
        this.status = TaskStatus.IN_PROGRESS;
    }

    /**
     * Completes the task
     */
    public void complete() {
        this.status = TaskStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
    }

    /**
     * Cancels the task
     */
    public void cancel() {
        this.status = TaskStatus.CANCELLED;
    }

    /**
     * Marks task as overdue
     */
    public void markOverdue() {
        this.status = TaskStatus.OVERDUE;
    }

    /**
     * Checks if task is overdue
     */
    public boolean isOverdue() {
        return LocalDateTime.now().isAfter(dueDate) && status != TaskStatus.COMPLETED;
    }

    // Getters
    public String getTaskId() {
        return taskId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public User getAssignedUser() {
        return assignedUser;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    @Override
    public String toString() {
        return String.format("Task{id='%s', title='%s', user='%s', priority=%s, status=%s}", 
                           taskId, title, assignedUser.getName(), priority, status);
    }
}