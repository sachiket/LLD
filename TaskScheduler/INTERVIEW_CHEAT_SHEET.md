# 📝 Task Scheduler Interview Cheat Sheet

## 🚀 Quick Reference for Live Coding

### **Core Models (5 minutes)**
```java
// Task.java
public class Task {
    private final String taskId;
    private final String title;
    private final User assignedUser;
    private final TaskPriority priority;
    private TaskStatus status;
    private LocalDateTime dueDate;
}

// User.java
public class User {
    private final String userId;
    private final String name;
    private final String email;
}
```

### **Key Enums**
```java
public enum TaskStatus { PENDING, IN_PROGRESS, COMPLETED, CANCELLED, OVERDUE }
public enum TaskPriority { LOW, MEDIUM, HIGH, URGENT }
```

---

## 🎯 Design Patterns Quick Implementation

### **1. Factory Pattern (Task Creation)**
```java
public class TaskFactory {
    public static Task createTask(String title, String description, User assignedUser, 
                                 LocalDateTime dueDate, TaskPriority priority) {
        String taskId = "TASK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return new Task(taskId, title, description, assignedUser, dueDate, priority);
    }
}
```

### **2. Observer Pattern (Notifications)**
```java
public interface TaskEventObserver {
    void onTaskCreated(Task task);
    void onTaskCompleted(Task task);
}

// In Service Implementation
private void notifyTaskCreated(Task task) {
    for (TaskEventObserver observer : observers) {
        observer.onTaskCreated(task);
    }
}
```

### **3. Singleton Pattern (Task Manager)**
```java
public class TaskManager {
    private static TaskManager instance;
    
    public static synchronized TaskManager getInstance() {
        if (instance == null) {
            instance = new TaskManager();
        }
        return instance;
    }
}
```

---

## ⚖️ SOLID Principles Examples

### **Single Responsibility**
```java
// ✅ Good - Each class has one responsibility
public class Task { /* Only manages task state */ }
public class TaskFactory { /* Only creates tasks */ }
public class TaskManager { /* Only manages collections */ }
```

### **Open/Closed**
```java
// ✅ Easy to extend without modifying existing code
public class EmailNotificationObserver implements TaskEventObserver {
    @Override
    public void onTaskCompleted(Task task) {
        // Email notification logic
        sendEmail(task.getAssignedUser().getEmail(), "Task completed!");
    }
}
```

### **Dependency Inversion**
```java
// ✅ Depend on abstractions
public class TaskSchedulerServiceImpl {
    private final TaskManager taskManager; // Interface/abstraction
    private final List<TaskEventObserver> observers; // Interface
}
```

---

## 🔧 Core Service Methods

### **Task Creation**
```java
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
```

### **Task State Management**
```java
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
```

---

## 🎤 Common Interview Questions & Answers

### **Q: "How would you handle concurrent task updates?"**
```java
// Option 1: Synchronized methods
public synchronized boolean startTask(String taskId) { }

// Option 2: Database-level locking
@Transactional
public Task createTask(...) {
    // Use SELECT FOR UPDATE in database
}

// Option 3: Optimistic locking
public class Task {
    private int version; // JPA @Version annotation
}
```

### **Q: "How would you add task dependencies?"**
```java
public class Task {
    private List<String> prerequisiteTaskIds;
    
    public boolean canStart() {
        return prerequisiteTaskIds.stream()
            .allMatch(id -> taskManager.getTask(id).getStatus() == TaskStatus.COMPLETED);
    }
}
```

### **Q: "How would you implement recurring tasks?"**
```java
public class RecurringTask extends Task {
    private RecurrencePattern pattern; // DAILY, WEEKLY, MONTHLY
    private LocalDateTime nextDueDate;
    
    public Task createNextInstance() {
        // Create new task based on pattern
        return TaskFactory.createTask(getTitle(), getDescription(), 
                                    getAssignedUser(), calculateNextDueDate(), getPriority());
    }
}
```

---

## 🚀 Quick Extensions

### **Add New Task Status (2 minutes)**
```java
// 1. Add to enum
public enum TaskStatus { PENDING, IN_PROGRESS, COMPLETED, CANCELLED, OVERDUE, ON_HOLD }

// 2. Update state transitions
public void pauseTask() {
    if (status == TaskStatus.IN_PROGRESS) {
        this.status = TaskStatus.ON_HOLD;
    }
}
```

