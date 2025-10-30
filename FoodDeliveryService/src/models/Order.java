package models;

import enums.OrderStatus;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Order {
    private String id;
    private String customerId;
    private String restaurantId;
    private Map<FoodInfo, Integer> orderedItems;
    private OrderStatus status;
    private double totalAmount;
    private LocalDateTime orderTime;
    private LocalDateTime deliveryTime;
    private String deliveryPartnerId;

    public Order(String id, String customerId, String restaurantId) {
        this.id = id;
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.orderedItems = new HashMap<>();
        this.status = OrderStatus.PLACED;
        this.totalAmount = 0.0;
        this.orderTime = LocalDateTime.now();
    }

    public void addItem(FoodInfo foodItem, int quantity) {
        orderedItems.put(foodItem, orderedItems.getOrDefault(foodItem, 0) + quantity);
        calculateTotalAmount();
    }

    public void removeItem(FoodInfo foodItem) {
        orderedItems.remove(foodItem);
        calculateTotalAmount();
    }

    public void updateItemQuantity(FoodInfo foodItem, int quantity) {
        if (quantity <= 0) {
            removeItem(foodItem);
        } else {
            orderedItems.put(foodItem, quantity);
        }
        calculateTotalAmount();
    }

    private void calculateTotalAmount() {
        totalAmount = orderedItems.entrySet().stream()
                .mapToDouble(entry -> entry.getKey().getPrice() * entry.getValue())
                .sum();
    }

    public List<String> getOrderSummary() {
        List<String> summary = new ArrayList<>();
        for (Map.Entry<FoodInfo, Integer> entry : orderedItems.entrySet()) {
            FoodInfo food = entry.getKey();
            int quantity = entry.getValue();
            summary.add(String.format("%s x %d = ₹%.2f", 
                food.getName(), quantity, food.getPrice() * quantity));
        }
        return summary;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Map<FoodInfo, Integer> getOrderedItems() {
        return new HashMap<>(orderedItems);
    }

    public void setOrderedItems(Map<FoodInfo, Integer> orderedItems) {
        this.orderedItems = orderedItems;
        calculateTotalAmount();
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }

    public LocalDateTime getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(LocalDateTime deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getDeliveryPartnerId() {
        return deliveryPartnerId;
    }

    public void setDeliveryPartnerId(String deliveryPartnerId) {
        this.deliveryPartnerId = deliveryPartnerId;
    }

    public int getTotalItems() {
        return orderedItems.values().stream().mapToInt(Integer::intValue).sum();
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", customerId='" + customerId + '\'' +
                ", restaurantId='" + restaurantId + '\'' +
                ", totalItems=" + getTotalItems() +
                ", status=" + status +
                ", totalAmount=" + totalAmount +
                ", orderTime=" + orderTime +
                '}';
    }
}