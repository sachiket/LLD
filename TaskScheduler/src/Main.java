import models.*;
import enums.*;
import services.TaskSchedulerServiceImpl;
import observers.TaskEventObserver;
import observers.ConsoleNotificationObserver;
import managers.TaskManager;
import java.time.LocalDateTime;

/**
 * Main class demonstrating Task Scheduler system functionality
 * Shows all design patterns and SOLID principles in action
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("[DEMO] Welcome to Task Scheduler System Demo!");
        System.out.println("=" + "=".repeat(60));
        
        // Step 1: Setup the system
        TaskSchedulerServiceImpl taskService = setupSystem();
        
        // Step 2: Display all users
        System.out.println("\n[STEP 1] Display All Users");
        displayUsers(taskService);
        
        // Step 3: Create tasks
        System.out.println("\n[STEP 2] Create Tasks");
        demonstrateTaskCreation(taskService);
        
        // Step 4: Display all tasks
        System.out.println("\n[STEP 3] Display All Tasks");
        taskService.displayAllTasks();
        
        // Step 5: Demonstrate task operations
        System.out.println("\n[STEP 4] Task Operations");
        demonstrateTaskOperations(taskService);
        
        // Step 6: Display tasks for specific user
        System.out.println("\n[STEP 5] Display Tasks for User");
        taskService.displayTasksForUser("USER001");
        
        // Step 7: Check overdue tasks
        System.out.println("\n[STEP 6] Check Overdue Tasks");
        taskService.displayOverdueTasks();
        
        // Step 8: Display task details
        System.out.println("\n[STEP 7] Display Task Details");
        // Get the first task ID from the manager to display details
        TaskManager manager = TaskManager.getInstance();
        if (!manager.getAllTasks().isEmpty()) {
            String firstTaskId = manager.getAllTasks().get(0).getTaskId();
            taskService.displayTaskDetails(firstTaskId);
        }
        
        System.out.println("\n[SUCCESS] Demo completed successfully!");
        System.out.println("=" + "=".repeat(60));
    }
    
    /**
     * Sets up the complete Task Scheduler system with sample data
     */
    private static TaskSchedulerServiceImpl setupSystem() {
        // Create service with observer
        TaskSchedulerServiceImpl service = new TaskSchedulerServiceImpl();
        ConsoleNotificationObserver observer = new ConsoleNotificationObserver();
        service.addObserver((TaskEventObserver) observer);
        
        // Create users
        User alice = new User("USER001", "Alice Johnson", "alice@company.com");
        User bob = new User("USER002", "Bob Smith", "bob@company.com");
        User charlie = new User("USER003", "Charlie Brown", "charlie@company.com");
        
        service.addUser(alice);
        service.addUser(bob);
        service.addUser(charlie);
        
        return service;
    }
    
    /**
     * Displays all users in the system
     */
    private static void displayUsers(TaskSchedulerServiceImpl service) {
        TaskManager manager = TaskManager.getInstance();
        System.out.println("\n[USERS] Registered Users:");
        System.out.println("=" + "=".repeat(50));
        for (User user : manager.getAllUsers()) {
            System.out.println("[USER] " + user.getName() + " (" + user.getEmail() + ")");
        }
    }
    
    /**
     * Demonstrates task creation with different priorities and due dates
     */
    private static void demonstrateTaskCreation(TaskSchedulerServiceImpl service) {
        try {
            LocalDateTime now = LocalDateTime.now();
            
            // Create tasks with different priorities
            System.out.println("\n[SCENARIO 1] Creating High Priority Task");
            Task task1 = service.createTask("Implement user authentication",
                    "Complete the login and registration system", "USER001",
                    now.plusDays(3), TaskPriority.HIGH);
            
            System.out.println("\n[SCENARIO 2] Creating Medium Priority Task");
            Task task2 = service.createTask("Design database schema",
                    "Create ERD and database tables", "USER002",
                    now.plusDays(5), TaskPriority.MEDIUM);
            
            System.out.println("\n[SCENARIO 3] Creating Urgent Task");
            Task task3 = service.createTask("Fix critical bug",
                    "Resolve payment processing issue", "USER001",
                    now.plusHours(2), TaskPriority.URGENT);
            
            System.out.println("\n[SCENARIO 4] Creating Low Priority Task");
            Task task4 = service.createTask("Update documentation",
                    "Update API documentation", "USER003",
                    now.plusDays(7), TaskPriority.LOW);
            
            // Create an overdue task (due in the past)
            System.out.println("\n[SCENARIO 5] Creating Overdue Task");
            Task task5 = service.createTask("Code review",
                    "Review pull requests", "USER002",
                    now.minusDays(1), TaskPriority.MEDIUM);
            
        } catch (Exception e) {
            System.out.println("[X] Error during task creation: " + e.getMessage());
        }
    }
    
    /**
     * Demonstrates task operations like start, complete, cancel
     */
    private static void demonstrateTaskOperations(TaskSchedulerServiceImpl service) {
        try {
            TaskManager manager = TaskManager.getInstance();
            if (manager.getAllTasks().isEmpty()) {
                System.out.println("No tasks available for operations.");
                return;
            }
            
            // Get task IDs dynamically
            String firstTaskId = manager.getAllTasks().get(0).getTaskId();
            String secondTaskId = manager.getAllTasks().size() > 1 ?
                                 manager.getAllTasks().get(1).getTaskId() : firstTaskId;
            String thirdTaskId = manager.getAllTasks().size() > 2 ?
                                manager.getAllTasks().get(2).getTaskId() : firstTaskId;
            String fourthTaskId = manager.getAllTasks().size() > 3 ?
                                 manager.getAllTasks().get(3).getTaskId() : firstTaskId;
            
            // Start a task
            System.out.println("\n[OPERATION 1] Starting Task");
            boolean started = service.startTask(firstTaskId);
            System.out.println("Task started: " + started);
            
            // Complete a task
            System.out.println("\n[OPERATION 2] Completing Task");
            boolean completed = service.completeTask(firstTaskId);
            System.out.println("Task completed: " + completed);
            
            // Start another task
            System.out.println("\n[OPERATION 3] Starting Another Task");
            service.startTask(secondTaskId);
            
            // Cancel a task
            System.out.println("\n[OPERATION 4] Cancelling Task");
            boolean cancelled = service.cancelTask(fourthTaskId);
            System.out.println("Task cancelled: " + cancelled);
            
            // Try to complete a non-started task (should fail)
            System.out.println("\n[OPERATION 5] Try to Complete Non-Started Task");
            boolean failedComplete = service.completeTask(thirdTaskId);
            System.out.println("Task completion failed as expected: " + !failedComplete);
            
        } catch (Exception e) {
            System.out.println("[X] Error during task operations: " + e.getMessage());
        }
    }
}