### **Add Email Notifications (3 minutes)**
```java
public class EmailNotificationObserver implements TaskEventObserver {
    @Override
    public void onTaskOverdue(Task task) {
        String email = task.getAssignedUser().getEmail();
        sendEmail(email, "Task Overdue: " + task.getTitle());
    }
    
    private void sendEmail(String email, String message) {
        System.out.println("Email sent to " + email + ": " + message);
    }
}
```

### **Add Task Categories (5 minutes)**
```java
public enum TaskCategory { DEVELOPMENT, TESTING, DOCUMENTATION, MEETING }

public class Task {
    private TaskCategory category;
    
    // Add category-based filtering in service
    public List<Task> getTasksByCategory(TaskCategory category) {
        return taskManager.getAllTasks().stream()
            .filter(task -> task.getCategory() == category)
            .collect(Collectors.toList());
    }
}
```

---

## 🎯 Demo Script

### **Step 1: Show Users**
```java
service.displayUsers();
// Shows: Alice Johnson, Bob Smith, Charlie Brown
```

### **Step 2: Create Tasks**
```java
Task task1 = service.createTask("Implement authentication", 
    "Complete login system", "USER001", 
    LocalDateTime.now().plusDays(3), TaskPriority.HIGH);
// Shows: Observer notification, task creation
```

### **Step 3: Task Operations**
```java
service.startTask(task1.getTaskId());
service.completeTask(task1.getTaskId());
// Shows: State transitions, notifications
```

### **Step 4: Display Tasks**
```java
service.displayAllTasks();
service.displayTasksForUser("USER001");
service.displayOverdueTasks();
// Shows: Filtering, formatting, overdue detection
```

---

## 💡 Pro Tips for Live Coding

### **Start with This Template**
```java
public class Main {
    public static void main(String[] args) {
        // 1. Setup system
        TaskSchedulerServiceImpl service = new TaskSchedulerServiceImpl();
        
        // 2. Add sample data
        setupSampleData(service);
        
        // 3. Demonstrate functionality
        demonstrateTaskOperations(service);
    }
}
```

### **Time-Saving Shortcuts**
- Use `LocalDateTime.now().plusDays(3)` for future due dates
- Use `Arrays.asList()` for quick list creation
- Use `String.format()` for clean output
- Use `UUID.randomUUID().toString().substring(0, 8)` for IDs

### **Error Handling Pattern**
```java
try {
    // Main operation
} catch (IllegalArgumentException e) {
    System.out.println("Error: " + e.getMessage());
    return false;
}
```

---

## 🏆 Success Checklist

### **Must Have (60% score)**
- ✅ Basic models (Task, User)
- ✅ Core task operations (create, start, complete)
- ✅ At least 1 design pattern
- ✅ Working demo

### **Should Have (80% score)**
- ✅ 2-3 design patterns
- ✅ SOLID principles demonstrated
- ✅ Error handling
- ✅ Observer notifications

### **Nice to Have (95% score)**
- ✅ All design patterns
- ✅ Overdue task detection
- ✅ Comprehensive demo
- ✅ Extension examples

---

## 🎯 Key Talking Points

### **Design Patterns**
- *"I'm using Factory pattern for consistent task creation and ID generation"*
- *"Observer pattern allows us to add new notification channels without changing core logic"*
- *"Singleton ensures centralized task and user management"*

### **SOLID Principles**
- *"Each class has a single responsibility - Task manages state, Factory creates objects"*
- *"The system is open for extension - easy to add new task statuses or priorities"*
- *"I'm depending on abstractions through interfaces"*

### **Scalability**
- *"This design supports multiple users and teams"*
- *"The service layer can be easily converted to REST APIs"*
- *"Task management can be distributed across multiple instances"*

---

## ⚠️ Common Pitfalls to Avoid

### **1. Over-Engineering Early**
❌ **Don't**: Start with complex inheritance hierarchies
✅ **Do**: Begin with simple, clean models

### **2. Poor State Management**
❌ **Don't**: Allow invalid state transitions
✅ **Do**: Validate state changes in task methods

### **3. Forgetting Edge Cases**
❌ **Don't**: Assume all operations succeed
✅ **Do**: Handle null checks and invalid inputs

### **4. No Demonstration**
❌ **Don't**: Just write code without running it
✅ **Do**: Create a working demo that shows all features

**Remember**: Quality over quantity. Better to have fewer features implemented well than many features implemented poorly!