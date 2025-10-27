package services;

import enums.UserStatus;
import managers.MessagingSystemManager;
import models.*;
import java.util.List;

/**
 * Implementation of the MessagingService interface.
 * 
 * This class provides the concrete implementation of all messaging operations.
 * It acts as a facade that delegates operations to the MessagingSystemManager
 * while providing additional validation and business logic.
 * 
 * Key Responsibilities:
 * - Input validation and sanitization
 * - Business rule enforcement (blocking, permissions)
 * - Error handling and exception management
 * - Delegation to the system manager for actual operations
 * 
 * Design Patterns Demonstrated:
 * - Facade Pattern: Simplifies complex subsystem interactions
 * - Dependency Inversion: Depends on MessagingSystemManager abstraction
 * - Template Method: Consistent validation pattern across methods
 * 
 * SOLID Principles:
 * - Single Responsibility: Handles service layer concerns only
 * - Dependency Inversion: Depends on manager abstraction
 * - Open/Closed: Can be extended without modification
 * 
 * @author MessagingSystem
 * @version 1.0
 */
public class MessagingServiceImpl implements MessagingService {
    
    /** 
     * Reference to the singleton system manager that handles core operations.
     * This demonstrates Dependency Inversion - we depend on the manager interface.
     */
    private final MessagingSystemManager manager;
    
    /**
     * Constructor that initializes the service with the system manager.
     * Uses Singleton pattern to get the manager instance.
     */
    public MessagingServiceImpl() {
        this.manager = MessagingSystemManager.getInstance();
    }
    
    // ==================== USER MANAGEMENT IMPLEMENTATIONS ====================
    
    /**
     * Creates a new user with comprehensive input validation.
     * 
     * Validation Rules:
     * - Name cannot be null, empty, or whitespace-only
     * - Email cannot be null, empty, or whitespace-only
     * 
     * @param name User's display name
     * @param email User's email address
     * @return Created user with unique ID
     * @throws IllegalArgumentException if validation fails
     */
    @Override
    public User createUser(String name, String email) {
        // Validate user name
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("User name cannot be empty");
        }
        
