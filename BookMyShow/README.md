# 🎬 BookMyShow System - Complete Interview Guide

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

This is a **comprehensive BookMyShow Movie Booking System** implemented in Java, demonstrating multiple design patterns, OOP principles, and best practices commonly asked in FAANG technical interviews.

### Core Functionality
- **Movie Management**: Display available movies with details
- **Theater & Screen Management**: Multi-theater, multi-screen support
- **Show Scheduling**: Time-based show management
- **Seat Booking**: Real-time seat availability and booking
- **Pricing Strategy**: Dynamic pricing based on seat types
- **Booking Management**: Create, confirm, cancel bookings
- **Real-time Notifications**: Observer pattern for booking events

---

## 🎯 Architecture & Design Patterns

### 1. **Strategy Pattern** 🎯
**Location**: [`src/strategies/`](src/strategies/)

**Purpose**: Encapsulates pricing algorithms and makes them interchangeable.

```java
// Interface
public interface PricingStrategy {
    double calculatePrice(Show show, List<Seat> seats);
}

// Concrete Implementation
public class StandardPricingStrategy implements PricingStrategy {
    private static final double REGULAR_MULTIPLIER = 1.0;
    private static final double PREMIUM_MULTIPLIER = 1.5;
    private static final double VIP_MULTIPLIER = 2.0;
}
```

**Interview Benefit**: Shows understanding of **Open/Closed Principle** and algorithm flexibility.

### 2. **Factory Pattern** 🏭
**Location**: [`src/factories/BookingFactory.java`](src/factories/BookingFactory.java)

**Purpose**: Centralizes booking creation and provides consistent ID generation.

```java
public class BookingFactory {
    public static Booking createBooking(String userId, Show show, List<Seat> seats, double totalAmount) {
        String bookingId = generateBookingId();
        return new Booking(bookingId, userId, show, seats, totalAmount);
    }
    
    private static String generateBookingId() {
        return "BMS-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
```

**Interview Benefit**: Demonstrates **creational patterns** and **separation of concerns**.

### 3. **Observer Pattern** 👁️
**Location**: [`src/observers/`](src/observers/)

**Purpose**: Implements event-driven notifications for booking events.

```java
// Observer Interface
public interface BookingEventObserver {
    void onBookingCreated(Booking booking);
    void onBookingConfirmed(Booking booking);
    void onBookingCancelled(Booking booking);
    void onShowFullyBooked(Show show);
    void onSeatsAvailable(Show show);
}

// Concrete Observer
public class ConsoleNotificationObserver implements BookingEventObserver {
    // Handles all booking events with console output
}
```

**Interview Benefit**: Shows **loose coupling** and **event-driven architecture**.

### 4. **Singleton Pattern** 🔒
**Location**: [`src/managers/TheaterManager.java`](src/managers/TheaterManager.java)

**Purpose**: Manages multiple theaters with a single point of access.

```java
public class TheaterManager {
    private static TheaterManager instance;
    
    public static synchronized TheaterManager getInstance() {
        if (instance == null) {
            instance = new TheaterManager();
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
public class Seat {
    private final String seatId;           // Immutable
    private final SeatType seatType;       // Immutable
    private boolean isAvailable;           // Controlled access
    private boolean isBooked;              // Controlled access
    
    // Public methods control access to private data
    public void book() { /* validation logic */ }
    public void release() { /* cleanup logic */ }
}
```

### 2. **Inheritance** 🌳
- **Interface inheritance** for contracts
- **Composition over inheritance** approach

```java
// Interface defines contract
public interface BookMyShowService {
    Booking createBooking(String userId, String showId, List<String> seatIds);
    boolean confirmBooking(String bookingId);
    void displayMovies();
}

// Implementation provides behavior
public class BookMyShowServiceImpl implements BookMyShowService {
    // Concrete implementation
}
```

### 3. **Polymorphism** 🎭
- **Interface-based polymorphism**
- **Strategy pattern implementation**

```java
// Same interface, different implementations
PricingStrategy strategy1 = new StandardPricingStrategy();
PricingStrategy strategy2 = new WeekendPricingStrategy(); // Future extension

// Polymorphic behavior
double price = strategy.calculatePrice(show, seats); // Calls appropriate implementation
```

### 4. **Abstraction** 🎨
- **Interface abstractions** hide implementation details
- **Service layer abstractions**

