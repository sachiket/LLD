# 🎯 SIMPLE START GUIDE - Food Delivery Service Interview

## 🚀 **DON'T PANIC! Start Here:**

### **Your Opening Line (30 seconds):**
*"I'll design a food delivery system step by step. Let me start with the basic entities and build up gradually."*

---

## 📝 **STEP 1: Draw Basic Entities (2 minutes)**

**On whiteboard/screen, draw:**
```
[User] ----places----> [Order] ----contains----> [FoodInfo]
   |                      |                         |
   |                      |                         |
[Customer]           [Restaurant] ----offers----> [Menu]
   |                      |
   |                      |
[DeliveryPartner] ----delivers----> [Order]
```

**Say:** *"These are my core entities. Let me code them..."*

### **Code Step 1:**
```java
// Start with just this!
class User {
    String id;
    String name;
    String phone;
    String role; // "CUSTOMER", "RESTAURANT_OWNER", "DELIVERY_PARTNER"
    Location location;
    boolean isActive = true;
}

class Location {
    double latitude;
    double longitude;
    String address;
    
    double calculateDistance(Location other) {
        // Simple distance calculation for demo
        return Math.sqrt(Math.pow(this.latitude - other.latitude, 2) + 
                        Math.pow(this.longitude - other.longitude, 2));
    }
}

class FoodInfo {
    String id;
    String name;
    String description;
    double price;
    String foodType; // "VEG", "NON_VEG", "VEGAN"
    boolean isAvailable = true;
}

class Restaurant {
    String id;
    String name;
    String ownerId;
    Location location;
    List<FoodInfo> menu = new ArrayList<>();
    boolean isActive = true;
}
```

**Say:** *"This covers the basics. Now let me add orders and delivery partners..."*

---

## 📝 **STEP 2: Add Order & DeliveryPartner (3 minutes)**

```java
class Order {
    String id;
    String customerId;
    String restaurantId;
    Map<FoodInfo, Integer> orderedItems = new HashMap<>();
    String status = "PLACED"; // "PLACED", "CONFIRMED", "PREPARING", "READY_FOR_PICKUP", "OUT_FOR_DELIVERY", "DELIVERED", "CANCELLED"
    double totalAmount;
    LocalDateTime orderTime;
    String deliveryPartnerId;
    
    Order(String id, String customerId, String restaurantId) {
        this.id = id;
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.orderTime = LocalDateTime.now();
    }
    
    void addItem(FoodInfo foodItem, int quantity) {
        orderedItems.put(foodItem, orderedItems.getOrDefault(foodItem, 0) + quantity);
        calculateTotalAmount();
    }
    
    void calculateTotalAmount() {
        totalAmount = orderedItems.entrySet().stream()
                .mapToDouble(entry -> entry.getKey().price * entry.getValue())
                .sum();
    }
}

class DeliveryPartner extends User {
    String status = "AVAILABLE"; // "AVAILABLE", "BUSY", "OFFLINE"
    String vehicleNumber;
    double rating = 5.0;
    int totalDeliveries = 0;
    double totalEarnings = 0.0;
    
    DeliveryPartner(String id, String name, String phone, Location location, String vehicleNumber) {
        super();
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.role = "DELIVERY_PARTNER";
        this.location = location;
        this.vehicleNumber = vehicleNumber;
    }
    
    void acceptOrder(String orderId) {
        if ("AVAILABLE".equals(status)) {
            status = "BUSY";
            totalDeliveries++;
            System.out.println("Delivery partner " + name + " accepted order: " + orderId);
        }
    }
    
    void completeDelivery(double earnings) {
        status = "AVAILABLE";
        totalEarnings += earnings;
        System.out.println("Delivery completed by " + name + ". Earnings: ₹" + earnings);
    }
}
```

**Say:** *"Great! This works. Now let me add the core service functionality..."*

---

## 📝 **STEP 3: Add Core Service Methods (3 minutes)**

