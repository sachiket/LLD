# Splitwise-like Expense Sharing System

## Overview

This is a simplified in-memory implementation of an expense sharing system inspired by Splitwise. It allows multiple users to create and split expenses among friends, track who owes whom, and manage friendships.

### Key Features
- Create users with roles (ADMIN, USER).
- Add friends for users.
- Create expenses with various types (TRAVEL, FOOD, DRINKS, OTHER).
- Split expenses among users based on percentage splits.
- Track debts (amount others owe you) and owes (amount you owe others).
- In-memory data structures for simplicity; singleton pattern used for ExpenseManager.

---

## Main Components

### Models
- **User:** Represents a person with `id`, `role`, `name`, `email`, `number`, list of friends, debts (dues), and owes.
- **Expense:** Represents an expense with `id`, creator, payer, name, type, amount, and splits.
- **Split:** Represents a share of an expense for a user, currently using percentage-based split.
- **Owe / Debt:** Represent amounts owed between users to track debts.

### Services
- **UserService:** CRUD operations for users and friend management.
- **ExpenseManager:** Singleton responsible for expense creation, updating debts and owes accordingly.

---

## How The System Works

1. **User Management:** Users are created via `UserService`, which stores users in an in-memory map. Users can add friends (mutual friendship is ensured).

2. **Creating Expenses:** An expense is created by a user, specifying who paid, the amount, type, and splits (percentage for now).

3. **Splitting Logic:** The `ExpenseManager` calculates each user's share based on split percentages and updates:
    - The payer’s "owes" list (who owes them money).
    - The payees' "dues" list (who they owe money to).

4. **Tracking Balances:** Each `User` maintains lists of `Owe` and `Debt` objects reflecting current balances with other users.

---

## Design Decisions & Tradeoffs

- Enums used for `UserRole` and `ExpenseType` for domain clarity.
- User references are stored as user IDs (strings) for lightweight modeling.
- Percent splits supported; equal and exact splits can be added by extending `Split`.
- Singleton pattern for centralized expense management in memory.
- No persistent storage — suitable for demonstration and interview scope.
- Validation and error handling to be added for robustness.
- Concurrency and scaling considerations are out of scope but discussed.

---

## Possible Improvements & Extensions

- Add validation to ensure splits sum to 100% (or full amount).
- Support equal and exact splits with polymorphic `Split` subclasses.
- Implement expense update and deletion with debt adjustments.
- Add group management and settlements.
- Persist data to a database for durability.
- Handle concurrency with synchronization or distributed locks.
- Add RESTful APIs and authentication layers.
- Write unit and integration tests.

---

## Example Class Diagram (SQL Table Representation)

```sql
-- User Table
CREATE TABLE Users (
    id VARCHAR PRIMARY KEY,
    role VARCHAR, -- ADMIN or USER
    name VARCHAR,
    email VARCHAR,
    number BIGINT
);

-- Friends Table (Many-to-many User friendships)
CREATE TABLE Friends (
    user_id VARCHAR,
    friend_id VARCHAR,
    PRIMARY KEY(user_id, friend_id),
    FOREIGN KEY(user_id) REFERENCES Users(id),
    FOREIGN KEY(friend_id) REFERENCES Users(id)
);

-- Expense Table
CREATE TABLE Expenses (
    id VARCHAR PRIMARY KEY,
    creator_user_id VARCHAR,
    paid_by_user_id VARCHAR,
    name VARCHAR,
    type VARCHAR, -- ExpenseType enum: TRAVEL, FOOD, etc.
    amount INT,
    FOREIGN KEY(creator_user_id) REFERENCES Users(id),
    FOREIGN KEY(paid_by_user_id) REFERENCES Users(id)
);

-- Split Table
CREATE TABLE Splits (
    id VARCHAR PRIMARY KEY,
    expense_id VARCHAR,
    user_id VARCHAR,
    split_percent INT,
    FOREIGN KEY(expense_id) REFERENCES Expenses(id),
    FOREIGN KEY(user_id) REFERENCES Users(id)
);

-- Owe Table (User owes other users)
CREATE TABLE Owes (
    user_id VARCHAR,
    owed_to_user_id VARCHAR,
    amount INT,
    PRIMARY KEY(user_id, owed_to_user_id),
    FOREIGN KEY(user_id) REFERENCES Users(id),
    FOREIGN KEY(owed_to_user_id) REFERENCES Users(id)
);

-- Debt Table (User is owed by other users)
CREATE TABLE Debts (
    user_id VARCHAR,
    owed_by_user_id VARCHAR,
    amount INT,
    PRIMARY KEY(user_id, owed_by_user_id),
    FOREIGN KEY(user_id) REFERENCES Users(id),
    FOREIGN KEY(owed_by_user_id) REFERENCES Users(id)
);
```
```sql
+----------------+            +----------------+           +-------------+
|     User       |            |    Expense     |           |    Split    |
+----------------+            +----------------+           +-------------+
| - id: String   |<>----------| - id: String   |1         * | - id: String|
| - role: UserRole|           | - creatorUserId:String|     | - userId:String|
| - name: String |           | - paidByUserId:String |      | - splitPercent:int|
| - email: String|           | - name: String |           +-------------+
| - number: long |           | - type: ExpenseType |
| - friends: List<String> |   | - amount: int  |
| - owes: List<Owe>  |       | - splits: List<Split>|
| - dues: List<Debt> |       +----------------+
+----------------+

       |                             
       | 1                        *  
       |                            

+---------+            +--------+            +-------+
|   Owe   |            |  Debt  |            |UserRole|
+---------+            +--------+            +-------+
| - userId: String|    | - userId:String|    | ADMIN |
| - amount: int   |    | - amount:int   |    | USER  |
+---------+            +--------+            +-------+

+----------------+
| ExpenseType    |
+----------------+
| TRAVEL         |
| FOOD           |
| DRINKS         |
| OTHER          |
+----------------+

```