```java
// Client code doesn't need to know internal implementation
BookMyShowService service = new BookMyShowServiceImpl();
Booking booking = service.createBooking(userId, showId, seatIds); // Abstract operation
```

---

## ⚖️ SOLID Principles

### 1. **Single Responsibility Principle (SRP)** ✅
Each class has **one reason to change**:

- **`Seat`**: Manages seat state only
- **`Movie`**: Represents movie information only  
- **`PricingStrategy`**: Calculates prices only
- **`BookingFactory`**: Creates bookings only

### 2. **Open/Closed Principle (OCP)** ✅
**Open for extension, closed for modification**:

```java
// Adding new pricing strategy without modifying existing code
public class WeekendPricingStrategy implements PricingStrategy {
    @Override
    public double calculatePrice(Show show, List<Seat> seats) {
        // Weekend pricing logic (1.5x normal rate)
        double basePrice = new StandardPricingStrategy().calculatePrice(show, seats);
        return basePrice * 1.5;
    }
}
```

### 3. **Liskov Substitution Principle (LSP)** ✅
**Subtypes must be substitutable**:

```java
// Any PricingStrategy implementation can replace another
PricingStrategy strategy = new StandardPricingStrategy();
// Can be replaced with any other implementation without breaking code
strategy = new WeekendPricingStrategy();
```

### 4. **Interface Segregation Principle (ISP)** ✅
**Clients shouldn't depend on unused interfaces**:

```java
// Specific interfaces for specific needs
public interface BookingEventObserver {
    void onBookingCreated(Booking booking);
    void onBookingConfirmed(Booking booking);
    // Only booking-related events
}
```

### 5. **Dependency Inversion Principle (DIP)** ✅
**Depend on abstractions, not concretions**:

```java
public class BookMyShowServiceImpl {
    private final PricingStrategy pricingStrategy;          // Abstraction
    private final List<BookingEventObserver> observers;     // Abstraction
    
    // Depends on interfaces, not concrete classes
}
```

---

## 📁 Code Structure

```
src/
├── enums/
│   ├── SeatType.java              # Seat types (Regular, Premium, VIP)
│   ├── BookingStatus.java         # Booking statuses
│   └── MovieGenre.java            # Movie genres
├── models/
│   ├── Movie.java                 # Movie entity
│   ├── Theater.java               # Theater entity
│   ├── Screen.java                # Screen entity with seat management
│   ├── Seat.java                  # Individual seat
│   ├── Show.java                  # Movie show with timing
│   └── Booking.java               # Booking entity
├── services/
│   ├── BookMyShowService.java         # Service interface
│   └── BookMyShowServiceImpl.java     # Service implementation
├── strategies/
│   ├── PricingStrategy.java           # Strategy interface
│   └── StandardPricingStrategy.java   # Standard pricing implementation
├── factories/
│   └── BookingFactory.java            # Booking creation factory
├── observers/
│   ├── BookingEventObserver.java          # Observer interface
│   └── ConsoleNotificationObserver.java   # Console notification implementation
├── managers/
│   └── TheaterManager.java            # Singleton theater manager
└── Main.java                           # Application entry point
```

---

## 🚀 Key Features

### ✨ **Multi-Theater Support**
- **Multiple theaters**: Support for different theater chains
- **Multiple screens**: Each theater can have multiple screens
- **Flexible seating**: Different seat layouts per screen

### ✨ **Dynamic Pricing**
- **Seat-based pricing**: Different rates per seat type (Regular, Premium, VIP)
- **Extensible**: Easy to add new pricing strategies
- **Strategy pattern**: Clean separation of pricing logic

### ✨ **Real-time Booking Management**
- **Seat availability**: Real-time seat status tracking
- **Booking lifecycle**: Create → Confirm → Cancel flow
- **Validation**: Prevents double booking and invalid operations

### ✨ **Event-Driven Notifications**
- **Observer pattern**: Loose coupling for notifications
- **Multiple events**: Booking created, confirmed, cancelled, show full
- **Extensible**: Easy to add new notification channels

---

## 🎤 Interview Questions & Answers

### **Q1: How would you handle concurrent booking requests for the same seat?**
**A**: Implement **thread-safety** using:
```java
public synchronized boolean bookSeat(String seatId) {
    // Atomic operation for seat booking
    // Use ReentrantLock for fine-grained control
    // Database-level locking for distributed systems
}
```

### **Q2: How would you scale this system for multiple cities?**
**A**: Use **TheaterManager** (Singleton pattern):
- Extend to support city-wise theater management
- Microservices architecture for distributed deployment
- Database partitioning by city/region