```java
class Restaurant {
    // ... previous code ...
    
    void addMenuItem(FoodInfo foodItem) {
        menu.add(foodItem);
    }
    
    FoodInfo getMenuItem(String foodId) {
        return menu.stream()
                .filter(food -> food.id.equals(foodId))
                .findFirst()
                .orElse(null);
    }
    
    List<FoodInfo> getAvailableItems() {
        return menu.stream()
                .filter(food -> food.isAvailable)
                .collect(Collectors.toList());
    }
}

class FoodDeliveryService {
    Map<String, User> users = new HashMap<>();
    Map<String, Restaurant> restaurants = new HashMap<>();
    Map<String, Order> orders = new HashMap<>();
    Map<String, DeliveryPartner> deliveryPartners = new HashMap<>();
    
    // User Management
    void registerUser(User user) {
        users.put(user.id, user);
        System.out.println("User registered: " + user.name + " (" + user.role + ")");
    }
    
    // Restaurant Management
    void addRestaurant(Restaurant restaurant) {
        restaurants.put(restaurant.id, restaurant);
        System.out.println("Restaurant added: " + restaurant.name);
    }
    
    void addMenuItemToRestaurant(String restaurantId, FoodInfo foodItem) {
        Restaurant restaurant = restaurants.get(restaurantId);
        if (restaurant != null) {
            restaurant.addMenuItem(foodItem);
            System.out.println("Menu item added: " + foodItem.name + " to " + restaurant.name);
        }
    }
    
    // Order Management
    Order placeOrder(String customerId, String restaurantId) {
        Restaurant restaurant = restaurants.get(restaurantId);
        if (restaurant == null || !restaurant.isActive) {
            throw new IllegalArgumentException("Restaurant not found or inactive");
        }
        
        String orderId = "ORD_" + System.currentTimeMillis();
        Order order = new Order(orderId, customerId, restaurantId);
        orders.put(orderId, order);
        
        System.out.println("Order placed: " + orderId + " for customer: " + customerId);
        return order;
    }
    
    void addItemToOrder(String orderId, String foodId, int quantity) {
        Order order = orders.get(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order not found");
        }
        
        Restaurant restaurant = restaurants.get(order.restaurantId);
        FoodInfo foodItem = restaurant.getMenuItem(foodId);
        
        if (foodItem == null || !foodItem.isAvailable) {
            throw new IllegalArgumentException("Food item not available");
        }
        
        order.addItem(foodItem, quantity);
        System.out.println("Added " + quantity + "x " + foodItem.name + " to order " + orderId);
    }
    
    void confirmOrder(String orderId) {
        Order order = orders.get(orderId);
        if (order != null) {
            order.status = "CONFIRMED";
            System.out.println("Order confirmed: " + orderId + " - Total: ₹" + order.totalAmount);
        }
    }
}
```

**Say:** *"Perfect! This works. Now let me add delivery partner management..."*

---

## 📝 **STEP 4: Add Delivery Management (3 minutes)**

