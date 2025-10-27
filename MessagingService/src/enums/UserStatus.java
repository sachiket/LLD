package enums;

/**
 * Enum representing the online status of a user in the messaging system.
 * 
 * This enum provides different user states that affect message delivery behavior:
 * - ONLINE: User is actively using the application
 * - OFFLINE: User is not connected to the application
 * - AWAY: User is connected but inactive
 * - BUSY: User is connected but should not be disturbed
 * - INVISIBLE: User appears offline to others but can receive messages
 * 
 * Design Pattern: Type-safe enumeration pattern
 * SOLID Principle: Single Responsibility - Each status has a clear meaning
 * 
 * @author MessagingSystem
 * @version 1.0
 */
public enum UserStatus {
    /** User is actively online and available for messaging */
    ONLINE("Online"),
    
    /** User is completely offline and not available */
    OFFLINE("Offline"),
    
    /** User is online but inactive/away from device */
    AWAY("Away"),
    
    /** User is online but busy and should not be disturbed */
    BUSY("Busy"),
    
    /** User appears offline to others but can still receive messages */
    INVISIBLE("Invisible");

    /** Human-readable display name for the status */
    private final String displayName;

    /**
     * Constructor for UserStatus enum.
     * 
     * @param displayName Human-readable name for the status
     */
    UserStatus(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets the human-readable display name for this status.
     * 
     * @return The display name of the status
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Determines if a user with this status is available for messaging.
     * Used by the Strategy pattern for message delivery decisions.
     * 
     * @return true if user can receive messages immediately, false otherwise
     */
    public boolean isAvailableForMessaging() {
        return this == ONLINE || this == AWAY;
    }
}