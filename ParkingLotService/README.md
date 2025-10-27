
# 🚗 Parking Lot System - Complete Interview Guide

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

This is a **comprehensive Parking Lot Management System** implemented in Java, demonstrating multiple design patterns, OOP principles, and best practices commonly asked in technical interviews.

### Core Functionality
- **Vehicle Parking**: Park different types of vehicles (Bike, Car, Truck)
- **Slot Management**: Manage parking slots of different sizes (Small, Medium, Large)
- **Fee Calculation**: Calculate parking fees based on duration and vehicle type
- **Real-time Monitoring**: Track available slots and parking status
- **Multi-floor Support**: Support for multiple floors in a parking lot

---

## 🎯 Architecture & Design Patterns

### 1. **Strategy Pattern** 🎯
**Location**: [`src/strategies/`](src/strategies/)

**Purpose**: Encapsulates fee calculation algorithms and makes them interchangeable.

```java
// Interface
public interface FeeCalculationStrategy {
    double calculateFee(ParkingTicket ticket);
}

// Concrete Implementation
public class HourlyFeeCalculationStrategy implements FeeCalculationStrategy {
    // Different rates for different vehicle types
    private static final double BIKE_RATE_PER_HOUR = 10.0;
    private static final double CAR_RATE_PER_HOUR = 20.0;
    private static final double TRUCK_RATE_PER_HOUR = 40.0;
}
```

**Interview Benefit**: Shows understanding of **Open/Closed Principle** and algorithm flexibility.

### 2. **Factory Pattern** 🏭
**Location**: [`src/factories/ParkingTicketFactory.java`](src/factories/ParkingTicketFactory.java)

**Purpose**: Centralizes object creation and provides consistent ticket ID generation.

```java
public class ParkingTicketFactory {
    public static ParkingTicket createTicket(Vehicle vehicle, ParkingSlot slot) {
        String ticketId = generateTicketId();
        return new ParkingTicket(ticketId, vehicle, slot);
    }
    
    private static String generateTicketId() {
        return "TICKET-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
```

**Interview Benefit**: Demonstrates **creational patterns** and **separation of concerns**.

### 3. **Observer Pattern** 👁️
**Location**: [`src/observers/`](src/observers/)

**Purpose**: Implements event-driven notifications for parking events.

```java
// Observer Interface
public interface ParkingEventObserver {
    void onVehicleParked(ParkingTicket ticket);
    void onVehicleUnparked(ParkingTicket ticket, double fee);
    void onParkingFull();
    void onSlotAvailable();
}

// Concrete Observer
public class ConsoleNotificationObserver implements ParkingEventObserver {
    // Handles all parking events with console output
}
```

**Interview Benefit**: Shows **loose coupling** and **event-driven architecture**.

### 4. **Singleton Pattern** 🔒
**Location**: [`src/managers/ParkingLotManager.java`](src/managers/ParkingLotManager.java)

**Purpose**: Manages multiple parking lots with a single point of access.

```java
public class ParkingLotManager {
    private static ParkingLotManager instance;
    
    public static synchronized ParkingLotManager getInstance() {
        if (instance == null) {
            instance = new ParkingLotManager();
        }
        return instance;
    }
}
```

**Interview Benefit**: Demonstrates **thread-safety** and **global state management**.

---

## 🧱 OOP Principles Implementation

### 1. **Encapsulation** 🔐
- **Private fields** with **public getters/setters**
- **Data validation** in constructors
- **Internal state protection**

```java
public class ParkingSlot {
    private final int slotNumber;           // Immutable
    private final SlotSize size;            // Immutable
    private boolean isAvailable;            // Controlled access
    private Vehicle parkedVehicle;          // Controlled access
    
    // Public methods control access to private data
    public void park(Vehicle vehicle) { /* validation logic */ }
    public void unpark() { /* cleanup logic */ }
}
```

### 2. **Inheritance** 🌳
- **Interface inheritance** for contracts
- **Composition over inheritance** approach

```java
// Interface defines contract
public interface ParkingLotService {
    ParkingTicket parkVehicle(Vehicle vehicle);
    double unparkVehicle(String ticketId);
    void showAvailableSlots();
}

// Implementation provides behavior
public class ParkingLotServiceImpl implements ParkingLotService {
    // Concrete implementation
}
```

### 3. **Polymorphism** 🎭
- **Interface-based polymorphism**
- **Strategy pattern implementation**

```java
// Same interface, different implementations
FeeCalculationStrategy strategy1 = new HourlyFeeCalculationStrategy();
FeeCalculationStrategy strategy2 = new FlatRateFeeCalculationStrategy(); // Future extension

// Polymorphic behavior
double fee = strategy.calculateFee(ticket); // Calls appropriate implementation
```

