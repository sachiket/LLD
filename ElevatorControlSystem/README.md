# 🏢 Elevator Control System - Complete Interview Guide

## 📋 Table of Contents
1. [System Overview](#system-overview)
2. [Architecture & Design Patterns](#architecture--design-patterns)
3. [OOP Principles Implementation](#oop-principles-implementation)
4. [SOLID Principles](#solid-principles)
5. [Code Structure](#code-structure)
6. [Key Features](#key-features)
7. [Interview Questions & Answers](#interview-questions--answers)
8. [How to Run](#how-to-run)
9. [Extension Points](#extension-points)

---

## 🏗️ System Overview

This is a **comprehensive Elevator Control System** implemented in Java, demonstrating multiple design patterns, OOP principles, and best practices commonly asked in FAANG technical interviews.

### Core Functionality
- **Elevator Management**: Multiple elevators with real-time status tracking
- **Building Management**: Multi-floor building support with elevator allocation
- **Request Handling**: External requests (floor-to-floor) and internal requests (inside elevator)
- **Intelligent Scheduling**: Nearest elevator strategy with direction preference
- **Capacity Management**: Real-time capacity tracking and validation
- **Movement Simulation**: Floor-by-floor movement with realistic timing
- **Thread Safety**: Synchronized operations for concurrent requests

---

## 🎯 Architecture & Design Patterns

### 1. **Strategy Pattern** 🎯
**Location**: [`src/strategies/`](src/strategies/)

**Purpose**: Encapsulates elevator scheduling algorithms and makes them interchangeable.

```java
// Interface
public interface SchedulingStrategy {
    Elevator selectElevator(List<Elevator> availableElevators, int requestFloor, Direction direction);
}

// Concrete Implementation
public class NearestElevatorStrategy implements SchedulingStrategy {
    @Override
    public Elevator selectElevator(List<Elevator> availableElevators, int requestFloor, Direction direction) {
        // Intelligent nearest elevator selection with direction preference
    }
}
```

**Interview Benefit**: Shows understanding of **Open/Closed Principle** and algorithm flexibility.

### 2. **Factory Pattern** 🏭
**Location**: [`src/factories/`](src/factories/)

**Purpose**: Centralizes object creation and provides consistent initialization.

```java
public class ElevatorFactory {
    public static Elevator createElevator() {
        return new Elevator(
            ElevatorStatus.NOT_MOVING, 
            Direction.IDLE, 
            10, // default maximum capacity
            0,  // default current capacity
            0   // default starting floor
        );
    }
}

public class BuildingFactory {
    public static Building createBuilding(int floorCount, int elevatorCount) {
        List<Elevator> elevators = new ArrayList<>();
        for (int i = 0; i < elevatorCount; i++) {
            elevators.add(ElevatorFactory.createElevator());
        }
        return new Building(elevators, floorCount);
    }
}
```

**Interview Benefit**: Demonstrates **creational patterns** and **separation of concerns**.

### 3. **Singleton Pattern** 🔒
**Location**: [`src/managers/ElevatorManager.java`](src/managers/ElevatorManager.java)

**Purpose**: Manages all elevators with a single point of access and thread safety.

```java
public class ElevatorManager {
    private static ElevatorManager instance;
    private final Map<String, Elevator> db;
    
    private ElevatorManager() {
        this.db = new ConcurrentHashMap<>();
    }
    
    public static synchronized ElevatorManager getInstance() {
        if (instance == null) {
            instance = new ElevatorManager();
        }
        return instance;
    }
}
```

**Interview Benefit**: Demonstrates **thread-safety** and **global state management**.

### 4. **Service Layer Pattern** 🔧
**Location**: [`src/services/ElevatorService.java`](src/services/ElevatorService.java)

**Purpose**: Encapsulates business logic and provides clean API for elevator operations.

```java
public class ElevatorService {
    // People inside the lift
    public synchronized void move(Direction dir, int targetFloor, String elevatorId);
    
    // People outside lift
    public synchronized void request(int currFloor, int targetFloor);
}
```

**Interview Benefit**: Shows **separation of concerns** and **business logic encapsulation**.

---

## 🧱 OOP Principles Implementation

### 1. **Encapsulation** 🔐
- **Private fields** with **public getters/setters**
- **Data validation** in constructors
- **Internal state protection**

```java
public class Elevator {
    private final String id = UUID.randomUUID().toString();  // Immutable
    private ElevatorStatus status;                           // Controlled access
    private Direction direction;                             // Controlled access
    private int maximumCapacity;                            // Controlled access
    private int currentCapacity;                            // Controlled access
    private int currentFloor;                               // Controlled access
    
    // Public methods control access to private data
    public void move() { /* movement logic with validation */ }
    public void setDirection(Direction direction) { /* state change logic */ }
}
```

### 2. **Inheritance** 🌳
- **Interface inheritance** for contracts
- **Composition over inheritance** approach

```java
// Interface defines contract
public interface SchedulingStrategy {
    Elevator selectElevator(List<Elevator> availableElevators, int requestFloor, Direction direction);
}

// Implementation provides behavior
public class NearestElevatorStrategy implements SchedulingStrategy {
    // Concrete implementation
}
```

### 3. **Polymorphism** 🎭
- **Interface-based polymorphism**
- **Strategy pattern implementation**

```java
// Same interface, different implementations
SchedulingStrategy strategy1 = new NearestElevatorStrategy();
SchedulingStrategy strategy2 = new EvenOddStrategy(); // Future extension

// Polymorphic behavior
Elevator selected = strategy.selectElevator(elevators, floor, direction);
```

### 4. **Abstraction** 🎨
- **Interface abstractions** hide implementation details
- **Service layer abstractions**

```java
// Client code doesn't need to know internal implementation
ElevatorService service = new ElevatorService();
service.request(3, 7); // Abstract operation - handles all complexity internally
```

---

## ⚖️ SOLID Principles

### 1. **Single Responsibility Principle (SRP)** ✅
Each class has **one reason to change**:

- **`Elevator`**: Manages elevator state and movement only
- **`Building`**: Represents building structure only  
- **`ElevatorManager`**: Manages elevator collection only
- **`ElevatorService`**: Handles business logic only
- **`SchedulingStrategy`**: Selects elevators only

### 2. **Open/Closed Principle (OCP)** ✅
**Open for extension, closed for modification**:

```java
// Adding new scheduling strategy without modifying existing code
public class EvenOddStrategy implements SchedulingStrategy {
    @Override
    public Elevator selectElevator(List<Elevator> availableElevators, int requestFloor, Direction direction) {
        // Even floors use even-numbered elevators, odd floors use odd-numbered elevators
        return requestFloor % 2 == 0 ? findEvenElevator(availableElevators) : findOddElevator(availableElevators);
    }
}
```

### 3. **Liskov Substitution Principle (LSP)** ✅
**Subtypes must be substitutable**:

```java
// Any SchedulingStrategy implementation can replace another
SchedulingStrategy strategy = new NearestElevatorStrategy();
// Can be replaced with any other implementation without breaking code
strategy = new EvenOddStrategy();
```

### 4. **Interface Segregation Principle (ISP)** ✅
**Clients shouldn't depend on unused interfaces**:

```java
// Specific interfaces for specific needs
public interface SchedulingStrategy {
    Elevator selectElevator(List<Elevator> availableElevators, int requestFloor, Direction direction);
    // Only scheduling-related methods
}
```

### 5. **Dependency Inversion Principle (DIP)** ✅
**Depend on abstractions, not concretions**:

```java
public class ElevatorService {
    private final ElevatorManager elevatorManager;     // Abstraction
    
    // Could easily inject different scheduling strategies
    private SchedulingStrategy schedulingStrategy = new NearestElevatorStrategy();
}
```

---

## 📁 Code Structure

```
src/
├── enums/
│   ├── ElevatorStatus.java        # Elevator states (MOVING, NOT_MOVING, etc.)
│   └── Direction.java             # Movement directions (UP, DOWN, IDLE)
├── models/
│   ├── Elevator.java              # Elevator entity with capacity and movement
│   └── Building.java              # Building entity with elevators and floors
├── services/
│   └── ElevatorService.java       # Business logic for elevator operations
├── strategies/
│   ├── SchedulingStrategy.java        # Strategy interface
│   └── NearestElevatorStrategy.java   # Nearest elevator implementation
├── factories/
│   ├── ElevatorFactory.java           # Elevator creation factory
│   └── BuildingFactory.java           # Building creation factory
├── managers/
│   └── ElevatorManager.java           # Singleton elevator manager
└── Main.java                          # Application entry point with demo
```

---

## 🚀 Key Features

### ✨ **Multi-Elevator Support**
- **Multiple elevators**: Support for any number of elevators
- **Independent operation**: Each elevator operates independently
- **Load balancing**: Intelligent distribution of requests

### ✨ **Intelligent Scheduling**
- **Nearest elevator**: Selects closest available elevator
- **Direction preference**: Prefers elevators moving in same direction
- **Availability checking**: Only assigns to available elevators

### ✨ **Real-time Capacity Management**
- **Capacity tracking**: Real-time current vs maximum capacity
- **Validation**: Prevents overloading
- **Future extension**: Ready for passenger count validation

### ✨ **Thread-Safe Operations**
- **Synchronized methods**: Prevents race conditions
- **ConcurrentHashMap**: Thread-safe elevator storage
- **Atomic operations**: Safe concurrent access

### ✨ **Realistic Movement Simulation**
- **Floor-by-floor movement**: Simulates real elevator movement
- **Timing simulation**: Includes movement delays
- **Status updates**: Real-time floor position tracking

---

## 🎤 Interview Questions & Answers

### **Q1: How would you handle multiple requests for the same elevator?**
**A**: Implement **request queuing** using:
```java
public class Elevator {
    private PriorityQueue<Integer> upRequests = new PriorityQueue<>();
    private PriorityQueue<Integer> downRequests = new PriorityQueue<>(Collections.reverseOrder());
    
    public void addRequest(int floor, Direction direction) {
        if (direction == Direction.UP) {
            upRequests.offer(floor);
        } else {
            downRequests.offer(floor);
        }
    }
}
```

### **Q2: How would you optimize elevator efficiency?**
**A**: Implement **SCAN algorithm** (elevator algorithm):
- Continue in current direction until no more requests
- Then reverse direction
- Minimizes total travel time

### **Q3: How would you handle emergency situations?**
**A**: Add **emergency mode**:
```java
public enum ElevatorStatus {
    MOVING, NOT_MOVING, MAINTENANCE, OUT_OF_SERVICE, EMERGENCY
}

public void handleEmergency() {
    status = ElevatorStatus.EMERGENCY;
    // Move to nearest floor and open doors
    // Clear all pending requests
}
```

### **Q4: How would you scale this for multiple buildings?**
**A**: Extend with **Building Manager**:
```java
public class BuildingManager {
    private Map<String, Building> buildings = new ConcurrentHashMap<>();
    
    public void handleRequest(String buildingId, int fromFloor, int toFloor) {
        Building building = buildings.get(buildingId);
        building.getElevatorService().request(fromFloor, toFloor);
    }
}
```

### **Q5: How would you implement different elevator types?**
**A**: Use **Strategy Pattern** for elevator behavior:
```java
public interface ElevatorBehavior {
    void move(Elevator elevator, int targetFloor);
}

public class ExpressElevatorBehavior implements ElevatorBehavior {
    // Skips floors, only stops at designated floors
}

public class LocalElevatorBehavior implements ElevatorBehavior {
    // Stops at every floor
}
```

### **Q6: How would you handle power failures?**
**A**: Implement **state persistence**:
```java
public class ElevatorStateManager {
    public void saveState(Elevator elevator) {
        // Save current floor, direction, pending requests to persistent storage
    }
    
    public void restoreState(Elevator elevator) {
        // Restore state after power restoration
    }
}
```

---

## 🏃‍♂️ How to Run

### **Prerequisites**
- Java 8 or higher
- Any IDE (IntelliJ IDEA, Eclipse, VS Code)

### **Steps**
1. **Navigate** to the ElevatorControlSystem directory
2. **Compile** all Java files:
   ```bash
   javac -d out src/*.java src/models/*.java src/enums/*.java src/factories/*.java src/managers/*.java src/services/*.java src/strategies/*.java
   ```
3. **Run** the main class:
   ```bash
   java -cp out Main
   ```

### **Expected Output**
```
=== Elevator Control System - Starting ===
Building created with 10 floors and 3 elevators

=== Elevator Status ===
Elevator{id='5e01a629-e3ad-4d9e-ab7d-d1a3fa45a32b', status=NOT_MOVING, direction=IDLE, maximumCapacity=8, currentCapacity=0, currentFloor=0}
Elevator{id='b09b041b-6d02-4b89-833b-d30e7d6be2b4', status=NOT_MOVING, direction=IDLE, maximumCapacity=8, currentCapacity=0, currentFloor=0}
Elevator{id='3c86da5f-fb68-482a-995d-73e23de298c8', status=NOT_MOVING, direction=IDLE, maximumCapacity=8, currentCapacity=0, currentFloor=0}

--- Scenario 1: Floor 3 to Floor 7 ---
Request received: From floor 3 to floor 7 (UP)
Assigned elevator: 5e01a629-e3ad-4d9e-ab7d-d1a3fa45a32b
Moving elevator 5e01a629-e3ad-4d9e-ab7d-d1a3fa45a32b UP to floor 3
Elevator 5e01a629-e3ad-4d9e-ab7d-d1a3fa45a32b reached target floor 7
```

---

## 🔧 Extension Points

### **Easy Extensions** (15-30 minutes in interview)
1. **Add new elevator status**: Extend `ElevatorStatus` enum
2. **Add new scheduling strategy**: Implement `SchedulingStrategy`
3. **Add elevator capacity validation**: Extend service layer

### **Medium Extensions** (30-45 minutes in interview)
1. **Request queuing**: Add PriorityQueue for pending requests
2. **SCAN algorithm**: Implement efficient elevator movement
3. **Multiple buildings**: Add building management layer

### **Advanced Extensions** (45+ minutes in interview)
1. **Database integration**: Add Repository pattern with JPA/Hibernate
2. **REST API**: Add Spring Boot controllers
3. **Real-time monitoring**: Add WebSocket support for live updates
4. **Analytics**: Add performance metrics and reporting

---

## 🎯 Interview Success Tips

### **What to Highlight**
1. **Design Patterns**: Strategy, Factory, Singleton, Service Layer
2. **SOLID Principles**: Clear examples of each principle
3. **Thread Safety**: Synchronized operations and concurrent access
4. **Scalability**: Multi-elevator, multi-building support
5. **Extensibility**: How easy it is to add new features

### **Common Follow-up Questions**
- "How would you handle peak usage times?"
- "How would you implement elevator maintenance scheduling?"
- "How would you optimize for energy efficiency?"
- "How would you handle system failures and recovery?"
- "How would you implement access control (key cards, etc.)?"

### **Code Quality Points**
- **Clean code**: Meaningful names, small methods
- **Documentation**: Clear comments and JavaDoc
- **Error handling**: Proper exception handling
- **Testing**: Unit test considerations
- **Performance**: Time/space complexity awareness

---

## 📚 Key Takeaways

This Elevator Control System demonstrates:

1. **🎯 Design Patterns**: Practical implementation of 4 major patterns
2. **🧱 OOP Principles**: All 4 pillars with real examples  
3. **⚖️ SOLID Principles**: Each principle clearly demonstrated
4. **🚀 Scalability**: Easy to extend and modify
5. **🔧 Interview Ready**: Perfect for 1-hour FAANG interviews
6. **⚡ Thread Safety**: Production-ready concurrent operations

**Perfect for demonstrating system design skills in technical interviews!**