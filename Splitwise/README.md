# 💰 Splitwise System - Complete Interview Guide

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

This is a **comprehensive Splitwise Expense Sharing System** implemented in Java, demonstrating multiple design patterns, OOP principles, and best practices commonly asked in FAANG technical interviews.

### Core Functionality
- **User Management**: Create and manage users
- **Group Management**: Create groups and manage members
- **Expense Management**: Add expenses with different split types
- **Settlement Calculation**: Calculate who owes whom and how much
- **Split Strategies**: Equal, Exact, Percentage-based splitting
- **Balance Tracking**: Real-time balance updates
- **Notification System**: Observer pattern for expense events

---

## 🎯 Architecture & Design Patterns

### 1. **Strategy Pattern** 🎯
**Location**: [`src/strategies/`](src/strategies/)

**Purpose**: Encapsulates different expense splitting algorithms and makes them interchangeable.

```java
// Interface
public interface SplitStrategy {
    List<Split> calculateSplits(Expense expense, List<User> participants, Map<String, Double> splitData);
}

// Concrete Implementations
public class EqualSplitStrategy implements SplitStrategy {
    // Splits expense equally among all participants
}

public class ExactSplitStrategy implements SplitStrategy {
    // Splits expense based on exact amounts specified
}

public class PercentageSplitStrategy implements SplitStrategy {
    // Splits expense based on percentage shares
}
```

**Interview Benefit**: Shows understanding of **Open/Closed Principle** and algorithm flexibility.

### 2. **Factory Pattern** 🏭
**Location**: [`src/factories/ExpenseFactory.java`](src/factories/ExpenseFactory.java)

**Purpose**: Centralizes expense creation and provides consistent ID generation.

```java
public class ExpenseFactory {
    public static Expense createExpense(String description, double amount, User paidBy, 
                                      List<User> participants, SplitType splitType, 
                                      Map<String, Double> splitData) {
        String expenseId = generateExpenseId();
        return new Expense(expenseId, description, amount, paidBy, participants, splitType, splitData);
    }
    
    private static String generateExpenseId() {
        return "EXP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
```

**Interview Benefit**: Demonstrates **creational patterns** and **separation of concerns**.

### 3. **Observer Pattern** 👁️
**Location**: [`src/observers/`](src/observers/)

**Purpose**: Implements event-driven notifications for expense events.

```java
// Observer Interface
public interface ExpenseEventObserver {
    void onExpenseAdded(Expense expense);
    void onExpenseUpdated(Expense expense);
    void onExpenseDeleted(String expenseId);
    void onSettlementMade(Settlement settlement);
}

// Concrete Observer
public class ConsoleNotificationObserver implements ExpenseEventObserver {
    // Handles all expense events with console output
}
```

**Interview Benefit**: Shows **loose coupling** and **event-driven architecture**.

### 4. **Singleton Pattern** 🔒
**Location**: [`src/managers/GroupManager.java`](src/managers/GroupManager.java)

**Purpose**: Manages multiple groups with a single point of access.

```java
public class GroupManager {
    private static GroupManager instance;
    
    public static synchronized GroupManager getInstance() {
        if (instance == null) {
            instance = new GroupManager();
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
public class User {
    private final String userId;           // Immutable
    private final String name;             // Immutable
    private final String email;            // Immutable
    private Map<String, Double> balances;  // Controlled access
    
    // Public methods control access to private data
    public void updateBalance(String friendId, double amount) { /* validation logic */ }
    public double getBalanceWith(String friendId) { /* calculation logic */ }
}
```

### 2. **Inheritance** 🌳
- **Interface inheritance** for contracts
- **Composition over inheritance** approach

```java
// Interface defines contract
public interface SplitwiseService {
    Expense addExpense(String groupId, String description, double amount, String paidById, 
                      List<String> participantIds, SplitType splitType, Map<String, Double> splitData);
    List<Balance> getBalances(String userId);
    List<Settlement> settleUp(String userId);
}

// Implementation provides behavior
public class SplitwiseServiceImpl implements SplitwiseService {
    // Concrete implementation
}
```

### 3. **Polymorphism** 🎭
- **Interface-based polymorphism**
- **Strategy pattern implementation**

```java
// Same interface, different implementations
SplitStrategy strategy1 = new EqualSplitStrategy();
SplitStrategy strategy2 = new ExactSplitStrategy();
SplitStrategy strategy3 = new PercentageSplitStrategy();

// Polymorphic behavior
List<Split> splits = strategy.calculateSplits(expense, participants, splitData);
```

