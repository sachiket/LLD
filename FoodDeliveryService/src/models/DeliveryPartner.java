package models;

import enums.UserRole;
import enums.PartnerStatus;
import java.util.ArrayList;
import java.util.List;

public class DeliveryPartner extends User {
    private PartnerStatus status;
    private String vehicleNumber;
    private double rating;
    private int totalDeliveries;
    private double totalEarnings;
    private List<String> deliveryHistory;

    public DeliveryPartner(String id, String name, String phone, Location location) {
        super(id, name, phone, UserRole.DELIVERY_PARTNER, location);
        this.status = PartnerStatus.AVAILABLE;
        this.rating = 5.0;
        this.totalDeliveries = 0;
        this.totalEarnings = 0.0;
        this.deliveryHistory = new ArrayList<>();
    }

    public DeliveryPartner(String id, String name, String phone, Location location, String vehicleNumber) {
        this(id, name, phone, location);
        this.vehicleNumber = vehicleNumber;
    }

    public void acceptOrder(String orderId) {
        if (status == PartnerStatus.AVAILABLE) {
            status = PartnerStatus.BUSY;
            deliveryHistory.add(orderId);
            totalDeliveries++;
            System.out.println("Delivery partner " + getName() + " accepted order: " + orderId);
        }
    }

    public void completeDelivery(double earnings) {
        status = PartnerStatus.AVAILABLE;
        totalEarnings += earnings;
        System.out.println("Delivery completed by " + getName() + ". Earnings: ₹" + earnings);
    }

    public void updateRating(double newRating) {
        // Simple average rating calculation
        this.rating = ((rating * (totalDeliveries - 1)) + newRating) / totalDeliveries;
    }

    public boolean isAvailable() {
        return status == PartnerStatus.AVAILABLE && isActive();
    }

    // Getters and Setters
    public PartnerStatus getStatus() {
        return status;
    }

    public void setStatus(PartnerStatus status) {
        this.status = status;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getTotalDeliveries() {
        return totalDeliveries;
    }

    public double getTotalEarnings() {
        return totalEarnings;
    }

    public List<String> getDeliveryHistory() {
        return new ArrayList<>(deliveryHistory);
    }

    @Override
    public String toString() {
        return "DeliveryPartner{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", status=" + status +
                ", vehicleNumber='" + vehicleNumber + '\'' +
                ", rating=" + rating +
                ", totalDeliveries=" + totalDeliveries +
                ", totalEarnings=" + totalEarnings +
                '}';
    }
}