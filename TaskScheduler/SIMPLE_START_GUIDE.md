# 🎯 SIMPLE START GUIDE - Task Scheduler Interview

## 🚀 **DON'T PANIC! Start Here:**

### **Your Opening Line (30 seconds):**
*"I'll design a task scheduling system step by step. Let me start with the basic entities and build up gradually."*

---

## 📝 **STEP 1: Draw Basic Entities (2 minutes)**

**On whiteboard/screen, draw:**
```
[User] ----assigned----> [Task] ----has----> [Priority]
                           |
                      [Status] & [DueDate]
```

**Say:** *"These are my core entities. Let me code them..."*

### **Code Step 1:**
```java
// Start with just this!
class User {
    String userId;
    String name;
    String email;
}

class Task {
    String taskId;
    String title;
    String description;
    User assignedUser;
    String priority; // "LOW", "MEDIUM", "HIGH", "URGENT"
    String status = "PENDING"; // "PENDING", "IN_PROGRESS", "COMPLETED"
    LocalDateTime dueDate;
    LocalDateTime createdAt;
}
```

**Say:** *"This covers the basics. Now let me add task operations..."*

---

## 📝 **STEP 2: Add Core Operations (3 minutes)**

```java
class Task {
    // ... previous fields ...
    
    void start() {
        if (status.equals("PENDING")) {
            this.status = "IN_PROGRESS";
            System.out.println("Task started: " + title);
        }
    }
    
    void complete() {
        if (status.equals("IN_PROGRESS")) {
            this.status = "COMPLETED";
            System.out.println("Task completed: " + title);
        }
    }
    
    void cancel() {
        this.status = "CANCELLED";
        System.out.println("Task cancelled: " + title);
    }
    
    boolean isOverdue() {
        return LocalDateTime.now().isAfter(dueDate) && !status.equals("COMPLETED");
    }
}
```

**Say:** *"Great! This handles basic task lifecycle. Now let me add a service to manage multiple tasks..."*

---

## 📝 **STEP 3: Add Task Management (3 minutes)**

```java
class TaskSchedulerService {
    List<User> users = new ArrayList<>();
    List<Task> tasks = new ArrayList<>();
    
    void addUser(User user) {
        users.add(user);
        System.out.println("User added: " + user.name);
    }
    
    Task createTask(String title, String description, String userId, 
                   LocalDateTime dueDate, String priority) {
        User user = findUserById(userId);
        if (user == null) {
            System.out.println("User not found!");
            return null;
        }
        
        String taskId = "TASK-" + System.currentTimeMillis();
        Task task = new Task(taskId, title, description, user, priority, dueDate);
        tasks.add(task);
        
        System.out.println("Task created: " + title + " assigned to " + user.name);
        return task;
    }
    
    boolean startTask(String taskId) {
        Task task = findTaskById(taskId);
        if (task != null) {
            task.start();
            return true;
        }
        return false;
    }
    
    List<Task> getTasksForUser(String userId) {
        List<Task> userTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.assignedUser.userId.equals(userId)) {
                userTasks.add(task);
            }
        }
        return userTasks;
    }
}
```

**Say:** *"Perfect! This works. Now let me make it more professional with enums..."*

---

## 📝 **STEP 4: Add Enums & Clean Up (2 minutes)**

```java
enum TaskStatus { PENDING, IN_PROGRESS, COMPLETED, CANCELLED, OVERDUE }
enum TaskPriority { LOW, MEDIUM, HIGH, URGENT }

class Task {
    String taskId;
    String title;
    String description;
    User assignedUser;
    TaskPriority priority;
    TaskStatus status = TaskStatus.PENDING;
    LocalDateTime dueDate;
    LocalDateTime createdAt;
    LocalDateTime completedAt;
    
    Task(String taskId, String title, String description, User assignedUser, 
         TaskPriority priority, LocalDateTime dueDate) {
        this.taskId = taskId;
        this.title = title;
        this.description = description;
        this.assignedUser = assignedUser;
        this.priority = priority;
        this.dueDate = dueDate;
        this.createdAt = LocalDateTime.now();
    }
    
    void start() {
        if (status == TaskStatus.PENDING) {
            this.status = TaskStatus.IN_PROGRESS;
        }
    }
    
    void complete() {
        if (status == TaskStatus.IN_PROGRESS) {
            this.status = TaskStatus.COMPLETED;
            this.completedAt = LocalDateTime.now();
        }
    }
}
```

**Say:** *"Much cleaner with enums. Now let me add better ID generation..."*

---

## 📝 **STEP 5: Add Factory Pattern (2 minutes)**

```java
class TaskFactory {
    static Task createTask(String title, String description, User assignedUser, 
                          LocalDateTime dueDate, TaskPriority priority) {
        String taskId = generateTaskId();
        return new Task(taskId, title, description, assignedUser, priority, dueDate);
    }
    
    private static String generateTaskId() {
        return "TASK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}

// Update service to use factory
class TaskSchedulerService {
    Task createTask(String title, String description, String userId, 
                   LocalDateTime dueDate, TaskPriority priority) {
        User user = findUserById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + userId);
        }
        
        Task task = TaskFactory.createTask(title, description, user, dueDate, priority);
        tasks.add(task);
        
        System.out.println("Task created: " + title);
        return task;
    }
}
```

**Say:** *"Great! Factory pattern ensures consistent task creation. Now let me add notifications..."*

---

## 📝 **STEP 6: Add Observer Pattern (3 minutes)**

