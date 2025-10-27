package models;

import enums.ExpenseType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Expense {
    private final String id = UUID.randomUUID().toString();
    private String creatorUserId;
    private String paidByUserId;
    private final List<Split> splits;
    private String name;
    private ExpenseType type;
    private int amount;

    public Expense(String creatorUserId, String paidByUserId, String name, ExpenseType type, int amount) {
        this.creatorUserId = creatorUserId;
        this.name = name;
        this.type = type;
        this.amount = amount;
        this.paidByUserId = paidByUserId;
        this.splits = new ArrayList<>();
    }

    public String getCreatorUserId() {
        return creatorUserId;
    }

    public void setCreatorUserId(String creatorUserId) {
        this.creatorUserId = creatorUserId;
    }

    public String getId() {
        return id;
    }

    public List<Split> getSplits() {
        return splits;
    }

    public void addSplits(Split split) {
        this.splits.add(split);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ExpenseType getType() {
        return type;
    }

    public void setType(ExpenseType type) {
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getPaidByUserId() {
        return paidByUserId;
    }

    public void setPaidByUserId(String paidByUserId) {
        this.paidByUserId = paidByUserId;
    }
}