### 4. **Abstraction** 🎨
- **Interface abstractions** hide implementation details
- **Service layer abstractions**

```java
// Client code doesn't need to know internal implementation
SplitwiseService service = new SplitwiseServiceImpl();
Expense expense = service.addExpense(groupId, description, amount, paidById, participantIds, splitType, splitData);
```

---

## ⚖️ SOLID Principles

### 1. **Single Responsibility Principle (SRP)** ✅
Each class has **one reason to change**:

- **`User`**: Manages user information and balances only
- **`Expense`**: Represents expense information only  
- **`SplitStrategy`**: Calculates splits only
- **`ExpenseFactory`**: Creates expenses only

### 2. **Open/Closed Principle (OCP)** ✅
**Open for extension, closed for modification**:

```java
// Adding new split strategy without modifying existing code
public class WeightedSplitStrategy implements SplitStrategy {
    @Override
    public List<Split> calculateSplits(Expense expense, List<User> participants, Map<String, Double> splitData) {
        // Weighted splitting logic based on custom weights
        // Implementation details...
    }
}
```

### 3. **Liskov Substitution Principle (LSP)** ✅
**Subtypes must be substitutable**:

```java
// Any SplitStrategy implementation can replace another
SplitStrategy strategy = new EqualSplitStrategy();
// Can be replaced with any other implementation without breaking code
strategy = new ExactSplitStrategy();
```

### 4. **Interface Segregation Principle (ISP)** ✅
**Clients shouldn't depend on unused interfaces**:

```java
// Specific interfaces for specific needs
public interface ExpenseEventObserver {
    void onExpenseAdded(Expense expense);
    void onExpenseUpdated(Expense expense);
    // Only expense-related events
}
```

### 5. **Dependency Inversion Principle (DIP)** ✅
**Depend on abstractions, not concretions**:

```java
public class SplitwiseServiceImpl {
    private final Map<SplitType, SplitStrategy> splitStrategies;     // Abstraction
    private final List<ExpenseEventObserver> observers;             // Abstraction
    
    // Depends on interfaces, not concrete classes
}
```

---

## 📁 Code Structure

```
src/
├── enums/
│   ├── SplitType.java              # Split types (Equal, Exact, Percentage)
│   ├── ExpenseType.java            # Expense categories
│   └── SettlementStatus.java       # Settlement statuses
├── models/
│   ├── User.java                   # User entity with balance management
│   ├── Group.java                  # Group entity with member management
│   ├── Expense.java                # Expense entity
│   ├── Split.java                  # Individual split information
│   ├── Balance.java                # Balance between two users
│   └── Settlement.java             # Settlement transaction
├── services/
│   ├── SplitwiseService.java           # Service interface
│   └── SplitwiseServiceImpl.java       # Service implementation
├── strategies/
│   ├── SplitStrategy.java              # Strategy interface
│   ├── EqualSplitStrategy.java         # Equal split implementation
│   ├── ExactSplitStrategy.java         # Exact amount split implementation
│   └── PercentageSplitStrategy.java    # Percentage split implementation
├── factories/
│   └── ExpenseFactory.java             # Expense creation factory
├── observers/
│   ├── ExpenseEventObserver.java           # Observer interface
│   └── ConsoleNotificationObserver.java    # Console notification implementation
├── managers/
│   └── GroupManager.java               # Singleton group manager
└── Main.java                            # Application entry point
```

---

## 🚀 Key Features

### ✨ **Multiple Split Types**
- **Equal Split**: Divide expense equally among participants
- **Exact Split**: Specify exact amounts for each participant
- **Percentage Split**: Divide based on percentage shares
- **Extensible**: Easy to add new split strategies

### ✨ **Smart Balance Calculation**
- **Real-time updates**: Balances update automatically with new expenses
- **Simplified tracking**: Net balances between users
- **Settlement optimization**: Minimize number of transactions needed

### ✨ **Group Management**
- **Multiple groups**: Support for different friend groups
- **Member management**: Add/remove members from groups
- **Group-specific expenses**: Track expenses per group

### ✨ **Event-Driven Notifications**
- **Observer pattern**: Loose coupling for notifications
- **Multiple events**: Expense added, updated, deleted, settlements made
- **Extensible**: Easy to add new notification channels

---

## 🎤 Interview Questions & Answers

### **Q1: How would you handle concurrent expense additions?**
**A**: Implement **thread-safety** using:
```java
public synchronized Expense addExpense(...) {
    // Atomic operation for expense addition
    // Use ReentrantLock for fine-grained control
    // Database-level locking for distributed systems
}
```

