package models;

/**
 * Represents an individual split of an expense for a specific user
 * Contains the user and their share of the expense
 */
public class Split {
    private final User user;
    private final double amount;
    private final double percentage; // For percentage-based splits
    
    public Split(User user, double amount, double percentage) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (amount < 0) {
            throw new IllegalArgumentException("Split amount cannot be negative");
        }
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Percentage must be between 0 and 100");
        }
        
        this.user = user;
        this.amount = amount;
        this.percentage = percentage;
    }
    
    public Split(User user, double amount) {
        this(user, amount, 0.0);
    }
    
    /**
     * Adjusts the split amount (used for rounding corrections)
     * @param adjustment The amount to add to the current split amount
     */
    public Split adjustAmount(double adjustment) {
        return new Split(user, amount + adjustment, percentage);
    }
    
    /**
     * Checks if this split is for a specific user
     * @param userId The user ID to check
     * @return true if this split belongs to the specified user
     */
    public boolean isForUser(String userId) {
        return user != null && user.getUserId().equals(userId);
    }
    
    /**
     * Checks if this split has a valid amount
     * @return true if the amount is greater than zero
     */
    public boolean hasValidAmount() {
        return amount > 0;
    }
    
    // Getters
    public User getUser() {
        return user;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public double getPercentage() {
        return percentage;
    }
    
    public String getUserId() {
        return user != null ? user.getUserId() : null;
    }
    
    public String getUserName() {
        return user != null ? user.getName() : null;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Split split = (Split) obj;
        return Double.compare(split.amount, amount) == 0 &&
               Double.compare(split.percentage, percentage) == 0 &&
               user.equals(split.user);
    }
    
    @Override
    public int hashCode() {
        int result = user.hashCode();
        result = 31 * result + Double.hashCode(amount);
        result = 31 * result + Double.hashCode(percentage);
        return result;
    }
    
    @Override
    public String toString() {
        if (percentage > 0) {
            return String.format("Split{user='%s', amount=%.2f, percentage=%.1f%%}", 
                               user.getName(), amount, percentage);
        } else {
            return String.format("Split{user='%s', amount=%.2f}", 
                               user.getName(), amount);
        }
    }
}