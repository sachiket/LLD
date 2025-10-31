# 🎯 SIMPLE START GUIDE - Video Streaming Service Interview

## 🚀 **DON'T PANIC! Start Here:**

### **Your Opening Line (30 seconds):**
*"I'll design a YouTube-like video streaming system step by step. Let me start with the basic entities and build up gradually."*

---

## 📝 **STEP 1: Draw Basic Entities (2 minutes)**

**On whiteboard/screen, draw:**
```
[User] ----creates----> [Channel] ----contains----> [Content]
   |                        |                          |
   |                        |                          |
[Viewer]              [Subscribers] ----watches----> [Videos/Audio/Images]
   |                        |                          |
   |                        |                          |
[Creator] ----uploads----> [Content] ----has----> [Comments]
```

**Say:** *"These are my core entities. Let me code them..."*

### **Code Step 1:**
```java
// Start with just this!
class User {
    String id;
    String name;
    String role; // "VIEWER", "CREATOR", "ADMIN"
    List<String> subscriptions = new ArrayList<>(); // Channel IDs
    List<String> createdChannels = new ArrayList<>(); // Channel IDs
    Map<String, String> metadata = new HashMap<>(); // bio, preferences, etc.
    boolean isActive = true;
}

class Channel {
    String id;
    String ownerUserId;
    String channelName;
    String description;
    String category;
    List<String> tags = new ArrayList<>();
    List<String> subscribers = new ArrayList<>(); // User IDs
    List<String> contents = new ArrayList<>(); // Content IDs
    Map<String, Long> stats = new HashMap<>(); // subscriberCount, totalViews
    boolean isActive = true;
}

class Content {
    String id;
    String channelId;
    String uploadedByUserId;
    String title;
    String description;
    String type; // "VIDEO", "AUDIO", "IMAGE"
    String url; // CDN URL
    long views = 0;
    long likes = 0;
    long dislikes = 0;
    String visibility = "PUBLIC"; // "PUBLIC", "PRIVATE", "UNLISTED"
    List<String> comments = new ArrayList<>(); // Comment IDs
    LocalDateTime createdAt;
}

class Comment {
    String id;
    String contentId;
    String userId;
    String text;
    long likes = 0;
    long dislikes = 0;
    LocalDateTime timestamp;
}
```

**Say:** *"This covers the basics. Now let me add core functionality..."*

---

## 📝 **STEP 2: Add Core Operations (3 minutes)**

```java
class VideoStreamingService {
    Map<String, User> users = new HashMap<>();
    Map<String, Channel> channels = new HashMap<>();
    Map<String, Content> contents = new HashMap<>();
    Map<String, Comment> comments = new HashMap<>();
    
    // User Management
    void registerUser(User user) {
        users.put(user.id, user);
        System.out.println("User registered: " + user.name + " (" + user.role + ")");
    }
    
    // Channel Management
    Channel createChannel(String userId, String channelName, String description, String category) {
        User user = users.get(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        
        String channelId = "CHANNEL_" + System.currentTimeMillis();
        Channel channel = new Channel();
        channel.id = channelId;
        channel.ownerUserId = userId;
        channel.channelName = channelName;
        channel.description = description;
        channel.category = category;
        channel.createdAt = LocalDateTime.now();
        
        channels.put(channelId, channel);
        user.createdChannels.add(channelId);
        
        // Promote user to CREATOR if they're a VIEWER
        if ("VIEWER".equals(user.role)) {
            user.role = "CREATOR";
        }
        
        System.out.println("Channel created: " + channelName + " by " + user.name);
        return channel;
    }
    
    // Subscription Management
    boolean subscribe(String userId, String channelId) {
        User user = users.get(userId);
        Channel channel = channels.get(channelId);
        
        if (user == null || channel == null) {
            return false;
        }
        
        if (!user.subscriptions.contains(channelId)) {
            user.subscriptions.add(channelId);
            channel.subscribers.add(userId);
            channel.stats.put("subscriberCount", (long) channel.subscribers.size());
            System.out.println(user.name + " subscribed to " + channel.channelName);
            return true;
        }
        return false;
    }
}
```

**Say:** *"Great! This handles users, channels, and subscriptions. Now let me add content management..."*

---

## 📝 **STEP 3: Add Content Upload & Management (3 minutes)**

