# 🍕 Food Delivery Service - Complete Interview Guide

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

This is a **comprehensive Food Delivery System** implemented in Java, demonstrating multiple design patterns, OOP principles, and best practices commonly asked in FAANG technical interviews.

### Core Functionality
- **User Management**: Multi-role user system (Customer, Restaurant Owner, Delivery Partner)
- **Restaurant & Menu Management**: Dynamic restaurant and menu item management
- **Order Processing**: Complete order lifecycle from placement to delivery
- **Delivery Assignment**: Smart delivery partner assignment using strategy patterns
- **Location Services**: Distance-based calculations and partner assignment
- **Real-time Tracking**: Order status updates throughout the delivery process

---

## 🎯 Architecture & Design Patterns

### 1. **Strategy Pattern** 🎯
**Location**: [`src/strategies/`](src/strategies/)

**Purpose**: Encapsulates delivery assignment algorithms and makes them interchangeable.

```java
// Interface
public interface DeliveryAssignmentStrategy {
    DeliveryPartner assignPartner(Restaurant restaurant, List<DeliveryPartner> availablePartners);
}

// Concrete Implementation
public class NearestPartnerStrategy implements DeliveryAssignmentStrategy {
    @Override
    public DeliveryPartner assignPartner(Restaurant restaurant, List<DeliveryPartner> availablePartners) {
        DeliveryPartner nearestPartner = null;
        double minDistance = Double.MAX_VALUE;
        
        for (DeliveryPartner partner : availablePartners) {
            if (partner.isAvailable()) {
                double distance = restaurant.getLocation().calculateDistance(partner.getLocation());
                if (distance < minDistance) {
                    minDistance = distance;
                    nearestPartner = partner;
                }
            }
        }
        return nearestPartner;
    }
}
```

**Interview Benefit**: Shows understanding of **Open/Closed Principle** and algorithm flexibility.

### 2. **Singleton Pattern** 🔒
**Location**: [`src/managers/OrderManager.java`](src/managers/OrderManager.java), [`src/managers/DeliveryManager.java`](src/managers/DeliveryManager.java)

**Purpose**: Ensures single instance of critical managers and provides global access.

```java
public class OrderManager {
    private static OrderManager instance;
    private Map<String, Order> orders;
    private Map<String, Restaurant> restaurants;
    
    private OrderManager() {
        this.orders = new HashMap<>();
        this.restaurants = new HashMap<>();
    }
    
    public static synchronized OrderManager getInstance() {
        if (instance == null) {
            instance = new OrderManager();
        }
        return instance;
    }
}
```

**Interview Benefit**: Demonstrates **thread-safety** and **centralized state management**.

### 3. **Factory Pattern** 🏭
**Location**: Demonstrated in order creation logic

**Purpose**: Centralizes object creation and provides consistent ID generation.

```java
// In OrderManager
public Order placeOrder(String customerId, String restaurantId) {
    String orderId = "ORD_" + UUID.randomUUID().toString().substring(0, 8);
    Order order = new Order(orderId, customerId, restaurantId);
    orders.put(orderId, order);
    return order;
}
```

**Interview Benefit**: Shows **creational patterns** and **separation of concerns**.

### 4. **Service Layer Pattern** 🔌
**Location**: [`src/services/FoodDeliveryServiceImpl.java`](src/services/FoodDeliveryServiceImpl.java)

**Purpose**: Provides clean API and coordinates between different managers.

```java
public class FoodDeliveryServiceImpl implements FoodDeliveryService {
    private final OrderManager orderManager;
    private final DeliveryManager deliveryManager;
    private final Map<String, User> users;
    
    @Override
    public Order placeOrder(String customerId, String restaurantId) {
        return orderManager.placeOrder(customerId, restaurantId);
    }
    
    @Override
    public boolean assignDeliveryPartner(String orderId) {
        Order order = orderManager.getOrder(orderId);
        return deliveryManager.assignDeliveryPartner(orderId, order.getRestaurantId());
    }
}
```

