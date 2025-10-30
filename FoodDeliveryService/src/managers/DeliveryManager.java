package managers;

import models.DeliveryPartner;
import models.Restaurant;
import models.Order;
import strategies.DeliveryAssignmentStrategy;
import strategies.NearestPartnerStrategy;
import enums.OrderStatus;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class DeliveryManager {
    private static DeliveryManager instance;
    private Map<String, DeliveryPartner> deliveryPartners;
    private DeliveryAssignmentStrategy assignmentStrategy;

    private DeliveryManager() {
        this.deliveryPartners = new HashMap<>();
        this.assignmentStrategy = new NearestPartnerStrategy();
    }

    public static DeliveryManager getInstance() {
        if (instance == null) {
            instance = new DeliveryManager();
        }
        return instance;
    }

    public void registerDeliveryPartner(DeliveryPartner partner) {
        deliveryPartners.put(partner.getId(), partner);
        System.out.println("Delivery partner registered: " + partner.getName());
    }

    public DeliveryPartner getDeliveryPartner(String partnerId) {
        return deliveryPartners.get(partnerId);
    }

    public List<DeliveryPartner> getAvailablePartners() {
        return deliveryPartners.values().stream()
                .filter(DeliveryPartner::isAvailable)
                .toList();
    }

    public boolean assignDeliveryPartner(String orderId, String restaurantId) {
        OrderManager orderManager = OrderManager.getInstance();
        Order order = orderManager.getOrder(orderId);
        Restaurant restaurant = orderManager.getRestaurant(restaurantId);
        
        if (order == null || restaurant == null) {
            System.out.println("Order or restaurant not found");
            return false;
        }

        if (order.getStatus() != OrderStatus.READY_FOR_PICKUP) {
            System.out.println("Order not ready for pickup. Current status: " + order.getStatus());
            return false;
        }

        List<DeliveryPartner> availablePartners = getAvailablePartners();
        if (availablePartners.isEmpty()) {
            System.out.println("No available delivery partners");
            return false;
        }

        DeliveryPartner assignedPartner = assignmentStrategy.assignPartner(restaurant, availablePartners);
        
        if (assignedPartner != null) {
            assignedPartner.acceptOrder(orderId);
            order.setDeliveryPartnerId(assignedPartner.getId());
            order.setStatus(OrderStatus.OUT_FOR_DELIVERY);
            
            System.out.println("Order " + orderId + " assigned to delivery partner: " + assignedPartner.getName());
            return true;
        }

        System.out.println("Failed to assign delivery partner for order: " + orderId);
        return false;
    }

    public void completeDelivery(String orderId, double deliveryFee) {
        OrderManager orderManager = OrderManager.getInstance();
        Order order = orderManager.getOrder(orderId);
        
        if (order == null) {
            System.out.println("Order not found: " + orderId);
            return;
        }

        String partnerId = order.getDeliveryPartnerId();
        if (partnerId == null) {
            System.out.println("No delivery partner assigned to order: " + orderId);
            return;
        }

        DeliveryPartner partner = deliveryPartners.get(partnerId);
        if (partner != null) {
            partner.completeDelivery(deliveryFee);
            order.setStatus(OrderStatus.DELIVERED);
            System.out.println("Delivery completed for order: " + orderId);
        }
    }

    public void setAssignmentStrategy(DeliveryAssignmentStrategy strategy) {
        this.assignmentStrategy = strategy;
    }

    public List<DeliveryPartner> getAllPartners() {
        return new ArrayList<>(deliveryPartners.values());
    }
}