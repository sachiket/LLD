package services;

import models.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpenseManager {
    private static ExpenseManager manager;
    private ExpenseManager() {}
    private final Map<User, List<Expense>> expenceMap = new HashMap<>();
    private static UserService userService;

    public static synchronized ExpenseManager getInstance() {
        if (manager == null) {
            manager = new ExpenseManager();
        }
        return manager;
    }

    public static synchronized void injectUserService(UserService service){
        if(userService == null){
            userService = service;
        }
    }

    public void createExpense(User creator, Expense expense){
        //add expense to the user
        expenceMap.putIfAbsent(creator, new ArrayList<>());
        expenceMap.get(creator).add(expense);

        //update dept and owe for the user and friends
        int amount = expense.getAmount();
        User paidByUser = userService.getUserById(expense.getPaidByUserId());

        for(Split split: expense.getSplits()){
            int amountPercentage = split.getSplitPercent() * (amount / 100);
            String payeeId = split.getUserId();
            User payee = userService.getUserById(payeeId);
            paidByUser.addOwe(new Owe(payeeId, amountPercentage));
            payee.addDue(new Debt(expense.getPaidByUserId(), amountPercentage));
            userService.updateUser(payee);
        }
        userService.updateUser(paidByUser);
    }

    public List<Expense> getAllExpenseByUser(User user){
        return expenceMap.get(user);
    }
}
