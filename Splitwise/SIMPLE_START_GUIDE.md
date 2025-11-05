# 🎯 SIMPLE START GUIDE - Splitwise Interview

## 🚀 **DON'T PANIC! Start Here:**

### **Your Opening Line (30 seconds):**
*"I'll design an expense sharing system step by step. Let me start with the basic entities and build up gradually."*

---

## 📝 **STEP 1: Draw Basic Entities (2 minutes)**

**On whiteboard/screen, draw:**
```
[User] ----belongs to----> [Group] ----has----> [Expense] ----contains----> [Split]
                                         |
                                    [Balance]
```

**Say:** *"These are my core entities. Let me code them..."*

### **Code Step 1:**
```java
// Start with just this!
class User {
    String userId;
    String name;
    String email;
    Map<String, Double> balances = new HashMap<>(); // friendId -> amount
}

class Group {
    String groupId;
    String name;
    List<User> members = new ArrayList<>();
}

class Expense {
    String expenseId;
    String description;
    double amount;
    User paidBy;
    List<User> participants;
    String splitType; // "EQUAL", "EXACT", "PERCENTAGE"
}

class Split {
    User user;
    double amount;
    double percentage; // for percentage splits
}
```

**Say:** *"This covers the basics. Now let me add balance calculation..."*

---

## 📝 **STEP 2: Add Balance Logic (3 minutes)**

```java
class User {
    // ... previous code ...
    
    void updateBalance(String friendId, double amount) {
        balances.put(friendId, balances.getOrDefault(friendId, 0.0) + amount);
    }
    
    double getBalanceWith(String friendId) {
        return balances.getOrDefault(friendId, 0.0);
    }
    
    Map<String, Double> getAllBalances() {
        return new HashMap<>(balances);
    }
}

class Balance {
    String fromUserId;
    String toUserId;
    double amount;
    
    Balance(String fromUserId, String toUserId, double amount) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.amount = amount;
    }
}
```

**Say:** *"Great! Now I can track who owes whom. Let me add the core splitting functionality..."*

---

## 📝 **STEP 3: Add Core Split Methods (4 minutes)**

```java
class SplitwiseService {
    Map<String, User> users = new HashMap<>();
    Map<String, Group> groups = new HashMap<>();
    List<Expense> expenses = new ArrayList<>();
    
    Expense addExpense(String groupId, String description, double amount, 
                      String paidById, List<String> participantIds, String splitType) {
        
        Group group = groups.get(groupId);
        User paidBy = users.get(paidById);
        List<User> participants = new ArrayList<>();
        
        for (String participantId : participantIds) {
            participants.add(users.get(participantId));
        }
        
        // Create expense
        String expenseId = "EXP-" + System.currentTimeMillis();
        Expense expense = new Expense(expenseId, description, amount, paidBy, participants, splitType);
        
        // Calculate splits
        List<Split> splits = calculateSplits(expense);
        
        // Update balances
        updateBalances(paidBy, splits);
        
        expenses.add(expense);
        return expense;
    }
    
    List<Split> calculateSplits(Expense expense) {
        List<Split> splits = new ArrayList<>();
        
        if ("EQUAL".equals(expense.splitType)) {
            double amountPerPerson = expense.amount / expense.participants.size();
            for (User participant : expense.participants) {
                splits.add(new Split(participant, amountPerPerson, 0));
            }
        }
        
        return splits;
    }
    
    void updateBalances(User paidBy, List<Split> splits) {
        for (Split split : splits) {
            if (!split.user.userId.equals(paidBy.userId)) {
                // split.user owes paidBy the split amount
                split.user.updateBalance(paidBy.userId, split.amount);
                // paidBy is owed by split.user
                paidBy.updateBalance(split.user.userId, -split.amount);
            }
        }
    }
}
```

**Say:** *"Perfect! This handles equal splits. Now let me add other split types..."*

---

## 📝 **STEP 4: Add Multiple Split Types (3 minutes)**

```java
enum SplitType { EQUAL, EXACT, PERCENTAGE }

class SplitwiseService {
    // ... previous code ...
    
    Expense addExpense(String groupId, String description, double amount, 
                      String paidById, List<String> participantIds, SplitType splitType,
                      Map<String, Double> splitData) {
        
        // ... existing code ...
        
        // Calculate splits based on type
        List<Split> splits = calculateSplits(expense, splitData);
        
        // ... rest of the method
    }
    
    List<Split> calculateSplits(Expense expense, Map<String, Double> splitData) {
        List<Split> splits = new ArrayList<>();
        
        switch (expense.splitType) {
            case EQUAL:
                double amountPerPerson = expense.amount / expense.participants.size();
                for (User participant : expense.participants) {
                    splits.add(new Split(participant, amountPerPerson, 0));
                }
                break;
                
            case EXACT:
                for (User participant : expense.participants) {
                    double exactAmount = splitData.get(participant.userId);
                    splits.add(new Split(participant, exactAmount, 0));
                }
                break;
                
            case PERCENTAGE:
                for (User participant : expense.participants) {
                    double percentage = splitData.get(participant.userId);
                    double amount = expense.amount * (percentage / 100.0);
                    splits.add(new Split(participant, amount, percentage));
                }
                break;
        }
        
        return splits;
    }
}
```

