package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Channel {
    private String id;
    private String ownerUserId;
    private String channelName;
    private String description;
    private String category;
    private List<String> tags;
    private List<String> subscribers; // User IDs
    private List<String> contents; // Content IDs
    private Map<String, Long> stats; // subscriberCount, totalViews, etc.
    private Map<String, String> metadata;
    private boolean isActive;

    public Channel(String id, String ownerUserId, String channelName, String description, String category) {
        this.id = id;
        this.ownerUserId = ownerUserId;
        this.channelName = channelName;
        this.description = description;
        this.category = category;
        this.tags = new ArrayList<>();
        this.subscribers = new ArrayList<>();
        this.contents = new ArrayList<>();
        this.stats = new HashMap<>();
        this.metadata = new HashMap<>();
        this.isActive = true;
        
        // Initialize default stats
        this.stats.put("subscriberCount", 0L);
        this.stats.put("totalViews", 0L);
        this.stats.put("totalContent", 0L);
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(String ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getTags() {
        return new ArrayList<>(tags);
    }

    public void addTag(String tag) {
        if (!tags.contains(tag)) {
            tags.add(tag);
        }
    }

    public void removeTag(String tag) {
        tags.remove(tag);
    }

    public List<String> getSubscribers() {
        return new ArrayList<>(subscribers);
    }

    public void addSubscriber(String userId) {
        if (!subscribers.contains(userId)) {
            subscribers.add(userId);
            updateStats("subscriberCount", (long) subscribers.size());
        }
    }

    public void removeSubscriber(String userId) {
        if (subscribers.remove(userId)) {
            updateStats("subscriberCount", (long) subscribers.size());
        }
    }

    public List<String> getContents() {
        return new ArrayList<>(contents);
    }

    public void addContent(String contentId) {
        if (!contents.contains(contentId)) {
            contents.add(contentId);
            updateStats("totalContent", (long) contents.size());
        }
    }

    public void removeContent(String contentId) {
        if (contents.remove(contentId)) {
            updateStats("totalContent", (long) contents.size());
        }
    }

    public Map<String, Long> getStats() {
        return new HashMap<>(stats);
    }

    public Long getStat(String key) {
        return stats.get(key);
    }

    public void updateStats(String key, Long value) {
        stats.put(key, value);
    }

    public void incrementStat(String key, Long increment) {
        stats.put(key, stats.getOrDefault(key, 0L) + increment);
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

    public int getSubscriberCount() {
        return subscribers.size();
    }

    public int getContentCount() {
        return contents.size();
    }

    @Override
    public String toString() {
        return "Channel{" +
                "id='" + id + '\'' +
                ", channelName='" + channelName + '\'' +
                ", category='" + category + '\'' +
                ", subscribers=" + subscribers.size() +
                ", contents=" + contents.size() +
                ", isActive=" + isActive +
                '}';
    }
}