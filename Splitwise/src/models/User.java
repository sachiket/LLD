package models;

import enums.UserRole;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {
    private final String id = UUID.randomUUID().toString();
    private UserRole role;
    private String name;
    private String email;
    private long number;
    private final List<String> friends;
    private final List<Owe> owes;
    private final List<Debt> dues;


    public User(UserRole role, String name, String email, long number) {
        this.role = role;        this.name = name;
        this.email = email;
        this.number = number;
        this.friends = new ArrayList<>();
        this.owes = new ArrayList<>();
        this.dues = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getFriends() {
        return friends;
    }

    public void addFriends(String userId) {
        this.friends.add(userId);
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public List<Owe> getOwes() {
        return owes;
    }

    public List<Debt> getDues() {
        return dues;
    }

    public void addDue(Debt due){
        dues.add(due);
    }

    public void addOwe(Owe owe){
        owes.add(owe);
    }
}
