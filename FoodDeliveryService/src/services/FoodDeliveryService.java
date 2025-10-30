package services;

import models.Restaurant;
import models.FoodInfo;
import models.Order;
import models.DeliveryPartner;
import models.User;
import java.util.List;

public interface FoodDeliveryService {
    
    // Restaurant Management
    void addRestaurant(Restaurant restaurant);
    Restaurant getRestaurant(String restaurantId);
    List<Restaurant> getAllRestaurants();
    void addMenuItemToRestaurant(String restaurantId, FoodInfo foodItem);
    List<FoodInfo> getRestaurantMenu(String restaurantId);
    
    // Order Management
    Order placeOrder(String customerId, String restaurantId);
    void addItemToOrder(String orderId, String foodId, int quantity);
    void confirmOrder(String orderId);
    void cancelOrder(String orderId);
    Order trackOrder(String orderId);
    List<Order> getOrderHistory(String customerId);
    
    // Delivery Management
    void registerDeliveryPartner(DeliveryPartner partner);
    boolean assignDeliveryPartner(String orderId);
    void completeDelivery(String orderId, double deliveryFee);
    List<DeliveryPartner> getAvailableDeliveryPartners();
    
    // User Management
    void registerUser(User user);
    User getUser(String userId);
}