```java
class FoodDeliveryService {
    // ... previous code ...
    
    // Delivery Partner Management
    void registerDeliveryPartner(DeliveryPartner partner) {
        deliveryPartners.put(partner.id, partner);
        System.out.println("Delivery partner registered: " + partner.name);
    }
    
    List<DeliveryPartner> getAvailablePartners() {
        return deliveryPartners.values().stream()
                .filter(partner -> "AVAILABLE".equals(partner.status) && partner.isActive)
                .collect(Collectors.toList());
    }
    
    boolean assignDeliveryPartner(String orderId) {
        Order order = orders.get(orderId);
        if (order == null || !"READY_FOR_PICKUP".equals(order.status)) {
            System.out.println("Order not ready for pickup");
            return false;
        }
        
        List<DeliveryPartner> availablePartners = getAvailablePartners();
        if (availablePartners.isEmpty()) {
            System.out.println("No available delivery partners");
            return false;
        }
        
        // Simple assignment: find nearest partner
        Restaurant restaurant = restaurants.get(order.restaurantId);
        DeliveryPartner nearestPartner = findNearestPartner(restaurant, availablePartners);
        
        if (nearestPartner != null) {
            nearestPartner.acceptOrder(orderId);
            order.deliveryPartnerId = nearestPartner.id;
            order.status = "OUT_FOR_DELIVERY";
            
            System.out.println("Order " + orderId + " assigned to delivery partner: " + nearestPartner.name);
            return true;
        }
        
        return false;
    }
    
    DeliveryPartner findNearestPartner(Restaurant restaurant, List<DeliveryPartner> partners) {
        DeliveryPartner nearestPartner = null;
        double minDistance = Double.MAX_VALUE;
        
        for (DeliveryPartner partner : partners) {
            double distance = restaurant.location.calculateDistance(partner.location);
            if (distance < minDistance) {
                minDistance = distance;
                nearestPartner = partner;
            }
        }
        
        return nearestPartner;
    }
    
    void completeDelivery(String orderId, double deliveryFee) {
        Order order = orders.get(orderId);
        if (order != null && order.deliveryPartnerId != null) {
            DeliveryPartner partner = deliveryPartners.get(order.deliveryPartnerId);
            if (partner != null) {
                partner.completeDelivery(deliveryFee);
                order.status = "DELIVERED";
                System.out.println("Delivery completed for order: " + orderId);
            }
        }
    }
    
    Order trackOrder(String orderId) {
        return orders.get(orderId);
    }
}
```

**Say:** *"Excellent! Now it handles the complete delivery flow. Let me add enums to make it more professional..."*

---

## 📝 **STEP 5: Add Enums & Clean Up (2 minutes)**

```java
enum UserRole { CUSTOMER, RESTAURANT_OWNER, DELIVERY_PARTNER }
enum OrderStatus { PLACED, CONFIRMED, PREPARING, READY_FOR_PICKUP, OUT_FOR_DELIVERY, DELIVERED, CANCELLED }
enum FoodType { VEG, NON_VEG, VEGAN }
enum PartnerStatus { AVAILABLE, BUSY, OFFLINE }

class User {
    String id;
    String name;
    String phone;
    UserRole role;
    Location location;
    boolean isActive = true;
    
    User(String id, String name, String phone, UserRole role, Location location) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.role = role;
        this.location = location;
    }
}

class FoodInfo {
    String id;
    String name;
    String description;
    double price;
    FoodType foodType;
    boolean isAvailable = true;
    
    FoodInfo(String id, String name, String description, double price, FoodType foodType) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.foodType = foodType;
    }
}

class Order {
    String id;
    String customerId;
    String restaurantId;
    Map<FoodInfo, Integer> orderedItems = new HashMap<>();
    OrderStatus status = OrderStatus.PLACED;
    double totalAmount;
    LocalDateTime orderTime;
    String deliveryPartnerId;
    
    // ... rest of the methods remain the same
}
```

**Say:** *"Much cleaner with enums. Now let me add design patterns to make it more professional..."*

---

## 📝 **STEP 6: Add Design Patterns (5 minutes)**

### **Strategy Pattern for Delivery Assignment:**
```java
interface DeliveryAssignmentStrategy {
    DeliveryPartner assignPartner(Restaurant restaurant, List<DeliveryPartner> availablePartners);
}

class NearestPartnerStrategy implements DeliveryAssignmentStrategy {
    public DeliveryPartner assignPartner(Restaurant restaurant, List<DeliveryPartner> availablePartners) {
        DeliveryPartner nearestPartner = null;
        double minDistance = Double.MAX_VALUE;
        
        for (DeliveryPartner partner : availablePartners) {
            if (partner.status == PartnerStatus.AVAILABLE) {
                double distance = restaurant.location.calculateDistance(partner.location);
                if (distance < minDistance) {
                    minDistance = distance;
                    nearestPartner = partner;
                }
            }
        }
        return nearestPartner;
    }
}

class HighestRatedStrategy implements DeliveryAssignmentStrategy {
    public DeliveryPartner assignPartner(Restaurant restaurant, List<DeliveryPartner> availablePartners) {
        return availablePartners.stream()
                .filter(partner -> partner.status == PartnerStatus.AVAILABLE)
                .max(Comparator.comparing(partner -> partner.rating))
                .orElse(null);
    }
}
```

