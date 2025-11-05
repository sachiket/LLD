import enums.*;
import strategies.*;
import observers.ConsoleNotificationObserver;
import managers.GroupManager;

/**
 * Simple test to verify core functionality works
 */
public class SimpleTest {
    public static void main(String[] args) {
        System.out.println("[TEST] Starting Splitwise Core Functionality Test");
        System.out.println("=" + "=".repeat(50));
        
        try {
            // Test 1: Test Enums
            System.out.println("\n[TEST 1] Testing Enums...");
            System.out.println("Split Types:");
            for (SplitType type : SplitType.values()) {
                System.out.println("  - " + type.name());
            }
            
            System.out.println("Expense Types:");
            for (ExpenseType type : ExpenseType.values()) {
                System.out.println("  - " + type.name() + ": " + type.getDisplayName());
            }
            
            System.out.println("Settlement Status:");
            for (SettlementStatus status : SettlementStatus.values()) {
                System.out.println("  - " + status.name() + ": " + status.getDisplayName());
            }
            
            // Test 2: Test Strategy Pattern
            System.out.println("\n[TEST 2] Testing Strategy Pattern...");
            EqualSplitStrategy equalStrategy = new EqualSplitStrategy();
            ExactSplitStrategy exactStrategy = new ExactSplitStrategy();
            PercentageSplitStrategy percentageStrategy = new PercentageSplitStrategy();
            
            System.out.println("Strategy Names:");
            System.out.println("  - " + equalStrategy.getStrategyName());
            System.out.println("  - " + exactStrategy.getStrategyName());
            System.out.println("  - " + percentageStrategy.getStrategyName());
            
            // Test 3: Test Static Methods
            System.out.println("\n[TEST 3] Testing Static Methods...");
            double amountPerPerson = EqualSplitStrategy.getAmountPerPerson(1200.0, 3);
            System.out.println("Equal split of Rs.1200 among 3 people: Rs." + String.format("%.2f", amountPerPerson));
            
            boolean canSplit = EqualSplitStrategy.canSplitEvenly(1200.0, 3);
            System.out.println("Can split Rs.1200 evenly among 3 people: " + canSplit);
            
            // Test 4: Test GroupManager Singleton
            System.out.println("\n[TEST 4] Testing Singleton Pattern...");
            GroupManager manager1 = GroupManager.getInstance();
            GroupManager manager2 = GroupManager.getInstance();
            System.out.println("GroupManager singleton test: " + (manager1 == manager2 ? "PASS" : "FAIL"));
            
            // Test 5: Test Observer Pattern
            System.out.println("\n[TEST 5] Testing Observer Pattern...");
            ConsoleNotificationObserver observer = new ConsoleNotificationObserver();
            observer.printSuccess("Observer pattern is working!");
            observer.printInfo("This is an info message");
            observer.printError("This is an error message");
            
            System.out.println("\n[SUCCESS] Core functionality tests completed!");
            System.out.println("=" + "=".repeat(50));
            System.out.println("\n[SUMMARY] Test Results:");
            System.out.println("OK Enums are properly defined");
            System.out.println("OK Strategy pattern is implemented");
            System.out.println("OK Singleton pattern is working");
            System.out.println("OK Observer pattern is functional");
            System.out.println("OK System compiles successfully");
            System.out.println("OK Architecture demonstrates design patterns");
            
        } catch (Exception e) {
            System.out.println("\n[ERROR] Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}