package models;

import enums.MessageStatus;
import enums.MessageType;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a message in the messaging system
 * Demonstrates encapsulation and proper state management
 */
public class Message {
    private final String id;
    private final String senderId;
    private final String receiverId;
    private final String content;
    private final MessageType type;
    private final LocalDateTime timestamp;
    private MessageStatus status;
    private LocalDateTime deliveredAt;
    private LocalDateTime readAt;

    public Message(String senderId, String receiverId, String content, MessageType type) {
        this.id = UUID.randomUUID().toString();
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.type = type;
        this.timestamp = LocalDateTime.now();
        this.status = MessageStatus.SENT;
    }

    // Business logic methods
    public void markAsDelivered() {
        if (this.status == MessageStatus.SENT) {
            this.status = MessageStatus.DELIVERED;
            this.deliveredAt = LocalDateTime.now();
        }
    }

    public void markAsRead() {
        if (this.status == MessageStatus.DELIVERED || this.status == MessageStatus.SENT) {
            this.status = MessageStatus.READ;
            this.readAt = LocalDateTime.now();
            if (this.deliveredAt == null) {
                this.deliveredAt = this.readAt;
            }
        }
    }

    public void markAsFailed() {
        this.status = MessageStatus.FAILED;
    }

    public boolean isDelivered() {
        return status.isDelivered();
    }

    public boolean isRead() {
        return status.isRead();
    }

    // Getters
    public String getId() { return id; }
    public String getSenderId() { return senderId; }
    public String getReceiverId() { return receiverId; }
    public String getContent() { return content; }
    public MessageType getType() { return type; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public MessageStatus getStatus() { return status; }
    public LocalDateTime getDeliveredAt() { return deliveredAt; }
    public LocalDateTime getReadAt() { return readAt; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Message message = (Message) obj;
        return Objects.equals(id, message.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
