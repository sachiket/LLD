package factories;

import models.Expense;
import models.User;
import enums.SplitType;
import enums.ExpenseType;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Factory class for creating Expense objects
 * Implements Factory Design Pattern for consistent expense creation
 */
public class ExpenseFactory {
    
    /**
     * Creates a new expense with all parameters
     * @param description The expense description
     * @param amount The expense amount
     * @param paidBy The user who paid for the expense
     * @param participants List of users participating in the expense
     * @param splitType The type of split (equal, exact, percentage)
     * @param expenseType The category of the expense
     * @param splitData Additional data for split calculation
     * @param groupId The group this expense belongs to
     * @return A new Expense object
     */
    public static Expense createExpense(String description, double amount, User paidBy, 
                                      List<User> participants, SplitType splitType, 
                                      ExpenseType expenseType, Map<String, Double> splitData, 
                                      String groupId) {
        
        String expenseId = generateExpenseId();
        
        return new Expense(expenseId, description, amount, paidBy, participants, 
                          splitType, expenseType, splitData, groupId);
    }
    
    /**
     * Creates a new expense with default expense type (OTHER)
     * @param description The expense description
     * @param amount The expense amount
     * @param paidBy The user who paid for the expense
     * @param participants List of users participating in the expense
     * @param splitType The type of split (equal, exact, percentage)
     * @param splitData Additional data for split calculation
     * @param groupId The group this expense belongs to
     * @return A new Expense object
     */
    public static Expense createExpense(String description, double amount, User paidBy, 
                                      List<User> participants, SplitType splitType, 
                                      Map<String, Double> splitData, String groupId) {
        
        return createExpense(description, amount, paidBy, participants, splitType, 
                           ExpenseType.OTHER, splitData, groupId);
    }
    
    /**
     * Creates a new equal split expense
     * @param description The expense description
     * @param amount The expense amount
     * @param paidBy The user who paid for the expense
     * @param participants List of users participating in the expense
     * @param groupId The group this expense belongs to
     * @return A new Expense object with equal split
     */
    public static Expense createEqualSplitExpense(String description, double amount, User paidBy, 
                                                 List<User> participants, String groupId) {
        
        return createExpense(description, amount, paidBy, participants, SplitType.EQUAL, 
                           ExpenseType.OTHER, null, groupId);
    }
    
    /**
     * Creates a new equal split expense with specified category
     * @param description The expense description
     * @param amount The expense amount
     * @param paidBy The user who paid for the expense
     * @param participants List of users participating in the expense
     * @param expenseType The category of the expense
     * @param groupId The group this expense belongs to
     * @return A new Expense object with equal split
     */
    public static Expense createEqualSplitExpense(String description, double amount, User paidBy, 
                                                 List<User> participants, ExpenseType expenseType, 
                                                 String groupId) {
        
        return createExpense(description, amount, paidBy, participants, SplitType.EQUAL, 
                           expenseType, null, groupId);
    }
    
    /**
     * Creates a new exact split expense
     * @param description The expense description
     * @param amount The expense amount
     * @param paidBy The user who paid for the expense
     * @param participants List of users participating in the expense
     * @param exactAmounts Map of user ID to exact amount
     * @param groupId The group this expense belongs to
     * @return A new Expense object with exact split
     */
    public static Expense createExactSplitExpense(String description, double amount, User paidBy, 
                                                 List<User> participants, Map<String, Double> exactAmounts, 
                                                 String groupId) {
        
        return createExpense(description, amount, paidBy, participants, SplitType.EXACT, 
                           ExpenseType.OTHER, exactAmounts, groupId);
    }
    
    /**
     * Creates a new percentage split expense
     * @param description The expense description
     * @param amount The expense amount
     * @param paidBy The user who paid for the expense
     * @param participants List of users participating in the expense
     * @param percentages Map of user ID to percentage
     * @param groupId The group this expense belongs to
     * @return A new Expense object with percentage split
     */
    public static Expense createPercentageSplitExpense(String description, double amount, User paidBy, 
                                                      List<User> participants, Map<String, Double> percentages, 
                                                      String groupId) {
        
        return createExpense(description, amount, paidBy, participants, SplitType.PERCENTAGE, 
                           ExpenseType.OTHER, percentages, groupId);
    }
    
    /**
     * Generates a unique expense ID
     * @return A unique expense ID string
     */
    private static String generateExpenseId() {
        return "EXP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    /**
     * Validates expense creation parameters
     * @param description The expense description
     * @param amount The expense amount
     * @param paidBy The user who paid for the expense
     * @param participants List of users participating in the expense
     * @param groupId The group this expense belongs to
     * @return true if all parameters are valid
     */
    public static boolean validateExpenseParameters(String description, double amount, User paidBy, 
                                                   List<User> participants, String groupId) {
        
        if (description == null || description.trim().isEmpty()) {
            return false;
        }
        
        if (amount <= 0) {
            return false;
        }
        
        if (paidBy == null) {
            return false;
        }
        
        if (participants == null || participants.isEmpty()) {
            return false;
        }
        
        if (groupId == null || groupId.trim().isEmpty()) {
            return false;
        }
        
        // Check if paidBy is in participants list
        return participants.contains(paidBy);
    }
    
    /**
     * Creates a formatted expense description with amount
     * @param description The base description
     * @param amount The expense amount
     * @return A formatted description string
     */
    public static String formatExpenseDescription(String description, double amount) {
        if (description == null || description.trim().isEmpty()) {
            return String.format("Expense - ₹%.2f", amount);
        }
        
        return String.format("%s - ₹%.2f", description.trim(), amount);
    }
}