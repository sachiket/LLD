package services;

import models.User;
import models.Channel;
import models.Content;
import models.Comment;
import enums.ContentType;
import enums.ContentVisibility;
import java.util.List;
import java.util.Map;

public interface VideoStreamingService {
    
    // User Management
    void registerUser(User user);
    User getUserById(String userId);
    List<User> getAllUsers();
    
    // Channel Management
    Channel createChannel(String userId, String channelName, String description, String category);
    Channel createChannel(String userId, String channelName, String description, String category, Map<String, String> metadata);
    Channel getChannelById(String channelId);
    List<Channel> getAllChannels();
    List<Channel> getUserChannels(String userId);
    
    // Subscription Management
    boolean subscribe(String userId, String channelId);
    boolean unsubscribe(String userId, String channelId);
    List<Channel> getUserSubscriptions(String userId);
    List<User> getChannelFollowers(String channelId);
    
    // Content Management
    Content uploadContent(String channelId, String userId, String title, String description, ContentType type, String url);
    Content uploadContent(String channelId, String userId, String title, String description, ContentType type, String url, ContentVisibility visibility);
    Content getContentById(String contentId);
    List<Content> getChannelContent(String channelId);
    List<Content> getAllContent();
    
    // Content Interaction
    void viewContent(String contentId);
    void likeContent(String contentId);
    void dislikeContent(String contentId);
    Comment addComment(String contentId, String userId, String commentText);
    List<Comment> getContentComments(String contentId);
    
    // Search and Discovery
    List<Content> searchContent(String keyword);
    List<Content> searchContent(String keyword, String description, String channelName);
    List<Content> getHomePage(String userId);
    List<Content> getTrendingContent();
    
    // Analytics
    String getChannelStats(String channelId);
    String getContentStats(String contentId);
}