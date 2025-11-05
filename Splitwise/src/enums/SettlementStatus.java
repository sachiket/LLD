package enums;

/**
 * Enum representing the status of a settlement transaction
 * Used to track the lifecycle of settlement payments
 */
public enum SettlementStatus {
    /**
     * Settlement has been initiated but not yet completed
     */
    PENDING("Pending"),
    
    /**
     * Settlement has been successfully completed
     */
    COMPLETED("Completed"),
    
    /**
     * Settlement has been cancelled before completion
     */
    CANCELLED("Cancelled"),
    
    /**
     * Settlement failed due to some error
     */
    FAILED("Failed");
    
    private final String displayName;
    
    SettlementStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Check if the settlement is in a final state (completed, cancelled, or failed)
     * @return true if the settlement cannot be modified further
     */
    public boolean isFinalState() {
        return this == COMPLETED || this == CANCELLED || this == FAILED;
    }
    
    /**
     * Check if the settlement is still active (pending)
     * @return true if the settlement can still be processed
     */
    public boolean isActive() {
        return this == PENDING;
    }
}