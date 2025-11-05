# 🔄 Splitwise System Flow Diagrams

## 📋 Quick Navigation
- [System Architecture Flow](#system-architecture-flow)
- [Expense Addition Flow](#expense-addition-flow)
- [Design Patterns Flow](#design-patterns-flow)
- [Balance Calculation Flow](#balance-calculation-flow)
- [Settlement Process Flow](#settlement-process-flow)

---

## 🏗️ System Architecture Flow

```mermaid
graph TD
    A[User] --> B[SplitwiseService]
    B --> C[User Management]
    B --> D[Group Management]
    B --> E[Expense Management]
    B --> F[Settlement Management]
    
    C --> G[User Repository]
    D --> H[Group Repository]
    E --> I[Expense Repository]
    F --> J[Settlement Repository]
    
    E --> K[SplitStrategy]
    E --> L[ExpenseFactory]
    E --> M[ExpenseObserver]
    
    N[GroupManager] --> D
    
    subgraph "Core Models"
        O[User]
        P[Group]
        Q[Expense]
        R[Split]
        S[Balance]
        T[Settlement]
    end
```

---

## 💰 Expense Addition Flow

### **Happy Path Flow:**
```
1. User Creates Expense
   ↓
2. System Validates Input
   ↓
3. System Selects Split Strategy
   ↓
4. Strategy Calculates Individual Splits
   ↓
5. System Updates User Balances
   ↓
6. System Creates Expense Record (Factory Pattern)
   ↓
7. System Notifies Observers (Observer Pattern)
   ↓
8. System Returns Expense Confirmation
```

### **Detailed Expense Addition Flow:**
```mermaid
sequenceDiagram
    participant U as User
    participant S as SplitwiseService
    participant SS as SplitStrategy
    participant EF as ExpenseFactory
    participant O as Observer
    participant User1 as User1
    participant User2 as User2

    U->>S: addExpense(groupId, description, amount, paidById, participants, splitType, splitData)
    S->>S: validateInput()
    S->>SS: calculateSplits(expense, participants, splitData)
    SS-->>S: List<Split>
    S->>EF: createExpense(description, amount, paidBy, participants, splitType, splitData)
    EF-->>S: expense
    S->>User1: updateBalance(paidById, splitAmount)
    S->>User2: updateBalance(paidById, splitAmount)
    S->>O: onExpenseAdded(expense)
    S-->>U: expense
```

---

## 🎯 Design Patterns Flow

### **Strategy Pattern (Split Calculation):**
```mermaid
graph LR
    A[SplitwiseService] --> B[SplitStrategy Interface]
    B --> C[EqualSplitStrategy]
    B --> D[ExactSplitStrategy]
    B --> E[PercentageSplitStrategy]
    B --> F[WeightedSplitStrategy]
    
    C --> G[Amount ÷ Participants]
    D --> H[Use Specified Amounts]
    E --> I[Amount × Percentage]
    F --> J[Amount × Weight Ratio]
```

### **Factory Pattern (Expense Creation):**
```mermaid
graph TD
    A[Service Layer] --> B[ExpenseFactory]
    B --> C[Generate Expense ID]
    B --> D[Validate Split Data]
    B --> E[Create Expense Object]
    B --> F[Return Expense]
    
    C --> G[EXP-XXXXXXXX]
    D --> H[Check Sum = Total]
    E --> I[new Expense(...)]
```

### **Observer Pattern (Notifications):**
```mermaid
graph TD
    A[Expense Event] --> B[SplitwiseService]
    B --> C[Notify All Observers]
    
    C --> D[ConsoleNotificationObserver]
    C --> E[EmailNotificationObserver]
    C --> F[PushNotificationObserver]
    
    D --> G[Print to Console]
    E --> H[Send Email]
    F --> I[Send Push Notification]
```

### **Singleton Pattern (Group Manager):**
```mermaid
graph TD
    A[Multiple Clients] --> B[GroupManager.getInstance()]
    B --> C{Instance Exists?}
    C -->|No| D[Create New Instance]
    C -->|Yes| E[Return Existing Instance]
    D --> F[Single GroupManager Instance]
    E --> F
```

---

## 📊 Balance Calculation Flow

### **Balance Update Process:**
```mermaid
graph TD
    A[New Expense Added] --> B[Get Paid By User]
    B --> C[Get All Splits]
    C --> D[For Each Split]
    D --> E{Split User = Paid By User?}
    E -->|No| F[Update Split User Balance: +amount]
    E -->|Yes| G[Skip - User paid for themselves]
    F --> H[Update Paid By User Balance: -amount]
    H --> I[Next Split]
    G --> I
    I --> J{More Splits?}
    J -->|Yes| D
    J -->|No| K[Balance Update Complete]
```

### **Balance Retrieval Flow:**
```mermaid
sequenceDiagram
    participant U as User
    participant S as Service
    participant User1 as User1
    participant User2 as User2

    U->>S: getBalances(userId)
    S->>User1: getAllBalances()
    User1-->>S: Map<friendId, amount>
    
    loop For each balance entry
        S->>S: if amount > 0: User owes Friend
        S->>S: if amount < 0: Friend owes User
        S->>S: if amount = 0: No balance
    end
    
    S-->>U: List<Balance>
```

---

## 💸 Settlement Process Flow

### **Settlement Creation:**
```mermaid
graph TD
    A[User Initiates Settlement] --> B[Validate Settlement Amount]
    B --> C{Amount <= Owed Amount?}
    C -->|No| D[Throw Exception]
    C -->|Yes| E[Update From User Balance]
    E --> F[Update To User Balance]
    F --> G[Create Settlement Record]
    G --> H[Notify Observers]
    H --> I[Return Settlement]
```

### **Settlement Optimization Flow:**
```mermaid
graph TD
    A[Get All User Balances] --> B[Calculate Net Balances]
    B --> C[Separate Creditors and Debtors]
    C --> D[Sort by Amount]
    D --> E[Match Largest Creditor with Largest Debtor]
    E --> F[Create Settlement Transaction]
    F --> G[Update Net Balances]
    G --> H{More Balances to Settle?}
    H -->|Yes| E
    H -->|No| I[Return Optimized Settlements]
```

---

## 🔄 State Transition Diagrams

### **Expense State Transitions:**
```mermaid
stateDiagram-v2
    [*] --> Created
    Created --> Validated : Input validation passed
    Validated --> SplitsCalculated : Strategy applied
    SplitsCalculated --> BalancesUpdated : User balances modified
    BalancesUpdated --> Persisted : Stored in system
    Persisted --> [*]
    
    Created --> [*] : Validation failed
```

### **Settlement State Transitions:**
```mermaid
stateDiagram-v2
    [*] --> Pending
    Pending --> Completed : Payment processed
    Pending --> Cancelled : User cancellation
    Completed --> [*]
    Cancelled --> [*]
```

---

## 👥 Multi-User Interaction Flow

### **Group Expense Scenario:**
```mermaid
sequenceDiagram
    participant A as Alice
    participant B as Bob
    participant C as Charlie
    participant S as System

    A->>S: addExpense("Dinner", 1200, "Bob", [Alice, Bob, Charlie], EQUAL)
    S->>S: Calculate splits: 400 each
    S->>A: Update balance: owes Bob +400
    S->>B: Update balance: owed by Alice -400, owed by Charlie -400
    S->>C: Update balance: owes Bob +400
    S-->>A: Expense created
    S-->>B: Notification: Expense added
    S-->>C: Notification: Expense added
```

### **Settlement Between Users:**
```mermaid
sequenceDiagram
    participant A as Alice
    participant B as Bob
    participant S as System

    A->>S: settleUp("Alice", "Bob", 400)
    S->>S: Validate: Alice owes Bob >= 400
    S->>A: Update balance: owes Bob -400
    S->>B: Update balance: owed by Alice +400
    S->>S: Create settlement record
    S-->>A: Settlement completed
    S-->>B: Notification: Payment received
```

---

## 🔍 Error Handling Flow

### **Expense Addition Error Flow:**
```mermaid
graph TD
    A[User Adds Expense] --> B{Valid Input?}
    B -->|No| C[Return Validation Error]
    B -->|Yes| D{Valid Split Data?}
    D -->|No| E[Return Split Validation Error]
    D -->|Yes| F{Users Exist?}
    F -->|No| G[Return User Not Found Error]
    F -->|Yes| H{Group Exists?}
    H -->|No| I[Return Group Not Found Error]
    H -->|Yes| J[Process Expense]
    
    C --> K[Show Error Message]
    E --> K
    G --> K
    I --> K
```

### **Settlement Error Handling:**
```mermaid
graph TD
    A[User Initiates Settlement] --> B{Users Exist?}
    B -->|No| C[User Not Found Error]
    B -->|Yes| D{Settlement Amount > 0?}
    D -->|No| E[Invalid Amount Error]
    D -->|Yes| F{Amount <= Owed Amount?}
    F -->|No| G[Insufficient Balance Error]
    F -->|Yes| H[Process Settlement]
    
    C --> I[Return Error Response]
    E --> I
    G --> I
```

---

## 📱 Split Strategy Comparison

### **Strategy Selection Flow:**
```mermaid
graph TD
    A[Expense Input] --> B{Split Type?}
    B -->|EQUAL| C[EqualSplitStrategy]
    B -->|EXACT| D[ExactSplitStrategy]
    B -->|PERCENTAGE| E[PercentageSplitStrategy]
    B -->|WEIGHTED| F[WeightedSplitStrategy]
    
    C --> G[Amount ÷ Participants]
    D --> H[Validate: Sum = Total]
    E --> I[Validate: Percentages = 100%]
    F --> J[Calculate: Amount × Weight/TotalWeight]
    
    G --> K[Return Equal Splits]
    H --> L[Return Exact Splits]
    I --> M[Return Percentage Splits]
    J --> N[Return Weighted Splits]
```

---

## 🏢 Multi-Group Architecture

### **Group Management Flow:**
```mermaid
graph TD
    A[GroupManager Singleton] --> B[Group 1: Weekend Trip]
    A --> C[Group 2: Office Lunch]
    A --> D[Group N: Roommates]
    
    B --> E[Alice, Bob, Charlie]
    B --> F[Expenses: Dinner, Hotel, Taxi]
    
    C --> G[Team Members]
    C --> H[Expenses: Lunch, Coffee]
    
    D --> I[Roommate 1, 2, 3]
    D --> J[Expenses: Rent, Utilities, Groceries]
```

### **Cross-Group Balance Isolation:**
```mermaid
sequenceDiagram
    participant U as User
    participant S as Service
    participant G1 as Group1
    participant G2 as Group2

    U->>S: getBalances(userId)
    S->>G1: getGroupBalances(userId, group1Id)
    G1-->>S: Group1 balances
    S->>G2: getGroupBalances(userId, group2Id)
    G2-->>S: Group2 balances
    S->>S: Aggregate all group balances
    S-->>U: Combined balance view
```

---

## 📈 Performance Optimization Flow

### **Balance Calculation Optimization:**
```mermaid
graph TD
    A[Expense Added] --> B{Use Cached Balances?}
    B -->|Yes| C[Update Only Affected Balances]
    B -->|No| D[Recalculate All Balances]
    
    C --> E[O(1) Update Operation]
    D --> F[O(n) Calculation Operation]
    
    E --> G[Update Cache]
    F --> H[Rebuild Cache]
    
    G --> I[Return Updated Balances]
    H --> I
```

**These diagrams provide a comprehensive visual understanding of the Splitwise system architecture and flows, perfect for interview discussions!**