### 4. **Abstraction** 🎨
- **Interface abstractions** hide implementation details
- **Service layer abstractions**

```java
// Client code doesn't need to know internal implementation
ParkingLotService service = new ParkingLotServiceImpl(parkingLot);
ParkingTicket ticket = service.parkVehicle(vehicle); // Abstract operation
```

---

## ⚖️ SOLID Principles

### 1. **Single Responsibility Principle (SRP)** ✅
Each class has **one reason to change**:

- **`ParkingSlot`**: Manages slot state only
- **`ParkingDisplayService`**: Handles display logic only  
- **`FeeCalculationStrategy`**: Calculates fees only
- **`ParkingTicketFactory`**: Creates tickets only

### 2. **Open/Closed Principle (OCP)** ✅
**Open for extension, closed for modification**:

```java
// Adding new fee calculation without modifying existing code
public class WeekendFeeCalculationStrategy implements FeeCalculationStrategy {
    @Override
    public double calculateFee(ParkingTicket ticket) {
        // Weekend pricing logic
    }
}
```

### 3. **Liskov Substitution Principle (LSP)** ✅
**Subtypes must be substitutable**:

```java
// Any FeeCalculationStrategy implementation can replace another
FeeCalculationStrategy strategy = new HourlyFeeCalculationStrategy();
// Can be replaced with any other implementation without breaking code
strategy = new WeekendFeeCalculationStrategy();
```

### 4. **Interface Segregation Principle (ISP)** ✅
**Clients shouldn't depend on unused interfaces**:

```java
// Specific interfaces for specific needs
public interface ParkingEventObserver {
    void onVehicleParked(ParkingTicket ticket);
    void onVehicleUnparked(ParkingTicket ticket, double fee);
    // Only parking-related events
}
```

### 5. **Dependency Inversion Principle (DIP)** ✅
**Depend on abstractions, not concretions**:

```java
public class ParkingLotServiceImpl {
    private final FeeCalculationStrategy feeCalculationStrategy; // Abstraction
    private final List<ParkingEventObserver> observers;          // Abstraction
    
    // Depends on interfaces, not concrete classes
}
```

---

## 📁 Code Structure

```
src/
├── enums/
│   ├── SlotSize.java           # Parking slot sizes
│   └── VehicleType.java        # Vehicle types
├── models/
│   ├── Floor.java              # Parking floor representation
│   ├── ParkingLot.java         # Main parking lot entity
│   ├── ParkingSlot.java        # Individual parking slot
│   ├── ParkingTicket.java      # Parking ticket entity
│   └── Vehicle.java            # Vehicle representation
├── services/
│   ├── ParkingLotService.java      # Service interface
│   ├── ParkingLotServiceImpl.java  # Service implementation
│   └── ParkingDisplayService.java  # Display service
├── strategies/
│   ├── FeeCalculationStrategy.java        # Strategy interface
│   └── HourlyFeeCalculationStrategy.java  # Hourly fee implementation
├── factories/
│   └── ParkingTicketFactory.java   # Ticket creation factory
├── observers/
│   ├── ParkingEventObserver.java           # Observer interface
│   └── ConsoleNotificationObserver.java   # Console notification implementation
├── managers/
│   └── ParkingLotManager.java      # Singleton parking lot manager
└── Main.java                       # Application entry point
```

---

## 🚀 Key Features

### ✨ **Multi-Vehicle Support**
- **Bikes**: Can park in any slot size
- **Cars**: Require Medium or Large slots
- **Trucks**: Require Large slots only

### ✨ **Dynamic Fee Calculation**
- **Hourly rates**: Different rates per vehicle type
- **Minimum 1-hour charge**
- **Extensible**: Easy to add new pricing strategies

### ✨ **Real-time Notifications**
- **Parking events**: Vehicle parked/unparked notifications
- **Status updates**: Parking full/available notifications
- **Observer pattern**: Loose coupling for notifications

### ✨ **Multi-floor Architecture**
- **Scalable design**: Support for multiple floors
- **Floor-wise management**: Individual floor status tracking
- **Efficient slot allocation**: First-available slot assignment

---

## 🎤 Interview Questions & Answers

### **Q1: How would you handle concurrent access to parking slots?**
**A**: Implement **thread-safety** using:
```java
public synchronized boolean parkVehicle(Vehicle vehicle) {
    // Atomic operation for slot allocation
    // Use ReentrantLock for fine-grained control
    // Database-level locking for distributed systems
}
```

### **Q2: How would you scale this system for multiple parking lots?**
**A**: Use **ParkingLotManager** (Singleton pattern):
- Centralized management of multiple parking lots
- Load balancing across parking lots
- Microservices architecture for distributed deployment