**Interview Benefit**: Demonstrates **layered architecture** and **dependency management**.

---

## 🧱 OOP Principles Implementation

### 1. **Encapsulation** 🔐
- **Private fields** with **controlled access**
- **Data validation** in methods
- **Internal state protection**

```java
public class Order {
    private String id;                              // Immutable
    private String customerId;                      // Immutable
    private Map<FoodInfo, Integer> orderedItems;    // Controlled access
    private OrderStatus status;                     // Controlled transitions
    private double totalAmount;                     // Calculated automatically
    
    public void addItem(FoodInfo foodItem, int quantity) {
        orderedItems.put(foodItem, orderedItems.getOrDefault(foodItem, 0) + quantity);
        calculateTotalAmount(); // Automatic recalculation
    }
    
    private void calculateTotalAmount() {
        // Internal calculation logic
    }
}
```

### 2. **Inheritance** 🌳
- **Class inheritance** with meaningful relationships
- **Interface inheritance** for contracts

```java
// Base class
public class User {
    protected String id;
    protected String name;
    protected String phone;
    protected UserRole role;
    protected Location location;
}

// Specialized class
public class DeliveryPartner extends User {
    private PartnerStatus status;
    private String vehicleNumber;
    private double rating;
    private int totalDeliveries;
    
    public DeliveryPartner(String id, String name, String phone, Location location, String vehicleNumber) {
        super(id, name, phone, UserRole.DELIVERY_PARTNER, location);
        this.vehicleNumber = vehicleNumber;
        this.status = PartnerStatus.AVAILABLE;
    }
}
```

### 3. **Polymorphism** 🎭
- **Interface-based polymorphism**
- **Strategy pattern implementation**

```java
// Same interface, different implementations
DeliveryAssignmentStrategy strategy1 = new NearestPartnerStrategy();
DeliveryAssignmentStrategy strategy2 = new HighestRatedPartnerStrategy();

// Polymorphic behavior
DeliveryPartner partner = strategy.assignPartner(restaurant, partners);
```

### 4. **Abstraction** 🎨
- **Interface abstractions** hide implementation details
- **Service layer abstractions**

```java
// Client code doesn't need to know internal implementation
FoodDeliveryService service = new FoodDeliveryServiceImpl();
Order order = service.placeOrder(customerId, restaurantId); // Abstract operation
```

---

## ⚖️ SOLID Principles

### 1. **Single Responsibility Principle (SRP)** ✅
Each class has **one reason to change**:

- **`Order`**: Manages order state and items only
- **`DeliveryPartner`**: Handles delivery partner operations only
- **`Location`**: Manages geographical calculations only
- **`DeliveryAssignmentStrategy`**: Handles partner assignment logic only

### 2. **Open/Closed Principle (OCP)** ✅
**Open for extension, closed for modification**:

```java
// Adding new delivery strategy without modifying existing code
public class HighestRatedPartnerStrategy implements DeliveryAssignmentStrategy {
    @Override
    public DeliveryPartner assignPartner(Restaurant restaurant, List<DeliveryPartner> partners) {
        return partners.stream()
                .filter(DeliveryPartner::isAvailable)
                .max(Comparator.comparing(DeliveryPartner::getRating))
                .orElse(null);
    }
}

public class LoadBalancedStrategy implements DeliveryAssignmentStrategy {
    @Override
    public DeliveryPartner assignPartner(Restaurant restaurant, List<DeliveryPartner> partners) {
        return partners.stream()
                .filter(DeliveryPartner::isAvailable)
                .min(Comparator.comparing(DeliveryPartner::getTotalDeliveries))
                .orElse(null);
    }
}
```

### 3. **Liskov Substitution Principle (LSP)** ✅
**Subtypes must be substitutable**:

```java
// Any DeliveryAssignmentStrategy implementation can replace another
DeliveryAssignmentStrategy strategy = new NearestPartnerStrategy();
// Can be replaced with any other implementation without breaking code
strategy = new HighestRatedPartnerStrategy();
strategy = new LoadBalancedStrategy();
```

