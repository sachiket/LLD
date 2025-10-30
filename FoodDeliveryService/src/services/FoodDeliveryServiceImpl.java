package services;

import models.Restaurant;
import models.FoodInfo;
import models.Order;
import models.DeliveryPartner;
import models.User;
import managers.OrderManager;
import managers.DeliveryManager;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class FoodDeliveryServiceImpl implements FoodDeliveryService {
    
    private OrderManager orderManager;
    private DeliveryManager deliveryManager;
    private Map<String, User> users;
    
    public FoodDeliveryServiceImpl() {
        this.orderManager = OrderManager.getInstance();
        this.deliveryManager = DeliveryManager.getInstance();
        this.users = new HashMap<>();
    }
    
    // Restaurant Management
    @Override
    public void addRestaurant(Restaurant restaurant) {
        orderManager.addRestaurant(restaurant);
    }
    
    @Override
    public Restaurant getRestaurant(String restaurantId) {
        return orderManager.getRestaurant(restaurantId);
    }
    
    @Override
    public List<Restaurant> getAllRestaurants() {
        return orderManager.getAllRestaurants();
    }
    
    @Override
    public void addMenuItemToRestaurant(String restaurantId, FoodInfo foodItem) {
        Restaurant restaurant = orderManager.getRestaurant(restaurantId);
        if (restaurant != null) {
            restaurant.addMenuItem(foodItem);
            System.out.println("Menu item added: " + foodItem.getName() + " to " + restaurant.getName());
        } else {
            throw new IllegalArgumentException("Restaurant not found: " + restaurantId);
        }
    }
    
    @Override
    public List<FoodInfo> getRestaurantMenu(String restaurantId) {
        Restaurant restaurant = orderManager.getRestaurant(restaurantId);
        if (restaurant != null) {
            return restaurant.getAvailableItems();
        }
        throw new IllegalArgumentException("Restaurant not found: " + restaurantId);
    }
    
    // Order Management
    @Override
    public Order placeOrder(String customerId, String restaurantId) {
        return orderManager.placeOrder(customerId, restaurantId);
    }
    
    @Override
    public void addItemToOrder(String orderId, String foodId, int quantity) {
        orderManager.addItemToOrder(orderId, foodId, quantity);
    }
    
    @Override
    public void confirmOrder(String orderId) {
        orderManager.confirmOrder(orderId);
    }
    
    @Override
    public void cancelOrder(String orderId) {
        orderManager.cancelOrder(orderId);
    }
    
    @Override
    public Order trackOrder(String orderId) {
        return orderManager.getOrder(orderId);
    }
    
    @Override
    public List<Order> getOrderHistory(String customerId) {
        return orderManager.getOrdersByCustomer(customerId);
    }
    
    // Delivery Management
    @Override
    public void registerDeliveryPartner(DeliveryPartner partner) {
        deliveryManager.registerDeliveryPartner(partner);
    }
    
    @Override
    public boolean assignDeliveryPartner(String orderId) {
        Order order = orderManager.getOrder(orderId);
        if (order != null) {
            return deliveryManager.assignDeliveryPartner(orderId, order.getRestaurantId());
        }
        return false;
    }
    
    @Override
    public void completeDelivery(String orderId, double deliveryFee) {
        deliveryManager.completeDelivery(orderId, deliveryFee);
    }
    
    @Override
    public List<DeliveryPartner> getAvailableDeliveryPartners() {
        return deliveryManager.getAvailablePartners();
    }
    
    // User Management
    @Override
    public void registerUser(User user) {
        users.put(user.getId(), user);
        System.out.println("User registered: " + user.getName() + " (" + user.getRole() + ")");
    }
    
    @Override
    public User getUser(String userId) {
        return users.get(userId);
    }
}