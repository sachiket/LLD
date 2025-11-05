# 📝 Splitwise Interview Cheat Sheet

## 🚀 Quick Reference for Live Coding

### **Core Models (5 minutes)**
```java
// User.java
public class User {
    private final String userId;
    private final String name;
    private final String email;
    private final Map<String, Double> balances; // friendId -> amount owed
}

// Group.java -> Expense.java -> Split.java
public class Group {
    private final String groupId;
    private final String name;
    private final List<User> members;
}
```

### **Key Enums**
```java
public enum SplitType { EQUAL, EXACT, PERCENTAGE }
public enum ExpenseType { FOOD, TRAVEL, ENTERTAINMENT, UTILITIES, OTHER }
public enum SettlementStatus { PENDING, COMPLETED, CANCELLED }
```

---

## 🎯 Design Patterns Quick Implementation

### **1. Strategy Pattern (Split Calculation)**
```java
public interface SplitStrategy {
    List<Split> calculateSplits(Expense expense, List<User> participants, Map<String, Double> splitData);
}

public class EqualSplitStrategy implements SplitStrategy {
    @Override
    public List<Split> calculateSplits(Expense expense, List<User> participants, Map<String, Double> splitData) {
        List<Split> splits = new ArrayList<>();
        double amountPerPerson = expense.getAmount() / participants.size();
        for (User participant : participants) {
            splits.add(new Split(participant, amountPerPerson));
        }
        return splits;
    }
}
```

### **2. Factory Pattern (Expense Creation)**
```java
public class ExpenseFactory {
    public static Expense createExpense(String description, double amount, User paidBy, 
                                      List<User> participants, SplitType splitType, Map<String, Double> splitData) {
        String expenseId = "EXP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return new Expense(expenseId, description, amount, paidBy, participants, splitType, splitData);
    }
}
```

### **3. Observer Pattern (Notifications)**
```java
public interface ExpenseEventObserver {
    void onExpenseAdded(Expense expense);
    void onSettlementMade(Settlement settlement);
}

// In Service Implementation
private void notifyExpenseAdded(Expense expense) {
    for (ExpenseEventObserver observer : observers) {
        observer.onExpenseAdded(expense);
    }
}
```

### **4. Singleton Pattern (Group Manager)**
```java
public class GroupManager {
    private static GroupManager instance;
    
    public static synchronized GroupManager getInstance() {
        if (instance == null) {
            instance = new GroupManager();
        }
        return instance;
    }
}
```

---

## ⚖️ SOLID Principles Examples

### **Single Responsibility**
```java
// ✅ Good - Each class has one responsibility
public class User { /* Only manages user data and balances */ }
public class SplitStrategy { /* Only calculates splits */ }
public class ExpenseFactory { /* Only creates expenses */ }
```

### **Open/Closed**
```java
// ✅ Easy to extend without modifying existing code
public class WeightedSplitStrategy implements SplitStrategy {
    @Override
    public List<Split> calculateSplits(Expense expense, List<User> participants, Map<String, Double> splitData) {
        // Custom weighted splitting logic
        return calculateWeightedSplits(expense, participants, splitData);
    }
}
```

### **Dependency Inversion**
```java
// ✅ Depend on abstractions
public class SplitwiseServiceImpl {
    private final Map<SplitType, SplitStrategy> splitStrategies; // Interface, not concrete class
    private final List<ExpenseEventObserver> observers; // Interface
}
```

---

## 🔧 Core Service Methods

### **Add Expense**
```java
@Override
public Expense addExpense(String groupId, String description, double amount, String paidById, 
                         List<String> participantIds, SplitType splitType, Map<String, Double> splitData) {
    
    Group group = findGroupById(groupId);
    User paidBy = findUserById(paidById);
    List<User> participants = getParticipants(participantIds);
    
    // Validate split data
    validateSplitData(amount, splitType, splitData, participants);
    
    // Create expense using factory
    Expense expense = ExpenseFactory.createExpense(description, amount, paidBy, participants, splitType, splitData);
    
    // Calculate splits using strategy
    SplitStrategy strategy = splitStrategies.get(splitType);
    List<Split> splits = strategy.calculateSplits(expense, participants, splitData);
    
    // Update balances
    updateBalances(paidBy, splits);
    
    // Store and notify
    expenses.add(expense);
    notifyExpenseAdded(expense);
    
    return expense;
}
```

