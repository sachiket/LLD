import models.*;
import enums.*;
import managers.DeliveryManager;
import managers.OrderManager;
import services.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Food Delivery Service Demo ===\n");
        
        // Initialize the food delivery service
        FoodDeliveryService deliveryService = new FoodDeliveryServiceImpl();
        
        // Create locations
        Location customerLocation = new Location(12.9716, 77.5946, "Bangalore, India");
        Location restaurantLocation = new Location(12.9726, 77.5956, "Restaurant Area, Bangalore");
        Location partnerLocation = new Location(12.9706, 77.5936, "Partner Location, Bangalore");
        
        // Create and register users
        User customer = new User("C1", "John Doe", "9876543210", UserRole.CUSTOMER, customerLocation);
        User restaurantOwner = new User("R1", "Jane Smith", "9876543211", UserRole.RESTAURANT_OWNER, restaurantLocation);
        
        deliveryService.registerUser(customer);
        deliveryService.registerUser(restaurantOwner);
        
        // Create and add restaurant
        Restaurant restaurant = new Restaurant("REST1", "Pizza Palace", "R1", restaurantLocation);
        deliveryService.addRestaurant(restaurant);
        
        // Add menu items
        FoodInfo pizza = new FoodInfo("F1", "Margherita Pizza", "Classic pizza with tomato and mozzarella", 299.99, FoodType.VEG);
        FoodInfo burger = new FoodInfo("F2", "Chicken Burger", "Grilled chicken burger with fries", 199.99, FoodType.NON_VEG);
        FoodInfo pasta = new FoodInfo("F3", "Vegan Pasta", "Delicious vegan pasta with vegetables", 249.99, FoodType.VEGAN);
        
        deliveryService.addMenuItemToRestaurant("REST1", pizza);
        deliveryService.addMenuItemToRestaurant("REST1", burger);
        deliveryService.addMenuItemToRestaurant("REST1", pasta);
        
        System.out.println("Restaurant and menu items added!\n");
        
        // Create and register delivery partner
        DeliveryPartner deliveryPartner = new DeliveryPartner("DP1", "Mike Wilson", "9876543212", partnerLocation, "KA01AB1234");
        deliveryService.registerDeliveryPartner(deliveryPartner);
        
        System.out.println("Delivery partner registered!\n");
        
        // Place an order
        System.out.println("--- Placing Order ---");
        Order order = deliveryService.placeOrder("C1", "REST1");
        
        // Add items to order
        deliveryService.addItemToOrder(order.getId(), "F1", 2);
        deliveryService.addItemToOrder(order.getId(), "F2", 1);
        
        // Confirm order
        deliveryService.confirmOrder(order.getId());
        
        System.out.println("\n--- Order Details ---");
        Order trackedOrder = deliveryService.trackOrder(order.getId());
        System.out.println("Order ID: " + trackedOrder.getId());
        System.out.println("Customer ID: " + trackedOrder.getCustomerId());
        System.out.println("Restaurant ID: " + trackedOrder.getRestaurantId());
        System.out.println("Status: " + trackedOrder.getStatus());
        System.out.println("Total Amount: ₹" + trackedOrder.getTotalAmount());
        System.out.println("Total Items: " + trackedOrder.getTotalItems());
        
        System.out.println("\nOrder Summary:");
        for (String item : trackedOrder.getOrderSummary()) {
            System.out.println("  " + item);
        }
        
        // Simulate order preparation
        System.out.println("\n--- Order Processing ---");
        OrderManager.getInstance().updateOrderStatus(order.getId(), OrderStatus.PREPARING);
        OrderManager.getInstance().updateOrderStatus(order.getId(), OrderStatus.READY_FOR_PICKUP);
        
        // Assign delivery partner
        System.out.println("\n--- Delivery Assignment ---");
        boolean assigned = deliveryService.assignDeliveryPartner(order.getId());
        if (assigned) {
            System.out.println("Delivery partner assigned successfully!");
        }
        
        // Complete delivery
        System.out.println("\n--- Completing Delivery ---");
        deliveryService.completeDelivery(order.getId(), 50.0);
        
        // Final order status
        Order finalOrder = deliveryService.trackOrder(order.getId());
        System.out.println("\n--- Final Order Status ---");
        System.out.println("Order Status: " + finalOrder.getStatus());
        System.out.println("Delivery Partner: " + finalOrder.getDeliveryPartnerId());
        
        // Show delivery partner stats
        DeliveryPartner partner = DeliveryManager.getInstance().getDeliveryPartner("DP1");
        System.out.println("\n--- Delivery Partner Stats ---");
        System.out.println("Partner: " + partner.getName());
        System.out.println("Total Deliveries: " + partner.getTotalDeliveries());
        System.out.println("Total Earnings: ₹" + partner.getTotalEarnings());
        System.out.println("Rating: " + partner.getRating());
        
        System.out.println("\n=== Food Delivery Demo Completed Successfully! ===");
    }
}