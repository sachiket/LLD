package services;

import models.User;
import models.Channel;
import models.Content;
import models.Comment;
import enums.ContentType;
import enums.ContentVisibility;
import enums.UserRole;
import managers.UserManager;
import managers.ContentManager;
import factories.UserFactory;
import factories.ContentFactory;
import strategies.SearchStrategy;
import strategies.BasicSearchStrategy;
import java.util.List;
import java.util.Map;

public class VideoStreamingServiceImpl implements VideoStreamingService {
    
    private final UserManager userManager;
    private final ContentManager contentManager;
    private final SearchStrategy searchStrategy;
    
    public VideoStreamingServiceImpl() {
        this.userManager = UserManager.getInstance();
        this.contentManager = ContentManager.getInstance();
        this.searchStrategy = new BasicSearchStrategy();
    }
    
    // User Management
    @Override
    public void registerUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        userManager.registerUser(user);
    }
    
    @Override
    public User getUserById(String userId) {
        return userManager.getUserById(userId);
    }
    
    @Override
    public List<User> getAllUsers() {
        return userManager.getAllUsers();
    }
    
    // Channel Management
    @Override
    public Channel createChannel(String userId, String channelName, String description, String category) {
        if (!UserFactory.validateChannelData(channelName, description, category)) {
            throw new IllegalArgumentException("Invalid channel data");
        }
        
        User user = userManager.getUserById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        
        // Promote user to CREATOR if they're creating their first channel
        if (user.getRole() == UserRole.VIEWER) {
            user.setRole(UserRole.CREATOR);
        }
        
        Channel channel = UserFactory.createChannel(userId, channelName, description, category);
        userManager.registerChannel(channel);
        return channel;
    }
    
    @Override
    public Channel createChannel(String userId, String channelName, String description, String category, Map<String, String> metadata) {
        Channel channel = createChannel(userId, channelName, description, category);
        if (metadata != null) {
            for (Map.Entry<String, String> entry : metadata.entrySet()) {
                channel.setMetadata(entry.getKey(), entry.getValue());
            }
        }
        return channel;
    }
    
    @Override
    public Channel getChannelById(String channelId) {
        return userManager.getChannelById(channelId);
    }
    
    @Override
    public List<Channel> getAllChannels() {
        return userManager.getAllChannels();
    }
    
    @Override
    public List<Channel> getUserChannels(String userId) {
        return userManager.getChannels(userId);
    }
    
    // Subscription Management
    @Override
    public boolean subscribe(String userId, String channelId) {
        return userManager.subscribe(userId, channelId);
    }
    
    @Override
    public boolean unsubscribe(String userId, String channelId) {
        return userManager.unSubscribe(userId, channelId);
    }
    
    @Override
    public List<Channel> getUserSubscriptions(String userId) {
        return userManager.getSubscriptions(userId);
    }
    
    @Override
    public List<User> getChannelFollowers(String channelId) {
        return userManager.getFollowers(channelId);
    }
    
    // Content Management
    @Override
    public Content uploadContent(String channelId, String userId, String title, String description, ContentType type, String url) {
        if (!ContentFactory.validateContentData(title, description, type, url)) {
            throw new IllegalArgumentException("Invalid content data");
        }
        
        Channel channel = userManager.getChannelById(channelId);
        if (channel == null) {
            throw new IllegalArgumentException("Channel not found");
        }
        
        if (!channel.getOwnerUserId().equals(userId)) {
            throw new IllegalArgumentException("User is not the owner of this channel");
        }
        
        Content content = ContentFactory.createContent(channelId, userId, title, description, type, url);
        contentManager.registerContent(content);
        return content;
    }
    
    @Override
    public Content uploadContent(String channelId, String userId, String title, String description, ContentType type, String url, ContentVisibility visibility) {
        Content content = uploadContent(channelId, userId, title, description, type, url);
        content.setVisibility(visibility);
        return content;
    }
    
    @Override
    public Content getContentById(String contentId) {
        return contentManager.getContentById(contentId);
    }
    
    @Override
    public List<Content> getChannelContent(String channelId) {
        return contentManager.getContentByChannel(channelId);
    }
    
    @Override
    public List<Content> getAllContent() {
        return contentManager.getAllContent();
    }
    
    // Content Interaction
    @Override
    public void viewContent(String contentId) {
        contentManager.incrementViews(contentId);
    }
    
    @Override
    public void likeContent(String contentId) {
        contentManager.addLike(contentId);
    }
    
    @Override
    public void dislikeContent(String contentId) {
        contentManager.addDislike(contentId);
    }
    
    @Override
    public Comment addComment(String contentId, String userId, String commentText) {
        if (!ContentFactory.validateCommentData(commentText)) {
            throw new IllegalArgumentException("Invalid comment data");
        }
        return contentManager.addComment(contentId, userId, commentText);
    }
    
    @Override
    public List<Comment> getContentComments(String contentId) {
        return contentManager.getComments(contentId);
    }
    
    // Search and Discovery
    @Override
    public List<Content> searchContent(String keyword) {
        List<Content> allContent = contentManager.getAllContent();
        return searchStrategy.search(keyword, allContent);
    }
    
    @Override
    public List<Content> searchContent(String keyword, String description, String channelName) {
        return contentManager.searchContent(keyword, description, channelName);
    }
    
    @Override
    public List<Content> getHomePage(String userId) {
        List<Content> subscriptionContent = contentManager.getAllContent(userId);
        
        // If user has no subscriptions, show trending content
        if (subscriptionContent.isEmpty()) {
            return getTrendingContent();
        }
        
        return subscriptionContent;
    }
    
    @Override
    public List<Content> getTrendingContent() {
        return contentManager.getTrendingContent();
    }
    
    // Analytics
    @Override
    public String getChannelStats(String channelId) {
        return userManager.getChannelStats(channelId);
    }
    
    @Override
    public String getContentStats(String contentId) {
        return contentManager.getContentStats(contentId);
    }
}