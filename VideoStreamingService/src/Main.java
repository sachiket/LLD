import models.*;
import enums.*;
import services.*;
import factories.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Video Streaming Service Demo ===\n");
        
        // Initialize the video streaming service
        VideoStreamingService streamingService = new VideoStreamingServiceImpl();
        
        // Create users with metadata
        Map<String, String> creatorMetadata = new HashMap<>();
        creatorMetadata.put("bio", "Tech content creator");
        creatorMetadata.put("location", "San Francisco");
        
        Map<String, String> viewerMetadata = new HashMap<>();
        viewerMetadata.put("preferences", "tech,gaming,music");
        
        User creator1 = UserFactory.createUser("Alice Johnson", UserRole.VIEWER, creatorMetadata);
        User creator2 = UserFactory.createUser("Bob Smith", UserRole.VIEWER, null);
        User viewer1 = UserFactory.createUser("Charlie Brown", UserRole.VIEWER, viewerMetadata);
        User viewer2 = UserFactory.createUser("Diana Prince", UserRole.VIEWER, null);
        
        // Register users
        streamingService.registerUser(creator1);
        streamingService.registerUser(creator2);
        streamingService.registerUser(viewer1);
        streamingService.registerUser(viewer2);
        
        System.out.println("Users registered successfully!\n");
        
        // Create channels
        System.out.println("--- Creating Channels ---");
        Channel techChannel = streamingService.createChannel(creator1.getId(), "TechTalks", 
                "Latest technology tutorials and reviews", "Technology");
        
        Channel musicChannel = streamingService.createChannel(creator2.getId(), "MusicVibes", 
                "Music covers and original compositions", "Music");
        
        System.out.println("Channels created successfully!\n");
        
        // Upload content
        System.out.println("--- Uploading Content ---");
        Content video1 = streamingService.uploadContent(techChannel.getId(), creator1.getId(),
                "Java Design Patterns Tutorial", "Learn about Singleton and Factory patterns", 
                ContentType.VIDEO, "https://cdn.example.com/java-patterns.mp4");
        
        Content video2 = streamingService.uploadContent(techChannel.getId(), creator1.getId(),
                "System Design Basics", "Introduction to scalable system design", 
                ContentType.VIDEO, "https://cdn.example.com/system-design.mp4");
        
        Content audio1 = streamingService.uploadContent(musicChannel.getId(), creator2.getId(),
                "Acoustic Guitar Cover", "Beautiful acoustic rendition of popular songs", 
                ContentType.AUDIO, "https://cdn.example.com/guitar-cover.mp3");
        
        Content privateVideo = streamingService.uploadContent(techChannel.getId(), creator1.getId(),
                "Advanced Algorithms", "Deep dive into complex algorithms", 
                ContentType.VIDEO, "https://cdn.example.com/algorithms.mp4", ContentVisibility.PRIVATE);
        
        System.out.println("Content uploaded successfully!\n");
        
        // Users subscribe to channels
        System.out.println("--- Managing Subscriptions ---");
        streamingService.subscribe(viewer1.getId(), techChannel.getId());
        streamingService.subscribe(viewer1.getId(), musicChannel.getId());
        streamingService.subscribe(viewer2.getId(), techChannel.getId());
        
        System.out.println("Subscriptions completed!\n");
        
        // Simulate content interactions
        System.out.println("--- Content Interactions ---");
        
        // View content
        streamingService.viewContent(video1.getId());
        streamingService.viewContent(video1.getId());
        streamingService.viewContent(video2.getId());
        streamingService.viewContent(audio1.getId());
        
        // Like/dislike content
        streamingService.likeContent(video1.getId());
        streamingService.likeContent(video1.getId());
        streamingService.likeContent(video2.getId());
        streamingService.dislikeContent(audio1.getId());
        
        // Add comments
        streamingService.addComment(video1.getId(), viewer1.getId(), "Great tutorial! Very helpful.");
        streamingService.addComment(video1.getId(), viewer2.getId(), "Thanks for the clear explanation!");
        streamingService.addComment(video2.getId(), viewer1.getId(), "Looking forward to more system design content.");
        
        System.out.println("Content interactions completed!\n");
        
        // Display user's home page
        System.out.println("--- " + viewer1.getName() + "'s Home Page ---");
        List<Content> homePage = streamingService.getHomePage(viewer1.getId());
        for (Content content : homePage) {
            System.out.println("📺 " + content.getTitle() + " (" + content.getType() + ")");
            System.out.println("   Views: " + content.getViews() + " | Likes: " + content.getLikes() + 
                             " | Comments: " + content.getCommentCount());
        }
        System.out.println();
        
        // Search content
        System.out.println("--- Search Results for 'design' ---");
        List<Content> searchResults = streamingService.searchContent("design");
        for (Content content : searchResults) {
            System.out.println("🔍 " + content.getTitle() + " - " + content.getDescription());
        }
        System.out.println();
        
        // Show trending content
        System.out.println("--- Trending Content ---");
        List<Content> trending = streamingService.getTrendingContent();
        for (Content content : trending) {
            System.out.println("🔥 " + content.getTitle() + " (Views: " + content.getViews() + 
                             ", Likes: " + content.getLikes() + ")");
        }
        System.out.println();
        
        // Display channel statistics
        System.out.println("--- Channel Statistics ---");
        System.out.println(streamingService.getChannelStats(techChannel.getId()));
        System.out.println();
        System.out.println(streamingService.getChannelStats(musicChannel.getId()));
        System.out.println();
        
        // Display content statistics
        System.out.println("--- Content Statistics ---");
        System.out.println(streamingService.getContentStats(video1.getId()));
        System.out.println();
        
        // Show comments for popular video
        System.out.println("--- Comments on '" + video1.getTitle() + "' ---");
        List<Comment> comments = streamingService.getContentComments(video1.getId());
        for (Comment comment : comments) {
            User commenter = streamingService.getUserById(comment.getUserId());
            System.out.println("💬 " + commenter.getName() + ": " + comment.getText());
            System.out.println("   👍 " + comment.getLikes() + " | 👎 " + comment.getDislikes());
        }
        System.out.println();
        
        // Show user subscriptions
        System.out.println("--- " + viewer1.getName() + "'s Subscriptions ---");
        List<Channel> subscriptions = streamingService.getUserSubscriptions(viewer1.getId());
        for (Channel channel : subscriptions) {
            System.out.println("📺 " + channel.getChannelName() + " (" + channel.getCategory() + ")");
            System.out.println("   Subscribers: " + channel.getSubscriberCount() + 
                             " | Content: " + channel.getContentCount());
        }
        System.out.println();
        
        // Advanced search
        System.out.println("--- Advanced Search: Technology Channels ---");
        List<Content> techContent = streamingService.searchContent(null, null, "TechTalks");
        for (Content content : techContent) {
            System.out.println("🔍 " + content.getTitle() + " from " + 
                             streamingService.getChannelById(content.getChannelId()).getChannelName());
        }
        System.out.println();
        
        // Show all users and their roles
        System.out.println("--- All Users ---");
        List<User> allUsers = streamingService.getAllUsers();
        for (User user : allUsers) {
            System.out.println("👤 " + user.getName() + " (" + user.getRole() + ")");
            System.out.println("   Subscriptions: " + user.getSubscriptions().size() + 
                             " | Channels: " + user.getCreatedChannels().size());
        }
        
        System.out.println("\n=== Video Streaming Service Demo Completed Successfully! ===");
    }
}