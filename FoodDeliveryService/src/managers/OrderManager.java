package managers;

import models.Order;
import models.Restaurant;
import models.FoodInfo;
import enums.OrderStatus;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

public class OrderManager {
    private static OrderManager instance;
    private Map<String, Order> orders;
    private Map<String, Restaurant> restaurants;

    private OrderManager() {
        this.orders = new HashMap<>();
        this.restaurants = new HashMap<>();
    }

    public static OrderManager getInstance() {
        if (instance == null) {
            instance = new OrderManager();
        }
        return instance;
    }

    public void addRestaurant(Restaurant restaurant) {
        restaurants.put(restaurant.getId(), restaurant);
        System.out.println("Restaurant added: " + restaurant.getName());
    }

    public Restaurant getRestaurant(String restaurantId) {
        return restaurants.get(restaurantId);
    }

    public List<Restaurant> getAllRestaurants() {
        return new ArrayList<>(restaurants.values());
    }

    public Order placeOrder(String customerId, String restaurantId) {
        Restaurant restaurant = restaurants.get(restaurantId);
        if (restaurant == null || !restaurant.isActive()) {
            throw new IllegalArgumentException("Restaurant not found or inactive");
        }

        String orderId = "ORD_" + UUID.randomUUID().toString().substring(0, 8);
        Order order = new Order(orderId, customerId, restaurantId);
        orders.put(orderId, order);
        
        System.out.println("Order placed: " + orderId + " for customer: " + customerId);
        return order;
    }

    public void addItemToOrder(String orderId, String foodId, int quantity) {
        Order order = orders.get(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order not found");
        }

        if (order.getStatus() != OrderStatus.PLACED) {
            throw new IllegalStateException("Cannot modify order in current status: " + order.getStatus());
        }

        Restaurant restaurant = restaurants.get(order.getRestaurantId());
        FoodInfo foodItem = restaurant.getMenuItem(foodId);
        
        if (foodItem == null || !foodItem.isAvailable()) {
            throw new IllegalArgumentException("Food item not available");
        }

        order.addItem(foodItem, quantity);
        System.out.println("Added " + quantity + "x " + foodItem.getName() + " to order " + orderId);
    }

    public void confirmOrder(String orderId) {
        Order order = orders.get(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order not found");
        }

        if (order.getTotalItems() == 0) {
            throw new IllegalStateException("Cannot confirm empty order");
        }

        order.setStatus(OrderStatus.CONFIRMED);
        System.out.println("Order confirmed: " + orderId + " - Total: ₹" + order.getTotalAmount());
    }

    public void updateOrderStatus(String orderId, OrderStatus status) {
        Order order = orders.get(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order not found");
        }

        order.setStatus(status);
        System.out.println("Order " + orderId + " status updated to: " + status);
    }

    public void cancelOrder(String orderId) {
        Order order = orders.get(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order not found");
        }

        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new IllegalStateException("Cannot cancel delivered order");
        }

        order.setStatus(OrderStatus.CANCELLED);
        System.out.println("Order cancelled: " + orderId);
    }

    public Order getOrder(String orderId) {
        return orders.get(orderId);
    }

    public List<Order> getOrdersByCustomer(String customerId) {
        return orders.values().stream()
                .filter(order -> order.getCustomerId().equals(customerId))
                .toList();
    }

    public List<Order> getOrdersByRestaurant(String restaurantId) {
        return orders.values().stream()
                .filter(order -> order.getRestaurantId().equals(restaurantId))
                .toList();
    }

    public List<Order> getAllOrders() {
        return new ArrayList<>(orders.values());
    }
}