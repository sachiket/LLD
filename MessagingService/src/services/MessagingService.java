package services;

import enums.UserStatus;
import models.*;
import java.util.List;

/**
 * Main service interface for messaging operations in the messaging system.
 * 
 * This interface defines all the core operations that can be performed in the messaging system:
 * - User management (create, update status, retrieve)
 * - Chat management (private and group chats)
 * - Message operations (send, retrieve, mark as read)
 * - Group administration (add admins, manage members)
 * 
 * Design Patterns Demonstrated:
 * - Interface Segregation Principle: Focused interface with related operations
 * - Dependency Inversion: High-level modules depend on this abstraction
 * 
 * SOLID Principles:
 * - Interface Segregation: Clients depend only on methods they use
 * - Dependency Inversion: Abstractions don't depend on details
 * - Single Responsibility: Each method has one clear purpose
 * 
 * @author MessagingSystem
 * @version 1.0
 */
public interface MessagingService {
    
    // ==================== USER MANAGEMENT OPERATIONS ====================
    
    /**
     * Creates a new user in the messaging system.
     * 
     * @param name User's display name (cannot be null or empty)
     * @param email User's email address (cannot be null or empty)
     * @return The created User object with generated unique ID
     * @throws IllegalArgumentException if name or email is invalid
     */
    User createUser(String name, String email);
    
    /**
     * Updates the online status of an existing user.
     * 
     * @param userId The unique ID of the user to update
     * @param status The new status to set
     * @return true if status was updated successfully, false if user not found
     */
    boolean updateUserStatus(String userId, UserStatus status);
    
    /**
     * Retrieves a user by their unique ID.
     * 
     * @param userId The unique ID of the user to retrieve
     * @return The User object if found, null otherwise
     */
    User getUser(String userId);
    
    // ==================== CHAT MANAGEMENT OPERATIONS ====================
    
    /**
     * Creates a private chat between two users.
     * Validates that both users exist and are not blocked.
     * 
     * @param user1Id The ID of the first user
     * @param user2Id The ID of the second user
     * @return The created Chat object
     * @throws IllegalArgumentException if users don't exist or are blocked
     */
    Chat createPrivateChat(String user1Id, String user2Id);
    
    /**
     * Creates a group chat with the specified admin.
     * The admin user is automatically added as the first participant.
     * 
     * @param groupName The name of the group (cannot be null or empty)
     * @param adminId The ID of the user who will be the admin
     * @return The created GroupChat object
     * @throws IllegalArgumentException if groupName is invalid or admin doesn't exist
     */
    GroupChat createGroupChat(String groupName, String adminId);
    
    /**
     * Adds a user to an existing chat (private or group).
     * For private chats, this will fail if chat already has 2 participants.
     * 
     * @param chatId The ID of the chat to add user to
     * @param userId The ID of the user to add
     * @return true if user was added successfully, false otherwise
     */
    boolean addUserToChat(String chatId, String userId);
    
    /**
     * Retrieves a chat by its unique ID.
     * 
     * @param chatId The unique ID of the chat to retrieve
     * @return The Chat object if found, null otherwise
     */
    Chat getChat(String chatId);
    
    // ==================== MESSAGE OPERATIONS ====================
    
    /**
     * Sends a message from one user to another.
     * Validates sender and receiver exist, checks blocking status,
     * and applies appropriate delivery strategy based on receiver status.
     * 
     * @param senderId The ID of the user sending the message
     * @param receiverId The ID of the user receiving the message
     * @param content The message content (cannot be null or empty)
     * @return The created Message object with delivery status
     * @throws IllegalArgumentException if parameters are invalid or users are blocked
     */
    Message sendMessage(String senderId, String receiverId, String content);
    
    /**
     * Retrieves a message by its unique ID.
     * 
     * @param messageId The unique ID of the message to retrieve
     * @return The Message object if found, null otherwise
     */
    Message getMessage(String messageId);
    
    /**
     * Retrieves all messages exchanged between two users.
     * Messages are returned in chronological order (oldest first).
     * 
     * @param user1Id The ID of the first user
     * @param user2Id The ID of the second user
     * @return List of messages between the users, empty list if none found
     * @throws IllegalArgumentException if user IDs are null
     */
    List<Message> getMessagesBetween(String user1Id, String user2Id);
    
    /**
     * Marks a message as read by the specified user.
     * Only the intended recipient can mark a message as read.
     * 
     * @param messageId The ID of the message to mark as read
     * @param userId The ID of the user marking the message as read
     * @return true if message was marked as read, false if not authorized or message not found
     */
    boolean markMessageAsRead(String messageId, String userId);
    
    // ==================== GROUP MANAGEMENT OPERATIONS ====================
    
    /**
     * Adds admin privileges to a user in a group chat.
     * The user must already be a participant in the group.
     * 
     * @param groupChatId The ID of the group chat
     * @param adminId The ID of the user to make admin
     * @return true if admin was added successfully, false otherwise
     */
    boolean addAdminToGroup(String groupChatId, String adminId);
    
    /**
     * Removes a user from a group chat.
     * If the user was an admin, their admin privileges are also removed.
     * 
     * @param groupChatId The ID of the group chat
     * @param userId The ID of the user to remove
     * @return true if user was removed successfully, false otherwise
     */
    boolean removeUserFromGroup(String groupChatId, String userId);
}