```java
class VideoStreamingService {
    // ... previous code ...
    
    // Content Management
    Content uploadContent(String channelId, String userId, String title, String description, String type, String url) {
        Channel channel = channels.get(channelId);
        if (channel == null || !channel.ownerUserId.equals(userId)) {
            throw new IllegalArgumentException("Channel not found or user not authorized");
        }
        
        String contentId = "CONTENT_" + System.currentTimeMillis();
        Content content = new Content();
        content.id = contentId;
        content.channelId = channelId;
        content.uploadedByUserId = userId;
        content.title = title;
        content.description = description;
        content.type = type;
        content.url = url;
        content.createdAt = LocalDateTime.now();
        
        contents.put(contentId, content);
        channel.contents.add(contentId);
        channel.stats.put("totalContent", (long) channel.contents.size());
        
        System.out.println("Content uploaded: " + title + " to channel " + channel.channelName);
        return content;
    }
    
    // Content Interaction
    void viewContent(String contentId) {
        Content content = contents.get(contentId);
        if (content != null && "PUBLIC".equals(content.visibility)) {
            content.views++;
            
            // Update channel stats
            Channel channel = channels.get(content.channelId);
            if (channel != null) {
                long totalViews = channel.stats.getOrDefault("totalViews", 0L) + 1;
                channel.stats.put("totalViews", totalViews);
            }
        }
    }
    
    void likeContent(String contentId) {
        Content content = contents.get(contentId);
        if (content != null) {
            content.likes++;
            System.out.println("Liked content: " + content.title);
        }
    }
    
    Comment addComment(String contentId, String userId, String text) {
        Content content = contents.get(contentId);
        User user = users.get(userId);
        
        if (content == null || user == null) {
            return null;
        }
        
        String commentId = "COMMENT_" + System.currentTimeMillis();
        Comment comment = new Comment();
        comment.id = commentId;
        comment.contentId = contentId;
        comment.userId = userId;
        comment.text = text;
        comment.timestamp = LocalDateTime.now();
        
        comments.put(commentId, comment);
        content.comments.add(commentId);
        
        System.out.println(user.name + " commented on " + content.title);
        return comment;
    }
}
```

**Say:** *"Perfect! Now it handles content upload and basic interactions. Let me add search and discovery..."*

---

## 📝 **STEP 4: Add Search & Discovery (3 minutes)**

```java
class VideoStreamingService {
    // ... previous code ...
    
    // Search & Discovery
    List<Content> searchContent(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllPublicContent();
        }
        
        String searchTerm = keyword.toLowerCase();
        return contents.values().stream()
                .filter(content -> "PUBLIC".equals(content.visibility))
                .filter(content -> 
                    content.title.toLowerCase().contains(searchTerm) ||
                    content.description.toLowerCase().contains(searchTerm)
                )
                .sorted((c1, c2) -> {
                    // Prioritize by engagement (views + likes)
                    long score1 = c1.views + c1.likes;
                    long score2 = c2.views + c2.likes;
                    return Long.compare(score2, score1); // Descending
                })
                .collect(Collectors.toList());
    }
    
    List<Content> getHomePage(String userId) {
        User user = users.get(userId);
        if (user == null) {
            return getTrendingContent();
        }
        
        List<Content> homeContent = new ArrayList<>();
        
        // Get content from subscribed channels
        for (String channelId : user.subscriptions) {
            Channel channel = channels.get(channelId);
            if (channel != null) {
                for (String contentId : channel.contents) {
                    Content content = contents.get(contentId);
                    if (content != null && "PUBLIC".equals(content.visibility)) {
                        homeContent.add(content);
                    }
                }
            }
        }
        
        // Sort by creation date (newest first)
        homeContent.sort((c1, c2) -> c2.createdAt.compareTo(c1.createdAt));
        
        // If no subscriptions, show trending
        if (homeContent.isEmpty()) {
            return getTrendingContent();
        }
        
        return homeContent;
    }
    
    List<Content> getTrendingContent() {
        return contents.values().stream()
                .filter(content -> "PUBLIC".equals(content.visibility))
                .sorted((c1, c2) -> {
                    // Trending score: views + likes - dislikes
                    long score1 = c1.views + c1.likes - c1.dislikes;
                    long score2 = c2.views + c2.likes - c2.dislikes;
                    return Long.compare(score2, score1);
                })
                .limit(10)
                .collect(Collectors.toList());
    }
    
    List<Content> getAllPublicContent() {
        return contents.values().stream()
                .filter(content -> "PUBLIC".equals(content.visibility))
                .sorted((c1, c2) -> c2.createdAt.compareTo(c1.createdAt))
                .collect(Collectors.toList());
    }
}
```

**Say:** *"Excellent! Now it has search, trending, and personalized home page. Let me add enums to make it more professional..."*

---

## 📝 **STEP 5: Add Enums & Clean Up (2 minutes)**