**Say:** *"Excellent! Now it handles all split types. Let me add balance display..."*

---

## 📝 **STEP 5: Add Balance Display (2 minutes)**

```java
class SplitwiseService {
    // ... previous code ...
    
    List<Balance> getBalances(String userId) {
        List<Balance> balances = new ArrayList<>();
        User user = users.get(userId);
        
        for (Map.Entry<String, Double> entry : user.getAllBalances().entrySet()) {
            String friendId = entry.getKey();
            double amount = entry.getValue();
            
            if (amount > 0) {
                // User owes friend
                balances.add(new Balance(userId, friendId, amount));
            } else if (amount < 0) {
                // Friend owes user
                balances.add(new Balance(friendId, userId, -amount));
            }
        }
        
        return balances;
    }
    
    void displayBalances(String userId) {
        List<Balance> balances = getBalances(userId);
        User user = users.get(userId);
        
        System.out.println("Balances for " + user.name + ":");
        
        if (balances.isEmpty()) {
            System.out.println("No outstanding balances!");
            return;
        }
        
        for (Balance balance : balances) {
            User fromUser = users.get(balance.fromUserId);
            User toUser = users.get(balance.toUserId);
            
            if (balance.fromUserId.equals(userId)) {
                System.out.println("You owe " + toUser.name + ": ₹" + String.format("%.2f", balance.amount));
            } else {
                System.out.println(fromUser.name + " owes you: ₹" + String.format("%.2f", balance.amount));
            }
        }
    }
}
```

**Say:** *"Great! Now users can see their balances. Let me add settlement functionality..."*

---

## 📝 **STEP 6: Add Settlement (3 minutes)**

```java
class Settlement {
    String fromUserId;
    String toUserId;
    double amount;
    LocalDateTime timestamp;
    
    Settlement(String fromUserId, String toUserId, double amount) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }
}

class SplitwiseService {
    List<Settlement> settlements = new ArrayList<>();
    
    // ... previous code ...
    
    Settlement settleUp(String fromUserId, String toUserId, double amount) {
        User fromUser = users.get(fromUserId);
        User toUser = users.get(toUserId);
        
        // Validate settlement
        double currentBalance = fromUser.getBalanceWith(toUserId);
        if (currentBalance < amount) {
            throw new IllegalArgumentException("Cannot settle more than owed amount");
        }
        
        // Update balances
        fromUser.updateBalance(toUserId, -amount);
        toUser.updateBalance(fromUserId, amount);
        
        // Record settlement
        Settlement settlement = new Settlement(fromUserId, toUserId, amount);
        settlements.add(settlement);
        
        System.out.println(fromUser.name + " paid " + toUser.name + ": ₹" + String.format("%.2f", amount));
        
        return settlement;
    }
    
    List<Settlement> getOptimalSettlements(String userId) {
        // Simple implementation - can be optimized with graph algorithms
        List<Settlement> optimalSettlements = new ArrayList<>();
        List<Balance> balances = getBalances(userId);
        
        for (Balance balance : balances) {
            if (balance.fromUserId.equals(userId)) {
                optimalSettlements.add(new Settlement(balance.fromUserId, balance.toUserId, balance.amount));
            }
        }
        
        return optimalSettlements;
    }
}
```

**Say:** *"Perfect! Now let me make it more professional with design patterns..."*

---

## 📝 **STEP 7: Add Design Patterns (5 minutes)**

### **Strategy Pattern for Split Calculation:**
```java
interface SplitStrategy {
    List<Split> calculateSplits(Expense expense, List<User> participants, Map<String, Double> splitData);
}

class EqualSplitStrategy implements SplitStrategy {
    public List<Split> calculateSplits(Expense expense, List<User> participants, Map<String, Double> splitData) {
        List<Split> splits = new ArrayList<>();
        double amountPerPerson = expense.amount / participants.size();
        
        for (User participant : participants) {
            splits.add(new Split(participant, amountPerPerson, 0));
        }
        
        return splits;
    }
}

class ExactSplitStrategy implements SplitStrategy {
    public List<Split> calculateSplits(Expense expense, List<User> participants, Map<String, Double> splitData) {
        List<Split> splits = new ArrayList<>();
        
        for (User participant : participants) {
            double exactAmount = splitData.get(participant.userId);
            splits.add(new Split(participant, exactAmount, 0));
        }
        
        return splits;
    }
}
```

