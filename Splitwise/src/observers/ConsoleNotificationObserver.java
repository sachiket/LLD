package observers;

import models.Expense;
import models.Settlement;
import java.time.format.DateTimeFormatter;

/**
 * Console-based implementation of ExpenseEventObserver
 * Prints notifications to the console for all expense-related events
 */
public class ConsoleNotificationObserver implements ExpenseEventObserver {
    
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    @Override
    public void onExpenseAdded(Expense expense) {
        System.out.println("\n[NOTIFICATION] $ New Expense Added");
        System.out.println("  * Description: " + expense.getDescription());
        System.out.println("  * Amount: Rs." + String.format("%.2f", expense.getAmount()));
        System.out.println("  * Paid by: " + expense.getPaidBy().getName());
        System.out.println("  * Participants: " + expense.getParticipantCount());
        System.out.println("  * Split Type: " + expense.getSplitType());
        System.out.println("  * Category: " + expense.getExpenseType().getDisplayName());
        System.out.println("  * Time: " + expense.getCreatedAt().format(TIME_FORMATTER));
        System.out.println("  * Expense ID: " + expense.getExpenseId());
    }
    
    @Override
    public void onExpenseUpdated(Expense expense) {
        System.out.println("\n[NOTIFICATION] EDIT Expense Updated");
        System.out.println("  * Expense ID: " + expense.getExpenseId());
        System.out.println("  * Description: " + expense.getDescription());
        System.out.println("  * Amount: Rs." + String.format("%.2f", expense.getAmount()));
        System.out.println("  * Split Type: " + expense.getSplitType());
    }
    
    @Override
    public void onExpenseDeleted(String expenseId, String groupId) {
        System.out.println("\n[NOTIFICATION] DELETE Expense Deleted");
        System.out.println("  * Expense ID: " + expenseId);
        System.out.println("  * Group ID: " + groupId);
    }
    
    @Override
    public void onSettlementMade(Settlement settlement) {
        System.out.println("\n[NOTIFICATION] PAY Settlement Made");
        System.out.println("  * " + settlement.getFromUserName() + " paid " + settlement.getToUserName() +
                          ": Rs." + String.format("%.2f", settlement.getAmount()));
        System.out.println("  * Time: " + settlement.getTimestamp().format(TIME_FORMATTER));
        System.out.println("  * Status: " + settlement.getStatus().getDisplayName());
        System.out.println("  * Settlement ID: " + settlement.getSettlementId());
    }
    
    @Override
    public void onSettlementConfirmed(Settlement settlement) {
        System.out.println("\n[NOTIFICATION] OK Settlement Confirmed");
        System.out.println("  * Settlement ID: " + settlement.getSettlementId());
        System.out.println("  * Amount: Rs." + String.format("%.2f", settlement.getAmount()));
        System.out.println("  * From: " + settlement.getFromUserName() + " -> To: " + settlement.getToUserName());
    }
    
    @Override
    public void onSettlementCancelled(Settlement settlement, String reason) {
        System.out.println("\n[NOTIFICATION] X Settlement Cancelled");
        System.out.println("  * Settlement ID: " + settlement.getSettlementId());
        System.out.println("  * Amount: Rs." + String.format("%.2f", settlement.getAmount()));
        System.out.println("  * Reason: " + (reason != null ? reason : "No reason provided"));
    }
    
    @Override
    public void onBalanceUpdated(String userId, String friendId, double oldBalance, double newBalance) {
        System.out.println("\n[NOTIFICATION] BALANCE Balance Updated");
        System.out.println("  * User: " + userId);
        System.out.println("  * Friend: " + friendId);
        System.out.println("  * Old Balance: Rs." + String.format("%.2f", oldBalance));
        System.out.println("  * New Balance: Rs." + String.format("%.2f", newBalance));
        
        if (Math.abs(newBalance) < 0.01) {
            System.out.println("  * Balance settled! No money owed.");
        } else if (newBalance > 0) {
            System.out.println("  * " + userId + " owes Rs." + String.format("%.2f", newBalance));
        } else {
            System.out.println("  * " + userId + " is owed Rs." + String.format("%.2f", -newBalance));
        }
    }
    
    @Override
    public void onUserJoinedGroup(String userId, String groupId) {
        System.out.println("\n[NOTIFICATION] JOIN User Joined Group");
        System.out.println("  * User: " + userId);
        System.out.println("  * Group: " + groupId);
        System.out.println("  * Welcome to the group!");
    }
    
    @Override
    public void onUserLeftGroup(String userId, String groupId) {
        System.out.println("\n[NOTIFICATION] LEAVE User Left Group");
        System.out.println("  * User: " + userId);
        System.out.println("  * Group: " + groupId);
        System.out.println("  * User has left the group");
    }
    
    /**
     * Prints a formatted expense summary
     * @param expense The expense to summarize
     */
    public void printExpenseSummary(Expense expense) {
        System.out.println("\n[EXPENSE SUMMARY] " + expense.getDescription());
        System.out.println("=" + "=".repeat(50));
        System.out.println("Total Amount: Rs." + String.format("%.2f", expense.getAmount()));
        System.out.println("Paid by: " + expense.getPaidBy().getName());
        System.out.println("Split Type: " + expense.getSplitType());
        System.out.println("Category: " + expense.getExpenseType().getDisplayName());
        System.out.println("Participants (" + expense.getParticipantCount() + "):");
        
        for (int i = 0; i < expense.getParticipants().size(); i++) {
            System.out.println("  " + (i + 1) + ". " + expense.getParticipants().get(i).getName());
        }
    }
    
    /**
     * Prints a formatted settlement summary
     * @param settlement The settlement to summarize
     */
    public void printSettlementSummary(Settlement settlement) {
        System.out.println("\n[SETTLEMENT SUMMARY]");
        System.out.println("=" + "=".repeat(50));
        System.out.println("Amount: Rs." + String.format("%.2f", settlement.getAmount()));
        System.out.println("From: " + settlement.getFromUserName());
        System.out.println("To: " + settlement.getToUserName());
        System.out.println("Status: " + settlement.getStatus().getDisplayName());
        System.out.println("Time: " + settlement.getTimestamp().format(TIME_FORMATTER));
        
        if (settlement.getNotes() != null && !settlement.getNotes().trim().isEmpty()) {
            System.out.println("Notes: " + settlement.getNotes());
        }
    }
    
    /**
     * Prints a simple success message
     * @param message The success message
     */
    public void printSuccess(String message) {
        System.out.println("\n[SUCCESS] OK " + message);
    }
    
    /**
     * Prints a simple error message
     * @param message The error message
     */
    public void printError(String message) {
        System.out.println("\n[ERROR] X " + message);
    }
    
    /**
     * Prints a simple info message
     * @param message The info message
     */
    public void printInfo(String message) {
        System.out.println("\n[INFO] i " + message);
    }
}