package models;

import java.time.LocalDateTime;

public class Comment {
    private String id;
    private String contentId;
    private String userId;
    private String text;
    private long likes;
    private long dislikes;
    private LocalDateTime timestamp;

    public Comment(String id, String contentId, String userId, String text) {
        this.id = id;
        this.contentId = contentId;
        this.userId = userId;
        this.text = text;
        this.likes = 0;
        this.dislikes = 0;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public double getLikeRatio() {
        long totalReactions = likes + dislikes;
        if (totalReactions == 0) {
            return 0.0;
        }
        return (double) likes / totalReactions;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", text='" + text + '\'' +
                ", likes=" + likes +
                ", dislikes=" + dislikes +
                ", timestamp=" + timestamp +
                '}';
    }
}