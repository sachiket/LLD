package enums;

/**
 * Enum representing the delivery and read status of a message in the messaging system.
 * 
 * This enum tracks the lifecycle of a message from creation to final delivery:
 * - SENT: Message has been created and sent by the sender
 * - DELIVERED: Message has reached the recipient's device/system
 * - READ: Message has been opened and read by the recipient
 * - FAILED: Message delivery failed due to some error
 * - PENDING: Message is waiting to be processed/delivered
 * 
 * Design Pattern: State pattern concept - represents different states of a message
 * SOLID Principle: Single Responsibility - Each status represents one state
 * 
 * @author MessagingSystem
 * @version 1.0
 */
public enum MessageStatus {
    /** Message has been successfully sent from sender */
    SENT("Sent"),
    
    /** Message has been delivered to recipient's system */
    DELIVERED("Delivered"),
    
    /** Message has been read by the recipient */
    READ("Read"),
    
    /** Message delivery failed */
    FAILED("Failed"),
    
    /** Message is pending delivery */
    PENDING("Pending");

    /** Human-readable display name for the status */
    private final String displayName;

    /**
     * Constructor for MessageStatus enum.
     * 
     * @param displayName Human-readable name for the status
     */
    MessageStatus(String displayName) {
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
     * Checks if the message has been delivered to the recipient.
     * A message is considered delivered if it's either DELIVERED or READ.
     * 
     * @return true if message has been delivered, false otherwise
     */
    public boolean isDelivered() {
        return this == DELIVERED || this == READ;
    }

    /**
     * Checks if the message has been read by the recipient.
     * 
     * @return true if message has been read, false otherwise
     */
    public boolean isRead() {
        return this == READ;
    }

    /**
     * Checks if the message delivery failed.
     * 
     * @return true if message delivery failed, false otherwise
     */
    public boolean isFailed() {
        return this == FAILED;
    }
}
