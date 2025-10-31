package managers;

import models.User;
import models.Channel;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class UserManager {
    private static UserManager instance;
    private Map<String, User> users;
    private Map<String, Channel> channels;

    private UserManager() {
        this.users = new HashMap<>();
        this.channels = new HashMap<>();
    }

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    /**
     * Gets user by ID
     * @param userId User ID
     * @return User object or null if not found
     */
    public User getUserById(String userId) {
        return users.get(userId);
    }

    /**
     * Registers a new user
     * @param user User to register
     */
    public void registerUser(User user) {
        if (user == null || user.getId() == null) {
            throw new IllegalArgumentException("User and user ID cannot be null");
        }
        users.put(user.getId(), user);
        System.out.println("User registered: " + user.getName() + " (" + user.getRole() + ")");
    }

    /**
     * Registers a new channel
     * @param channel Channel to register
     */
    public void registerChannel(Channel channel) {
        if (channel == null || channel.getId() == null) {
            throw new IllegalArgumentException("Channel and channel ID cannot be null");
        }
        
        // Verify owner exists
        User owner = users.get(channel.getOwnerUserId());
        if (owner == null) {
            throw new IllegalArgumentException("Channel owner not found");
        }
        
        channels.put(channel.getId(), channel);
        owner.addCreatedChannel(channel.getId());
        System.out.println("Channel registered: " + channel.getChannelName() + " by " + owner.getName());
    }

    /**
     * Subscribe user to a channel
     * @param userId User ID
     * @param channelId Channel ID
     * @return true if subscription successful, false otherwise
     */
    public boolean subscribe(String userId, String channelId) {
        User user = users.get(userId);
        Channel channel = channels.get(channelId);
        
        if (user == null || channel == null) {
            System.out.println("User or channel not found");
            return false;
        }
        
        if (user.getSubscriptions().contains(channelId)) {
            System.out.println("User already subscribed to this channel");
            return false;
        }
        
        user.addSubscription(channelId);
        channel.addSubscriber(userId);
        System.out.println(user.getName() + " subscribed to " + channel.getChannelName());
        return true;
    }

    /**
     * Unsubscribe user from a channel
     * @param userId User ID
     * @param channelId Channel ID
     * @return true if unsubscription successful, false otherwise
     */
    public boolean unSubscribe(String userId, String channelId) {
        User user = users.get(userId);
        Channel channel = channels.get(channelId);
        
        if (user == null || channel == null) {
            System.out.println("User or channel not found");
            return false;
        }
        
        if (!user.getSubscriptions().contains(channelId)) {
            System.out.println("User not subscribed to this channel");
            return false;
        }
        
        user.removeSubscription(channelId);
        channel.removeSubscriber(userId);
        System.out.println(user.getName() + " unsubscribed from " + channel.getChannelName());
        return true;
    }

    /**
     * Get user's subscriptions
     * @param userId User ID
     * @return List of subscribed channels
     */
    public List<Channel> getSubscriptions(String userId) {
        User user = users.get(userId);
        if (user == null) {
            return new ArrayList<>();
        }
        
        List<Channel> subscribedChannels = new ArrayList<>();
        for (String channelId : user.getSubscriptions()) {
            Channel channel = channels.get(channelId);
            if (channel != null) {
                subscribedChannels.add(channel);
            }
        }
        return subscribedChannels;
    }

    /**
     * Get channel followers
     * @param channelId Channel ID
     * @return List of users following the channel
     */
    public List<User> getFollowers(String channelId) {
        Channel channel = channels.get(channelId);
        if (channel == null) {
            return new ArrayList<>();
        }
        
        List<User> followers = new ArrayList<>();
        for (String userId : channel.getSubscribers()) {
            User user = users.get(userId);
            if (user != null) {
                followers.add(user);
            }
        }
        return followers;
    }

    /**
     * Get channels owned by user
     * @param userId User ID
     * @return List of channels owned by the user
     */
    public List<Channel> getChannels(String userId) {
        User user = users.get(userId);
        if (user == null) {
            return new ArrayList<>();
        }
        
        List<Channel> ownedChannels = new ArrayList<>();
        for (String channelId : user.getCreatedChannels()) {
            Channel channel = channels.get(channelId);
            if (channel != null) {
                ownedChannels.add(channel);
            }
        }
        return ownedChannels;
    }

    /**
     * Get channel by ID
     * @param channelId Channel ID
     * @return Channel object or null if not found
     */
    public Channel getChannelById(String channelId) {
        return channels.get(channelId);
    }

    /**
     * Get all channels
     * @return List of all channels
     */
    public List<Channel> getAllChannels() {
        return new ArrayList<>(channels.values());
    }

    /**
     * Get all users
     * @return List of all users
     */
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    /**
     * Get channel statistics
     * @param channelId Channel ID
     * @return Channel statistics as a formatted string
     */
    public String getChannelStats(String channelId) {
        Channel channel = channels.get(channelId);
        if (channel == null) {
            return "Channel not found";
        }
        
        return String.format("Channel: %s\nSubscribers: %d\nContent: %d\nTotal Views: %d",
                channel.getChannelName(),
                channel.getSubscriberCount(),
                channel.getContentCount(),
                channel.getStat("totalViews"));
    }
}