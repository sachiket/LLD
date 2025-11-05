package models;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a user in the Splitwise system
 * Manages user information and balance tracking with other users
 */
public class User {
    private final String userId;
    private final String name;
    private final String email;
    private final Map<String, Double> balances; // friendId -> amount (positive = owes, negative = owed)
    
    public User(String userId, String name, String email) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.balances = new ConcurrentHashMap<>(); // Thread-safe for concurrent access
    }
    
    /**
     * Updates the balance with another user
     * @param friendId The ID of the other user
     * @param amount The amount to add to the balance (positive = this user owes more, negative = this user owes less)
     */
    public void updateBalance(String friendId, double amount) {
        if (friendId == null || friendId.equals(this.userId)) {
            return; // Cannot have balance with self
        }
        
        balances.merge(friendId, amount, Double::sum);
        
        // Remove zero balances to keep the map clean
        if (Math.abs(balances.get(friendId)) < 0.01) {
            balances.remove(friendId);
        }
    }
    
    /**
     * Gets the balance with a specific user
     * @param friendId The ID of the other user
     * @return The balance amount (positive = this user owes, negative = this user is owed)
     */
    public double getBalanceWith(String friendId) {
        return balances.getOrDefault(friendId, 0.0);
    }
    
    /**
     * Gets all balances with other users
     * @return A copy of the balances map
     */
    public Map<String, Double> getAllBalances() {
        return new HashMap<>(balances);
    }
    
    /**
     * Checks if the user has any outstanding balances
     * @return true if the user has any non-zero balances
     */
    public boolean hasOutstandingBalances() {
        return !balances.isEmpty();
    }
    
    /**
     * Gets the total amount this user owes to others
     * @return The sum of all positive balances
     */
    public double getTotalOwed() {
        return balances.values().stream()
                .filter(amount -> amount > 0)
                .mapToDouble(Double::doubleValue)
                .sum();
    }
    
    /**
     * Gets the total amount others owe to this user
     * @return The sum of all negative balances (as positive value)
     */
    public double getTotalOwedBy() {
        return balances.values().stream()
                .filter(amount -> amount < 0)
                .mapToDouble(amount -> -amount)
                .sum();
    }
    
    /**
     * Gets the net balance (total owed by others minus total owed to others)
     * @return Positive if user is owed money overall, negative if user owes money overall
     */
    public double getNetBalance() {
        return getTotalOwedBy() - getTotalOwed();
    }
    
    // Getters
    public String getUserId() {
        return userId;
    }
    
    public String getName() {
        return name;
    }
    
    public String getEmail() {
        return email;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return userId.equals(user.userId);
    }
    
    @Override
    public int hashCode() {
        return userId.hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("User{id='%s', name='%s', email='%s', balances=%d}", 
                           userId, name, email, balances.size());
    }
}