### 4. **Interface Segregation Principle (ISP)** ✅
**Clients shouldn't depend on unused interfaces**:

```java
// Specific interfaces for specific needs
public interface FoodDeliveryService {
    // Only food delivery related methods
    Order placeOrder(String customerId, String restaurantId);
    void addItemToOrder(String orderId, String foodId, int quantity);
    boolean assignDeliveryPartner(String orderId);
}

public interface DeliveryAssignmentStrategy {
    // Only delivery assignment related methods
    DeliveryPartner assignPartner(Restaurant restaurant, List<DeliveryPartner> availablePartners);
}
```

### 5. **Dependency Inversion Principle (DIP)** ✅
**Depend on abstractions, not concretions**:

```java
public class DeliveryManager {
    private DeliveryAssignmentStrategy assignmentStrategy;  // Abstraction
    
    public DeliveryManager() {
        this.assignmentStrategy = new NearestPartnerStrategy(); // Default implementation
    }
    
    public void setAssignmentStrategy(DeliveryAssignmentStrategy strategy) {
        this.assignmentStrategy = strategy; // Depends on interface, not concrete class
    }
}
```

---

## 📁 Code Structure

```
src/
├── enums/
│   ├── UserRole.java              # User roles (Customer, Restaurant Owner, Delivery Partner)
│   ├── OrderStatus.java           # Order lifecycle states
│   ├── FoodType.java              # Food categories (VEG, NON_VEG, VEGAN)
│   └── PartnerStatus.java         # Delivery partner availability states
├── models/
│   ├── User.java                  # Base user entity
│   ├── DeliveryPartner.java       # Delivery partner (extends User)
│   ├── Restaurant.java            # Restaurant entity
│   ├── Order.java                 # Order entity with lifecycle management
│   ├── FoodInfo.java              # Menu item entity
│   └── Location.java              # Geographical location with distance calculation
├── managers/
│   ├── OrderManager.java          # Singleton order management
│   └── DeliveryManager.java       # Singleton delivery management
├── strategies/
│   ├── DeliveryAssignmentStrategy.java    # Strategy interface
│   └── NearestPartnerStrategy.java        # Distance-based assignment
├── services/
│   ├── FoodDeliveryService.java           # Service interface
│   └── FoodDeliveryServiceImpl.java       # Service implementation
└── Main.java                              # Application entry point with demo
```

---

## 🚀 Key Features

### ✨ **Multi-User System**
- **Customer**: Place orders, track delivery
- **Restaurant Owner**: Manage restaurant and menu
- **Delivery Partner**: Accept and complete deliveries

### ✨ **Smart Delivery Assignment**
- **Distance-based assignment**: Nearest partner selection
- **Strategy pattern**: Easy to add new assignment algorithms
- **Real-time availability**: Only assigns to available partners

### ✨ **Complete Order Lifecycle**
- **Order placement**: Multi-item order support
- **Order confirmation**: Total calculation and validation
- **Order tracking**: Real-time status updates
- **Delivery completion**: Earnings tracking for partners

### ✨ **Location Services**
- **Distance calculation**: Haversine formula implementation
- **Geographical coordination**: Latitude/longitude support
- **Delivery optimization**: Location-based partner assignment

### ✨ **Robust Data Management**
- **Singleton managers**: Centralized data management
- **State validation**: Proper order status transitions
- **Error handling**: Comprehensive validation and error messages

---

## 🎤 Interview Questions & Answers

### **Q1: How would you handle concurrent orders for the same delivery partner?**
**A**: Implement **thread-safety** using:
```java
public class DeliveryPartner {
    private volatile PartnerStatus status;
    
    public synchronized boolean acceptOrder(String orderId) {
        if (status == PartnerStatus.AVAILABLE) {
            status = PartnerStatus.BUSY;
            // Accept order logic
            return true;
        }
        return false;
    }
}
```