### **Q3: How would you add new vehicle types?**
**A**: **Open/Closed Principle** implementation:
1. Add new enum value in `VehicleType`
2. Update `canFitVehicle()` logic in `ParkingSlot`
3. Add new rate in `HourlyFeeCalculationStrategy`
4. **No modification** of existing classes required

### **Q4: How would you implement different pricing strategies?**
**A**: **Strategy Pattern** allows easy extension:
```java
public class PeakHourFeeCalculationStrategy implements FeeCalculationStrategy {
    @Override
    public double calculateFee(ParkingTicket ticket) {
        // Peak hour pricing logic (1.5x normal rate)
        double baseFee = new HourlyFeeCalculationStrategy().calculateFee(ticket);
        return baseFee * 1.5;
    }
}
```

### **Q5: How would you handle payment processing?**
**A**: Add **Payment Strategy Pattern**:
```java
public interface PaymentStrategy {
    boolean processPayment(double amount, PaymentDetails details);
}

public class CreditCardPaymentStrategy implements PaymentStrategy { }
public class DigitalWalletPaymentStrategy implements PaymentStrategy { }
```

### **Q6: How would you implement reservation system?**
**A**: Extend with **State Pattern**:
```java
public enum SlotState {
    AVAILABLE, RESERVED, OCCUPIED
}

public class ParkingSlot {
    private SlotState state;
    private LocalDateTime reservationExpiry;
    // Reservation logic
}
```

### **Q7: How would you optimize slot allocation?**
**A**: Implement **allocation strategies**:
- **Nearest to entrance**: Minimize walking distance
- **Size optimization**: Prefer exact size match
- **Load balancing**: Distribute across floors

### **Q8: How would you handle database persistence?**
**A**: Add **Repository Pattern**:
```java
public interface ParkingTicketRepository {
    void save(ParkingTicket ticket);
    ParkingTicket findById(String ticketId);
    List<ParkingTicket> findActiveTickets();
}
```

---

## 🏃‍♂️ How to Run

### **Prerequisites**
- Java 8 or higher
- Any IDE (IntelliJ IDEA, Eclipse, VS Code)

### **Steps**
1. **Clone/Download** the project
2. **Compile** all Java files:
   ```bash
   javac -d out src/**/*.java
   ```
3. **Run** the main class:
   ```bash
   java -cp out Main
   ```

### **Expected Output**
```
✅ Vehicle KA-01-BIKE parked successfully. Ticket ID: TICKET-ABC12345
📍 Location: Floor 0, Slot 1

==================================================
🏢 Parking Lot: MyLot
📈 Status Summary:
  Total Slots: 5
  Available: 2
  Occupied: 3
  Occupancy Rate: 60.0%
==================================================

🚗 Vehicle KA-01-BIKE unparked successfully.
💰 Parking fee: ₹10.00
⏰ Duration: 1 hour(s)
```

---

## 🔧 Extension Points

### **Easy Extensions** (15-30 minutes in interview)
1. **Add new vehicle type**: Extend `VehicleType` enum
2. **Add new pricing strategy**: Implement `FeeCalculationStrategy`
3. **Add new notification method**: Implement `ParkingEventObserver`

### **Medium Extensions** (30-45 minutes in interview)
1. **Reservation system**: Add reservation state and time management
2. **Payment integration**: Add payment processing with Strategy pattern
3. **Multi-location support**: Extend ParkingLotManager

### **Advanced Extensions** (45+ minutes in interview)
1. **Database integration**: Add Repository pattern with JPA/Hibernate
2. **REST API**: Add Spring Boot controllers
3. **Real-time updates**: Add WebSocket support
4. **Analytics**: Add reporting and analytics features

---

## 🎯 Interview Success Tips

### **What to Highlight**
1. **Design Patterns**: Strategy, Factory, Observer, Singleton
2. **SOLID Principles**: Clear examples of each principle
3. **Extensibility**: How easy it is to add new features
4. **Thread Safety**: Concurrent access considerations
5. **Scalability**: Multi-parking lot support

### **Common Follow-up Questions**
- "How would you handle peak hours?"
- "How would you implement dynamic pricing?"
- "How would you scale to 1000+ parking lots?"
- "How would you handle system failures?"
- "How would you implement analytics?"

### **Code Quality Points**
- **Clean code**: Meaningful names, small methods
- **Documentation**: Clear comments and JavaDoc
- **Error handling**: Proper exception handling
- **Testing**: Unit test considerations
- **Performance**: Time/space complexity awareness

---

## 📚 Key Takeaways

This parking lot system demonstrates:

1. **🎯 Design Patterns**: Practical implementation of 4 major patterns
2. **🧱 OOP Principles**: All 4 pillars with real examples  
3. **⚖️ SOLID Principles**: Each principle clearly demonstrated
4. **🚀 Scalability**: Easy to extend and modify
5. **🔧