### **Balance Calculation**
```java
private void updateBalances(User paidBy, List<Split> splits) {
    for (Split split : splits) {
        if (!split.getUser().getUserId().equals(paidBy.getUserId())) {
            // split.user owes paidBy the split amount
            split.getUser().updateBalance(paidBy.getUserId(), split.getAmount());
            // paidBy is owed by split.user (negative amount)
            paidBy.updateBalance(split.getUser().getUserId(), -split.getAmount());
        }
    }
}
```

---

## 🎤 Common Interview Questions & Answers

### **Q: "How would you handle floating-point precision issues?"**
```java
// Use BigDecimal for monetary calculations
public class MoneyUtils {
    public static BigDecimal divide(BigDecimal amount, int participants) {
        return amount.divide(BigDecimal.valueOf(participants), 2, RoundingMode.HALF_UP);
    }
    
    public static void adjustForRounding(List<Split> splits, BigDecimal totalAmount) {
        BigDecimal splitSum = splits.stream()
            .map(Split::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal difference = totalAmount.subtract(splitSum);
        if (difference.compareTo(BigDecimal.ZERO) != 0) {
            // Add difference to last split
            splits.get(splits.size() - 1).adjustAmount(difference);
        }
    }
}
```

### **Q: "How would you optimize settlement calculations?"**
```java
public class SettlementOptimizer {
    public List<Settlement> optimizeSettlements(Map<String, Map<String, Double>> balances) {
        // Build debt graph
        Map<String, Double> netBalances = calculateNetBalances(balances);
        
        // Separate creditors and debtors
        List<UserBalance> creditors = new ArrayList<>();
        List<UserBalance> debtors = new ArrayList<>();
        
        for (Map.Entry<String, Double> entry : netBalances.entrySet()) {
            if (entry.getValue() > 0) {
                creditors.add(new UserBalance(entry.getKey(), entry.getValue()));
            } else if (entry.getValue() < 0) {
                debtors.add(new UserBalance(entry.getKey(), -entry.getValue()));
            }
        }
        
        // Minimize transactions using greedy approach
        return minimizeTransactions(creditors, debtors);
    }
}
```

### **Q: "How would you implement recurring expenses?"**
```java
public class RecurringExpense extends Expense {
    private final RecurrencePattern pattern;
    private final LocalDate startDate;
    private final LocalDate endDate;
    
    public List<Expense> generateExpenses(LocalDate fromDate, LocalDate toDate) {
        List<Expense> expenses = new ArrayList<>();
        LocalDate currentDate = startDate;
        
        while (!currentDate.isAfter(toDate) && !currentDate.isAfter(endDate)) {
            if (!currentDate.isBefore(fromDate)) {
                Expense expense = createExpenseForDate(currentDate);
                expenses.add(expense);
            }
            currentDate = pattern.getNextDate(currentDate);
        }
        
        return expenses;
    }
}
```

---

## 🚀 Quick Extensions

### **Add New Split Type (3 minutes)**
```java
// 1. Add to enum
public enum SplitType { EQUAL, EXACT, PERCENTAGE, WEIGHTED }

// 2. Implement strategy
public class WeightedSplitStrategy implements SplitStrategy {
    @Override
    public List<Split> calculateSplits(Expense expense, List<User> participants, Map<String, Double> splitData) {
        double totalWeight = splitData.values().stream().mapToDouble(Double::doubleValue).sum();
        List<Split> splits = new ArrayList<>();
        
        for (User participant : participants) {
            double weight = splitData.get(participant.getUserId());
            double amount = expense.getAmount() * (weight / totalWeight);
            splits.add(new Split(participant, amount));
        }
        
        return splits;
    }
}

// 3. Register in service
splitStrategies.put(SplitType.WEIGHTED, new WeightedSplitStrategy());
```

### **Add Expense Categories (2 minutes)**
```java
public enum ExpenseType {
    FOOD, TRAVEL, ENTERTAINMENT, UTILITIES, RENT, SHOPPING, OTHER
}

public class Expense {
    // ... existing fields ...
    private final ExpenseType expenseType;
    
    // Add category-based logic if needed
    public boolean isEssential() {
        return expenseType == ExpenseType.UTILITIES || expenseType == ExpenseType.RENT;
    }
}
```

### **Add Settlement History (4 minutes)**
```java
public class Settlement {
    private final String settlementId;
    private final String fromUserId;
    private final String toUserId;
    private final double amount;
    private final LocalDateTime timestamp;
    private SettlementStatus status;
    
    public void complete() {
        this.status = SettlementStatus.COMPLETED;
    }
}

// In Service
public Settlement settleUp(String fromUserId, String toUserId, double amount) {
    // Validate and update balances
    Settlement settlement = new Settlement(fromUserId, toUserId, amount);
    settlements.add(settlement);
    settlement.complete();
    
    notifySettlementMade(settlement);
    return settlement;
}
```