### **Factory Pattern for Expense Creation:**
```java
class ExpenseFactory {
    static Expense createExpense(String description, double amount, User paidBy, 
                                List<User> participants, SplitType splitType, Map<String, Double> splitData) {
        String expenseId = "EXP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return new Expense(expenseId, description, amount, paidBy, participants, splitType, splitData);
    }
}
```

### **Observer Pattern for Notifications:**
```java
interface ExpenseObserver {
    void onExpenseAdded(Expense expense);
    void onSettlementMade(Settlement settlement);
}

class ConsoleNotificationObserver implements ExpenseObserver {
    public void onExpenseAdded(Expense expense) {
        System.out.println("New expense added: " + expense.description + " - ₹" + expense.amount);
    }
    
    public void onSettlementMade(Settlement settlement) {
        System.out.println("Settlement made: ₹" + settlement.amount);
    }
}
```

### **Updated Service with Patterns:**
```java
class SplitwiseService {
    Map<SplitType, SplitStrategy> splitStrategies = new HashMap<>();
    List<ExpenseObserver> observers = new ArrayList<>();
    
    SplitwiseService() {
        splitStrategies.put(SplitType.EQUAL, new EqualSplitStrategy());
        splitStrategies.put(SplitType.EXACT, new ExactSplitStrategy());
        splitStrategies.put(SplitType.PERCENTAGE, new PercentageSplitStrategy());
    }
    
    void addObserver(ExpenseObserver observer) {
        observers.add(observer);
    }
    
    Expense addExpense(String groupId, String description, double amount, 
                      String paidById, List<String> participantIds, SplitType splitType,
                      Map<String, Double> splitData) {
        
        // Get strategy and calculate splits
        SplitStrategy strategy = splitStrategies.get(splitType);
        List<Split> splits = strategy.calculateSplits(expense, participants, splitData);
        
        // Create expense using factory
        Expense expense = ExpenseFactory.createExpense(description, amount, paidBy, participants, splitType, splitData);
        
        // Update balances and notify observers
        updateBalances(paidBy, splits);
        notifyExpenseAdded(expense);
        
        return expense;
    }
}
```

**Say:** *"Now it's much more extensible and follows design patterns!"*

---

## 🎯 **Your Interview Mindset:**

### **Think Step by Step:**
1. **"What are the main entities?"** → User, Group, Expense, Split, Balance
2. **"What are the main operations?"** → Add Expense, Calculate Splits, Show Balances, Settle Up
3. **"How can I make it scalable?"** → Multiple Groups, Service Layer
4. **"How can I make it flexible?"** → Design Patterns for Split Strategies

### **Always Explain:**
- *"I'm starting simple and building up..."*
- *"Let me add this for better organization..."*
- *"This pattern will make it more flexible..."*
- *"I can extend this easily for new requirements..."*

### **If Asked About Extensions:**
- **New split type?** *"Just implement SplitStrategy interface"*
- **Currency support?** *"Add CurrencyConverter with Strategy pattern"*
- **Expense categories?** *"Extend ExpenseType enum and add category-based logic"*
- **Settlement optimization?** *"Implement graph algorithms to minimize transactions"*

---

## ✅ **Success Checklist:**

- ✅ Started simple with basic classes
- ✅ Built functionality incrementally  
- ✅ Explained reasoning at each step
- ✅ Added design patterns naturally
- ✅ Showed extensibility
- ✅ Handled balance calculations correctly
- ✅ Demonstrated clean code principles

**Remember: It's not about perfect code, it's about showing your thought process!**

---

## 🎪 **Quick Demo Script:**

1. **Create Users**: Alice, Bob, Charlie
2. **Create Group**: "Weekend Trip"
3. **Add Equal Expense**: "Dinner ₹1200" (split equally)
4. **Show Balances**: Alice owes Bob ₹400, Charlie owes Bob ₹400
5. **Add Exact Expense**: "Hotel ₹3000" (Alice: ₹1500, Bob: ₹1000, Charlie: ₹500)
6. **Show Updated Balances**: Net amounts after both expenses
7. **Settle Up**: Alice pays Bob ₹400
8. **Final Balances**: Show remaining amounts

**Key Points to Highlight:**
- Balance calculation logic
- Different split strategies
- Settlement functionality
- Design pattern usage
- Extensibility for new features