package models;

import enums.SettlementStatus;
import java.time.LocalDateTime;

/**
 * Represents a settlement transaction between two users in the Splitwise system
 * Tracks payment made to settle balances
 */
public class Settlement {
    private final String settlementId;
    private final String fromUserId;
    private final String toUserId;
    private final double amount;
    private final LocalDateTime timestamp;
    private final String fromUserName;
    private final String toUserName;
    private SettlementStatus status;
    private String notes;
    
    public Settlement(String settlementId, String fromUserId, String toUserId, double amount, 
                     String fromUserName, String toUserName) {
        if (settlementId == null || settlementId.trim().isEmpty()) {
            throw new IllegalArgumentException("Settlement ID cannot be null or empty");
        }
        if (fromUserId == null || fromUserId.trim().isEmpty()) {
            throw new IllegalArgumentException("From user ID cannot be null or empty");
        }
        if (toUserId == null || toUserId.trim().isEmpty()) {
            throw new IllegalArgumentException("To user ID cannot be null or empty");
        }
        if (fromUserId.equals(toUserId)) {
            throw new IllegalArgumentException("From user and to user cannot be the same");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Settlement amount must be positive");
        }
        
        this.settlementId = settlementId;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
        this.fromUserName = fromUserName != null ? fromUserName : fromUserId;
        this.toUserName = toUserName != null ? toUserName : toUserId;
        this.status = SettlementStatus.PENDING;
        this.notes = "";
    }
    
    public Settlement(String settlementId, String fromUserId, String toUserId, double amount) {
        this(settlementId, fromUserId, toUserId, amount, null, null);
    }
    
    /**
     * Marks the settlement as completed
     */
    public void complete() {
        if (status.isFinalState()) {
            throw new IllegalStateException("Cannot complete settlement in " + status + " state");
        }
        this.status = SettlementStatus.COMPLETED;
    }
    
    /**
     * Marks the settlement as cancelled
     * @param reason The reason for cancellation
     */
    public void cancel(String reason) {
        if (status.isFinalState()) {
            throw new IllegalStateException("Cannot cancel settlement in " + status + " state");
        }
        this.status = SettlementStatus.CANCELLED;
        this.notes = reason != null ? reason : "Cancelled";
    }
    
    /**
     * Marks the settlement as failed
     * @param reason The reason for failure
     */
    public void fail(String reason) {
        if (status.isFinalState()) {
            throw new IllegalStateException("Cannot fail settlement in " + status + " state");
        }
        this.status = SettlementStatus.FAILED;
        this.notes = reason != null ? reason : "Failed";
    }
    
    /**
     * Checks if this settlement involves a specific user
     * @param userId The user ID to check
     * @return true if the user is either the payer or payee
     */
    public boolean involvesUser(String userId) {
        return fromUserId.equals(userId) || toUserId.equals(userId);
    }
    
    /**
     * Checks if this settlement was made by a specific user
     * @param userId The user ID to check
     * @return true if the user is the payer
     */
    public boolean wasMadeBy(String userId) {
        return fromUserId.equals(userId);
    }
    
    /**
     * Checks if this settlement was received by a specific user
     * @param userId The user ID to check
     * @return true if the user is the payee
     */
    public boolean wasReceivedBy(String userId) {
        return toUserId.equals(userId);
    }
    
    /**
     * Checks if the settlement is still active (can be modified)
     * @return true if the settlement is in pending state
     */
    public boolean isActive() {
        return status.isActive();
    }
    
    /**
     * Checks if the settlement is completed successfully
     * @return true if the settlement is completed
     */
    public boolean isCompleted() {
        return status == SettlementStatus.COMPLETED;
    }
    
    /**
     * Gets a formatted description of this settlement
     * @return A human-readable description
     */
    public String getFormattedDescription() {
        return String.format("%s paid %s: ₹%.2f (%s)", 
                           fromUserName, toUserName, amount, status.getDisplayName());
    }
    
    /**
     * Gets a formatted description from a specific user's perspective
     * @param userId The user ID for perspective
     * @return A human-readable description from the user's perspective
     */
    public String getFormattedDescription(String userId) {
        if (fromUserId.equals(userId)) {
            return String.format("You paid %s: ₹%.2f (%s)", 
                               toUserName, amount, status.getDisplayName());
        } else if (toUserId.equals(userId)) {
            return String.format("%s paid you: ₹%.2f (%s)", 
                               fromUserName, amount, status.getDisplayName());
        } else {
            return getFormattedDescription();
        }
    }
    
    /**
     * Sets notes for this settlement
     * @param notes The notes to set
     */
    public void setNotes(String notes) {
        this.notes = notes != null ? notes : "";
    }
    
    // Getters
    public String getSettlementId() {
        return settlementId;
    }
    
    public String getFromUserId() {
        return fromUserId;
    }
    
    public String getToUserId() {
        return toUserId;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public String getFromUserName() {
        return fromUserName;
    }
    
    public String getToUserName() {
        return toUserName;
    }
    
    public SettlementStatus getStatus() {
        return status;
    }
    
    public String getNotes() {
        return notes;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Settlement settlement = (Settlement) obj;
        return settlementId.equals(settlement.settlementId);
    }
    
    @Override
    public int hashCode() {
        return settlementId.hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("Settlement{id='%s', from='%s', to='%s', amount=%.2f, status=%s}", 
                           settlementId, fromUserId, toUserId, amount, status);
    }
}