```java
interface TaskEventObserver {
    void onTaskCreated(Task task);
    void onTaskStarted(Task task);
    void onTaskCompleted(Task task);
}

class ConsoleNotificationObserver implements TaskEventObserver {
    public void onTaskCreated(Task task) {
        System.out.println("[NOTIFICATION] Task Created: " + task.title + 
                          " assigned to " + task.assignedUser.name);
    }
    
    public void onTaskStarted(Task task) {
        System.out.println("[NOTIFICATION] Task Started: " + task.title + 
                          " by " + task.assignedUser.name);
    }
    
    public void onTaskCompleted(Task task) {
        System.out.println("[NOTIFICATION] Task Completed: " + task.title + 
                          " by " + task.assignedUser.name);
    }
}

class TaskSchedulerService {
    List<TaskEventObserver> observers = new ArrayList<>();
    
    void addObserver(TaskEventObserver observer) {
        observers.add(observer);
    }
    
    Task createTask(String title, String description, String userId, 
                   LocalDateTime dueDate, TaskPriority priority) {
        // ... creation logic ...
        
        // Notify observers
        for (TaskEventObserver observer : observers) {
            observer.onTaskCreated(task);
        }
        
        return task;
    }
}
```

**Say:** *"Excellent! Observer pattern makes notifications extensible. Now let me add centralized management..."*

---

## 📝 **STEP 7: Add Singleton Manager (2 minutes)**

```java
class TaskManager {
    private static TaskManager instance;
    private Map<String, Task> tasks = new HashMap<>();
    private Map<String, User> users = new HashMap<>();
    
    private TaskManager() {} // Private constructor
    
    public static synchronized TaskManager getInstance() {
        if (instance == null) {
            instance = new TaskManager();
        }
        return instance;
    }
    
    void addTask(Task task) {
        tasks.put(task.taskId, task);
    }
    
    void addUser(User user) {
        users.put(user.userId, user);
    }
    
    Task getTask(String taskId) {
        return tasks.get(taskId);
    }
    
    User getUser(String userId) {
        return users.get(userId);
    }
    
    List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }
}
```

**Say:** *"Perfect! Singleton ensures centralized management. Now let me create a clean service interface..."*

---

## 📝 **STEP 8: Add Service Interface (2 minutes)**

```java
interface TaskSchedulerService {
    Task createTask(String title, String description, String userId, 
                   LocalDateTime dueDate, TaskPriority priority);
    boolean startTask(String taskId);
    boolean completeTask(String taskId);
    boolean cancelTask(String taskId);
    List<Task> getAllTasks();
    List<Task> getTasksForUser(String userId);
    List<Task> getOverdueTasks();
}

class TaskSchedulerServiceImpl implements TaskSchedulerService {
    private TaskManager taskManager = TaskManager.getInstance();
    private List<TaskEventObserver> observers = new ArrayList<>();
    
    public void addObserver(TaskEventObserver observer) {
        observers.add(observer);
    }
    
    @Override
    public Task createTask(String title, String description, String userId, 
                          LocalDateTime dueDate, TaskPriority priority) {
        User user = taskManager.getUser(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + userId);
        }
        
        Task task = TaskFactory.createTask(title, description, user, dueDate, priority);
        taskManager.addTask(task);
        
        // Notify observers
        for (TaskEventObserver observer : observers) {
            observer.onTaskCreated(task);
        }
        
        return task;
    }
    
    @Override
    public boolean startTask(String taskId) {
        Task task = taskManager.getTask(taskId);
        if (task != null && task.status == TaskStatus.PENDING) {
            task.start();
            notifyTaskStarted(task);
            return true;
        }
        return false;
    }
}
```

**Say:** *"Now it's professional with clean interfaces and separation of concerns!"*

---

## 🎯 **Your Interview Mindset:**

### **Think Step by Step:**
1. **"What are the main entities?"** → User, Task
2. **"What are the main operations?"** → Create, Start, Complete, Cancel tasks
3. **"How can I make it scalable?"** → Service Layer, Manager Pattern
4. **"How can I make it flexible?"** → Design Patterns

### **Always Explain:**
- *"I'm starting simple and building up..."*
- *"Let me add this for better organization..."*
- *"This pattern will make it more flexible..."*
- *"I can extend this easily for new requirements..."*

### **If Asked About Extensions:**
- **New task status?** *"Just add to enum and update state transitions"*
- **Different notifications?** *"Create new TaskEventObserver implementation"*
- **Task dependencies?** *"Add prerequisiteTaskIds list to Task model"*
- **Recurring tasks?** *"Extend with RecurringTask class and scheduling logic"*

---

## ✅ **Success Checklist:**

- ✅ Started simple with basic classes
- ✅ Built functionality incrementally  
- ✅ Explained reasoning at each step
- ✅ Added design patterns naturally
- ✅ Showed extensibility
- ✅ Handled edge cases
- ✅ Demonstrated clean code principles

### **Final Demo Code:**
```java
public class Main {
    public static void main(String[] args) {
        // Setup
        TaskSchedulerServiceImpl service = new TaskSchedulerServiceImpl();
        service.addObserver(new ConsoleNotificationObserver());
        
        // Add users
        TaskManager.getInstance().addUser(new User("U1", "Alice", "alice@company.com"));
        
        // Create and manage tasks
        Task task = service.createTask("Implement login", "Add authentication", 
                                      "U1", LocalDateTime.now().plusDays(3), TaskPriority.HIGH);
        
        service.startTask(task.taskId);
        service.completeTask(task.taskId);
        
        // Show results
        System.out.println("All tasks: " + service.getAllTasks().size());
    }
}
```

**Remember: It's not about perfect code, it's about showing your thought process and building incrementally!**