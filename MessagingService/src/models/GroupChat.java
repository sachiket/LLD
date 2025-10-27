package models;

import enums.ChatType;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a group chat with additional features like admins and group name
 * Demonstrates inheritance and specialized behavior
 */
public class GroupChat extends Chat {
    private String groupName;
    private final Set<String> admins;
    private String description;

    public GroupChat(String groupName) {
        super(ChatType.GROUP);
        this.groupName = groupName;
        this.admins = new HashSet<>();
        this.description = "";
    }

    // Business logic methods
    public void addAdmin(String userId) {
        if (userId != null && hasParticipant(userId)) {
            admins.add(userId);
        }
    }

    public void removeAdmin(String userId) {
        admins.remove(userId);
    }

    public boolean isAdmin(String userId) {
        return admins.contains(userId);
    }

    public boolean canModifyGroup(String userId) {
        return isAdmin(userId);
    }

    @Override
    public void removeParticipant(String userId) {
        super.removeParticipant(userId);
        // Remove admin status if participant is removed
        admins.remove(userId);
    }

    // Getters and Setters
    public String getGroupName() { return groupName; }
    public Set<String> getAdmins() { return new HashSet<>(admins); }
    public String getDescription() { return description; }

    public void setGroupName(String groupName) {
        if (groupName != null && !groupName.trim().isEmpty()) {
            this.groupName = groupName.trim();
        }
    }

    public void setDescription(String description) {
        this.description = description != null ? description : "";
    }
}