### **Q2: How would you scale this system for multiple cities?**
**A**: Extend the system with:
- **City-based partitioning**: Separate managers per city
- **Location hierarchy**: City → Area → Restaurant/Partner
- **Microservices architecture**: City-specific services

```java
public class CityManager {
    private Map<String, OrderManager> cityOrderManagers;
    private Map<String, DeliveryManager> cityDeliveryManagers;
    
    public OrderManager getOrderManager(String cityId) {
        return cityOrderManagers.computeIfAbsent(cityId, k -> new OrderManager());
    }
}
```

### **Q3: How would you implement different delivery assignment strategies?**
**A**: **Strategy Pattern** allows easy extension:
```java
public class PriorityCustomerStrategy implements DeliveryAssignmentStrategy {
    @Override
    public DeliveryPartner assignPartner(Restaurant restaurant, List<DeliveryPartner> partners) {
        // Prioritize based on customer tier, order value, etc.
        return partners.stream()
                .filter(DeliveryPartner::isAvailable)
                .max(Comparator.comparing(this::calculatePriority))
                .orElse(null);
    }
}
```

### **Q4: How would you implement real-time order tracking?**
**A**: Add **Observer Pattern**:
```java
public interface OrderObserver {
    void onOrderStatusChanged(Order order, OrderStatus oldStatus, OrderStatus newStatus);
}

public class OrderManager {
    private List<OrderObserver> observers = new ArrayList<>();
    
    public void updateOrderStatus(String orderId, OrderStatus newStatus) {
        Order order = orders.get(orderId);
        OrderStatus oldStatus = order.getStatus();
        order.setStatus(newStatus);
        
        // Notify all observers
        notifyObservers(order, oldStatus, newStatus);
    }
}
```

### **Q5: How would you handle payment processing?**
**A**: Add **Payment Strategy Pattern**:
```java
public interface PaymentStrategy {
    PaymentResult processPayment(double amount, PaymentDetails details);
}

public class CreditCardPaymentStrategy implements PaymentStrategy {
    @Override
    public PaymentResult processPayment(double amount, PaymentDetails details) {
        // Credit card processing logic
        return new PaymentResult(true, generateTransactionId());
    }
}

public class DigitalWalletPaymentStrategy implements PaymentStrategy {
    @Override
    public PaymentResult processPayment(double amount, PaymentDetails details) {
        // Digital wallet processing logic
        return new PaymentResult(true, generateTransactionId());
    }
}
```

### **Q6: How would you implement restaurant search and filtering?**
**A**: Add **Search and Filter capabilities**:
```java
public class RestaurantSearchService {
    public List<Restaurant> searchRestaurants(SearchCriteria criteria) {
        return restaurants.stream()
                .filter(restaurant -> matchesLocation(restaurant, criteria.getLocation(), criteria.getRadius()))
                .filter(restaurant -> matchesCuisine(restaurant, criteria.getCuisine()))
                .filter(restaurant -> matchesRating(restaurant, criteria.getMinRating()))
                .sorted(Comparator.comparing(restaurant -> 
                    criteria.getLocation().calculateDistance(restaurant.getLocation())))
                .collect(Collectors.toList());
    }
}
```

---

## 🏃‍♂️ How to Run

### **Prerequisites**
- Java 8 or higher
- Any IDE (IntelliJ IDEA, Eclipse, VS Code)

### **Steps**
1. **Navigate** to the FoodDeliveryService directory
2. **Compile** all Java files:
   ```bash
   javac -encoding UTF-8 -d out src\enums\*.java src\models\*.java src\strategies\*.java src\managers\*.java src\services\*.java src\Main.java
   ```
3. **Run** the main class:
   ```bash
   java -cp out Main
   ```

