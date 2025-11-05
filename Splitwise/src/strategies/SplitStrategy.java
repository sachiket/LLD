package strategies;

import models.User;
import models.Split;
import java.util.List;
import java.util.Map;

/**
 * Strategy interface for calculating expense splits
 * Implements Strategy Design Pattern for different splitting algorithms
 */
public interface SplitStrategy {
    
    /**
     * Calculates splits for an expense based on the strategy
     * @param amount The total expense amount
     * @param participants List of users participating in the expense
     * @param splitData Additional data needed for split calculation (amounts, percentages, weights)
     * @return List of splits showing how much each participant owes
     */
    List<Split> calculateSplits(double amount, List<User> participants, Map<String, Double> splitData);
    
    /**
     * Validates the split data for this strategy
     * @param amount The total expense amount
     * @param participants List of users participating in the expense
     * @param splitData Additional data needed for split calculation
     * @return true if the split data is valid for this strategy
     */
    boolean validateSplitData(double amount, List<User> participants, Map<String, Double> splitData);
    
    /**
     * Gets the name of this split strategy
     * @return The strategy name
     */
    String getStrategyName();
}