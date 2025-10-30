package models;

import java.util.ArrayList;
import java.util.List;

public class Restaurant {
    private String id;
    private String name;
    private String ownerId;
    private Location location;
    private List<FoodInfo> menu;
    private boolean isActive;
    private double rating;

    public Restaurant(String id, String name, String ownerId, Location location) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.location = location;
        this.menu = new ArrayList<>();
        this.isActive = true;
        this.rating = 5.0;
    }

    public void addMenuItem(FoodInfo foodItem) {
        menu.add(foodItem);
    }

    public void removeMenuItem(String foodId) {
        menu.removeIf(food -> food.getId().equals(foodId));
    }

    public FoodInfo getMenuItem(String foodId) {
        return menu.stream()
                .filter(food -> food.getId().equals(foodId))
                .findFirst()
                .orElse(null);
    }

    public List<FoodInfo> getAvailableItems() {
        return menu.stream()
                .filter(FoodInfo::isAvailable)
                .toList();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<FoodInfo> getMenu() {
        return new ArrayList<>(menu);
    }

    public void setMenu(List<FoodInfo> menu) {
        this.menu = menu;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", location=" + location +
                ", menuSize=" + menu.size() +
                ", isActive=" + isActive +
                ", rating=" + rating +
                '}';
    }
}