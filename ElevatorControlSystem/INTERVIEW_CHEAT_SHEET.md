# 🎯 Elevator Control System - Interview Cheat Sheet

## 🚀 **QUICK REFERENCE - 30 SECONDS TO CONFIDENCE**

### **Opening Statement:**
*"I'll implement an elevator control system with multiple design patterns, demonstrating OOP principles and scalable architecture."*

---

## 📋 **CORE ENTITIES (Memorize This!)**

```java
// 1. ELEVATOR
class Elevator {
    String id;                    // Unique identifier
    ElevatorStatus status;        // MOVING, NOT_MOVING, MAINTENANCE
    Direction direction;          // UP, DOWN, IDLE
    int maximumCapacity;         // Max people capacity
    int currentCapacity;         // Current people count
    int currentFloor;            // Current position
}

// 2. BUILDING
class Building {
    String id;                   // Building identifier
    List<Elevator> elevators;    // All elevators in building
    int floorCount;             // Total floors
}

// 3. ENUMS
enum ElevatorStatus { MOVING, NOT_MOVING, MAINTENANCE, OUT_OF_SERVICE }
enum Direction { UP, DOWN, IDLE }
```

---

## 🎯 **DESIGN PATTERNS (Must Know!)**

### **1. SINGLETON - ElevatorManager**
```java
public class ElevatorManager {
    private static ElevatorManager instance;
    private Map<String, Elevator> db = new ConcurrentHashMap<>();
    
    public static synchronized ElevatorManager getInstance() {
        if (instance == null) instance = new ElevatorManager();
        return instance;
    }
}
```

### **2. FACTORY - Object Creation**
```java
public class ElevatorFactory {
    public static Elevator createElevator() {
        return new Elevator(ElevatorStatus.NOT_MOVING, Direction.IDLE, 10, 0, 0);
    }
}
```

### **3. STRATEGY - Scheduling**
```java
public interface SchedulingStrategy {
    Elevator selectElevator(List<Elevator> elevators, int floor, Direction dir);
}

public class NearestElevatorStrategy implements SchedulingStrategy {
    // Select closest available elevator
}
```

### **4. SERVICE LAYER - Business Logic**
```java
public class ElevatorService {
    public synchronized void request(int fromFloor, int toFloor);
    public synchronized void move(Direction dir, int targetFloor, String elevatorId);
}
```

---

## ⚡ **KEY METHODS (Copy-Paste Ready!)**

### **Core Movement:**
```java
public void move() {
    if(direction == Direction.UP) currentFloor++;
    if(direction == Direction.DOWN && currentFloor > 0) currentFloor--;
}
```

### **Request Handling:**
```java
public synchronized void request(int currFloor, int targetFloor) {
    Direction requestDirection = targetFloor > currFloor ? Direction.UP : Direction.DOWN;
    Elevator bestElevator = findNearestElevator(currFloor, requestDirection);
    // Move elevator to pickup, then to destination
}
```

### **Nearest Elevator Selection:**
```java
private Elevator findNearestElevator(int floor, Direction direction) {
    Elevator nearest = null;
    int minDistance = Integer.MAX_VALUE;
    
    for (Elevator elevator : availableElevators) {
        int distance = Math.abs(elevator.getCurrentFloor() - floor);
        if (distance < minDistance) {
            minDistance = distance;
            nearest = elevator;
        }
    }
    return nearest;
}
```

---

## 🎤 **INTERVIEW TALKING POINTS**

### **When Explaining Design Patterns:**
- *"I'm using **Singleton** for ElevatorManager to ensure single point of control"*
- *"**Factory pattern** provides consistent object creation with proper defaults"*
- *"**Strategy pattern** makes scheduling algorithms interchangeable"*
- *"**Service layer** separates business logic from data management"*

### **When Discussing SOLID Principles:**
- *"**SRP**: Each class has one responsibility - Elevator manages state, Manager manages collection"*
- *"**OCP**: I can add new scheduling strategies without modifying existing code"*
- *"**DIP**: Service depends on SchedulingStrategy interface, not concrete implementation"*

### **When Asked About Thread Safety:**
- *"Using **synchronized methods** for critical sections"*
- *"**ConcurrentHashMap** for thread-safe elevator storage"*
- *"Atomic operations prevent race conditions in elevator assignment"*

---

## 🚀 **QUICK EXTENSIONS (Impress Them!)**

### **1. Emergency Mode (2 minutes):**
```java
public void handleEmergency(String elevatorId) {
    Elevator elevator = elevatorManager.getElevator(elevatorId);
    elevator.setStatus(ElevatorStatus.EMERGENCY);
    // Move to nearest floor and stop
}
```

