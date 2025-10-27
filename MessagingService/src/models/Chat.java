package models;

import enums.ChatType;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Represents a chat conversation between participants
 * Demonstrates composition and proper state management
 */
public class Chat {
    private final String id;
    private final ChatType type;
    private final List<String> participants;
    private final List<String> messageIds;
    private final LocalDateTime createdAt;
    private LocalDateTime lastActivity;

    public Chat(ChatType type) {
        this.id = UUID.randomUUID().toString();
        this.type = type;
        this.participants = new ArrayList<>();
        this.messageIds = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.lastActivity = LocalDateTime.now();
    }

    // Business logic methods
    public void addParticipant(String userId) {
        if (userId != null && !participants.contains(userId)) {
            participants.add(userId);
            updateLastActivity();
        }
    }

    public void removeParticipant(String userId) {
        participants.remove(userId);
        updateLastActivity();
    }

    public void addMessage(String messageId) {
        if (messageId != null) {
            messageIds.add(messageId);
            updateLastActivity();
        }
    }

    public boolean hasParticipant(String userId) {
        return participants.contains(userId);
    }

    public boolean canAddParticipant() {
        return type == ChatType.GROUP || participants.size() < 2;
    }

    private void updateLastActivity() {
        this.lastActivity = LocalDateTime.now();
    }

    // Getters
    public String getId() { return id; }
    public ChatType getType() { return type; }
    public List<String> getParticipants() { return new ArrayList<>(participants); }
    public List<String> getMessageIds() { return new ArrayList<>(messageIds); }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getLastActivity() { return lastActivity; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Chat chat = (Chat) obj;
        return Objects.equals(id, chat.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
