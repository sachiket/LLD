package enums;

/**
 * Enum representing different types of expense splitting strategies
 * Used to determine how an expense should be divided among participants
 */
public enum SplitType {
    /**
     * Split the expense equally among all participants
     */
    EQUAL,
    
    /**
     * Split the expense based on exact amounts specified for each participant
     */
    EXACT,
    
    /**
     * Split the expense based on percentage shares for each participant
     */
    PERCENTAGE,
    
    /**
     * Split the expense based on custom weights for each participant
     */
    WEIGHTED
}