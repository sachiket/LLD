package managers;

import models.Content;
import models.Comment;
import models.Channel;
import models.User;
import enums.ContentVisibility;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.Comparator;

public class ContentManager {
    private static ContentManager instance;
    private Map<String, Content> contents;
    private Map<String, Comment> comments;

    private ContentManager() {
        this.contents = new HashMap<>();
        this.comments = new HashMap<>();
    }

    public static ContentManager getInstance() {
        if (instance == null) {
            instance = new ContentManager();
        }
        return instance;
    }

    /**
     * Registers new content
     * @param content Content to register
     */
    public void registerContent(Content content) {
        if (content == null || content.getId() == null) {
            throw new IllegalArgumentException("Content and content ID cannot be null");
        }
        
        contents.put(content.getId(), content);
        
        // Add content to channel
        Channel channel = UserManager.getInstance().getChannelById(content.getChannelId());
        if (channel != null) {
            channel.addContent(content.getId());
        }
        
        System.out.println("Content uploaded: " + content.getTitle() + " to channel " + content.getChannelId());
    }

    /**
     * Search content by keyword, description, or channel name
     * @param keyword Search keyword (can be null)
     * @param description Search in description (can be null)
     * @param channelName Search by channel name (can be null)
     * @return List of matching content
     */
    public List<Content> searchContent(String keyword, String description, String channelName) {
        return contents.values().stream()
                .filter(content -> content.isPublic()) // Only search public content
                .filter(content -> {
                    if (keyword != null && !keyword.trim().isEmpty()) {
                        return content.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                               content.getDescription().toLowerCase().contains(keyword.toLowerCase());
                    }
                    return true;
                })
                .filter(content -> {
                    if (description != null && !description.trim().isEmpty()) {
                        return content.getDescription().toLowerCase().contains(description.toLowerCase());
                    }
                    return true;
                })
                .filter(content -> {
                    if (channelName != null && !channelName.trim().isEmpty()) {
                        Channel channel = UserManager.getInstance().getChannelById(content.getChannelId());
                        return channel != null && 
                               channel.getChannelName().toLowerCase().contains(channelName.toLowerCase());
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }

    /**
     * Get all content from subscribed channels for user's home page
     * @param userId User ID
     * @return List of content from subscribed channels
     */
    public List<Content> getAllContent(String userId) {
        List<Channel> subscribedChannels = UserManager.getInstance().getSubscriptions(userId);
        List<Content> homePageContent = new ArrayList<>();
        
        for (Channel channel : subscribedChannels) {
            for (String contentId : channel.getContents()) {
                Content content = contents.get(contentId);
                if (content != null && content.isPublic()) {
                    homePageContent.add(content);
                }
            }
        }
        
        // Sort by creation date (newest first)
        homePageContent.sort(Comparator.comparing(Content::getCreatedAt).reversed());
        
        return homePageContent;
    }

    /**
     * Get content by channel
     * @param channelId Channel ID
     * @return List of content in the channel
     */
    public List<Content> getContentByChannel(String channelId) {
        Channel channel = UserManager.getInstance().getChannelById(channelId);
        if (channel == null) {
            return new ArrayList<>();
        }
        
        List<Content> channelContent = new ArrayList<>();
        for (String contentId : channel.getContents()) {
            Content content = contents.get(contentId);
            if (content != null) {
                channelContent.add(content);
            }
        }
        
        // Sort by creation date (newest first)
        channelContent.sort(Comparator.comparing(Content::getCreatedAt).reversed());
        
        return channelContent;
    }

    /**
     * Add comment to content
     * @param contentId Content ID
     * @param userId User ID
     * @param text Comment text
     * @return Created comment or null if failed
     */
    public Comment addComment(String contentId, String userId, String text) {
        Content content = contents.get(contentId);
        User user = UserManager.getInstance().getUserById(userId);
        
        if (content == null || user == null) {
            System.out.println("Content or user not found");
            return null;
        }
        
        if (!content.isPublic() && !content.isUnlisted()) {
            System.out.println("Cannot comment on private content");
            return null;
        }
        
        Comment comment = new Comment("COMMENT_" + System.currentTimeMillis(), contentId, userId, text);
        comments.put(comment.getId(), comment);
        content.addComment(comment.getId());
        
        System.out.println(user.getName() + " commented on " + content.getTitle());
        return comment;
    }

    /**
     * Increment views for content
     * @param contentId Content ID
     */
    public void incrementViews(String contentId) {
        Content content = contents.get(contentId);
        if (content != null) {
            content.incrementViews();
            
            // Update channel stats
            Channel channel = UserManager.getInstance().getChannelById(content.getChannelId());
            if (channel != null) {
                channel.incrementStat("totalViews", 1L);
            }
        }
    }

    /**
     * Add like to content
     * @param contentId Content ID
     */
    public void addLike(String contentId) {
        Content content = contents.get(contentId);
        if (content != null) {
            content.incrementLikes();
            System.out.println("Liked content: " + content.getTitle());
        }
    }

    /**
     * Add dislike to content
     * @param contentId Content ID
     */
    public void addDislike(String contentId) {
        Content content = contents.get(contentId);
        if (content != null) {
            content.incrementDislikes();
            System.out.println("Disliked content: " + content.getTitle());
        }
    }

    /**
     * Get trending content based on views and likes
     * @return List of trending content
     */
    public List<Content> getTrendingContent() {
        return contents.values().stream()
                .filter(content -> content.isPublic())
                .sorted((c1, c2) -> {
                    // Sort by engagement score (views + likes - dislikes)
                    long score1 = c1.getViews() + c1.getLikes() - c1.getDislikes();
                    long score2 = c2.getViews() + c2.getLikes() - c2.getDislikes();
                    return Long.compare(score2, score1); // Descending order
                })
                .limit(10) // Top 10 trending
                .collect(Collectors.toList());
    }

    /**
     * Get content by ID
     * @param contentId Content ID
     * @return Content object or null if not found
     */
    public Content getContentById(String contentId) {
        return contents.get(contentId);
    }

    /**
     * Get comments for content
     * @param contentId Content ID
     * @return List of comments
     */
    public List<Comment> getComments(String contentId) {
        Content content = contents.get(contentId);
        if (content == null) {
            return new ArrayList<>();
        }
        
        List<Comment> contentComments = new ArrayList<>();
        for (String commentId : content.getComments()) {
            Comment comment = comments.get(commentId);
            if (comment != null) {
                contentComments.add(comment);
            }
        }
        
        // Sort by timestamp (newest first)
        contentComments.sort(Comparator.comparing(Comment::getTimestamp).reversed());
        
        return contentComments;
    }

    /**
     * Get all content (for admin purposes)
     * @return List of all content
     */
    public List<Content> getAllContent() {
        return new ArrayList<>(contents.values());
    }

    /**
     * Get content statistics
     * @param contentId Content ID
     * @return Content statistics as formatted string
     */
    public String getContentStats(String contentId) {
        Content content = contents.get(contentId);
        if (content == null) {
            return "Content not found";
        }
        
        return String.format("Content: %s\nViews: %d\nLikes: %d\nDislikes: %d\nComments: %d\nLike Ratio: %.2f",
                content.getTitle(),
                content.getViews(),
                content.getLikes(),
                content.getDislikes(),
                content.getCommentCount(),
                content.getLikeRatio());
    }
}