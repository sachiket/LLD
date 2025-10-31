package models;

import enums.UserRole;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    private String id;
    private String name;
    private UserRole role;
    private List<String> subscriptions; // Channel IDs user follows
    private List<String> createdChannels; // Channel IDs user owns
    private Map<String, String> metadata; // Extra info like bio, preferences
    private boolean isActive;

    public User(String id, String name, UserRole role) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.subscriptions = new ArrayList<>();
        this.createdChannels = new ArrayList<>();
        this.metadata = new HashMap<>();
        this.isActive = true;
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

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public List<String> getSubscriptions() {
        return new ArrayList<>(subscriptions);
    }

    public void addSubscription(String channelId) {
        if (!subscriptions.contains(channelId)) {
            subscriptions.add(channelId);
        }
    }

    public void removeSubscription(String channelId) {
        subscriptions.remove(channelId);
    }

    public List<String> getCreatedChannels() {
        return new ArrayList<>(createdChannels);
    }

    public void addCreatedChannel(String channelId) {
        if (!createdChannels.contains(channelId)) {
            createdChannels.add(channelId);
        }
    }

    public Map<String, String> getMetadata() {
        return new HashMap<>(metadata);
    }

    public void setMetadata(String key, String value) {
        metadata.put(key, value);
    }

    public String getMetadata(String key) {
        return metadata.get(key);
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", role=" + role +
                ", subscriptions=" + subscriptions.size() +
                ", createdChannels=" + createdChannels.size() +
                ", isActive=" + isActive +
                '}';
    }
}