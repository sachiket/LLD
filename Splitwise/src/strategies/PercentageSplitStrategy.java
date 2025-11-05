package strategies;

import models.User;
import models.Split;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Strategy for splitting expenses based on percentage shares for each participant
 * Implements the Strategy Design Pattern
 */
public class PercentageSplitStrategy implements SplitStrategy {
    
    @Override
    public List<Split> calculateSplits(double amount, List<User> participants, Map<String, Double> splitData) {
        if (!validateSplitData(amount, participants, splitData)) {
            throw new IllegalArgumentException("Invalid split data for percentage split");
        }
        
        List<Split> splits = new ArrayList<>();
        double totalAssigned = 0;
        
        for (int i = 0; i < participants.size(); i++) {
            User participant = participants.get(i);
            String userId = participant.getUserId();
            Double userPercentage = splitData.get(userId);
            
            if (userPercentage != null && userPercentage > 0) {
                double splitAmount;
                
                if (i == participants.size() - 1) {
                    // Last participant gets the remaining amount to handle rounding
                    splitAmount = amount - totalAssigned;
                } else {
                    splitAmount = Math.round((amount * userPercentage / 100.0) * 100.0) / 100.0;
                    totalAssigned += splitAmount;
                }
                
                splits.add(new Split(participant, splitAmount, userPercentage));
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
        
        // Check that all participants have valid percentage data
        double totalPercentage = 0;
        for (User participant : participants) {
            String userId = participant.getUserId();
            Double userPercentage = splitData.get(userId);
            
            if (userPercentage == null || userPercentage < 0 || userPercentage > 100) {
                return false;
            }
            
            totalPercentage += userPercentage;
        }
        
        // Check that percentages sum to 100% (within a small tolerance)
        return Math.abs(totalPercentage - 100.0) < 0.01;
    }
    
    @Override
    public String getStrategyName() {
        return "Percentage Split";
    }
    
    /**
     * Validates that percentages sum to 100%
     * @param percentages Map of user ID to percentage
     * @return true if percentages are valid
     */
    public static boolean validatePercentages(Map<String, Double> percentages) {
        if (percentages == null || percentages.isEmpty()) {
            return false;
        }
        
        double totalPercentage = percentages.values().stream()
                .filter(percentage -> percentage != null && percentage >= 0 && percentage <= 100)
                .mapToDouble(Double::doubleValue)
                .sum();
        
        return Math.abs(totalPercentage - 100.0) < 0.01;
    }
    
    /**
     * Adjusts the last percentage to ensure exact 100% total
     * @param percentages Map of user ID to percentage (will be modified)
     * @param lastUserId The user ID whose percentage should be adjusted
     */
    public static void adjustForExactTotal(Map<String, Double> percentages, String lastUserId) {
        if (percentages == null || lastUserId == null || !percentages.containsKey(lastUserId)) {
            return;
        }
        
        double currentSum = percentages.values().stream()
                .filter(percentage -> percentage != null)
                .mapToDouble(Double::doubleValue)
                .sum();
        
        double lastUserCurrentPercentage = percentages.get(lastUserId);
        double adjustment = 100.0 - currentSum;
        double newLastUserPercentage = lastUserCurrentPercentage + adjustment;
        
        if (newLastUserPercentage >= 0 && newLastUserPercentage <= 100) {
            percentages.put(lastUserId, newLastUserPercentage);
        }
    }
    
    /**
     * Creates a map for equal distribution as percentages
     * @param participants List of participants
     * @return Map of user ID to percentage for equal distribution
     */
    public static Map<String, Double> createEqualPercentages(List<User> participants) {
        Map<String, Double> percentages = new java.util.HashMap<>();
        
        if (participants == null || participants.isEmpty()) {
            return percentages;
        }
        
        double percentagePerPerson = 100.0 / participants.size();
        double totalAssigned = 0;
        
        for (int i = 0; i < participants.size(); i++) {
            User participant = participants.get(i);
            double percentage;
            
            if (i == participants.size() - 1) {
                // Last participant gets remaining percentage to handle rounding
                percentage = 100.0 - totalAssigned;
            } else {
                percentage = Math.round(percentagePerPerson * 100.0) / 100.0;
                totalAssigned += percentage;
            }
            
            percentages.put(participant.getUserId(), percentage);
        }
        
        return percentages;
    }
    
    /**
     * Converts exact amounts to percentages
     * @param exactAmounts Map of user ID to exact amount
     * @param totalAmount The total amount
     * @return Map of user ID to percentage
     */
    public static Map<String, Double> convertAmountsToPercentages(Map<String, Double> exactAmounts, double totalAmount) {
        Map<String, Double> percentages = new java.util.HashMap<>();
        
        if (exactAmounts == null || exactAmounts.isEmpty() || totalAmount <= 0) {
            return percentages;
        }
        
        for (Map.Entry<String, Double> entry : exactAmounts.entrySet()) {
            String userId = entry.getKey();
            Double amount = entry.getValue();
            
            if (amount != null && amount >= 0) {
                double percentage = (amount / totalAmount) * 100.0;
                percentages.put(userId, Math.round(percentage * 100.0) / 100.0);
            }
        }
        
        return percentages;
    }
}