### **Singleton Pattern for Managers:**
```java
class OrderManager {
    private static OrderManager instance;
    private Map<String, Order> orders = new HashMap<>();
    private Map<String, Restaurant> restaurants = new HashMap<>();
    
    private OrderManager() {}
    
    public static OrderManager getInstance() {
        if (instance == null) {
            instance = new OrderManager();
        }
        return instance;
    }
    
    public Order placeOrder(String customerId, String restaurantId) {
        // Order placement logic
        String orderId = "ORD_" + UUID.randomUUID().toString().substring(0, 8);
        Order order = new Order(orderId, customerId, restaurantId);
        orders.put(orderId, order);
        return order;
    }
    
    public void addRestaurant(Restaurant restaurant) {
        restaurants.put(restaurant.id, restaurant);
    }
    
    public Restaurant getRestaurant(String restaurantId) {
        return restaurants.get(restaurantId);
    }
}

class DeliveryManager {
    private static DeliveryManager instance;
    private Map<String, DeliveryPartner> deliveryPartners = new HashMap<>();
    private DeliveryAssignmentStrategy assignmentStrategy = new NearestPartnerStrategy();
    
    private DeliveryManager() {}
    
    public static DeliveryManager getInstance() {
        if (instance == null) {
            instance = new DeliveryManager();
        }
        return instance;
    }
    
    public boolean assignDeliveryPartner(String orderId, String restaurantId) {
        OrderManager orderManager = OrderManager.getInstance();
        Order order = orderManager.getOrder(orderId);
        Restaurant restaurant = orderManager.getRestaurant(restaurantId);
        
        List<DeliveryPartner> availablePartners = getAvailablePartners();
        DeliveryPartner assignedPartner = assignmentStrategy.assignPartner(restaurant, availablePartners);
        
        if (assignedPartner != null) {
            assignedPartner.acceptOrder(orderId);
            order.deliveryPartnerId = assignedPartner.id;
            order.status = OrderStatus.OUT_FOR_DELIVERY;
            return true;
        }
        return false;
    }
    
    public void setAssignmentStrategy(DeliveryAssignmentStrategy strategy) {
        this.assignmentStrategy = strategy;
    }
}
```

### **Service Layer:**
```java
class FoodDeliveryService {
    private OrderManager orderManager = OrderManager.getInstance();
    private DeliveryManager deliveryManager = DeliveryManager.getInstance();
    private Map<String, User> users = new HashMap<>();
    
    // User Management
    public void registerUser(User user) {
        users.put(user.id, user);
        System.out.println("User registered: " + user.name + " (" + user.role + ")");
    }
    
    // Restaurant Management
    public void addRestaurant(Restaurant restaurant) {
        orderManager.addRestaurant(restaurant);
    }
    
    // Order Management
    public Order placeOrder(String customerId, String restaurantId) {
        return orderManager.placeOrder(customerId, restaurantId);
    }
    
    public void addItemToOrder(String orderId, String foodId, int quantity) {
        orderManager.addItemToOrder(orderId, foodId, quantity);
    }
    
    public void confirmOrder(String orderId) {
        orderManager.confirmOrder(orderId);
    }
    
    // Delivery Management
    public void registerDeliveryPartner(DeliveryPartner partner) {
        deliveryManager.registerDeliveryPartner(partner);
    }
    
    public boolean assignDeliveryPartner(String orderId) {
        Order order = orderManager.getOrder(orderId);
        return deliveryManager.assignDeliveryPartner(orderId, order.restaurantId);
    }
    
    public void completeDelivery(String orderId, double deliveryFee) {
        deliveryManager.completeDelivery(orderId, deliveryFee);
    }
}
```