### **2. Request Queuing (3 minutes):**
```java
public class Elevator {
    private PriorityQueue<Integer> upRequests = new PriorityQueue<>();
    private PriorityQueue<Integer> downRequests = new PriorityQueue<>(Collections.reverseOrder());
    
    public void addRequest(int floor, Direction direction) {
        if (direction == Direction.UP) upRequests.offer(floor);
        else downRequests.offer(floor);
    }
}
```

### **3. Capacity Validation (1 minute):**
```java
public boolean canAcceptPassengers(int passengerCount) {
    return currentCapacity + passengerCount <= maximumCapacity;
}
```

---

## 🎯 **COMMON QUESTIONS & INSTANT ANSWERS**

### **Q: "How would you handle concurrent requests?"**
**A:** *"I use synchronized methods in ElevatorService and ConcurrentHashMap in ElevatorManager for thread-safe operations."*

### **Q: "How would you optimize elevator efficiency?"**
**A:** *"Implement SCAN algorithm - continue in current direction until no more requests, then reverse. Also group requests by direction."*

### **Q: "How would you scale to multiple buildings?"**
**A:** *"Extend ElevatorManager to include building IDs, or create BuildingManager that manages multiple ElevatorManagers."*

### **Q: "How would you handle system failures?"**
**A:** *"Add state persistence to save elevator positions and pending requests. Implement graceful degradation and automatic recovery."*

### **Q: "How would you add different elevator types?"**
**A:** *"Use Strategy pattern for elevator behavior - ExpressElevator, LocalElevator, FreightElevator with different movement strategies."*

---

## 📊 **DEMO SCENARIOS (Always Include!)**

```java
public static void main(String[] args) {
    // 1. Create building
    Building building = BuildingFactory.createBuilding(10, 3, 8);
    
    // 2. Initialize system
    ElevatorManager.getInstance().addElevators(building.getElevators());
    ElevatorService service = new ElevatorService();
    
    // 3. Demo scenarios
    service.request(3, 7);        // External request
    service.move(Direction.UP, 9, elevatorId);  // Internal request
    service.displayElevatorStatus();            // Show results
}
```

---

## ⚖️ **SOLID PRINCIPLES QUICK CHECK**

- ✅ **SRP**: Elevator (state), Manager (collection), Service (logic)
- ✅ **OCP**: Add new SchedulingStrategy without changing existing code
- ✅ **LSP**: Any SchedulingStrategy can replace another
- ✅ **ISP**: Focused interfaces (SchedulingStrategy only has selection method)
- ✅ **DIP**: Depend on SchedulingStrategy interface, not concrete class

---

## 🔧 **THREAD SAFETY CHECKLIST**

- ✅ **Synchronized methods** in ElevatorService
- ✅ **ConcurrentHashMap** for elevator storage
- ✅ **Atomic state transitions** in elevator movement
- ✅ **No shared mutable state** without protection

---

## 💡 **PRO TIPS FOR SUCCESS**

### **Opening (First 5 minutes):**
1. Start with simple Elevator and Building classes
2. Add basic enums immediately
3. Explain your approach: "I'll build incrementally"

### **Middle (Next 30 minutes):**
1. Add ElevatorManager (Singleton)
2. Implement ElevatorService with core methods
3. Add Factory patterns
4. Introduce Strategy pattern

### **Closing (Last 15 minutes):**
1. Add thread safety
2. Create comprehensive demo
3. Discuss extensions and scalability

### **Throughout Interview:**
- **Explain as you code**: "I'm adding this because..."
- **Ask clarifying questions**: "Should I focus on efficiency or simplicity?"
- **Test frequently**: Run code after major additions
- **Handle edge cases**: "Let me add validation for invalid floors"

---

## 🎯 **CONFIDENCE BOOSTERS**

### **If You Get Stuck:**
- *"Let me step back and think about the core requirements..."*
- *"I'll start with a simpler approach and then optimize..."*
- *"This is a good place to add [design pattern] for extensibility..."*

### **If Asked About Advanced Topics:**
- **Distributed Systems**: *"I'd use message queues for request handling and database for state persistence"*
- **Performance**: *"I'd add caching for elevator status and implement connection pooling"*
- **Monitoring**: *"I'd add metrics collection for response times and elevator utilization"*

---

## 🏆 **SUCCESS METRICS**

### **Minimum Viable (60%):**
- ✅ Basic Elevator and Building classes
- ✅ Simple request handling
- ✅ One design pattern (Factory or Singleton)
- ✅ Working demo

### **Good Performance (80%):**
- ✅ All core functionality
- ✅ 2-3 design patterns
- ✅ Thread safety considerations
- ✅ Clean code structure

### **Excellent Performance (95%):**
- ✅ All design patterns implemented
- ✅ SOLID principles demonstrated
- ✅ Comprehensive thread safety
- ✅ Multiple extension examples
- ✅ Professional-level code quality

**Remember: Show your thought process, not just the final code!**