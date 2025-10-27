package models;

import enums.UserStatus;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Represents a user in the messaging system.
 * 
 * This class encapsulates all user-related data and behavior including:
 * - Basic user information (name, email, unique ID)
 * - Online status management
 * - Contact list management
 * - User blocking functionality
 * - Last seen timestamp tracking
 * 
 * Design Patterns Demonstrated:
 * - Encapsulation: Private fields with controlled access
 * - Immutable ID: UUID generated once and never changed
 * 
 * SOLID Principles:
 * - Single Responsibility: Manages only user-related data and behavior
 * - Open/Closed: Can be extended without modification
 * 
 * @author MessagingSystem
 * @version 1.0
 */
public class User {
    /** Unique identifier for the user, generated once and immutable */
    private final String id;
    
    /** User's display name */
    private String name;
    
    /** User's email address */
    private String email;
    
    /** Current online status of the user */
    private UserStatus status;
    
    /** Timestamp of when user was last seen/active */
    private LocalDateTime lastSeen;
    
    /** Set of user IDs that this user has added as contacts */
    private final Set<String> contacts;
    
    /** Set of user IDs that this user has blocked */
    private final Set<String> blockedUsers;

    /**
     * Constructor to create a new user.
     * 
     * @param name User's display name (cannot be null or empty)
     * @param email User's email address (cannot be null or empty)
     * @throws IllegalArgumentException if name or email is null/empty
     */
    public User(String name, String email) {
        // Generate unique ID for the user
        this.id = UUID.randomUUID().toString();
        
        // Set basic user information
        this.name = name;
        this.email = email;
        
        // Initialize user as offline
        this.status = UserStatus.OFFLINE;
        this.lastSeen = LocalDateTime.now();
        
        // Initialize empty collections
        this.contacts = new HashSet<>();
        this.blockedUsers = new HashSet<>();
    }

    // ==================== STATUS MANAGEMENT METHODS ====================

    /**
     * Sets the user status to online and updates last seen timestamp.
     * This method is called when user connects to the messaging system.
     */
    public void goOnline() {
        this.status = UserStatus.ONLINE;
        this.lastSeen = LocalDateTime.now();
    }

    /**
     * Sets the user status to offline and updates last seen timestamp.
     * This method is called when user disconnects from the messaging system.
     */
    public void goOffline() {
        this.status = UserStatus.OFFLINE;
        this.lastSeen = LocalDateTime.now();
    }

    // ==================== CONTACT MANAGEMENT METHODS ====================

    /**
     * Adds another user to this user's contact list.
     * 
     * @param userId The ID of the user to add as contact
     * @return true if user was added, false if userId is null or same as this user
     */
    public void addContact(String userId) {
        // Validate input and prevent self-addition
        if (userId != null && !userId.equals(this.id)) {
            contacts.add(userId);
        }
    }

    /**
     * Blocks another user, preventing them from sending messages.
     * Also removes them from contacts if they were added.
     * 
     * @param userId The ID of the user to block
     */
    public void blockUser(String userId) {
        // Validate input and prevent self-blocking
        if (userId != null && !userId.equals(this.id)) {
            blockedUsers.add(userId);
            // Remove from contacts if they were added
            contacts.remove(userId);
        }
    }

    /**
     * Checks if a specific user is blocked by this user.
     * 
     * @param userId The ID of the user to check
     * @return true if the user is blocked, false otherwise
     */
    public boolean isBlocked(String userId) {
        return blockedUsers.contains(userId);
    }

    /**
     * Checks if a specific user is in this user's contact list.
     * 
     * @param userId The ID of the user to check
     * @return true if the user is a contact, false otherwise
     */
    public boolean isContact(String userId) {
        return contacts.contains(userId);
    }

    // ==================== GETTER METHODS ====================

    /**
     * Gets the unique identifier of this user.
     * 
     * @return The user's unique ID
     */
    public String getId() { 
        return id; 
    }

    /**
     * Gets the display name of this user.
     * 
     * @return The user's name
     */
    public String getName() { 
        return name; 
    }

    /**
     * Gets the email address of this user.
     * 
     * @return The user's email
     */
    public String getEmail() { 
        return email; 
    }

    /**
     * Gets the current online status of this user.
     * 
     * @return The user's current status
     */
    public UserStatus getStatus() { 
        return status; 
    }

    /**
     * Gets the timestamp when this user was last seen.
     * 
     * @return The last seen timestamp
     */
    public LocalDateTime getLastSeen() { 
        return lastSeen; 
    }

    /**
     * Gets a copy of this user's contact list.
     * Returns a new HashSet to prevent external modification.
     * 
     * @return A copy of the user's contacts
     */
    public Set<String> getContacts() { 
        return new HashSet<>(contacts); 
    }

    // ==================== SETTER METHODS WITH VALIDATION ====================

    /**
     * Sets the user's display name with validation.
     * 
     * @param name The new name (cannot be null or empty)
     */
    public void setName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name.trim();
        }
    }

    /**
     * Sets the user's status and updates last seen timestamp.
     * 
     * @param status The new status (cannot be null)
     */
    public void setStatus(UserStatus status) {
        if (status != null) {
            this.status = status;
            this.lastSeen = LocalDateTime.now();
        }
    }

    // ==================== OBJECT METHODS ====================

    /**
     * Checks equality based on user ID.
     * Two users are equal if they have the same ID.
     * 
     * @param obj The object to compare with
     * @return true if objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return Objects.equals(id, user.id);
    }

    /**
     * Generates hash code based on user ID.
     * 
     * @return Hash code for this user
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Returns string representation of the user.
     * 
     * @return String containing user's basic information
     */
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", status=" + status +
                '}';
    }
}
