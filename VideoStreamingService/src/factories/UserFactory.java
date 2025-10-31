package factories;

import models.User;
import models.Channel;
import enums.UserRole;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserFactory {
    
    /**
     * Creates a new user with the given name and metadata
     * @param name User's name
     * @param metadata Additional user metadata
     * @return Created User object
     */
    public static User createUser(String name, Map<String, String> metadata) {
        String userId = "USER_" + UUID.randomUUID().toString().substring(0, 8);
        User user = new User(userId, name, UserRole.VIEWER);
        
        // Set metadata if provided
        if (metadata != null) {
            for (Map.Entry<String, String> entry : metadata.entrySet()) {
                user.setMetadata(entry.getKey(), entry.getValue());
            }
        }
        
        return user;
    }
    
    /**
     * Creates a new user with specific role
     * @param name User's name
     * @param role User's role
     * @param metadata Additional user metadata
     * @return Created User object
     */
    public static User createUser(String name, UserRole role, Map<String, String> metadata) {
        String userId = "USER_" + UUID.randomUUID().toString().substring(0, 8);
        User user = new User(userId, name, role);
        
        // Set metadata if provided
        if (metadata != null) {
            for (Map.Entry<String, String> entry : metadata.entrySet()) {
                user.setMetadata(entry.getKey(), entry.getValue());
            }
        }
        
        return user;
    }
    
    /**
     * Creates a new channel for the given user
     * @param userId Owner user ID
     * @param channelName Channel name
     * @param description Channel description
     * @param category Channel category
     * @return Created Channel object
     */
    public static Channel createChannel(String userId, String channelName, String description, String category) {
        String channelId = "CHANNEL_" + UUID.randomUUID().toString().substring(0, 8);
        Channel channel = new Channel(channelId, userId, channelName, description, category);
        
        // Set default metadata
        channel.setMetadata("createdBy", "UserFactory");
        channel.setMetadata("version", "1.0");
        
        return channel;
    }
    
    /**
     * Creates a new channel with additional metadata
     * @param userId Owner user ID
     * @param channelName Channel name
     * @param description Channel description
     * @param category Channel category
     * @param metadata Additional channel metadata
     * @return Created Channel object
     */
    public static Channel createChannel(String userId, String channelName, String description, 
                                      String category, Map<String, String> metadata) {
        Channel channel = createChannel(userId, channelName, description, category);
        
        // Set additional metadata if provided
        if (metadata != null) {
            for (Map.Entry<String, String> entry : metadata.entrySet()) {
                channel.setMetadata(entry.getKey(), entry.getValue());
            }
        }
        
        return channel;
    }
    
    /**
     * Validates user data before creation
     * @param name User name
     * @return true if valid, false otherwise
     */
    public static boolean validateUserData(String name) {
        return name != null && !name.trim().isEmpty() && name.length() >= 2;
    }
    
    /**
     * Validates channel data before creation
     * @param channelName Channel name
     * @param description Channel description
     * @param category Channel category
     * @return true if valid, false otherwise
     */
    public static boolean validateChannelData(String channelName, String description, String category) {
        return channelName != null && !channelName.trim().isEmpty() &&
               description != null && !description.trim().isEmpty() &&
               category != null && !category.trim().isEmpty();
    }
}