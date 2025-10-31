# 🎥 Video Streaming Service - Complete Interview Guide

## 📋 Table of Contents
1. [System Overview](#system-overview)
2. [Architecture & Design Patterns](#architecture--design-patterns)
3. [OOP Principles Implementation](#oop-principles-implementation)
4. [SOLID Principles](#solid-principles)
5. [Code Structure](#code-structure)
6. [Key Features](#key-features)
7. [Interview Questions & Answers](#interview-questions--answers)
8. [How to Run](#how-to-run)
9. [Extension Points](#extension-points)

---

## 🏗️ System Overview

This is a **comprehensive Video Streaming System** (YouTube-like) implemented in Java, demonstrating multiple design patterns, OOP principles, and best practices commonly asked in FAANG technical interviews.

### Core Functionality
- **User Management**: Multi-role user system (Viewer, Creator, Admin)
- **Channel Management**: Users can create and manage channels
- **Content Management**: Upload videos, audio, and images with visibility controls
- **Subscription System**: Users can subscribe to channels and get personalized content
- **Content Discovery**: Search functionality and trending content algorithms
- **Engagement Features**: Likes, dislikes, comments, and view tracking
- **Analytics**: Channel and content statistics

---

## 🎯 Architecture & Design Patterns

### 1. **Factory Pattern** 🏭
**Location**: [`src/factories/`](src/factories/)

**Purpose**: Centralizes object creation with validation and default initialization.

```java
// UserFactory
public static User createUser(String name, UserRole role, Map<String, String> metadata) {
    String userId = "USER_" + UUID.randomUUID().toString().substring(0, 8);
    User user = new User(userId, name, role);
    
    if (metadata != null) {
        for (Map.Entry<String, String> entry : metadata.entrySet()) {
            user.setMetadata(entry.getKey(), entry.getValue());
        }
    }
    return user;
}

// ContentFactory
public static Content createContent(String channelId, String uploadedByUserId, String title, 
                                  String description, ContentType type, String url) {
    String contentId = "CONTENT_" + UUID.randomUUID().toString().substring(0, 8);
    Content content = new Content(contentId, channelId, uploadedByUserId, title, description, type, url);
    initializeDefaults(content);
    return content;
}
```

**Interview Benefit**: Shows **creational patterns** and **separation of concerns**.

### 2. **Singleton Pattern** 🔒
**Location**: [`src/managers/UserManager.java`](src/managers/UserManager.java), [`src/managers/ContentManager.java`](src/managers/ContentManager.java)

**Purpose**: Ensures single instance of critical managers and provides global access.

```java
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
}
```

**Interview Benefit**: Demonstrates **thread-safety** considerations and **centralized state management**.

### 3. **Strategy Pattern** 🎯
**Location**: [`src/strategies/SearchStrategy.java`](src/strategies/SearchStrategy.java)

**Purpose**: Encapsulates search algorithms and makes them interchangeable.

```java
public interface SearchStrategy {
    List<Content> search(String keyword, List<Content> allContent);
}

public class BasicSearchStrategy implements SearchStrategy {
    @Override
    public List<Content> search(String keyword, List<Content> allContent) {
        return allContent.stream()
                .filter(Content::isPublic)
                .filter(content -> 
                    content.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                    content.getDescription().toLowerCase().contains(keyword.toLowerCase())
                )
                .sorted(/* engagement-based sorting */)
                .collect(Collectors.toList());
    }
}
```

**Interview Benefit**: Shows understanding of **Open/Closed Principle** and algorithm flexibility.

### 4. **Service Layer Pattern** 🔌
**Location**: [`src/services/VideoStreamingServiceImpl.java`](src/services/VideoStreamingServiceImpl.java)

**Purpose**: Provides clean API and coordinates between different managers.

```java
public class VideoStreamingServiceImpl implements VideoStreamingService {
    private final UserManager userManager;
    private final ContentManager contentManager;
    private final SearchStrategy searchStrategy;
    
    @Override
    public Content uploadContent(String channelId, String userId, String title, 
                               String description, ContentType type, String url) {
        // Validation
        if (!ContentFactory.validateContentData(title, description, type, url)) {
            throw new IllegalArgumentException("Invalid content data");
        }
        
        // Business logic coordination
        Channel channel = userManager.getChannelById(channelId);
        Content content = ContentFactory.createContent(channelId, userId, title, description, type, url);
        contentManager.registerContent(content);
        return content;
    }
}
```

**Interview Benefit**: Demonstrates **layered architecture** and **dependency management**.

---

## 🧱 OOP Principles Implementation

### 1. **Encapsulation** 🔐
- **Private fields** with **controlled access**
- **Data validation** in methods
- **Internal state protection**

```java
public class Content {
    private String id;                    // Immutable
    private long views;                   // Controlled access
    private ContentVisibility visibility; // Controlled transitions
    
    public void incrementViews() {
        this.views++;  // Controlled modification
    }
    
    public boolean isPublic() {
        return visibility == ContentVisibility.PUBLIC;  // Derived property
    }
}
```

### 2. **Inheritance** 🌳
- **Interface inheritance** for contracts
- **Composition over inheritance** approach

```java
// Interface inheritance
public interface VideoStreamingService {
    Content uploadContent(String channelId, String userId, String title, String description, ContentType type, String url);
    List<Content> searchContent(String keyword);
}

// Implementation inheritance
public class VideoStreamingServiceImpl implements VideoStreamingService {
    // Implementation details
}
```

### 3. **Polymorphism** 🎭
- **Interface-based polymorphism**
- **Strategy pattern implementation**

```java
// Same interface, different implementations
SearchStrategy basicSearch = new BasicSearchStrategy();
SearchStrategy advancedSearch = new AdvancedSearchStrategy(); // Future implementation

// Polymorphic behavior
List<Content> results = searchStrategy.search(keyword, allContent);
```

### 4. **Abstraction** 🎨
- **Interface abstractions** hide implementation details
- **Service layer abstractions**

```java
// Client code doesn't need to know internal implementation
VideoStreamingService service = new VideoStreamingServiceImpl();
Content content = service.uploadContent(channelId, userId, title, description, type, url);
```

---

## ⚖️ SOLID Principles

### 1. **Single Responsibility Principle (SRP)** ✅
Each class has **one reason to change**:

- **`User`**: Manages user data and subscriptions only
- **`Channel`**: Handles channel information and statistics only
- **`Content`**: Manages content metadata and engagement only
- **`UserManager`**: Handles user and channel operations only
- **`ContentManager`**: Manages content and search operations only

### 2. **Open/Closed Principle (OCP)** ✅
**Open for extension, closed for modification**:

```java
// Adding new search strategy without modifying existing code
public class AdvancedSearchStrategy implements SearchStrategy {
    @Override
    public List<Content> search(String keyword, List<Content> allContent) {
        // Advanced search with ML-based ranking
        return allContent.stream()
                .filter(content -> advancedMatch(keyword, content))
                .sorted(Comparator.comparing(this::calculateRelevanceScore).reversed())
                .collect(Collectors.toList());
    }
}

public class TagBasedSearchStrategy implements SearchStrategy {
    @Override
    public List<Content> search(String keyword, List<Content> allContent) {
        // Tag-based search implementation
        return allContent.stream()
                .filter(content -> matchesTags(keyword, content))
                .collect(Collectors.toList());
    }
}
```

### 3. **Liskov Substitution Principle (LSP)** ✅
**Subtypes must be substitutable**:

```java
// Any SearchStrategy implementation can replace another
SearchStrategy strategy = new BasicSearchStrategy();
// Can be replaced with any other implementation without breaking code
strategy = new AdvancedSearchStrategy();
strategy = new TagBasedSearchStrategy();
```

### 4. **Interface Segregation Principle (ISP)** ✅
**Clients shouldn't depend on unused interfaces**:

```java
// Specific interfaces for specific needs
public interface VideoStreamingService {
    // Only video streaming related methods
    Content uploadContent(String channelId, String userId, String title, String description, ContentType type, String url);
    List<Content> searchContent(String keyword);
    boolean subscribe(String userId, String channelId);
}

public interface SearchStrategy {
    // Only search related methods
    List<Content> search(String keyword, List<Content> allContent);
}
```

### 5. **Dependency Inversion Principle (DIP)** ✅
**Depend on abstractions, not concretions**:

```java
public class VideoStreamingServiceImpl implements VideoStreamingService {
    private final SearchStrategy searchStrategy;  // Abstraction
    
    public VideoStreamingServiceImpl() {
        this.searchStrategy = new BasicSearchStrategy(); // Default implementation
    }
    
    // Depends on interface, not concrete class
    public List<Content> searchContent(String keyword) {
        return searchStrategy.search(keyword, contentManager.getAllContent());
    }
}
```

---

## 📁 Code Structure

```
src/
├── enums/
│   ├── ContentType.java           # Content types (VIDEO, AUDIO, IMAGE)
│   ├── ContentVisibility.java     # Visibility states (PUBLIC, PRIVATE, UNLISTED)
│   └── UserRole.java              # User roles (VIEWER, CREATOR, ADMIN)
├── models/
│   ├── User.java                  # User entity with subscriptions and metadata
│   ├── Channel.java               # Channel entity with statistics and content
│   ├── Content.java               # Content entity with engagement metrics
│   └── Comment.java               # Comment entity with likes/dislikes
├── factories/
│   ├── UserFactory.java           # User and channel creation with validation
│   └── ContentFactory.java        # Content and comment creation with defaults
├── managers/
│   ├── UserManager.java           # Singleton user and channel management
│   └── ContentManager.java        # Singleton content and search management
├── strategies/
│   ├── SearchStrategy.java        # Strategy interface for search algorithms
│   └── BasicSearchStrategy.java   # Basic keyword-based search implementation
├── services/
│   ├── VideoStreamingService.java      # Service interface
│   └── VideoStreamingServiceImpl.java  # Service implementation
└── Main.java                           # Application entry point with demo
```

---

## 🚀 Key Features

### ✨ **Multi-User System**
- **Viewer**: Subscribe to channels, view content, comment
- **Creator**: Create channels, upload content, manage visibility
- **Admin**: System-wide management capabilities

### ✨ **Smart Content Discovery**
- **Search functionality**: Keyword-based content search
- **Strategy pattern**: Easy to add new search algorithms
- **Trending algorithm**: Engagement-based content ranking
- **Personalized home page**: Content from subscribed channels

### ✨ **Complete Content Lifecycle**
- **Content upload**: Multi-format support (video, audio, image)
- **Visibility controls**: Public, private, unlisted content
- **Engagement tracking**: Views, likes, dislikes, comments
- **Analytics**: Detailed statistics for channels and content

### ✨ **Subscription System**
- **Channel subscriptions**: Users can follow creators
- **Subscriber management**: Track followers and subscriptions
- **Content feed**: Personalized content from subscribed channels

### ✨ **Robust Data Management**
- **Singleton managers**: Centralized data management
- **Metadata support**: Extensible key-value metadata
- **Statistics tracking**: Real-time engagement metrics

---

## 🎤 Interview Questions & Answers

### **Q1: How would you handle concurrent uploads and views?**
**A**: Implement **thread-safety** using:
```java
public class ContentManager {
    private final Map<String, Content> contents = new ConcurrentHashMap<>();
    
    public synchronized void incrementViews(String contentId) {
        Content content = contents.get(contentId);
        if (content != null) {
            content.incrementViews();
            updateChannelStats(content.getChannelId());
        }
    }
}
```

### **Q2: How would you scale this system for millions of users?**
**A**: Extend the system with:
- **Database sharding**: Partition users and content by region/hash
- **CDN integration**: Distribute content globally
- **Caching layer**: Redis for trending content and user feeds
- **Microservices**: Separate user, content, and analytics services

```java
public class ScalableVideoStreamingService {
    private final UserService userService;
    private final ContentService contentService;
    private final AnalyticsService analyticsService;
    private final CacheService cacheService;
    
    public List<Content> getTrendingContent() {
        return cacheService.getOrCompute("trending", 
            () -> analyticsService.calculateTrending());
    }
}
```

### **Q3: How would you implement different recommendation algorithms?**
**A**: **Strategy Pattern** allows easy extension:
```java
public interface RecommendationStrategy {
    List<Content> recommend(String userId, List<Content> allContent);
}

public class CollaborativeFilteringStrategy implements RecommendationStrategy {
    @Override
    public List<Content> recommend(String userId, List<Content> allContent) {
        // Find similar users and recommend their liked content
        return findSimilarUsers(userId).stream()
                .flatMap(user -> getLikedContent(user).stream())
                .distinct()
                .collect(Collectors.toList());
    }
}
```

### **Q4: How would you implement real-time notifications?**
**A**: Add **Observer Pattern**:
```java
public interface ContentObserver {
    void onContentUploaded(Content content, Channel channel);
    void onContentLiked(Content content, User user);
}

public class ContentManager {
    private List<ContentObserver> observers = new ArrayList<>();
    
    public void registerContent(Content content) {
        contents.put(content.getId(), content);
        notifyContentUploaded(content);
    }
    
    private void notifyContentUploaded(Content content) {
        Channel channel = userManager.getChannelById(content.getChannelId());
        observers.forEach(observer -> observer.onContentUploaded(content, channel));
    }
}
```

### **Q5: How would you handle content moderation?**
**A**: Add **Content Moderation Service**:
```java
public interface ModerationStrategy {
    ModerationResult moderate(Content content);
}

public class AIContentModerationStrategy implements ModerationStrategy {
    @Override
    public ModerationResult moderate(Content content) {
        // AI-based content analysis
        return new ModerationResult(isAppropriate(content), getConfidenceScore(content));
    }
}

public class ContentModerationService {
    private final ModerationStrategy strategy;
    
    public boolean approveContent(Content content) {
        ModerationResult result = strategy.moderate(content);
        if (result.isApproved()) {
            content.setVisibility(ContentVisibility.PUBLIC);
            return true;
        }
        return false;
    }
}
```

### **Q6: How would you implement analytics and reporting?**
**A**: Add **Analytics Service**:
```java
public class AnalyticsService {
    public ChannelAnalytics getChannelAnalytics(String channelId, DateRange range) {
        return ChannelAnalytics.builder()
                .totalViews(calculateTotalViews(channelId, range))
                .subscriberGrowth(calculateSubscriberGrowth(channelId, range))
                .topContent(getTopContent(channelId, range))
                .engagementRate(calculateEngagementRate(channelId, range))
                .build();
    }
    
    public List<Content> getTrendingContent(int limit) {
        return contents.values().stream()
                .filter(Content::isPublic)
                .sorted(Comparator.comparing(this::calculateTrendingScore).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }
}
```

---

## 🏃‍♂️ How to Run

### **Prerequisites**
- Java 8 or higher
- Any IDE (IntelliJ IDEA, Eclipse, VS Code)

### **Steps**
1. **Navigate** to the VideoStreamingService directory
2. **Compile** all Java files:
   ```bash
   javac -encoding UTF-8 -d out src\enums\*.java src\models\*.java src\factories\*.java src\managers\*.java src\strategies\*.java src\services\*.java src\Main.java
   ```
3. **Run** the main class:
   ```bash
   java -cp out Main
   ```

### **Expected Output**
```
=== Video Streaming Service Demo ===

User registered: Alice Johnson (VIEWER)
User registered: Bob Smith (VIEWER)
User registered: Charlie Brown (VIEWER)
User registered: Diana Prince (VIEWER)
Users registered successfully!

--- Creating Channels ---
Channel registered: TechTalks by Alice Johnson
Channel registered: MusicVibes by Bob Smith
Channels created successfully!

--- Uploading Content ---
Content uploaded: Java Design Patterns Tutorial to channel CHANNEL_12345678
Content uploaded: System Design Basics to channel CHANNEL_12345678
Content uploaded: Acoustic Guitar Cover to channel CHANNEL_87654321
Content uploaded: Advanced Algorithms to channel CHANNEL_12345678
Content uploaded successfully!

--- Managing Subscriptions ---
Charlie Brown subscribed to TechTalks
Charlie Brown subscribed to MusicVibes
Diana Prince subscribed to TechTalks
Subscriptions completed!

--- Content Interactions ---
Liked content: Java Design Patterns Tutorial
Liked content: Java Design Patterns Tutorial
Liked content: System Design Basics
Disliked content: Acoustic Guitar Cover
Charlie Brown commented on Java Design Patterns Tutorial
Diana Prince commented on Java Design Patterns Tutorial
Charlie Brown commented on System Design Basics
Content interactions completed!

--- Charlie Brown's Home Page ---
📺 Java Design Patterns Tutorial (VIDEO)
   Views: 2 | Likes: 2 | Comments: 2
📺 System Design Basics (VIDEO)
   Views: 1 | Likes: 1 | Comments: 1
📺 Acoustic Guitar Cover (AUDIO)
   Views: 1 | Likes: 0 | Comments: 0

--- Search Results for 'design' ---
🔍 Java Design Patterns Tutorial - Learn about Singleton and Factory patterns
🔍 System Design Basics - Introduction to scalable system design

--- Trending Content ---
🔥 Java Design Patterns Tutorial (Views: 2, Likes: 2)
🔥 System Design Basics (Views: 1, Likes: 1)
🔥 Acoustic Guitar Cover (Views: 1, Likes: 0)

--- Channel Statistics ---
Channel: TechTalks
Subscribers: 2
Content: 3
Total Views: 3

Channel: MusicVibes
Subscribers: 1
Content: 1
Total Views: 1

--- Content Statistics ---
Content: Java Design Patterns Tutorial
Views: 2
Likes: 2
Dislikes: 0
Comments: 2
Like Ratio: 1.00

--- Comments on 'Java Design Patterns Tutorial' ---
💬 Diana Prince: Thanks for the clear explanation!
   👍 0 | 👎 0
💬 Charlie Brown: Great tutorial! Very helpful.
   👍 0 | 👎 0

--- Charlie Brown's Subscriptions ---
📺 TechTalks (Technology)
   Subscribers: 2 | Content: 3
📺 MusicVibes (Music)
   Subscribers: 1 | Content: 1

--- Advanced Search: Technology Channels ---
🔍 Java Design Patterns Tutorial from TechTalks
🔍 System Design Basics from TechTalks

--- All Users ---
👤 Alice Johnson (CREATOR)
   Subscriptions: 0 | Channels: 1
👤 Bob Smith (CREATOR)
   Subscriptions: 0 | Channels: 1
👤 Charlie Brown (VIEWER)
   Subscriptions: 2 | Channels: 0
👤 Diana Prince (VIEWER)
   Subscriptions: 1 | Channels: 0

=== Video Streaming Service Demo Completed Successfully! ===
```

---

## 🔧 Extension Points

### **Easy Extensions** (15-30 minutes in interview)
1. **Add new search strategy**: Implement `SearchStrategy` interface
2. **Add new content type**: Extend `ContentType` enum
3. **Add new user role**: Extend `UserRole` enum and permissions

### **Medium Extensions** (30-45 minutes in interview)
1. **Recommendation engine**: Add ML-based content recommendations
2. **Content moderation**: Add automated content filtering
3. **Live streaming**: Add real-time streaming capabilities
4. **Playlist management**: Add user-created playlists

### **Advanced Extensions** (45+ minutes in interview)
1. **Microservices architecture**: Split into independent services
2. **Real-time notifications**: Add WebSocket support for live updates
3. **Advanced analytics**: Add detailed engagement metrics and reporting
4. **CDN integration**: Add content delivery network support
5. **Machine learning**: Add intelligent content categorization and recommendations

---

## 🎯 Interview Success Tips

### **What to Highlight**
1. **Design Patterns**: Factory, Singleton, Strategy, Service Layer
2. **SOLID Principles**: Clear examples of each principle
3. **Extensibility**: How easy it is to add new features
4. **Real-world modeling**: Practical YouTube-like functionality
5. **Scalability considerations**: How to handle millions of users

### **Common Follow-up Questions**
- "How would you handle video transcoding and storage?"
- "How would you implement live streaming?"
- "How would you scale to handle viral content?"
- "How would you implement content recommendations?"
- "How would you handle copyright detection?"

### **Code Quality Points**
- **Clean code**: Meaningful names, small methods, clear structure
- **Documentation**: Clear comments and method descriptions
- **Error handling**: Proper exception handling and validation
- **Testing**: Unit test considerations and testable design
- **Performance**: Time/space complexity awareness

---

## 📚 Key Takeaways

This Video Streaming System demonstrates:

1. **🎯 Design Patterns**: Practical implementation of multiple patterns
2. **🧱 OOP Principles**: All 4 pillars with real examples  
3. **⚖️ SOLID Principles**: Each principle clearly demonstrated
4. **🚀 Scalability**: Easy to extend and modify
5. **🔧 Interview Ready**: Perfect for 45-60 minute FAANG interviews

**Perfect for demonstrating system design skills and coding expertise in technical interviews!**