        // Validate email address
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("User email cannot be empty");
        }
        
        // Delegate to manager for actual user creation
        return manager.createUser(name, email);
    }
    
    /**
     * Updates user status with null safety checks.
     * 
     * @param userId The user ID to update
     * @param status The new status to set
     * @return true if update successful, false if user not found or invalid input
     */
    @Override
    public boolean updateUserStatus(String userId, UserStatus status) {
        // Null safety check - return false for invalid input
        if (userId == null || status == null) {
            return false;
        }
        
        // Delegate to manager
        return manager.updateUserStatus(userId, status);
    }
    
    /**
     * Retrieves user by ID with simple delegation.
     * 
     * @param userId The user ID to retrieve
     * @return User object or null if not found
     */
    @Override
    public User getUser(String userId) {
        return manager.getUser(userId);
    }
    
    // ==================== CHAT MANAGEMENT IMPLEMENTATIONS ====================
    
    /**
     * Creates a private chat with comprehensive validation.
     * 
     * Validation Rules:
     * - Both user IDs must be non-null
     * - User IDs must be different (can't chat with yourself)
     * - Both users must exist in the system
     * - Users must not have blocked each other
     * 
     * @param user1Id First user's ID
     * @param user2Id Second user's ID
     * @return Created private chat
     * @throws IllegalArgumentException if validation fails
     */
    @Override
    public Chat createPrivateChat(String user1Id, String user2Id) {
        // Validate input parameters
        if (user1Id == null || user2Id == null || user1Id.equals(user2Id)) {
            throw new IllegalArgumentException("Invalid user IDs for private chat");
        }
        
        // Verify both users exist
        User user1 = manager.getUser(user1Id);
        User user2 = manager.getUser(user2Id);
        
        if (user1 == null || user2 == null) {
            throw new IllegalArgumentException("Users must exist to create chat");
        }
        
        // Check blocking status - either user blocking the other prevents chat creation
        if (user1.isBlocked(user2Id) || user2.isBlocked(user1Id)) {
            throw new IllegalArgumentException("Cannot create chat with blocked user");
        }
        
        // All validations passed - delegate to manager
        return manager.createPrivateChat(user1Id, user2Id);
    }
    
    /**
     * Creates a group chat with validation.
     * 
     * Validation Rules:
     * - Group name cannot be null, empty, or whitespace-only
     * - Admin user must exist in the system
     * 
     * @param groupName Name for the group
     * @param adminId ID of the user who will be admin
     * @return Created group chat
     * @throws IllegalArgumentException if validation fails
     */
    @Override
    public GroupChat createGroupChat(String groupName, String adminId) {
        // Validate group name
        if (groupName == null || groupName.trim().isEmpty()) {
            throw new IllegalArgumentException("Group name cannot be empty");
        }
        
        // Validate admin exists
        if (adminId == null || manager.getUser(adminId) == null) {
            throw new IllegalArgumentException("Admin user must exist");
        }
        
        // Delegate to manager
        return manager.createGroupChat(groupName, adminId);
    }
    
    /**
     * Adds user to chat with validation.
     * 
     * Validation Rules:
     * - Chat must exist
     * - User must exist
     * - Chat must allow additional participants (private chats limited to 2)
     * 
     * @param chatId ID of the chat to add user to
     * @param userId ID of the user to add
     * @return true if user added successfully
     */
    @Override
    public boolean addUserToChat(String chatId, String userId) {
        // Verify chat and user exist
        Chat chat = manager.getChat(chatId);
        User user = manager.getUser(userId);
        
        if (chat == null || user == null) {
            return false;
        }
        
        // Check if chat can accept more participants
        if (!chat.canAddParticipant()) {
            return false;
        }
        
        // Add participant to chat
        chat.addParticipant(userId);
        return true;
    }
    
    /**
     * Simple delegation to retrieve chat by ID.
     * 
     * @param chatId The chat ID to retrieve
     * @return Chat object or null if not found
     */
    @Override
    public Chat getChat(String chatId) {
        return manager.getChat(chatId);
    }
    
    // ==================== MESSAGE OPERATIONS IMPLEMENTATIONS ====================
    
    /**
     * Sends a message with comprehensive validation and business logic.
     * 
     * Validation Rules:
     * - Sender ID cannot be null
     * - Receiver ID cannot be null
     * - Content cannot be null, empty, or whitespace-only
     * - Both sender and receiver must exist
     * - Receiver must not have blocked the sender
     * 
     * Business Logic:
     * - Trims whitespace from content
     * - Applies delivery strategy based on receiver status
     * - Triggers observer notifications
     * 
     * @param senderId ID of the message sender
     * @param receiverId ID of the message receiver
     * @param content Message content
     * @return Created message with delivery status
     * @throws IllegalArgumentException if validation fails
     */
    @Override
    public Message sendMessage(String senderId, String receiverId, String content) {
        // Validate all required parameters
        if (senderId == null || receiverId == null || content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid message parameters");
        }
        
        // Verify sender and receiver exist
        User sender = manager.getUser(senderId);
        User receiver = manager.getUser(receiverId);
        
        if (sender == null || receiver == null) {
            throw new IllegalArgumentException("Sender and receiver must exist");
        }
        
        // Check if receiver has blocked the sender
        if (receiver.isBlocked(senderId)) {
            throw new IllegalArgumentException("Cannot send message to user who blocked you");
        }
        
        // All validations passed - delegate to manager with trimmed content
        return manager.sendMessage(senderId, receiverId, content.trim());
    }
    
    /**
     * Simple delegation to retrieve message by ID.
     * 
     * @param messageId The message ID to retrieve
     * @return Message object or null if not found
     */
    @Override
    public Message getMessage(String messageId) {
        return manager.getMessage(messageId);
    }
    
    /**
     * Retrieves message history between two users with validation.
     * 
     * @param user1Id First user's ID
     * @param user2Id Second user's ID
     * @return List of messages in chronological order
     * @throws IllegalArgumentException if user IDs are null
     */
    @Override
    public List<Message> getMessagesBetween(String user1Id, String user2Id) {
        // Validate input parameters
        if (user1Id == null || user2Id == null) {
            throw new IllegalArgumentException("User IDs cannot be null");
        }
        
        // Delegate to manager
        return manager.getMessagesBetween(user1Id, user2Id);
    }
    
    /**
     * Marks a message as read with authorization check.
     * 
     * Authorization Rules:
     * - Message must exist
     * - Only the intended receiver can mark message as read
     * 
     * @param messageId ID of the message to mark as read
     * @param userId ID of the user marking as read
     * @return true if marked successfully, false if unauthorized or not found
     */
    @Override
    public boolean markMessageAsRead(String messageId, String userId) {
        // Retrieve the message
        Message message = manager.getMessage(messageId);
        
        // Verify message exists and user is authorized to mark it as read
        if (message == null || !message.getReceiverId().equals(userId)) {
            return false;
        }
        
        // Mark message as read
        message.markAsRead();
        return true;
    }
    
    // ==================== GROUP MANAGEMENT IMPLEMENTATIONS ====================
    
    /**
     * Adds admin privileges to a group member with validation.
     * 
     * Validation Rules:
     * - Chat must exist and be a group chat
     * - User must already be a participant in the group
     * 
     * @param groupChatId ID of the group chat
     * @param adminId ID of the user to make admin
     * @return true if admin added successfully
     */
    @Override
    public boolean addAdminToGroup(String groupChatId, String adminId) {
        // Retrieve chat and verify it's a group chat
        Chat chat = manager.getChat(groupChatId);
        if (!(chat instanceof GroupChat)) {
            return false;
        }
        
        // Cast to GroupChat for group-specific operations
        GroupChat groupChat = (GroupChat) chat;
        
        // Verify user is already a participant before making them admin
        if (groupChat.hasParticipant(adminId)) {
            groupChat.addAdmin(adminId);
            return true;
        }
        
        return false;
    }
    
    /**
     * Removes a user from a group chat.
     * 
     * Validation Rules:
     * - Chat must exist and be a group chat
     * 
     * Side Effects:
     * - If user was an admin, admin privileges are automatically removed
     * 
     * @param groupChatId ID of the group chat
     * @param userId ID of the user to remove
     * @return true if user removed successfully
     */
    @Override
    public boolean removeUserFromGroup(String groupChatId, String userId) {
        // Retrieve chat and verify it's a group chat
        Chat chat = manager.getChat(groupChatId);
        if (!(chat instanceof GroupChat)) {
            return false;
        }
        
        // Cast to GroupChat and remove participant
        // Note: removeParticipant also removes admin privileges if user was admin
        GroupChat groupChat = (GroupChat) chat;
        groupChat.removeParticipant(userId);
        return true;
    }
}