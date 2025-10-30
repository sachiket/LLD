# 📝 Food Delivery Service Interview Cheat Sheet

## 🚀 Quick Reference for Live Coding

### **Core Models (5 minutes)**
```java
// User.java
public class User {
    private final String id;
    private final String name;
    private final String phone;
    private final UserRole role;
    private final Location location;
    private boolean isActive;
}

// Order.java -> Restaurant.java -> FoodInfo.java
public class Order {
    private String id;
    private String customerId;
    private String restaurantId;
    private Map<FoodInfo, Integer> orderedItems;
    private OrderStatus status;
    private double totalAmount;
    private String deliveryPartnerId;
}
```

### **Key Enums**
```java
public enum UserRole { CUSTOMER, RESTAURANT_OWNER, DELIVERY_PARTNER }
public enum OrderStatus { PLACED, CONFIRMED, PREPARING, READY_FOR_PICKUP, OUT_FOR_DELIVERY, DELIVERED, CANCELLED }
public enum FoodType { VEG, NON_VEG, VEGAN }
public enum PartnerStatus { AVAILABLE, BUSY, OFFLINE }
```

---

## 🎯 Design Patterns Quick Implementation

### **1. Strategy Pattern (Delivery Assignment)**
```java
public interface DeliveryAssignmentStrategy {
    DeliveryPartner assignPartner(Restaurant restaurant, List<DeliveryPartner> availablePartners);
}

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

### **2. Singleton Pattern (Managers)**
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

### **3. Factory Pattern (Order Creation)**
```java
public class OrderFactory {
    public static Order createOrder(String customerId, String restaurantId) {
        String orderId = "ORD_" + UUID.randomUUID().toString().substring(0, 8);
        return new Order(orderId, customerId, restaurantId);
    }
}
```

### **4. Service Layer Pattern**
```java
public interface FoodDeliveryService {
    Order placeOrder(String customerId, String restaurantId);
    void addItemToOrder(String orderId, String foodId, int quantity);
    void confirmOrder(String orderId);
    boolean assignDeliveryPartner(String orderId);
}

// In Service Implementation
private final OrderManager orderManager = OrderManager.getInstance();
private final DeliveryManager deliveryManager = DeliveryManager.getInstance();
```

---

## ⚖️ SOLID Principles Examples

### **Single Responsibility**
```java
// ✅ Good - Each class has one responsibility
public class Order { /* Only manages order state and items */ }
public class DeliveryAssignmentStrategy { /* Only handles partner assignment */ }
public class Location { /* Only handles geographical calculations */ }
```

### **Open/Closed**
```java
// ✅ Easy to extend without modifying existing code
public class HighestRatedPartnerStrategy implements DeliveryAssignmentStrategy {
    @Override
    public DeliveryPartner assignPartner(Restaurant restaurant, List<DeliveryPartner> partners) {
        return partners.stream()
                .filter(DeliveryPartner::isAvailable)
                .max(Comparator.comparing(DeliveryPartner::getRating))
                .orElse(null);
    }
}
```

### **Dependency Inversion**
```java
// ✅ Depend on abstractions
public class DeliveryManager {
    private DeliveryAssignmentStrategy assignmentStrategy; // Interface, not concrete class
    
    public void setAssignmentStrategy(DeliveryAssignmentStrategy strategy) {
        this.assignmentStrategy = strategy;
    }
}
```

---

## 🔧 Core Service Methods

### **Order Creation**
```java
@Override
public Order placeOrder(String customerId, String restaurantId) {
    Restaurant restaurant = orderManager.getRestaurant(restaurantId);
    if (restaurant == null || !restaurant.isActive()) {
        throw new IllegalArgumentException("Restaurant not found or inactive");
    }
    
    String orderId = "ORD_" + UUID.randomUUID().toString().substring(0, 8);
    Order order = new Order(orderId, customerId, restaurantId);
    orderManager.addOrder(order);
    
    return order;
}
```

### **Item Addition with Validation**
```java
public void addItemToOrder(String orderId, String foodId, int quantity) {
    Order order = orderManager.getOrder(orderId);
    if (order == null) {
        throw new IllegalArgumentException("Order not found");
    }
    
    if (order.getStatus() != OrderStatus.PLACED) {
        throw new IllegalStateException("Cannot modify order in current status: " + order.getStatus());
    }
    
    Restaurant restaurant = orderManager.getRestaurant(order.getRestaurantId());
    FoodInfo foodItem = restaurant.getMenuItem(foodId);
    
    if (foodItem == null || !foodItem.isAvailable()) {
        throw new IllegalArgumentException("Food item not available");
    }
    
    order.addItem(foodItem, quantity);
}
```

### **Delivery Assignment**
```java
public boolean assignDeliveryPartner(String orderId) {
    Order order = orderManager.getOrder(orderId);
    if (order.getStatus() != OrderStatus.READY_FOR_PICKUP) {
        return false;
    }
    
    List<DeliveryPartner> availablePartners = deliveryManager.getAvailablePartners();
    if (availablePartners.isEmpty()) {
        return false;
    }
    
    Restaurant restaurant = orderManager.getRestaurant(order.getRestaurantId());
    DeliveryPartner assignedPartner = assignmentStrategy.assignPartner(restaurant, availablePartners);
    
    if (assignedPartner != null) {
        assignedPartner.acceptOrder(orderId);
        order.setDeliveryPartnerId(assignedPartner.getId());
        order.setStatus(OrderStatus.OUT_FOR_DELIVERY);
        return true;
    }
    
    return false;
}
```

---

## 🎤 Common Interview Questions & Answers

### **Q: "How would you handle concurrent orders for the same food item?"**
```java
// Option 1: Synchronized methods
public synchronized boolean addItemToOrder(String orderId, String foodId, int quantity) { }