### **Q3: How would you add new seat types?**
**A**: **Open/Closed Principle** implementation:
1. Add new enum value in `SeatType`
2. Update pricing logic in `StandardPricingStrategy`
3. **No modification** of existing classes required

### **Q4: How would you implement different pricing strategies?**
**A**: **Strategy Pattern** allows easy extension:
```java
public class HolidayPricingStrategy implements PricingStrategy {
    @Override
    public double calculatePrice(Show show, List<Seat> seats) {
        // Holiday pricing logic (2x normal rate)
        double basePrice = new StandardPricingStrategy().calculatePrice(show, seats);
        return basePrice * 2.0;
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

### **Q6: How would you implement seat reservation with timeout?**
**A**: Extend with **State Pattern**:
```java
public enum SeatState {
    AVAILABLE, RESERVED, BOOKED
}

public class Seat {
    private SeatState state;
    private LocalDateTime reservationExpiry;
    // Reservation logic with timeout
}
```

---

## 🏃‍♂️ How to Run

### **Prerequisites**
- Java 8 or higher
- Any IDE (IntelliJ IDEA, Eclipse, VS Code)

### **Steps**
1. **Navigate** to the BookMyShow directory
2. **Compile** all Java files:
   ```bash
   javac -encoding UTF-8 -d out src\enums\*.java src\models\*.java src\strategies\*.java src\factories\*.java src\observers\*.java src\managers\*.java src\services\*.java src\Main.java
   ```
3. **Run** the main class:
   ```bash
   java -cp out Main
   ```

### **Expected Output**
```
[DEMO] Welcome to BookMyShow System Demo!
=============================================================

[STEP 1] Display Available Movies
[MOVIES] Available Movies:
===================================================
[MOVIE] Avengers: Endgame (ACTION)
    Duration: 180 mins | Rating: 8.4
    Epic superhero finale

[STEP 4] Create Bookings
[SCENARIO 1] Successful Booking
[TICKET] Booking created successfully!
   Booking ID: BMS-598A2108
   Movie: Avengers: Endgame
   Seats: 2
   Total Amount: ₹750.00
   Status: PENDING
[SUCCESS] Booking confirmed successfully!
```

---

## 🔧 Extension Points

### **Easy Extensions** (15-30 minutes in interview)
1. **Add new seat type**: Extend `SeatType` enum
2. **Add new pricing strategy**: Implement `PricingStrategy`
3. **Add new notification method**: Implement `BookingEventObserver`

### **Medium Extensions** (30-45 minutes in interview)
1. **User management**: Add User entity and authentication
2. **Payment integration**: Add payment processing with Strategy pattern
3. **Show scheduling**: Add complex scheduling with time conflicts

### **Advanced Extensions** (45+ minutes in interview)
1. **Database integration**: Add Repository pattern with JPA/Hibernate
2. **REST API**: Add Spring Boot controllers
3. **Real-time updates**: Add WebSocket support for live seat updates
4. **Analytics**: Add reporting and analytics features

---

## 🎯 Interview Success Tips

### **What to Highlight**
1. **Design Patterns**: Strategy, Factory, Observer, Singleton
2. **SOLID Principles**: Clear examples of each principle
3. **Extensibility**: How easy it is to add new features
4. **Thread Safety**: Concurrent booking considerations
5. **Scalability**: Multi-theater, multi-city support

### **Common Follow-up Questions**
- "How would you handle peak booking times?"
- "How would you implement dynamic pricing?"
- "How would you scale to 1000+ theaters?"
- "How would you handle system failures?"
- "How would you implement recommendation system?"

### **Code Quality Points**
- **Clean code**: Meaningful names, small methods
- **Documentation**: Clear comments and JavaDoc
- **Error handling**: Proper exception handling
- **Testing**: Unit test considerations
- **Performance**: Time/space complexity awareness

---

## 📚 Key Takeaways

This BookMyShow system demonstrates:

1. **🎯 Design Patterns**: Practical implementation of 4 major patterns
2. **🧱 OOP Principles**: All 4 pillars with real examples  
3. **⚖️ SOLID Principles**: Each principle clearly demonstrated
4. **🚀 Scalability**: Easy to extend and modify
5. **🔧 Interview Ready**: Perfect for 1-hour FAANG interviews

**Perfect for demonstrating system design skills in technical interviews!**