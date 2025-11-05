package strategies;

import models.User;
import models.Split;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Strategy for splitting expenses based on exact amounts specified for each participant
 * Implements the Strategy Design Pattern
 */
public class ExactSplitStrategy implements SplitStrategy {
    
    @Override
    public List<Split> calculateSplits(double amount, List<User> participants, Map<String, Double> splitData) {
        if (!validateSplitData(amount, participants, splitData)) {
            throw new IllegalArgumentException("Invalid split data for exact split");
        }
        
        List<Split> splits = new ArrayList<>();
        
        for (User participant : participants) {
            String userId = participant.getUserId();
            Double userAmount = splitData.get(userId);
            
            if (userAmount != null && userAmount > 0) {
                splits.add(new Split(participant, userAmount));
            }
        }
        
        return splits;
    }
    
    @Override
    public boolean validateSplitData(double amount, List<User> participants, Map<String, Double> splitData) {
        if (amount <= 0) {
            return false;
        }
        
        if (participants == null || participants.isEmpty()) {
            return false;
        }
        
        if (splitData == null || splitData.isEmpty()) {
            return false;
        }
        
        // Check that all participants have split data
        for (User participant : participants) {
            String userId = participant.getUserId();
            Double userAmount = splitData.get(userId);
            
            if (userAmount == null || userAmount < 0) {
                return false;
            }
        }
        
        // Check that the sum of all splits equals the total amount (within a small tolerance)
        double totalSplitAmount = 0;
        for (User participant : participants) {
            String userId = participant.getUserId();
            Double userAmount = splitData.get(userId);
            if (userAmount != null) {
                totalSplitAmount += userAmount;
            }
        }
        
        // Allow for small rounding differences (1 cent tolerance)
        return Math.abs(totalSplitAmount - amount) < 0.01;
    }
    
    @Override
    public String getStrategyName() {
        return "Exact Split";
    }
    
    /**
     * Validates that exact amounts sum to the total
     * @param totalAmount The total expense amount
     * @param exactAmounts Map of user ID to exact amount
     * @return true if amounts are valid
     */
    public static boolean validateExactAmounts(double totalAmount, Map<String, Double> exactAmounts) {
        if (exactAmounts == null || exactAmounts.isEmpty()) {
            return false;
        }
        
        double sum = exactAmounts.values().stream()
                .filter(amount -> amount != null && amount >= 0)
                .mapToDouble(Double::doubleValue)
                .sum();
        
        return Math.abs(sum - totalAmount) < 0.01;
    }
    
    /**
     * Adjusts the last amount to ensure exact total match
     * @param totalAmount The total expense amount
     * @param exactAmounts Map of user ID to exact amount (will be modified)
     * @param lastUserId The user ID whose amount should be adjusted
     */
    public static void adjustForExactTotal(double totalAmount, Map<String, Double> exactAmounts, String lastUserId) {
        if (exactAmounts == null || lastUserId == null || !exactAmounts.containsKey(lastUserId)) {
            return;
        }
        
        double currentSum = exactAmounts.values().stream()
                .filter(amount -> amount != null)
                .mapToDouble(Double::doubleValue)
                .sum();
        
        double lastUserCurrentAmount = exactAmounts.get(lastUserId);
        double adjustment = totalAmount - currentSum;
        double newLastUserAmount = lastUserCurrentAmount + adjustment;
        
        if (newLastUserAmount >= 0) {
            exactAmounts.put(lastUserId, newLastUserAmount);
        }
    }
    
    /**
     * Creates a map for equal distribution as exact amounts
     * @param participants List of participants
     * @param totalAmount The total amount to distribute
     * @return Map of user ID to exact amount for equal distribution
     */
    public static Map<String, Double> createEqualExactAmounts(List<User> participants, double totalAmount) {
        Map<String, Double> exactAmounts = new java.util.HashMap<>();
        
        if (participants == null || participants.isEmpty()) {
            return exactAmounts;
        }
        
        double amountPerPerson = totalAmount / participants.size();
        double totalAssigned = 0;
        
        for (int i = 0; i < participants.size(); i++) {
            User participant = participants.get(i);
            double amount;
            
            if (i == participants.size() - 1) {
                // Last participant gets remaining amount to handle rounding
                amount = totalAmount - totalAssigned;
            } else {
                amount = Math.round(amountPerPerson * 100.0) / 100.0;
                totalAssigned += amount;
            }
            
            exactAmounts.put(participant.getUserId(), amount);
        }
        
        return exactAmounts;
    }
}