```java
enum UserRole { VIEWER, CREATOR, ADMIN }
enum ContentType { VIDEO, AUDIO, IMAGE }
enum ContentVisibility { PUBLIC, PRIVATE, UNLISTED }

class User {
    String id;
    String name;
    UserRole role;
    List<String> subscriptions = new ArrayList<>();
    List<String> createdChannels = new ArrayList<>();
    Map<String, String> metadata = new HashMap<>();
    boolean isActive = true;
    
    User(String id, String name, UserRole role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }
}

class Content {
    String id;
    String channelId;
    String uploadedByUserId;
    String title;
    String description;
    ContentType type;
    String url;
    long views = 0;
    long likes = 0;
    long dislikes = 0;
    ContentVisibility visibility = ContentVisibility.PUBLIC;
    List<String> comments = new ArrayList<>();
    LocalDateTime createdAt;
    
    Content(String id, String channelId, String uploadedByUserId, String title, String description, ContentType type, String url) {
        this.id = id;
        this.channelId = channelId;
        this.uploadedByUserId = uploadedByUserId;
        this.title = title;
        this.description = description;
        this.type = type;
        this.url = url;
        this.createdAt = LocalDateTime.now();
    }
}
```

**Say:** *"Much cleaner with enums. Now let me add design patterns to make it more professional..."*

---

## 📝 **STEP 6: Add Design Patterns (5 minutes)**

### **Factory Pattern for Object Creation:**
```java
class UserFactory {
    public static User createUser(String name, UserRole role, Map<String, String> metadata) {
        String userId = "USER_" + UUID.randomUUID().toString().substring(0, 8);
        User user = new User(userId, name, role);
        
        if (metadata != null) {
            user.metadata.putAll(metadata);
        }
        
        return user;
    }
    
    public static Channel createChannel(String userId, String channelName, String description, String category) {
        String channelId = "CHANNEL_" + UUID.randomUUID().toString().substring(0, 8);
        Channel channel = new Channel();
        channel.id = channelId;
        channel.ownerUserId = userId;
        channel.channelName = channelName;
        channel.description = description;
        channel.category = category;
        
        // Initialize default stats
        channel.stats.put("subscriberCount", 0L);
        channel.stats.put("totalViews", 0L);
        channel.stats.put("totalContent", 0L);
        
        return channel;
    }
}

class ContentFactory {
    public static Content createContent(String channelId, String userId, String title, String description, ContentType type, String url) {
        String contentId = "CONTENT_" + UUID.randomUUID().toString().substring(0, 8);
        Content content = new Content(contentId, channelId, userId, title, description, type, url);
        
        // Set default metadata
        content.metadata.put("createdBy", "ContentFactory");
        content.metadata.put("processed", "false");
        
        return content;
    }
}
```

### **Strategy Pattern for Search:**
```java
interface SearchStrategy {
    List<Content> search(String keyword, List<Content> allContent);
}

class BasicSearchStrategy implements SearchStrategy {
    public List<Content> search(String keyword, List<Content> allContent) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return allContent.stream()
                    .filter(content -> content.visibility == ContentVisibility.PUBLIC)
                    .sorted((c1, c2) -> c2.createdAt.compareTo(c1.createdAt))
                    .collect(Collectors.toList());
        }
        
        String searchTerm = keyword.toLowerCase();
        return allContent.stream()
                .filter(content -> content.visibility == ContentVisibility.PUBLIC)
                .filter(content -> 
                    content.title.toLowerCase().contains(searchTerm) ||
                    content.description.toLowerCase().contains(searchTerm)
                )
                .sorted((c1, c2) -> {
                    long score1 = c1.views + c1.likes;
                    long score2 = c2.views + c2.likes;
                    return Long.compare(score2, score1);
                })
                .collect(Collectors.toList());
    }
}
```

### **Singleton Pattern for Managers:**
```java
class UserManager {
    private static UserManager instance;
    private Map<String, User> users = new HashMap<>();
    private Map<String, Channel> channels = new HashMap<>();
    
    private UserManager() {}
    
    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }
    
    public void registerUser(User user) {
        users.put(user.id, user);
    }
    
    public void registerChannel(Channel channel) {
        channels.put(channel.id, channel);
        User owner = users.get(channel.ownerUserId);
        if (owner != null) {
            owner.createdChannels.add(channel.id);
            if (owner.role == UserRole.VIEWER) {
                owner.role = UserRole.CREATOR;
            }
        }
    }
    
    public boolean subscribe(String userId, String channelId) {
        User user = users.get(userId);
        Channel channel = channels.get(channelId);
        
        if (user != null && channel != null && !user.subscriptions.contains(channelId)) {
            user.subscriptions.add(channelId);
            channel.subscribers.add(userId);
            channel.stats.put("subscriberCount", (long) channel.subscribers.size());
            return true;
        }
        return false;
    }
}

class ContentManager {
    private static ContentManager instance;
    private Map<String, Content> contents = new HashMap<>();
    private Map<String, Comment> comments = new HashMap<>();
    private SearchStrategy searchStrategy = new BasicSearchStrategy();
    
    private ContentManager() {}
    
    public static ContentManager getInstance() {
        if (instance == null) {
            instance = new ContentManager();
        }
        return instance;
    }
    
    public void registerContent(Content content) {
        contents.put(content.id, content);
        
        // Add to channel
        UserManager userManager = UserManager.getInstance();
        Channel channel = userManager.getChannelById(content.channelId);
        if (channel != null) {
            channel.contents.add(content.id);
            channel.stats.put("totalContent", (long) channel.contents.size());
        }
    }
    
    public List<Content> searchContent(String keyword) {
        return searchStrategy.search(keyword, new ArrayList<>(contents.values()));
    }
}
```

