package models;

/**
 * Represents a balance between two users in the Splitwise system
 * Shows who owes money to whom and how much
 */
public class Balance {
    private final String fromUserId;
    private final String toUserId;
    private final double amount;
    private final String fromUserName;
    private final String toUserName;
    
    public Balance(String fromUserId, String toUserId, double amount, String fromUserName, String toUserName) {
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
            throw new IllegalArgumentException("Balance amount must be positive");
        }
        
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.amount = amount;
        this.fromUserName = fromUserName != null ? fromUserName : fromUserId;
        this.toUserName = toUserName != null ? toUserName : toUserId;
    }
    
    public Balance(String fromUserId, String toUserId, double amount) {
        this(fromUserId, toUserId, amount, null, null);
    }
    
    /**
     * Checks if this balance involves a specific user
     * @param userId The user ID to check
     * @return true if the user is either the debtor or creditor
     */
    public boolean involvesUser(String userId) {
        return fromUserId.equals(userId) || toUserId.equals(userId);
    }
    
    /**
     * Gets the amount owed by a specific user in this balance
     * @param userId The user ID to check
     * @return The amount owed by the user, 0 if user is not the debtor
     */
    public double getAmountOwedBy(String userId) {
        return fromUserId.equals(userId) ? amount : 0.0;
    }
    
    /**
     * Gets the amount owed to a specific user in this balance
     * @param userId The user ID to check
     * @return The amount owed to the user, 0 if user is not the creditor
     */
    public double getAmountOwedTo(String userId) {
        return toUserId.equals(userId) ? amount : 0.0;
    }
    
    /**
     * Creates a reverse balance (swaps debtor and creditor)
     * @return A new Balance with reversed roles
     */
    public Balance reverse() {
        return new Balance(toUserId, fromUserId, amount, toUserName, fromUserName);
    }
    
    /**
     * Checks if this balance can be settled with a given amount
     * @param settlementAmount The amount to settle
     * @return true if the settlement amount is valid (positive and <= balance amount)
     */
    public boolean canSettleWith(double settlementAmount) {
        return settlementAmount > 0 && settlementAmount <= amount;
    }
    
    /**
     * Creates a new balance after partial settlement
     * @param settlementAmount The amount being settled
     * @return A new Balance with the reduced amount, or null if fully settled
     */
    public Balance afterSettlement(double settlementAmount) {
        if (!canSettleWith(settlementAmount)) {
            throw new IllegalArgumentException("Invalid settlement amount");
        }
        
        double remainingAmount = amount - settlementAmount;
        if (remainingAmount < 0.01) { // Consider amounts less than 1 cent as zero
            return null; // Fully settled
        }
        
        return new Balance(fromUserId, toUserId, remainingAmount, fromUserName, toUserName);
    }
    
    /**
     * Gets a formatted description of this balance
     * @return A human-readable description
     */
    public String getFormattedDescription() {
        return String.format("%s owes %s: ₹%.2f", fromUserName, toUserName, amount);
    }
    
    /**
     * Gets a formatted description from a specific user's perspective
     * @param userId The user ID for perspective
     * @return A human-readable description from the user's perspective
     */
    public String getFormattedDescription(String userId) {
        if (fromUserId.equals(userId)) {
            return String.format("You owe %s: ₹%.2f", toUserName, amount);
        } else if (toUserId.equals(userId)) {
            return String.format("%s owes you: ₹%.2f", fromUserName, amount);
        } else {
            return getFormattedDescription();
        }
    }
    
    // Getters
    public String getFromUserId() {
        return fromUserId;
    }
    
    public String getToUserId() {
        return toUserId;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public String getFromUserName() {
        return fromUserName;
    }
    
    public String getToUserName() {
        return toUserName;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Balance balance = (Balance) obj;
        return Double.compare(balance.amount, amount) == 0 &&
               fromUserId.equals(balance.fromUserId) &&
               toUserId.equals(balance.toUserId);
    }
    
    @Override
    public int hashCode() {
        int result = fromUserId.hashCode();
        result = 31 * result + toUserId.hashCode();
        result = 31 * result + Double.hashCode(amount);
        return result;
    }
    
    @Override
    public String toString() {
        return String.format("Balance{from='%s', to='%s', amount=%.2f}", 
                           fromUserId, toUserId, amount);
    }
}