**Say:** *"Now it's much more extensible and follows design patterns!"*

---

## 🎯 **Your Interview Mindset:**

### **Think Step by Step:**
1. **"What are the main entities?"** → User, Restaurant, Order, DeliveryPartner, FoodInfo
2. **"What are the main operations?"** → Place Order, Add Items, Assign Delivery, Track Order
3. **"How can I make it scalable?"** → Managers with Singleton, Service Layer
4. **"How can I make it flexible?"** → Strategy Pattern for Assignment

### **Always Explain:**
- *"I'm starting simple and building up..."*
- *"Let me add this for better organization..."*
- *"This pattern will make it more flexible..."*
- *"I can extend this easily for new requirements..."*

### **If Asked About Extensions:**
- **New assignment strategy?** *"Just implement DeliveryAssignmentStrategy interface"*
- **Different user types?** *"Add to UserRole enum and extend User class"*
- **Payment processing?** *"Add PaymentStrategy pattern"*
- **Real-time tracking?** *"Add Observer pattern for status updates"*

---

## 📱 **Demo Script:**

### **Step 1: Setup System**
```java
FoodDeliveryService service = new FoodDeliveryService();

// Create locations
Location customerLoc = new Location(12.9716, 77.5946, "Customer Location");
Location restaurantLoc = new Location(12.9726, 77.5956, "Restaurant Location");
Location partnerLoc = new Location(12.9706, 77.5936, "Partner Location");
```

### **Step 2: Register Users**
```java
User customer = new User("C1", "John Doe", "9876543210", UserRole.CUSTOMER, customerLoc);
User restaurantOwner = new User("R1", "Jane Smith", "9876543211", UserRole.RESTAURANT_OWNER, restaurantLoc);

service.registerUser(customer);
service.registerUser(restaurantOwner);
```

### **Step 3: Add Restaurant & Menu**
```java
Restaurant restaurant = new Restaurant("REST1", "Pizza Palace", "R1", restaurantLoc);
service.addRestaurant(restaurant);

FoodInfo pizza = new FoodInfo("F1", "Margherita Pizza", "Classic pizza", 299.99, FoodType.VEG);
FoodInfo burger = new FoodInfo("F2", "Chicken Burger", "Grilled burger", 199.99, FoodType.NON_VEG);

service.addMenuItemToRestaurant("REST1", pizza);
service.addMenuItemToRestaurant("REST1", burger);
```

### **Step 4: Register Delivery Partner**
```java
DeliveryPartner partner = new DeliveryPartner("DP1", "Mike Wilson", "9876543212", partnerLoc, "KA01AB1234");
service.registerDeliveryPartner(partner);
```

### **Step 5: Place & Process Order**
```java
Order order = service.placeOrder("C1", "REST1");
service.addItemToOrder(order.id, "F1", 2);
service.addItemToOrder(order.id, "F2", 1);
service.confirmOrder(order.id);

// Simulate restaurant preparation
OrderManager.getInstance().updateOrderStatus(order.id, OrderStatus.READY_FOR_PICKUP);

// Assign delivery partner
service.assignDeliveryPartner(order.id);

// Complete delivery
service.completeDelivery(order.id, 50.0);
```

---

## ✅ **Success Checklist:**

- ✅ Started simple with basic classes
- ✅ Built functionality incrementally  
- ✅ Explained reasoning at each step
- ✅ Added design patterns naturally
- ✅ Showed extensibility
- ✅ Handled edge cases
- ✅ Demonstrated clean code principles

**Remember: It's not about perfect code, it's about showing your thought process and system design skills!**