---

## 🎯 Demo Script

### **Step 1: Setup Users and Group**
```java
User alice = new User("U001", "Alice", "alice@email.com");
User bob = new User("U002", "Bob", "bob@email.com");
User charlie = new User("U003", "Charlie", "charlie@email.com");

Group group = new Group("G001", "Weekend Trip");
group.addMember(alice);
group.addMember(bob);
group.addMember(charlie);
```

### **Step 2: Add Equal Split Expense**
```java
// Dinner: ₹1200 split equally
List<String> participants = Arrays.asList("U001", "U002", "U003");
Expense dinner = service.addExpense("G001", "Dinner", 1200.0, "U002", 
                                   participants, SplitType.EQUAL, null);
// Result: Alice owes Bob ₹400, Charlie owes Bob ₹400
```

### **Step 3: Add Exact Split Expense**
```java
// Hotel: ₹3000 with exact amounts
Map<String, Double> exactSplits = new HashMap<>();
exactSplits.put("U001", 1500.0); // Alice pays ₹1500
exactSplits.put("U002", 1000.0); // Bob pays ₹1000
exactSplits.put("U003", 500.0);  // Charlie pays ₹500

Expense hotel = service.addExpense("G001", "Hotel", 3000.0, "U001", 
                                  participants, SplitType.EXACT, exactSplits);
```

### **Step 4: Show Balances**
```java
service.displayBalances("U001"); // Alice's view
service.displayBalances("U002"); // Bob's view
service.displayBalances("U003"); // Charlie's view
```

### **Step 5: Settlement**
```java
// Alice settles with Bob
Settlement settlement = service.settleUp("U001", "U002", 400.0);
service.displayBalances("U001"); // Updated balances
```

---

## 💡 Pro Tips for Live Coding

### **Start with This Template**
```java
public class Main {
    public static void main(String[] args) {
        // 1. Setup system
        SplitwiseServiceImpl service = new SplitwiseServiceImpl();
        
        // 2. Add sample data
        setupSampleData(service);
        
        // 3. Demonstrate functionality
        demonstrateExpenseSharing(service);
    }
}
```

### **Time-Saving Shortcuts**
- Use `Arrays.asList()` for quick list creation
- Use `String.format("%.2f", amount)` for currency formatting
- Use `LocalDateTime.now()` for timestamps
- Use `UUID.randomUUID().toString().substring(0, 8)` for IDs

### **Error Handling Pattern**
```java
private void validateSplitData(double amount, SplitType splitType, Map<String, Double> splitData, List<User> participants) {
    switch (splitType) {
        case EXACT:
            double sum = splitData.values().stream().mapToDouble(Double::doubleValue).sum();
            if (Math.abs(sum - amount) > 0.01) {
                throw new IllegalArgumentException("Exact splits must sum to total amount");
            }
            break;
        case PERCENTAGE:
            double totalPercentage = splitData.values().stream().mapToDouble(Double::doubleValue).sum();
            if (Math.abs(totalPercentage - 100.0) > 0.01) {
                throw new IllegalArgumentException("Percentages must sum to 100%");
            }
            break;
    }
}
```

---

## 🏆 Success Checklist

### **Must Have (60% score)**
- ✅ Basic models (User, Group, Expense, Split)
- ✅ Equal split functionality
- ✅ Balance calculation and display
- ✅ At least 1 design pattern

### **Should Have (80% score)**
- ✅ Multiple split strategies
- ✅ 2-3 design patterns
- ✅ Settlement functionality
- ✅ Error handling and validation

### **Nice to Have (95% score)**
- ✅ All 4 design patterns
- ✅ Settlement optimization
- ✅ Comprehensive demo
- ✅ Extension examples (categories, recurring expenses)

**Remember**: Quality over quantity. Better to have fewer features implemented well than many features implemented poorly!

---

## 🔍 Key Algorithms

### **Balance Calculation Complexity**
- **Time**: O(1) for single balance lookup
- **Space**: O(n²) for n users (worst case: everyone owes everyone)

### **Settlement Optimization**
- **Time**: O(n²) for n users using greedy approach
- **Space**: O(n) for storing net balances

### **Split Calculation**
- **Equal**: O(n) where n = participants
- **Exact**: O(n) where n = participants  
- **Percentage**: O(n) where n = participants

**Focus on correctness first, then optimize if asked!**