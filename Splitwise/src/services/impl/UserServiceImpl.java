package services.impl;

import enums.UserRole;
import models.User;
import services.UserService;

import java.util.*;

public class UserServiceImpl implements UserService {
    private final Map<String, User> userMap = new HashMap<>();

    @Override
    public User createUser(String name, String email, long number, String role) {
        User user = new User(UserRole.valueOf(role.toUpperCase()), name, email, number);
        userMap.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUserById(String userId) {
        return userMap.get(userId);
    }

    @Override
    public void updateUser(User user) {
        userMap.put(user.getId(), user);
    }

    @Override
    public void deleteUser(String userId) {
        userMap.remove(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public void addFriend(String userId, String friendId) {
        User user = userMap.get(userId);
        User friend = userMap.get(friendId);
        if (user != null && friend != null && !user.getFriends().contains(friendId)) {
            user.addFriends(friendId);
            friend.addFriends(userId);
        }
    }
}