// Option 2: Optimistic locking with version control
public class FoodInfo {
    private int availableQuantity;
    private int version; // For optimistic locking
}

// Option 3: Database-level constraints
@Transactional
public void addItemToOrder(...) {
    // Use SELECT FOR UPDATE in database
}
```

### **Q: "How would you implement different delivery assignment strategies?"**
```java
public class LoadBalancedStrategy implements DeliveryAssignmentStrategy {
    @Override
    public DeliveryPartner assignPartner(Restaurant restaurant, List<DeliveryPartner> partners) {
        return partners.stream()
                .filter(DeliveryPartner::isAvailable)
                .min(Comparator.comparing(DeliveryPartner::getTotalDeliveries))
                .orElse(null);
    }
}

public class FastestDeliveryStrategy implements DeliveryAssignmentStrategy {
    @Override
    public DeliveryPartner assignPartner(Restaurant restaurant, List<DeliveryPartner> partners) {
        // Consider both distance and partner's average delivery time
        return partners.stream()
                .filter(DeliveryPartner::isAvailable)
                .min(Comparator.comparing(partner -> 
                    calculateEstimatedDeliveryTime(restaurant, partner)))
                .orElse(null);
    }
}
```

### **Q: "How would you implement real-time order tracking?"**
```java
public interface OrderObserver {
    void onOrderStatusChanged(Order order, OrderStatus oldStatus, OrderStatus newStatus);
}

public class OrderManager {
    private List<OrderObserver> observers = new ArrayList<>();
    
    public void addObserver(OrderObserver observer) {
        observers.add(observer);
    }
    
    public void updateOrderStatus(String orderId, OrderStatus newStatus) {
        Order order = orders.get(orderId);
        OrderStatus oldStatus = order.getStatus();
        order.setStatus(newStatus);
        
        // Notify all observers
        for (OrderObserver observer : observers) {
            observer.onOrderStatusChanged(order, oldStatus, newStatus);
        }
    }
}
```

### **Q: "How would you handle payment processing?"**
```java
public interface PaymentStrategy {
    PaymentResult processPayment(double amount, PaymentDetails details);
}

public class CreditCardPaymentStrategy implements PaymentStrategy {
    @Override
    public PaymentResult processPayment(double amount, PaymentDetails details) {
        // Credit card processing logic
        return new PaymentResult(true, "TXN_" + UUID.randomUUID());
    }
}

public class DigitalWalletPaymentStrategy implements PaymentStrategy {
    @Override
    public PaymentResult processPayment(double amount, PaymentDetails details) {
        // Digital wallet processing logic
        return new PaymentResult(true, "WALLET_" + UUID.randomUUID());
    }
}
```

### **Q: "How would you implement location-based restaurant search?"**
```java
public class Location {
    private double latitude;
    private double longitude;
    
