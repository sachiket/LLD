package models;

import enums.ContentType;
import enums.ContentVisibility;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Content {
    private String id;
    private String channelId;
    private String uploadedByUserId;
    private String title;
    private String description;
    private ContentType type;
    private String url; // Points to CDN/Blob storage
    private long views;
    private long likes;
    private long dislikes;
    private ContentVisibility visibility;
    private List<String> comments; // Comment IDs
    private LocalDateTime createdAt;
    private Map<String, String> metadata;

    public Content(String id, String channelId, String uploadedByUserId, String title, 
                   String description, ContentType type, String url) {
        this.id = id;
        this.channelId = channelId;
        this.uploadedByUserId = uploadedByUserId;
        this.title = title;
        this.description = description;
        this.type = type;
        this.url = url;
        this.views = 0;
        this.likes = 0;
        this.dislikes = 0;
        this.visibility = ContentVisibility.PUBLIC;
        this.comments = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.metadata = new HashMap<>();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getUploadedByUserId() {
        return uploadedByUserId;
    }

    public void setUploadedByUserId(String uploadedByUserId) {
        this.uploadedByUserId = uploadedByUserId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ContentType getType() {
        return type;
    }

    public void setType(ContentType type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getViews() {
        return views;
    }

    public void incrementViews() {
        this.views++;
    }

    public void setViews(long views) {
        this.views = views;
    }

    public long getLikes() {
        return likes;
    }

    public void incrementLikes() {
        this.likes++;
    }

    public void decrementLikes() {
        if (this.likes > 0) {
            this.likes--;
        }
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public long getDislikes() {
        return dislikes;
    }

    public void incrementDislikes() {
        this.dislikes++;
    }

    public void decrementDislikes() {
        if (this.dislikes > 0) {
            this.dislikes--;
        }
    }

    public void setDislikes(long dislikes) {
        this.dislikes = dislikes;
    }

    public ContentVisibility getVisibility() {
        return visibility;
    }

    public void setVisibility(ContentVisibility visibility) {
        this.visibility = visibility;
    }

    public List<String> getComments() {
        return new ArrayList<>(comments);
    }

    public void addComment(String commentId) {
        if (!comments.contains(commentId)) {
            comments.add(commentId);
        }
    }

    public void removeComment(String commentId) {
        comments.remove(commentId);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
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

    public boolean isPublic() {
        return visibility == ContentVisibility.PUBLIC;
    }

    public boolean isPrivate() {
        return visibility == ContentVisibility.PRIVATE;
    }

    public boolean isUnlisted() {
        return visibility == ContentVisibility.UNLISTED;
    }

    public double getLikeRatio() {
        long totalReactions = likes + dislikes;
        if (totalReactions == 0) {
            return 0.0;
        }
        return (double) likes / totalReactions;
    }

    public int getCommentCount() {
        return comments.size();
    }

    @Override
    public String toString() {
        return "Content{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", type=" + type +
                ", views=" + views +
                ", likes=" + likes +
                ", dislikes=" + dislikes +
                ", visibility=" + visibility +
                ", comments=" + comments.size() +
                ", createdAt=" + createdAt +
                '}';
    }
}