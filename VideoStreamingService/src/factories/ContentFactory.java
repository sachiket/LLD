package factories;

import models.Content;
import models.Comment;
import enums.ContentType;
import enums.ContentVisibility;
import java.util.Map;
import java.util.UUID;

public class ContentFactory {
    
    /**
     * Creates new content with the given parameters
     * @param channelId Channel ID where content belongs
     * @param uploadedByUserId User ID who uploaded the content
     * @param title Content title
     * @param description Content description
     * @param type Content type (VIDEO, AUDIO, IMAGE)
     * @param url Content URL pointing to CDN/storage
     * @return Created Content object
     */
    public static Content createContent(String channelId, String uploadedByUserId, String title, 
                                      String description, ContentType type, String url) {
        String contentId = "CONTENT_" + UUID.randomUUID().toString().substring(0, 8);
        Content content = new Content(contentId, channelId, uploadedByUserId, title, description, type, url);
        
        // Initialize default metadata
        initializeDefaults(content);
        
        return content;
    }
    
    /**
     * Creates new content with additional metadata
     * @param channelId Channel ID where content belongs
     * @param uploadedByUserId User ID who uploaded the content
     * @param title Content title
     * @param description Content description
     * @param type Content type (VIDEO, AUDIO, IMAGE)
     * @param url Content URL pointing to CDN/storage
     * @param metadata Additional content metadata
     * @return Created Content object
     */
    public static Content createContent(String channelId, String uploadedByUserId, String title, 
                                      String description, ContentType type, String url, 
                                      Map<String, String> metadata) {
        Content content = createContent(channelId, uploadedByUserId, title, description, type, url);
        
        // Set additional metadata if provided
        if (metadata != null) {
            for (Map.Entry<String, String> entry : metadata.entrySet()) {
                content.setMetadata(entry.getKey(), entry.getValue());
            }
        }
        
        return content;
    }
    
    /**
     * Creates new content with specific visibility
     * @param channelId Channel ID where content belongs
     * @param uploadedByUserId User ID who uploaded the content
     * @param title Content title
     * @param description Content description
     * @param type Content type (VIDEO, AUDIO, IMAGE)
     * @param url Content URL pointing to CDN/storage
     * @param visibility Content visibility (PUBLIC, PRIVATE, UNLISTED)
     * @return Created Content object
     */
    public static Content createContent(String channelId, String uploadedByUserId, String title, 
                                      String description, ContentType type, String url, 
                                      ContentVisibility visibility) {
        Content content = createContent(channelId, uploadedByUserId, title, description, type, url);
        content.setVisibility(visibility);
        
        return content;
    }
    
    /**
     * Creates a new comment for content
     * @param contentId Content ID where comment belongs
     * @param userId User ID who posted the comment
     * @param text Comment text
     * @return Created Comment object
     */
    public static Comment createComment(String contentId, String userId, String text) {
        String commentId = "COMMENT_" + UUID.randomUUID().toString().substring(0, 8);
        Comment comment = new Comment(commentId, contentId, userId, text);
        
        return comment;
    }
    
    /**
     * Initializes default values for content
     * @param content Content object to initialize
     */
    public static void initializeDefaults(Content content) {
        // Set default metadata
        content.setMetadata("createdBy", "ContentFactory");
        content.setMetadata("version", "1.0");
        content.setMetadata("processed", "false");
        
        // Ensure initial values are set to 0
        content.setViews(0);
        content.setLikes(0);
        content.setDislikes(0);
        
        // Set default visibility to PUBLIC
        if (content.getVisibility() == null) {
            content.setVisibility(ContentVisibility.PUBLIC);
        }
    }
    
    /**
     * Validates content data before creation
     * @param title Content title
     * @param description Content description
     * @param type Content type
     * @param url Content URL
     * @return true if valid, false otherwise
     */
    public static boolean validateContentData(String title, String description, ContentType type, String url) {
        return title != null && !title.trim().isEmpty() &&
               description != null && !description.trim().isEmpty() &&
               type != null &&
               url != null && !url.trim().isEmpty();
    }
    
    /**
     * Validates comment data before creation
     * @param text Comment text
     * @return true if valid, false otherwise
     */
    public static boolean validateCommentData(String text) {
        return text != null && !text.trim().isEmpty() && text.length() <= 1000;
    }
    
    /**
     * Generates content URL based on type and ID
     * @param contentId Content ID
     * @param type Content type
     * @return Generated URL
     */
    public static String generateContentUrl(String contentId, ContentType type) {
        String baseUrl = "https://cdn.videostreaming.com/";
        String extension;
        
        switch (type) {
            case VIDEO:
                extension = ".mp4";
                break;
            case AUDIO:
                extension = ".mp3";
                break;
            case IMAGE:
                extension = ".jpg";
                break;
            default:
                extension = ".bin";
        }
        
        return baseUrl + type.toString().toLowerCase() + "/" + contentId + extension;
    }
}