    public double calculateDistance(Location other) {
        // Haversine formula
        double R = 6371; // Earth's radius in kilometers
        double dLat = Math.toRadians(other.latitude - this.latitude);
        double dLon = Math.toRadians(other.longitude - this.longitude);
        
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                   Math.cos(Math.toRadians(this.latitude)) * Math.cos(Math.toRadians(other.latitude)) *
                   Math.sin(dLon/2) * Math.sin(dLon/2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R * c;
    }
}

public List<Restaurant> getNearbyRestaurants(Location userLocation, double radiusKm) {
    return restaurants.values().stream()
            .filter(restaurant -> restaurant.isActive())
            .filter(restaurant -> userLocation.calculateDistance(restaurant.getLocation()) <= radiusKm)
            .sorted(Comparator.comparing(restaurant -> 
                userLocation.calculateDistance(restaurant.getLocation())))
            .collect(Collectors.toList());
}
```

---

## 🚀 Quick Extensions

### **Add New Delivery Strategy (2 minutes)**
```java
// 1. Create new strategy
public class PriorityCustomerStrategy implements DeliveryAssignmentStrategy {
    @Override
    public DeliveryPartner assignPartner(Restaurant restaurant, List<DeliveryPartner> partners) {
        // Prioritize premium customers
        return partners.stream()
                .filter(DeliveryPartner::isAvailable)
                .max(Comparator.comparing(DeliveryPartner::getRating))
                .orElse(null);
    }
}

// 2. Use in DeliveryManager
deliveryManager.setAssignmentStrategy(new PriorityCustomerStrategy());
```

### **Add Order Notifications (3 minutes)**
```java
public class EmailNotificationObserver implements OrderObserver {
    @Override
    public void onOrderStatusChanged(Order order, OrderStatus oldStatus, OrderStatus newStatus) {
        if (newStatus == OrderStatus.CONFIRMED) {
            sendConfirmationEmail(order);
        } else if (newStatus == OrderStatus.OUT_FOR_DELIVERY) {
            sendDeliveryNotification(order);
        }
    }
    
    private void sendConfirmationEmail(Order order) {
        System.out.println("Email sent: Order " + order.getId() + " confirmed");
    }
}
```

### **Add Restaurant Analytics (5 minutes)**
```java
public class RestaurantAnalytics {
    public RestaurantStats generateStats(String restaurantId, DateRange range) {
        List<Order> orders = orderManager.getOrdersByRestaurant(restaurantId, range);
        
        return RestaurantStats.builder()
                .totalOrders(orders.size())
                .totalRevenue(orders.stream().mapToDouble(Order::getTotalAmount).sum())
                .averageOrderValue(orders.stream().mapToDouble(Order::getTotalAmount).average().orElse(0))
                .popularItems(getPopularItems(orders))
                .build();
    }
}
```

---

## 🎯 Demo Script

### **Step 1: Setup System**
```java
FoodDeliveryService service = new FoodDeliveryServiceImpl();
// Register users, restaurants, delivery partners
```

### **Step 2: Place Order**
```java
Order order = service.placeOrder("C1", "REST1");
service.addItemToOrder(order.getId(), "F1", 2);
service.addItemToOrder(order.getId(), "F2", 1);
service.confirmOrder(order.getId());
```

### **Step 3: Process Order**
```java
// Simulate restaurant preparation
OrderManager.getInstance().updateOrderStatus(order.getId(), OrderStatus.PREPARING);
OrderManager.getInstance().updateOrderStatus(order.getId(), OrderStatus.READY_FOR_PICKUP);
```

### **Step 4: Assign Delivery**
```java
boolean assigned = service.assignDeliveryPartner(order.getId());
// Shows strategy pattern in action
```

### **Step 5: Complete Delivery**
```java
service.completeDelivery(order.getId(), 50.0);
// Shows final order status and partner earnings
```

---

## 💡 Pro Tips for Live Coding

### **Start with This Template**
```java
public class Main {
    public static void main(String[] args) {
        // 1. Setup system
        FoodDeliveryService service = new FoodDeliveryServiceImpl();
        
        // 2. Add sample data
        setupSampleData(service);
        
        // 3. Demonstrate functionality
        demonstrateOrderFlow(service);
    }
}
```

### **Time-Saving Shortcuts**
- Use `UUID.randomUUID().toString().substring(0, 8)` for IDs
- Use `Arrays.asList()` for quick list creation
- Use `String.format()` for clean output
- Use `LocalDateTime.now()` for timestamps

### **Error Handling Pattern**
```java
try {
    // Main operation
} catch (IllegalArgumentException e) {
    System.out.println("Error: " + e.getMessage());
    return false;
} catch (IllegalStateException e) {
    System.out.println("Invalid state: " + e.getMessage());
    return false;
}
```

---

## 🏆 Success Checklist

### **Must Have (60% score)**
- ✅ Basic models (User, Restaurant, Order, DeliveryPartner)
- ✅ Core order functionality
- ✅ At least 1 design pattern (Strategy or Singleton)
- ✅ Working demo

### **Should Have (80% score)**
- ✅ 2-3 design patterns
- ✅ SOLID principles demonstrated
- ✅ Error handling
- ✅ Location-based assignment

### **Nice to Have (95% score)**
- ✅ All design patterns
- ✅ Multiple assignment strategies
- ✅ Observer pattern for notifications
- ✅ Comprehensive demo with analytics

**Remember**: Quality over quantity. Better to have fewer features implemented well than many features implemented poorly!