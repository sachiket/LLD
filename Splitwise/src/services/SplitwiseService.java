package services;

import models.*;
import enums.SplitType;
import enums.ExpenseType;
import java.util.List;
import java.util.Map;

/**
 * Main service interface for Splitwise system
 * Defines all operations for expense sharing and balance management
 */
public interface SplitwiseService {
    
    // User operations
    User createUser(String name, String email);
    User getUserById(String userId);
    List<User> getAllUsers();
    boolean deleteUser(String userId);
    
    // Group operations
    Group createGroup(String name, String description);
    Group getGroupById(String groupId);
    List<Group> getAllGroups();
    List<Group> getGroupsForUser(String userId);
    boolean addUserToGroup(String groupId, String userId);
    boolean removeUserFromGroup(String groupId, String userId);
    boolean deleteGroup(String groupId);
    
    // Expense operations
    Expense addExpense(String groupId, String description, double amount, String paidById, 
                      List<String> participantIds, SplitType splitType, Map<String, Double> splitData);
    Expense addExpense(String groupId, String description, double amount, String paidById, 
                      List<String> participantIds, SplitType splitType, ExpenseType expenseType, 
                      Map<String, Double> splitData);
    Expense getExpenseById(String expenseId);
    List<Expense> getExpensesForGroup(String groupId);
    List<Expense> getExpensesForUser(String userId);
    boolean updateExpense(String expenseId, String description, double amount, 
                         SplitType splitType, Map<String, Double> splitData);
    boolean deleteExpense(String expenseId);
    
    // Balance operations
    List<Balance> getBalances(String userId);
    List<Balance> getBalancesInGroup(String userId, String groupId);
    List<Balance> getAllBalancesForUser(String userId);
    double getTotalOwed(String userId);
    double getTotalOwedBy(String userId);
    double getNetBalance(String userId);
    
    // Settlement operations
    Settlement settleUp(String fromUserId, String toUserId, double amount);
    Settlement settleUpInGroup(String fromUserId, String toUserId, double amount, String groupId);
    List<Settlement> getSettlementsForUser(String userId);
    List<Settlement> getOptimalSettlements(String userId);
    List<Settlement> getOptimalSettlementsForGroup(String groupId);
    boolean confirmSettlement(String settlementId);
    boolean cancelSettlement(String settlementId, String reason);
    
    // Display operations
    void displayUserBalances(String userId);
    void displayGroupBalances(String groupId);
    void displayExpenseDetails(String expenseId);
    void displaySettlementHistory(String userId);
    void displayGroupExpenses(String groupId);
    
    // Analytics operations
    Map<ExpenseType, Double> getExpensesByCategory(String userId);
    Map<ExpenseType, Double> getExpensesByCategoryInGroup(String groupId);
    double getTotalExpensesForUser(String userId);
    double getTotalExpensesInGroup(String groupId);
    List<Expense> getRecentExpenses(String userId, int limit);
}