package managers;

import enums.UserStatus;
import models.*;
import observers.MessageEventObserver;
import strategies.MessageDeliveryStrategy;
import strategies.InstantDeliveryStrategy;
import strategies.QueuedDeliveryStrategy;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Singleton manager for the entire messaging system.
 * 
 * This class serves as the central coordinator for all messaging operations.
 * It maintains the system state and orchestrates interactions between different
 * components using various design patterns.
 * 
 * Key Responsibilities:
 * - Centralized data storage (users, chats, messages)
 * - Observer pattern coordination for notifications
 * - Strategy pattern implementation for message delivery
 * - Thread-safe operations using concurrent collections
 * - System-wide state management
 * 
 * Design Patterns Implemented:
 * - Singleton: Ensures single instance of system manager
 * - Observer: Manages notification observers
 * - Strategy: Delegates message delivery to appropriate strategy
 * - Facade: Provides simplified interface to complex subsystem
 * 
 * SOLID Principles:
 * - Single Responsibility: Manages system coordination only
 * - Open/Closed: Extensible through strategies and observers
 * - Dependency Inversion: Uses strategy and observer abstractions
 * 
 * Thread Safety:
 * - Uses ConcurrentHashMap for thread-safe operations
 * - Synchronized singleton initialization
 * 
 * @author MessagingSystem
 * @version 1.0
 */
public class MessagingSystemManager {
    
    // ==================== SINGLETON IMPLEMENTATION ====================
    
    /** Single instance of the manager (Singleton pattern) */
    private static MessagingSystemManager instance;
    
    // ==================== DATA STORAGE ====================
    
    /** Thread-safe storage for all users in the system */
    private final Map<String, User> users;
    
    /** Thread-safe storage for all chats in the system */
    private final Map<String, Chat> chats;
    
    /** Thread-safe storage for all messages in the system */
    private final Map<String, Message> messages;
    
    // ==================== DESIGN PATTERN COMPONENTS ====================
    
    /** List of observers for message events (Observer pattern) */
    private final List<MessageEventObserver> observers;
    
    /** Strategy for instant message delivery to online users */
    private final MessageDeliveryStrategy instantDelivery;
    
    /** Strategy for queued message delivery to offline users */
    private final MessageDeliveryStrategy queuedDelivery;

    /**
     * Private constructor to prevent external instantiation (Singleton pattern).
     * Initializes all data structures and strategy implementations.
     */
    private MessagingSystemManager() {
        // Initialize thread-safe collections for concurrent access
        this.users = new ConcurrentHashMap<>();
        this.chats = new ConcurrentHashMap<>();
        this.messages = new ConcurrentHashMap<>();
        
        // Initialize observer list for notifications
        this.observers = new ArrayList<>();
        
        // Initialize delivery strategies (Strategy pattern)
        this.instantDelivery = new InstantDeliveryStrategy();
        this.queuedDelivery = new QueuedDeliveryStrategy();
    }

    /**
     * Gets the singleton instance of the messaging system manager.
     * Uses double-checked locking for thread-safe lazy initialization.
     * 
     * @return The single instance of MessagingSystemManager
     */
    public static synchronized MessagingSystemManager getInstance() {
        if (instance == null) {
            instance = new MessagingSystemManager();
        }
        return instance;
    }

    // ==================== OBSERVER PATTERN IMPLEMENTATION ====================

    /**
     * Adds an observer to receive notifications about message events.
     * 
     * @param observer The observer to add to the notification list
     */
    public void addObserver(MessageEventObserver observer) {
        observers.add(observer);
    }

