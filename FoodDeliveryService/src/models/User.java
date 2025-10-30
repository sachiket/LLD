package models;

import enums.UserRole;

public class User {
    protected String id;
    protected String name;
    protected String phone;
    protected UserRole role;
    protected Location location;
    protected boolean isActive;

    public User(String id, String name, String phone, UserRole role, Location location) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.role = role;
        this.location = location;
        this.isActive = true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", role=" + role +
                ", location=" + location +
                ", isActive=" + isActive +
                '}';
    }
}