### **Expected Output**
```
=== Food Delivery Service Demo ===

User registered: John Doe (CUSTOMER)
User registered: Jane Smith (RESTAURANT_OWNER)
Restaurant added: Pizza Palace
Menu item added: Margherita Pizza to Pizza Palace
Menu item added: Chicken Burger to Pizza Palace
Menu item added: Vegan Pasta to Pizza Palace
Restaurant and menu items added!

Delivery partner registered: Mike Wilson
Delivery partner registered!

--- Placing Order ---
Order placed: ORD_12345678 for customer: C1
Added 2x Margherita Pizza to order ORD_12345678
Added 1x Chicken Burger to order ORD_12345678
Order confirmed: ORD_12345678 - Total: ₹799.97

--- Order Details ---
Order ID: ORD_12345678
Customer ID: C1
Restaurant ID: REST1
Status: CONFIRMED
Total Amount: ₹799.97
Total Items: 3

Order Summary:
  Margherita Pizza x 2 = ₹599.98
  Chicken Burger x 1 = ₹199.99

--- Order Processing ---
Order ORD_12345678 status updated to: PREPARING
Order ORD_12345678 status updated to: READY_FOR_PICKUP

--- Delivery Assignment ---
Delivery partner Mike Wilson accepted order: ORD_12345678
Order ORD_12345678 assigned to delivery partner: Mike Wilson
Delivery partner assigned successfully!

--- Completing Delivery ---
Delivery completed by Mike Wilson. Earnings: ₹50.0
Delivery completed for order: ORD_12345678

--- Final Order Status ---
Order Status: DELIVERED
Delivery Partner: DP1

--- Delivery Partner Stats ---
Partner: Mike Wilson
Total Deliveries: 1
Total Earnings: ₹50.0
Rating: 5.0

=== Food Delivery Demo Completed Successfully! ===
```

---

## 🔧 Extension Points

### **Easy Extensions** (15-30 minutes in interview)
1. **Add new delivery strategy**: Implement `DeliveryAssignmentStrategy`
2. **Add new user role**: Extend `UserRole` enum and `User` class
3. **Add new food type**: Extend `FoodType` enum

### **Medium Extensions** (30-45 minutes in interview)
1. **Payment integration**: Add payment processing with Strategy pattern
2. **Order notifications**: Add Observer pattern for real-time updates
3. **Restaurant analytics**: Add reporting and metrics features
4. **Multi-city support**: Extend with geographical partitioning

### **Advanced Extensions** (45+ minutes in interview)
1. **Database integration**: Add Repository pattern with JPA/Hibernate
2. **REST API**: Add Spring Boot controllers and endpoints
3. **Real-time tracking**: Add WebSocket support for live location updates
4. **Machine learning**: Add intelligent delivery time prediction
5. **Microservices**: Split into independent services with API gateway

---

## 🎯 Interview Success Tips

### **What to Highlight**
1. **Design Patterns**: Strategy, Singleton, Factory, Service Layer
2. **SOLID Principles**: Clear examples of each principle
3. **Extensibility**: How easy it is to add new features
4. **Real-world modeling**: Practical business logic implementation
5. **Error handling**: Comprehensive validation and edge cases

### **Common Follow-up Questions**
- "How would you handle peak ordering times?"
- "How would you implement surge pricing?"
- "How would you scale to 1000+ restaurants?"
- "How would you handle delivery partner unavailability?"
- "How would you implement customer recommendations?"

### **Code Quality Points**
- **Clean code**: Meaningful names, small methods, clear structure
- **Documentation**: Clear comments and method descriptions
- **Error handling**: Proper exception handling and validation
- **Testing**: Unit test considerations and testable design
- **Performance**: Time/space complexity awareness

---

## 📚 Key Takeaways

This Food Delivery System demonstrates:

1. **🎯 Design Patterns**: Practical implementation of multiple patterns
2. **🧱 OOP Principles**: All 4 pillars with real examples  
3. **⚖️ SOLID Principles**: Each principle clearly demonstrated
4. **🚀 Scalability**: Easy to extend and modify
5. **🔧 Interview Ready**: Perfect for 45-60 minute FAANG interviews

**Perfect for demonstrating system design skills and coding expertise in technical interviews!**