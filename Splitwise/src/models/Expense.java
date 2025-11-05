package models;

import enums.ExpenseType;
import enums.SplitType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents an expense in the Splitwise system
 * Contains expense details and information about how it should be split
 */
public class Expense {
    private final String expenseId;
    private final String description;
    private final double amount;
    private final User paidBy;
    private final List<User> participants;
    private final SplitType splitType;
    private final ExpenseType expenseType;
    private final Map<String, Double> splitData; // userId -> amount/percentage/weight
    private final LocalDateTime createdAt;
    private final String groupId;
    
    public Expense(String expenseId, String description, double amount, User paidBy, 
                   List<User> participants, SplitType splitType, ExpenseType expenseType,
                   Map<String, Double> splitData, String groupId) {
        
        if (expenseId == null || expenseId.trim().isEmpty()) {
            throw new IllegalArgumentException("Expense ID cannot be null or empty");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (paidBy == null) {
            throw new IllegalArgumentException("Paid by user cannot be null");
        }
        if (participants == null || participants.isEmpty()) {
            throw new IllegalArgumentException("Participants list cannot be null or empty");
        }
        if (splitType == null) {
            throw new IllegalArgumentException("Split type cannot be null");
        }
        if (groupId == null || groupId.trim().isEmpty()) {
            throw new IllegalArgumentException("Group ID cannot be null or empty");
        }
        
        this.expenseId = expenseId;
        this.description = description;
        this.amount = amount;
        this.paidBy = paidBy;
        this.participants = new ArrayList<>(participants);
        this.splitType = splitType;
        this.expenseType = expenseType != null ? expenseType : ExpenseType.OTHER;
        this.splitData = splitData;
        this.createdAt = LocalDateTime.now();
        this.groupId = groupId;
    }
    
    public Expense(String expenseId, String description, double amount, User paidBy, 
                   List<User> participants, SplitType splitType, Map<String, Double> splitData, String groupId) {
        this(expenseId, description, amount, paidBy, participants, splitType, ExpenseType.OTHER, splitData, groupId);
    }
    
    /**
     * Checks if a user is a participant in this expense
     * @param user The user to check
     * @return true if the user is a participant
     */
    public boolean isParticipant(User user) {
        return user != null && participants.contains(user);
    }
    
    /**
     * Checks if a user (by ID) is a participant in this expense
     * @param userId The user ID to check
     * @return true if the user is a participant
     */
    public boolean isParticipant(String userId) {
        if (userId == null) {
            return false;
        }
        return participants.stream().anyMatch(user -> user.getUserId().equals(userId));
    }
    
    /**
     * Gets a participant by their user ID
     * @param userId The user ID to find
     * @return The user if found, null otherwise
     */
    public User getParticipantById(String userId) {
        if (userId == null) {
            return null;
        }
        return participants.stream()
                .filter(user -> user.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Gets the number of participants in this expense
     * @return The participant count
     */
    public int getParticipantCount() {
        return participants.size();
    }
    
    /**
     * Checks if this expense was paid by a specific user
     * @param userId The user ID to check
     * @return true if the expense was paid by this user
     */
    public boolean wasPaidBy(String userId) {
        return paidBy != null && paidBy.getUserId().equals(userId);
    }
    
    /**
     * Gets the split data for a specific user
     * @param userId The user ID
     * @return The split data (amount/percentage/weight) for the user, or null if not found
     */
    public Double getSplitDataForUser(String userId) {
        return splitData != null ? splitData.get(userId) : null;
    }
    
    /**
     * Checks if the expense has valid split data based on its split type
     * @return true if the split data is valid
     */
    public boolean hasValidSplitData() {
        if (splitType == SplitType.EQUAL) {
            return true; // No split data needed for equal splits
        }
        
        if (splitData == null || splitData.isEmpty()) {
            return false;
        }
        
        switch (splitType) {
            case EXACT:
                // Check if all participants have split data and sum equals total amount
                double sum = 0;
                for (User participant : participants) {
                    Double userAmount = splitData.get(participant.getUserId());
                    if (userAmount == null || userAmount < 0) {
                        return false;
                    }
                    sum += userAmount;
                }
                return Math.abs(sum - amount) < 0.01; // Allow for small rounding differences
                
            case PERCENTAGE:
                // Check if all participants have percentages and they sum to 100%
                double totalPercentage = 0;
                for (User participant : participants) {
                    Double userPercentage = splitData.get(participant.getUserId());
                    if (userPercentage == null || userPercentage < 0 || userPercentage > 100) {
                        return false;
                    }
                    totalPercentage += userPercentage;
                }
                return Math.abs(totalPercentage - 100.0) < 0.01;
                
            case WEIGHTED:
                // Check if all participants have positive weights
                for (User participant : participants) {
                    Double userWeight = splitData.get(participant.getUserId());
                    if (userWeight == null || userWeight <= 0) {
                        return false;
                    }
                }
                return true;
                
            default:
                return false;
        }
    }
    
    /**
     * Checks if this expense is essential based on its type
     * @return true if the expense is essential
     */
    public boolean isEssential() {
        return expenseType.isEssential();
    }
    
    /**
     * Gets a formatted description of the expense
     * @return A formatted string with expense details
     */
    public String getFormattedDescription() {
        return String.format("%s - ₹%.2f (%s)", description, amount, expenseType.getDisplayName());
    }
    
    // Getters
    public String getExpenseId() {
        return expenseId;
    }
    
    public String getDescription() {
        return description;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public User getPaidBy() {
        return paidBy;
    }
    
    public List<User> getParticipants() {
        return new ArrayList<>(participants); // Return a copy to prevent external modification
    }
    
    public SplitType getSplitType() {
        return splitType;
    }
    
    public ExpenseType getExpenseType() {
        return expenseType;
    }
    
    public Map<String, Double> getSplitData() {
        return splitData;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public String getGroupId() {
        return groupId;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Expense expense = (Expense) obj;
        return expenseId.equals(expense.expenseId);
    }
    
    @Override
    public int hashCode() {
        return expenseId.hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("Expense{id='%s', description='%s', amount=%.2f, paidBy='%s', participants=%d, type=%s}", 
                           expenseId, description, amount, paidBy.getName(), participants.size(), splitType);
    }
}