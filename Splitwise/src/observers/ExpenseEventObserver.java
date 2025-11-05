package observers;

import models.Expense;
import models.Settlement;

/**
 * Observer interface for expense-related events
 * Implements Observer Design Pattern for event-driven notifications
 */
public interface ExpenseEventObserver {
    
    /**
     * Called when a new expense is added to the system
     * @param expense The expense that was added
     */
    void onExpenseAdded(Expense expense);
    
    /**
     * Called when an existing expense is updated
     * @param expense The expense that was updated
     */
    void onExpenseUpdated(Expense expense);
    
    /**
     * Called when an expense is deleted from the system
     * @param expenseId The ID of the expense that was deleted
     * @param groupId The ID of the group the expense belonged to
     */
    void onExpenseDeleted(String expenseId, String groupId);
    
    /**
     * Called when a settlement is made between users
     * @param settlement The settlement that was made
     */
    void onSettlementMade(Settlement settlement);
    
    /**
     * Called when a settlement is confirmed
     * @param settlement The settlement that was confirmed
     */
    void onSettlementConfirmed(Settlement settlement);
    
    /**
     * Called when a settlement is cancelled
     * @param settlement The settlement that was cancelled
     * @param reason The reason for cancellation
     */
    void onSettlementCancelled(Settlement settlement, String reason);
    
    /**
     * Called when user balances are updated due to expense or settlement changes
     * @param userId The ID of the user whose balance was updated
     * @param friendId The ID of the friend with whom the balance changed
     * @param oldBalance The previous balance amount
     * @param newBalance The new balance amount
     */
    void onBalanceUpdated(String userId, String friendId, double oldBalance, double newBalance);
    
    /**
     * Called when a user joins a group
     * @param userId The ID of the user who joined
     * @param groupId The ID of the group that was joined
     */
    void onUserJoinedGroup(String userId, String groupId);
    
    /**
     * Called when a user leaves a group
     * @param userId The ID of the user who left
     * @param groupId The ID of the group that was left
     */
    void onUserLeftGroup(String userId, String groupId);
}