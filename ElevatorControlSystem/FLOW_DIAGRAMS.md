# 🔄 Elevator Control System - Flow Diagrams

## 📋 Table of Contents
1. [System Architecture Overview](#system-architecture-overview)
2. [Request Processing Flow](#request-processing-flow)
3. [Elevator Movement Flow](#elevator-movement-flow)
4. [Design Patterns Interaction](#design-patterns-interaction)
5. [Thread Safety Flow](#thread-safety-flow)
6. [Extension Points Flow](#extension-points-flow)

---

## 🏗️ System Architecture Overview

```mermaid
graph TB
    subgraph "Presentation Layer"
        Main[Main.java - Demo]
    end
    
    subgraph "Service Layer"
        ES[ElevatorService]
        ES --> |uses| EM[ElevatorManager - Singleton]
        ES --> |uses| SS[SchedulingStrategy]
    end
    
    subgraph "Factory Layer"
        EF[ElevatorFactory]
        BF[BuildingFactory]
    end
    
    subgraph "Domain Models"
        E[Elevator]
        B[Building]
        EST[ElevatorStatus Enum]
        D[Direction Enum]
    end
    
    subgraph "Strategy Implementations"
        NES[NearestElevatorStrategy]
        SS --> NES
    end
    
    Main --> ES
    ES --> E
    EM --> E
    BF --> EF
    BF --> B
    B --> E
    E --> EST
    E --> D
    
    style ES fill:#e1f5fe
    style EM fill:#f3e5f5
    style SS fill:#e8f5e8
    style E fill:#fff3e0
```

---

## 🎯 Request Processing Flow

### **External Request Flow (People Outside Elevator)**

```mermaid
sequenceDiagram
    participant User
    participant ElevatorService
    participant SchedulingStrategy
    participant ElevatorManager
    participant Elevator
    
    User->>ElevatorService: request(fromFloor=3, toFloor=7)
    ElevatorService->>ElevatorService: Calculate direction (UP)
    ElevatorService->>ElevatorManager: getAvailableElevators()
    ElevatorManager->>ElevatorService: List<Elevator>
    ElevatorService->>SchedulingStrategy: selectElevator(elevators, floor=3, direction=UP)
    SchedulingStrategy->>SchedulingStrategy: Find nearest available elevator
    SchedulingStrategy->>ElevatorService: bestElevator
    
    ElevatorService->>ElevatorService: move(UP, 3, elevatorId) [Pickup]
    ElevatorService->>Elevator: setDirection(UP)
    ElevatorService->>Elevator: move() [Floor by floor to 3]
    ElevatorService->>ElevatorService: move(UP, 7, elevatorId) [Destination]
    ElevatorService->>Elevator: move() [Floor by floor to 7]
    ElevatorService->>Elevator: setDirection(IDLE)
    
    ElevatorService->>User: Request completed
```

### **Internal Request Flow (People Inside Elevator)**

```mermaid
sequenceDiagram
    participant User
    participant ElevatorService
    participant ElevatorManager
    participant Elevator
    
    User->>ElevatorService: move(direction=UP, targetFloor=9, elevatorId="E1")
    ElevatorService->>ElevatorManager: getElevator("E1")
    ElevatorManager->>ElevatorService: elevator
    
    loop Until target floor reached
        ElevatorService->>Elevator: setDirection(UP)
        ElevatorService->>Elevator: move()
        ElevatorService->>Elevator: getCurrentFloor()
        ElevatorService->>ElevatorService: Display current floor
        ElevatorService->>ElevatorService: Sleep(1000ms) [Simulate movement]
    end
    
    ElevatorService->>Elevator: setDirection(IDLE)
    ElevatorService->>Elevator: setStatus(NOT_MOVING)
    ElevatorService->>User: Movement completed
```

---

## 🚀 Elevator Movement Flow

```mermaid
flowchart TD
    Start([Start Movement]) --> CheckTarget{Current Floor == Target Floor?}
    CheckTarget -->|Yes| Stop([Stop & Set IDLE])
    CheckTarget -->|No| DetermineDirection{Target > Current?}
    
    DetermineDirection -->|Yes| SetUp[Set Direction UP]
    DetermineDirection -->|No| SetDown[Set Direction DOWN]
    
    SetUp --> MoveUp[currentFloor++]
    SetDown --> MoveDown[currentFloor--]
    
    MoveUp --> SetMoving[Set Status MOVING]
    MoveDown --> SetMoving
    
    SetMoving --> UpdateDisplay[Display Current Floor]
    UpdateDisplay --> Sleep[Sleep 1000ms]
    Sleep --> CheckTarget
    
    Stop --> Complete([Movement Complete])
    
    style Start fill:#e8f5e8
    style Stop fill:#ffebee
    style Complete fill:#e1f5fe
```

---

## 🎨 Design Patterns Interaction

```mermaid
graph LR
    subgraph "Factory Pattern"
        EF[ElevatorFactory]
        BF[BuildingFactory]
        EF --> |creates| E[Elevator]
        BF --> |creates| B[Building]
        BF --> |uses| EF
    end
    
    subgraph "Singleton Pattern"
        EM[ElevatorManager]
        EM --> |manages| E
    end
    
    subgraph "Strategy Pattern"
        SS[SchedulingStrategy Interface]
        NES[NearestElevatorStrategy]
        SS --> NES
    end
    
    subgraph "Service Layer Pattern"
        ES[ElevatorService]
        ES --> |uses| EM
        ES --> |uses| SS
    end
    
    subgraph "Client"
        Main[Main Application]
        Main --> ES
        Main --> BF
    end
    
    style EF fill:#fff3e0
    style BF fill:#fff3e0
    style EM fill:#f3e5f5
    style SS fill:#e8f5e8
    style NES fill:#e8f5e8
    style ES fill:#e1f5fe
```

---

## 🔒 Thread Safety Flow

```mermaid
sequenceDiagram
    participant Thread1 as Request Thread 1
    participant Thread2 as Request Thread 2
    participant ElevatorService
    participant ElevatorManager
    participant ConcurrentHashMap
    
    par Concurrent Requests
        Thread1->>ElevatorService: request(3, 7) [synchronized]
        and
        Thread2->>ElevatorService: request(5, 2) [synchronized]
    end
    
    Note over ElevatorService: Only one thread can execute at a time
    
    ElevatorService->>ElevatorManager: getAvailableElevators()
    ElevatorManager->>ConcurrentHashMap: getAllValues() [Thread-safe]
    ConcurrentHashMap->>ElevatorManager: List<Elevator>
    ElevatorManager->>ElevatorService: Available elevators
    
    ElevatorService->>ElevatorService: Select best elevator
    ElevatorService->>ElevatorManager: moveElevator(elevatorId, direction)
    
    Note over ElevatorService: Thread 1 completes, Thread 2 can now execute
    
    ElevatorService->>Thread1: Request completed
    ElevatorService->>Thread2: Begin processing request
```

---

## 🔧 Extension Points Flow

### **Adding New Scheduling Strategy**

```mermaid
flowchart TD
    Start([Need New Scheduling]) --> CreateStrategy[Create New Strategy Class]
    CreateStrategy --> Implement[Implement SchedulingStrategy Interface]
    Implement --> AddLogic[Add Selection Logic]
    AddLogic --> Test[Test Implementation]
    Test --> Inject[Inject into ElevatorService]
    Inject --> Complete([Strategy Ready])
    
    style Start fill:#e8f5e8
    style Complete fill:#e1f5fe
    
    subgraph "Example: EvenOddStrategy"
        EOS[EvenOddStrategy]
        EOS --> |implements| SI[SchedulingStrategy]
        EOS --> Logic[Even floors → Even elevators<br/>Odd floors → Odd elevators]
    end
```

### **Adding Emergency Handling**

```mermaid
flowchart TD
    Emergency([Emergency Detected]) --> CheckElevators[Get All Elevators]
    CheckElevators --> SetEmergency[Set Status to EMERGENCY]
    SetEmergency --> MoveToNearest[Move to Nearest Floor]
    MoveToNearest --> OpenDoors[Open Doors]
    OpenDoors --> ClearRequests[Clear All Pending Requests]
    ClearRequests --> NotifySystem[Notify Emergency Services]
    NotifySystem --> WaitForReset[Wait for Manual Reset]
    
    style Emergency fill:#ffebee
    style NotifySystem fill:#fff3e0
    style WaitForReset fill:#f3e5f5
```

---

## 📊 System State Transitions

```mermaid
stateDiagram-v2
    [*] --> NOT_MOVING
    NOT_MOVING --> MOVING : Request received
    MOVING --> NOT_MOVING : Target reached
    NOT_MOVING --> MAINTENANCE : Scheduled maintenance
    MOVING --> EMERGENCY : Emergency detected
    MAINTENANCE --> NOT_MOVING : Maintenance complete
    EMERGENCY --> NOT_MOVING : Emergency resolved
    NOT_MOVING --> OUT_OF_SERVICE : System failure
    OUT_OF_SERVICE --> NOT_MOVING : Repair complete
    
    state MOVING {
        [*] --> UP
        [*] --> DOWN
        UP --> IDLE : Target reached
        DOWN --> IDLE : Target reached
        IDLE --> UP : New request up
        IDLE --> DOWN : New request down
    }
```

---

## 🎯 Performance Optimization Flow

```mermaid
flowchart TD
    Request([New Request]) --> CheckCache{Elevator Status Cached?}
    CheckCache -->|Yes| UseCache[Use Cached Status]
    CheckCache -->|No| QueryDB[Query Elevator Status]
    
    UseCache --> SelectStrategy[Apply Scheduling Strategy]
    QueryDB --> UpdateCache[Update Cache]
    UpdateCache --> SelectStrategy
    
    SelectStrategy --> OptimalElevator{Found Optimal Elevator?}
    OptimalElevator -->|Yes| AssignElevator[Assign Elevator]
    OptimalElevator -->|No| QueueRequest[Queue Request]
    
    AssignElevator --> UpdateMetrics[Update Performance Metrics]
    QueueRequest --> RetryLater[Retry After Delay]
    
    UpdateMetrics --> Complete([Request Processed])
    RetryLater --> Request
    
    style Request fill:#e8f5e8
    style Complete fill:#e1f5fe
    style QueueRequest fill:#fff3e0
```

---

## 🔄 Scalability Architecture

```mermaid
graph TB
    subgraph "Load Balancer"
        LB[Load Balancer]
    end
    
    subgraph "Application Layer"
        App1[ElevatorService Instance 1]
        App2[ElevatorService Instance 2]
        App3[ElevatorService Instance 3]
    end
    
    subgraph "Caching Layer"
        Redis[Redis Cache]
    end
    
    subgraph "Database Layer"
        DB[(Elevator State DB)]
        Metrics[(Metrics DB)]
    end
    
    subgraph "Message Queue"
        MQ[Request Queue]
    end
    
    LB --> App1
    LB --> App2
    LB --> App3
    
    App1 --> Redis
    App2 --> Redis
    App3 --> Redis
    
    App1 --> DB
    App2 --> DB
    App3 --> DB
    
    App1 --> MQ
    App2 --> MQ
    App3 --> MQ
    
    App1 --> Metrics
    App2 --> Metrics
    App3 --> Metrics
    
    style LB fill:#e1f5fe
    style Redis fill:#f3e5f5
    style MQ fill:#e8f5e8
```

---

## 💡 Interview Discussion Points

### **When Explaining Architecture:**
- *"The service layer provides clean separation between business logic and data management"*
- *"Singleton pattern ensures single point of control for all elevators"*
- *"Strategy pattern allows different scheduling algorithms without code changes"*

### **When Discussing Scalability:**
- *"This architecture can easily scale to multiple buildings by extending ElevatorManager"*
- *"The service layer can be converted to REST APIs for distributed deployment"*
- *"Thread safety ensures the system can handle concurrent requests safely"*

### **When Showing Extensions:**
- *"Adding new elevator types just requires implementing the strategy interface"*
- *"Emergency handling can be added without modifying existing code"*
- *"Performance monitoring can be injected at any layer"*

**These diagrams demonstrate the system's clean architecture, extensibility, and production-ready design patterns!**