    /**
     * Removes an observer from the notification list.
     * 
     * @param observer The observer to remove
     */
    public void removeObserver(MessageEventObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notifies all observers when a message is sent.
     * Part of the Observer pattern implementation.
     * 
     * @param message The message that was sent
     */
    private void notifyMessageSent(Message message) {
        observers.forEach(observer -> observer.onMessageSent(message));
    }

    /**
     * Notifies all observers when a message is delivered.
     * Part of the Observer pattern implementation.
     * 
     * @param message The message that was delivered
     */
    private void notifyMessageDelivered(Message message) {
        observers.forEach(observer -> observer.onMessageDelivered(message));
    }

    /**
     * Notifies all observers when a user's status changes.
     * Part of the Observer pattern implementation.
     * 
     * @param user The user whose status changed
     */
    private void notifyUserStatusChanged(User user) {
        observers.forEach(observer -> observer.onUserStatusChanged(user));
    }

    // ==================== CORE BUSINESS OPERATIONS ====================

    /**
     * Creates a new user and stores it in the system.
     * 
     * @param name User's display name
     * @param email User's email address
     * @return The created user with generated unique ID
     */
    public User createUser(String name, String email) {
        // Create new user (ID is auto-generated in User constructor)
        User user = new User(name, email);
        
        // Store user in thread-safe map
        users.put(user.getId(), user);
        
        return user;
    }

    /**
     * Creates a private chat between two users.
     * 
     * @param user1Id ID of the first user
     * @param user2Id ID of the second user
     * @return The created private chat
     */
    public Chat createPrivateChat(String user1Id, String user2Id) {
        // Create new private chat
        Chat chat = new Chat(enums.ChatType.PRIVATE);
        
        // Add both users as participants
        chat.addParticipant(user1Id);
        chat.addParticipant(user2Id);
        
        // Store chat in system
        chats.put(chat.getId(), chat);
        
        return chat;
    }

    /**
     * Creates a group chat with the specified admin.
     * 
     * @param groupName Name of the group
     * @param adminId ID of the user who will be admin
     * @return The created group chat
     */
    public GroupChat createGroupChat(String groupName, String adminId) {
        // Create new group chat
        GroupChat groupChat = new GroupChat(groupName);
        
        // Add admin as participant and give admin privileges
        groupChat.addParticipant(adminId);
        groupChat.addAdmin(adminId);
        
        // Store group chat in system
        chats.put(groupChat.getId(), groupChat);
        
        return groupChat;
    }

    /**
     * Sends a message using the Strategy pattern for delivery.
     * 
     * This method demonstrates the Strategy pattern by choosing the appropriate
     * delivery strategy based on the receiver's online status.
     * 
     * Process Flow:
     * 1. Create and store the message
     * 2. Notify observers that message was sent
     * 3. Choose delivery strategy based on receiver status
     * 4. Execute delivery strategy
     * 5. Notify observers if message was delivered immediately
     * 
     * @param senderId ID of the message sender
     * @param receiverId ID of the message receiver
     * @param content Message content
     * @return The created message with appropriate delivery status
     */
    public Message sendMessage(String senderId, String receiverId, String content) {
        // Create new message with TEXT type (default)
        Message message = new Message(senderId, receiverId, content, enums.MessageType.TEXT);
        
        // Store message in system
        messages.put(message.getId(), message);
        
        // Notify observers that message was sent
        notifyMessageSent(message);
        
        // Strategy Pattern: Choose delivery strategy based on receiver status
        User receiver = users.get(receiverId);
        if (receiver != null && receiver.getStatus().isAvailableForMessaging()) {
            // Receiver is online - use instant delivery strategy
            instantDelivery.deliverMessage(message);
            notifyMessageDelivered(message);
        } else {
            // Receiver is offline - use queued delivery strategy
            queuedDelivery.deliverMessage(message);
        }
        
        return message;
    }

    /**
     * Updates a user's status and triggers observer notifications.
     * 
     * @param userId ID of the user to update
     * @param status New status to set
     * @return true if update successful, false if user not found
     */
    public boolean updateUserStatus(String userId, UserStatus status) {
        // Retrieve user from storage
        User user = users.get(userId);
        
        if (user != null) {
            // Update user status
            user.setStatus(status);
            
            // Notify observers of status change
            notifyUserStatusChanged(user);
            
            return true;
        }
        
        return false;
    }

    // ==================== DATA RETRIEVAL OPERATIONS ====================

    /**
     * Retrieves a user by their unique ID.
     * 
     * @param userId The unique ID of the user
     * @return The User object if found, null otherwise
     */
    public User getUser(String userId) {
        return users.get(userId);
    }

    /**
     * Retrieves a chat by its unique ID.
     * 
     * @param chatId The unique ID of the chat
     * @return The Chat object if found, null otherwise
     */
    public Chat getChat(String chatId) {
        return chats.get(chatId);
    }

    /**
     * Retrieves a message by its unique ID.
     * 
     * @param messageId The unique ID of the message
     * @return The Message object if found, null otherwise
     */
    public Message getMessage(String messageId) {
        return messages.get(messageId);
    }

    /**
     * Retrieves all messages exchanged between two users.
     * 
     * This method performs the following operations:
     * 1. Filters all messages to find those between the two users
     * 2. Sorts messages by timestamp in chronological order
     * 3. Returns as a list for easy iteration
     * 
     * Time Complexity: O(n log n) where n is total number of messages
     * Space Complexity: O(m) where m is number of messages between users
     * 
     * @param user1Id ID of the first user
     * @param user2Id ID of the second user
     * @return List of messages in chronological order
     */
    public List<Message> getMessagesBetween(String user1Id, String user2Id) {
        return messages.values().stream()
                // Filter messages between the two users (bidirectional)
                .filter(msg -> (msg.getSenderId().equals(user1Id) && msg.getReceiverId().equals(user2Id)) ||
                              (msg.getSenderId().equals(user2Id) && msg.getReceiverId().equals(user1Id)))
                // Sort by timestamp (chronological order)
                .sorted((m1, m2) -> m1.getTimestamp().compareTo(m2.getTimestamp()))
                // Collect to list for return
                .collect(java.util.stream.Collectors.toList());
    }

    // ==================== SYSTEM STATISTICS AND MONITORING ====================

    /**
     * Gets the total number of users in the system.
     * Useful for monitoring and statistics.
     * 
     * @return Total number of registered users
     */
    public int getTotalUsers() {
        return users.size();
    }

    /**
     * Gets the total number of chats in the system.
     * Useful for monitoring and statistics.
     * 
     * @return Total number of chats (private and group)
     */
    public int getTotalChats() {
        return chats.size();
    }

    /**
     * Gets the total number of messages in the system.
     * Useful for monitoring and statistics.
     * 
     * @return Total number of messages sent
     */
    public int getTotalMessages() {
        return messages.size();
    }
}