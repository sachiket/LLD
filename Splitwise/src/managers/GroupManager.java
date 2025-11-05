package managers;

import models.Group;
import models.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Singleton manager for handling group operations
 * Implements Singleton Design Pattern for centralized group management
 */
public class GroupManager {
    
    private static volatile GroupManager instance;
    private final Map<String, Group> groups;
    private final Map<String, List<String>> userGroups; // userId -> list of groupIds
    
    private GroupManager() {
        this.groups = new ConcurrentHashMap<>();
        this.userGroups = new ConcurrentHashMap<>();
    }
    
    /**
     * Gets the singleton instance of GroupManager
     * Thread-safe implementation using double-checked locking
     * @return The singleton GroupManager instance
     */
    public static GroupManager getInstance() {
        if (instance == null) {
            synchronized (GroupManager.class) {
                if (instance == null) {
                    instance = new GroupManager();
                }
            }
        }
        return instance;
    }
    
    /**
     * Creates a new group
     * @param name The group name
     * @param description The group description
     * @return The created group
     */
    public Group createGroup(String name, String description) {
        String groupId = generateGroupId();
        Group group = new Group(groupId, name, description);
        groups.put(groupId, group);
        return group;
    }
    
    /**
     * Creates a new group with just a name
     * @param name The group name
     * @return The created group
     */
    public Group createGroup(String name) {
        return createGroup(name, "");
    }
    
    /**
     * Gets a group by its ID
     * @param groupId The group ID
     * @return The group if found, null otherwise
     */
    public Group getGroupById(String groupId) {
        return groups.get(groupId);
    }
    
    /**
     * Gets all groups in the system
     * @return List of all groups
     */
    public List<Group> getAllGroups() {
        return new ArrayList<>(groups.values());
    }
    
    /**
     * Gets all groups that a user belongs to
     * @param userId The user ID
     * @return List of groups the user belongs to
     */
    public List<Group> getGroupsForUser(String userId) {
        List<Group> userGroupList = new ArrayList<>();
        List<String> groupIds = userGroups.get(userId);
        
        if (groupIds != null) {
            for (String groupId : groupIds) {
                Group group = groups.get(groupId);
                if (group != null) {
                    userGroupList.add(group);
                }
            }
        }
        
        return userGroupList;
    }
    
    /**
     * Adds a user to a group
     * @param groupId The group ID
     * @param user The user to add
     * @return true if the user was added successfully
     */
    public boolean addUserToGroup(String groupId, User user) {
        Group group = groups.get(groupId);
        if (group == null || user == null) {
            return false;
        }
        
        boolean added = group.addMember(user);
        if (added) {
            // Update user-groups mapping
            userGroups.computeIfAbsent(user.getUserId(), k -> new ArrayList<>()).add(groupId);
        }
        
        return added;
    }
    
    /**
     * Removes a user from a group
     * @param groupId The group ID
     * @param user The user to remove
     * @return true if the user was removed successfully
     */
    public boolean removeUserFromGroup(String groupId, User user) {
        Group group = groups.get(groupId);
        if (group == null || user == null) {
            return false;
        }
        
        boolean removed = group.removeMember(user);
        if (removed) {
            // Update user-groups mapping
            List<String> userGroupIds = userGroups.get(user.getUserId());
            if (userGroupIds != null) {
                userGroupIds.remove(groupId);
                if (userGroupIds.isEmpty()) {
                    userGroups.remove(user.getUserId());
                }
            }
        }
        
        return removed;
    }
    
    /**
     * Deletes a group from the system
     * @param groupId The group ID to delete
     * @return true if the group was deleted successfully
     */
    public boolean deleteGroup(String groupId) {
        Group group = groups.get(groupId);
        if (group == null) {
            return false;
        }
        
        // Remove group from all users' group lists
        for (User member : group.getMembers()) {
            List<String> userGroupIds = userGroups.get(member.getUserId());
            if (userGroupIds != null) {
                userGroupIds.remove(groupId);
                if (userGroupIds.isEmpty()) {
                    userGroups.remove(member.getUserId());
                }
            }
        }
        
        // Remove the group
        groups.remove(groupId);
        return true;
    }
    
    /**
     * Checks if a group exists
     * @param groupId The group ID to check
     * @return true if the group exists
     */
    public boolean groupExists(String groupId) {
        return groups.containsKey(groupId);
    }
    
    /**
     * Checks if a user is a member of a specific group
     * @param groupId The group ID
     * @param userId The user ID
     * @return true if the user is a member of the group
     */
    public boolean isUserInGroup(String groupId, String userId) {
        Group group = groups.get(groupId);
        return group != null && group.isMember(userId);
    }
    
    /**
     * Gets the number of groups in the system
     * @return The total number of groups
     */
    public int getGroupCount() {
        return groups.size();
    }
    
    /**
     * Gets the number of groups a user belongs to
     * @param userId The user ID
     * @return The number of groups the user belongs to
     */
    public int getUserGroupCount(String userId) {
        List<String> userGroupIds = userGroups.get(userId);
        return userGroupIds != null ? userGroupIds.size() : 0;
    }
    
    /**
     * Finds groups by name (case-insensitive partial match)
     * @param namePattern The name pattern to search for
     * @return List of groups matching the pattern
     */
    public List<Group> findGroupsByName(String namePattern) {
        List<Group> matchingGroups = new ArrayList<>();
        
        if (namePattern == null || namePattern.trim().isEmpty()) {
            return matchingGroups;
        }
        
        String lowerPattern = namePattern.toLowerCase().trim();
        
        for (Group group : groups.values()) {
            if (group.getName().toLowerCase().contains(lowerPattern)) {
                matchingGroups.add(group);
            }
        }
        
        return matchingGroups;
    }
    
    /**
     * Gets groups that have a specific user as a member
     * @param user The user to search for
     * @return List of groups containing the user
     */
    public List<Group> getGroupsContainingUser(User user) {
        List<Group> containingGroups = new ArrayList<>();
        
        if (user == null) {
            return containingGroups;
        }
        
        for (Group group : groups.values()) {
            if (group.isMember(user)) {
                containingGroups.add(group);
            }
        }
        
        return containingGroups;
    }
    
    /**
     * Clears all groups (useful for testing)
     */
    public void clearAllGroups() {
        groups.clear();
        userGroups.clear();
    }
    
    /**
     * Generates a unique group ID
     * @return A unique group ID string
     */
    private String generateGroupId() {
        return "GRP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    /**
     * Gets statistics about the group system
     * @return A map containing various statistics
     */
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalGroups", groups.size());
        stats.put("totalUsersInGroups", userGroups.size());
        
        int totalMembers = 0;
        int maxGroupSize = 0;
        int minGroupSize = Integer.MAX_VALUE;
        
        for (Group group : groups.values()) {
            int memberCount = group.getMemberCount();
            totalMembers += memberCount;
            maxGroupSize = Math.max(maxGroupSize, memberCount);
            if (memberCount > 0) {
                minGroupSize = Math.min(minGroupSize, memberCount);
            }
        }
        
        stats.put("totalMemberships", totalMembers);
        stats.put("averageGroupSize", groups.isEmpty() ? 0 : (double) totalMembers / groups.size());
        stats.put("maxGroupSize", maxGroupSize);
        stats.put("minGroupSize", minGroupSize == Integer.MAX_VALUE ? 0 : minGroupSize);
        
        return stats;
    }
}