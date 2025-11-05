package strategies;

import models.User;
import models.Split;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Strategy for splitting expenses equally among all participants
 * Implements the Strategy Design Pattern
 */
public class EqualSplitStrategy implements SplitStrategy {
    
    @Override
    public List<Split> calculateSplits(double amount, List<User> participants, Map<String, Double> splitData) {
        if (!validateSplitData(amount, participants, splitData)) {
            throw new IllegalArgumentException("Invalid split data for equal split");
        }
        
        List<Split> splits = new ArrayList<>();
        double amountPerPerson = amount / participants.size();
        
        // Handle rounding by adjusting the last split
        double totalAssigned = 0;
        
        for (int i = 0; i < participants.size(); i++) {
            User participant = participants.get(i);
            double splitAmount;
            
            if (i == participants.size() - 1) {
                // Last participant gets the remaining amount to handle rounding
                splitAmount = amount - totalAssigned;
            } else {
                splitAmount = Math.round(amountPerPerson * 100.0) / 100.0; // Round to 2 decimal places
                totalAssigned += splitAmount;
            }
            
            splits.add(new Split(participant, splitAmount));
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
        
        // For equal split, we don't need any split data
        // Split data can be null or empty
        return true;
    }
    
    @Override
    public String getStrategyName() {
        return "Equal Split";
    }
    
    /**
     * Gets the amount each person should pay in an equal split
     * @param amount The total amount
     * @param participantCount The number of participants
     * @return The amount per person
     */
    public static double getAmountPerPerson(double amount, int participantCount) {
        if (participantCount <= 0) {
            throw new IllegalArgumentException("Participant count must be positive");
        }
        return amount / participantCount;
    }
    
    /**
     * Checks if an amount can be split equally without significant rounding issues
     * @param amount The total amount
     * @param participantCount The number of participants
     * @return true if the split results in reasonable amounts
     */
    public static boolean canSplitEvenly(double amount, int participantCount) {
        if (participantCount <= 0) {
            return false;
        }
        
        double amountPerPerson = amount / participantCount;
        // Check if the amount per person is at least 1 cent
        return amountPerPerson >= 0.01;
    }
}