### **Q2: How would you optimize settlement calculations?**
**A**: Use **graph algorithms**:
- Model debts as directed graph
- Use algorithms to minimize transactions
- Implement cycle detection to simplify settlements

### **Q3: How would you add new split types?**
**A**: **Open/Closed Principle** implementation:
1. Add new enum value in `SplitType`
2. Create new strategy implementing `SplitStrategy`
3. **No modification** of existing classes required

### **Q4: How would you implement expense categories?**
**A**: **Strategy Pattern** extension:
```java
public enum ExpenseType {
    FOOD, TRAVEL, ENTERTAINMENT, UTILITIES, OTHER
}

public class CategoryBasedSplitStrategy implements SplitStrategy {
    // Different splitting logic based on expense category
}
```

### **Q5: How would you handle currency conversion?**
**A**: Add **Currency Strategy Pattern**:
```java
public interface CurrencyConverter {
    double convert(double amount, Currency from, Currency to);
}

public class RealTimeCurrencyConverter implements CurrencyConverter { }
public class FixedRateCurrencyConverter implements CurrencyConverter { }
```

### **Q6: How would you implement expense history and audit?**
**A**: Extend with **Command Pattern**:
```java
public interface ExpenseCommand {
    void execute();
    void undo();
}

public class AddExpenseCommand implements ExpenseCommand {
    // Command implementation with undo capability
}
```

---

## 🏃‍♂️ How to Run

### **Prerequisites**
- Java 8 or higher
- Any IDE (IntelliJ IDEA, Eclipse, VS Code)

### **Steps**
1. **Navigate** to the Splitwise directory
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
[DEMO] Welcome to Splitwise System Demo!
=============================================================

[STEP 1] Create Users and Groups
[USER] Created user: Alice (alice@email.com)
[USER] Created user: Bob (bob@email.com)
[GROUP] Created group: Weekend Trip

[STEP 2] Add Expenses
[EXPENSE] Added: Dinner - ₹1200.00 (Equal Split)
[EXPENSE] Added: Hotel - ₹3000.00 (Exact Split)

[STEP 3] Display Balances
[BALANCE] Alice owes Bob: ₹400.00
[BALANCE] Charlie owes Alice: ₹200.00

[STEP 4] Settle Up
[SETTLEMENT] Bob paid Alice: ₹400.00
```

---

## 🔧 Extension Points

### **Easy Extensions** (15-30 minutes in interview)
1. **Add new split type**: Implement `SplitStrategy`
2. **Add new expense category**: Extend `ExpenseType` enum
3. **Add new notification method**: Implement `ExpenseEventObserver`

### **Medium Extensions** (30-45 minutes in interview)
1. **Currency support**: Add currency conversion with Strategy pattern
2. **Expense history**: Add audit trail and undo functionality
3. **Advanced settlements**: Implement graph-based optimization

### **Advanced Extensions** (45+ minutes in interview)
1. **Database integration**: Add Repository pattern with JPA/Hibernate
2. **REST API**: Add Spring Boot controllers
3. **Real-time updates**: Add WebSocket support for live balance updates
4. **Mobile app integration**: Add API versioning and mobile-specific endpoints

---

## 🎯 Interview Success Tips

### **What to Highlight**
1. **Design Patterns**: Strategy, Factory, Observer, Singleton
2. **SOLID Principles**: Clear examples of each principle
3. **Extensibility**: How easy it is to add new features
4. **Algorithm Efficiency**: Settlement optimization considerations
5. **Scalability**: Multi-group, multi-currency support

### **Common Follow-up Questions**
- "How would you handle very large groups?"
- "How would you implement recurring expenses?"
- "How would you scale to millions of users?"
- "How would you handle offline functionality?"
- "How would you implement expense approval workflows?"

### **Code Quality Points**
- **Clean code**: Meaningful names, small methods
- **Documentation**: Clear comments and JavaDoc
- **Error handling**: Proper exception handling
- **Testing**: Unit test considerations
- **Performance**: Time/space complexity awareness

---

## 📚 Key Takeaways

This Splitwise system demonstrates:

1. **🎯 Design Patterns**: Practical implementation of 4 major patterns
2. **🧱 OOP Principles**: All 4 pillars with real examples  
3. **⚖️ SOLID Principles**: Each principle clearly demonstrated
4. **🚀 Scalability**: Easy to extend and modify
5. **🔧 Interview Ready**: Perfect for 45-60 minute FAANG interviews

**Perfect for demonstrating system design skills in technical interviews!**