### **Service Layer:**
```java
class VideoStreamingService {
    private UserManager userManager = UserManager.getInstance();
    private ContentManager contentManager = ContentManager.getInstance();
    
    // User Management
    public void registerUser(User user) {
        userManager.registerUser(user);
    }
    
    // Channel Management
    public Channel createChannel(String userId, String channelName, String description, String category) {
        Channel channel = UserFactory.createChannel(userId, channelName, description, category);
        userManager.registerChannel(channel);
        return channel;
    }
    
    // Content Management
    public Content uploadContent(String channelId, String userId, String title, String description, ContentType type, String url) {
        Content content = ContentFactory.createContent(channelId, userId, title, description, type, url);
        contentManager.registerContent(content);
        return content;
    }
    
    // Search & Discovery
    public List<Content> searchContent(String keyword) {
        return contentManager.searchContent(keyword);
    }
    
    public boolean subscribe(String userId, String channelId) {
        return userManager.subscribe(userId, channelId);
    }
}
```

**Say:** *"Now it's much more extensible and follows design patterns!"*

---

## 🎯 **Your Interview Mindset:**

### **Think Step by Step:**
1. **"What are the main entities?"** → User, Channel, Content, Comment
2. **"What are the main operations?"** → Create Channel, Upload Content, Subscribe, Search, View
3. **"How can I make it scalable?"** → Managers with Singleton, Service Layer
4. **"How can I make it flexible?"** → Strategy Pattern for Search, Factory for Creation

### **Always Explain:**
- *"I'm starting simple and building up..."*
- *"Let me add this for better organization..."*
- *"This pattern will make it more flexible..."*
- *"I can extend this easily for new requirements..."*

### **If Asked About Extensions:**
- **New search algorithm?** *"Just implement SearchStrategy interface"*
- **Recommendation system?** *"Add RecommendationStrategy pattern"*
- **Live streaming?** *"Add LiveStream model and real-time updates"*
- **Content moderation?** *"Add ModerationStrategy pattern"*

---

## 📱 **Demo Script:**

### **Step 1: Setup System**
```java
VideoStreamingService service = new VideoStreamingService();

// Create users
Map<String, String> creatorMetadata = new HashMap<>();
creatorMetadata.put("bio", "Tech content creator");

User creator = UserFactory.createUser("Alice Johnson", UserRole.VIEWER, creatorMetadata);
User viewer = UserFactory.createUser("Bob Smith", UserRole.VIEWER, null);
```

### **Step 2: Create Channels**
```java
service.registerUser(creator);
service.registerUser(viewer);

Channel techChannel = service.createChannel(creator.id, "TechTalks", "Technology tutorials", "Technology");
```

### **Step 3: Upload Content**
```java
Content video = service.uploadContent(techChannel.id, creator.id, 
    "Java Design Patterns", "Learn design patterns", ContentType.VIDEO, "video-url");

Content audio = service.uploadContent(techChannel.id, creator.id,
    "Tech Podcast", "Weekly tech discussion", ContentType.AUDIO, "audio-url");
```

### **Step 4: User Interactions**
```java
service.subscribe(viewer.id, techChannel.id);
service.viewContent(video.id);
service.likeContent(video.id);
service.addComment(video.id, viewer.id, "Great tutorial!");
```

### **Step 5: Discovery**
```java
List<Content> searchResults = service.searchContent("java");
List<Content> homePage = service.getHomePage(viewer.id);
List<Content> trending = service.getTrendingContent();
```

---

## ✅ **Success Checklist:**

- ✅ Started simple with basic classes
- ✅ Built functionality incrementally  
- ✅ Explained reasoning at each step
- ✅ Added design patterns naturally
- ✅ Showed extensibility
- ✅ Handled edge cases
- ✅ Demonstrated clean code principles

**Remember: It's not about perfect code, it's about showing your thought process and system design skills!**