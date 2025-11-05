package models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a group in the Splitwise system
 * Groups contain multiple users and track expenses shared among them
 */
public class Group {
    private final String groupId;
    private final String name;
    private final String description;
    private final LocalDateTime createdAt;
    private final Set<User> members;
    private final List<String> expenseIds; // References to expenses in this group
    
    public Group(String groupId, String name, String description) {
        if (groupId == null || groupId.trim().isEmpty()) {
            throw new IllegalArgumentException("Group ID cannot be null or empty");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Group name cannot be null or empty");
        }
        
        this.groupId = groupId;
        this.name = name;
        this.description = description != null ? description : "";
        this.createdAt = LocalDateTime.now();
        this.members = new HashSet<>();
        this.expenseIds = new ArrayList<>();
    }
    
    public Group(String groupId, String name) {
        this(groupId, name, "");
    }
    
    /**
     * Adds a user to the group
     * @param user The user to add
     * @return true if the user was added, false if already a member
     */
    public boolean addMember(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        return members.add(user);
    }
    
    /**
     * Removes a user from the group
     * @param user The user to remove
     * @return true if the user was removed, false if not a member
     */
    public boolean removeMember(User user) {
        if (user == null) {
            return false;
        }
        return members.remove(user);
    }
    
    /**
     * Checks if a user is a member of this group
     * @param user The user to check
     * @return true if the user is a member
     */
    public boolean isMember(User user) {
        return user != null && members.contains(user);
    }
    
    /**
     * Checks if a user (by ID) is a member of this group
     * @param userId The user ID to check
     * @return true if the user is a member
     */
    public boolean isMember(String userId) {
        if (userId == null) {
            return false;
        }
        return members.stream().anyMatch(user -> user.getUserId().equals(userId));
    }
    
    /**
     * Gets a user from the group by their ID
     * @param userId The user ID to find
     * @return The user if found, null otherwise
     */
    public User getMemberById(String userId) {
        if (userId == null) {
            return null;
        }
        return members.stream()
                .filter(user -> user.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Adds an expense ID to this group's expense list
     * @param expenseId The expense ID to add
     */
    public void addExpense(String expenseId) {
        if (expenseId != null && !expenseIds.contains(expenseId)) {
            expenseIds.add(expenseId);
        }
    }
    
    /**
     * Removes an expense ID from this group's expense list
     * @param expenseId The expense ID to remove
     * @return true if the expense was removed
     */
    public boolean removeExpense(String expenseId) {
        return expenseIds.remove(expenseId);
    }
    
    /**
     * Gets the number of members in the group
     * @return The member count
     */
    public int getMemberCount() {
        return members.size();
    }
    
    /**
     * Gets the number of expenses in the group
     * @return The expense count
     */
    public int getExpenseCount() {
        return expenseIds.size();
    }
    
    /**
     * Checks if the group is empty (no members)
     * @return true if the group has no members
     */
    public boolean isEmpty() {
        return members.isEmpty();
    }
    
    /**
     * Validates if all provided user IDs are members of this group
     * @param userIds List of user IDs to validate
     * @return true if all users are members
     */
    public boolean validateMembers(List<String> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return false;
        }
        return userIds.stream().allMatch(this::isMember);
    }
    
    // Getters
    public String getGroupId() {
        return groupId;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public Set<User> getMembers() {
        return new HashSet<>(members); // Return a copy to prevent external modification
    }
    
    public List<String> getExpenseIds() {
        return new ArrayList<>(expenseIds); // Return a copy to prevent external modification
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Group group = (Group) obj;
        return groupId.equals(group.groupId);
    }
    
    @Override
    public int hashCode() {
        return groupId.hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("Group{id='%s', name='%s', members=%d, expenses=%d}", 
                           groupId, name, members.size(), expenseIds.size());
    }
}