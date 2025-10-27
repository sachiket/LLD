package services;

import models.User;

import java.util.List;

public interface UserService {
    User createUser(String name, String email, long number, String role);
    User getUserById(String userId);
    void updateUser(User user);
    void deleteUser(String userId);
    List<User> getAllUsers();
    void addFriend(String userId, String friendId);
}