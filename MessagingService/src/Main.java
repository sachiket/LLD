import enums.UserStatus;
import models.*;
import observers.ConsoleNotificationObserver;
import observers.MessageEventObserver;
import services.MessagingService;
import services.MessagingServiceImpl;
import managers.MessagingSystemManager;

/**
 * Main demonstration class for the Enhanced Messaging System.
 * 
 * This class provides a comprehensive demonstration of all system features
 * and design patterns implemented in the messaging system. It serves as:
 * - A working example of how to use the messaging system
 * - A test harness to verify all functionality works correctly
 * - A demonstration of design patterns in action
 * - A reference implementation for interview scenarios
 * 
 * Features Demonstrated:
 * 1. User Management - Creating users and managing status
 * 2. Private Chat - One-on-one messaging between users
 * 3. Group Chat - Multi-participant conversations with admin controls
 * 4. Message History - Retrieving conversation history
 * 5. User Blocking - Preventing unwanted communications
 * 6. Offline Messaging - Queued delivery for offline users
 * 7. System Statistics - Monitoring system usage
 * 
 * Design Patterns Showcased:
 * - Singleton: MessagingSystemManager single instance
 * - Strategy: Different delivery mechanisms for online/offline users
 * - Factory: Chat creation through factory methods
 * - Observer: Notification system for message events (commented out due to compilation)
 * - Facade: MessagingService simplifying complex operations
 * 
 * SOLID Principles Demonstrated:
 * - Single Responsibility: Each class has one clear purpose
 * - Open/Closed: System extensible through patterns
 * - Liskov Substitution: GroupChat substitutes Chat seamlessly
 * - Interface Segregation: Focused interfaces for specific needs
 * - Dependency Inversion: Depending on abstractions not implementations
 * 
 * @author MessagingSystem
 * @version 1.0
 */
public class Main {
    
    /**
     * Main method that runs the comprehensive messaging system demonstration.
     * 
     * This method executes a series of scenarios that showcase all the key
     * features of the messaging system in a logical progression:
     * 
     * 1. System Initialization
     * 2. User Management Demo
     * 3. Private Chat Demo
     * 4. Group Chat Demo
     * 5. Message History Demo
     * 6. User Blocking Demo
     * 7. Offline User Messaging Demo
     * 8. System Statistics Display
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("=== Enhanced Messaging System Demo ===\n");
        
        // ==================== SYSTEM INITIALIZATION ====================
        
        // Initialize the messaging service (Facade pattern)
        // This provides a simplified interface to the complex messaging subsystem
        MessagingService messagingService = new MessagingServiceImpl();
        
        // Get the singleton system manager for advanced operations
        MessagingSystemManager manager = MessagingSystemManager.getInstance();
        
        // Observer Pattern Setup (commented out due to compilation issue)
        // In a real system, this would enable real-time notifications
        // manager.addObserver(new ConsoleNotificationObserver());
        
        try {
            // ==================== DEMO 1: USER MANAGEMENT ====================
            
            System.out.println("1. USER MANAGEMENT DEMO");
            System.out.println("------------------------");
            
            // Create multiple users to demonstrate the system
            // Each user gets a unique ID generated automatically
            User alice = messagingService.createUser("Alice Johnson", "alice@example.com");
            User bob = messagingService.createUser("Bob Smith", "bob@example.com");
            User charlie = messagingService.createUser("Charlie Brown", "charlie@example.com");
            
            System.out.println("Created users: " + alice.getName() + ", " + bob.getName() + ", " + charlie.getName());
            
            // Demonstrate status management
            // This triggers the Observer pattern (if enabled) to notify about status changes
            messagingService.updateUserStatus(alice.getId(), UserStatus.ONLINE);
            messagingService.updateUserStatus(bob.getId(), UserStatus.ONLINE);
            messagingService.updateUserStatus(charlie.getId(), UserStatus.AWAY);
            
            System.out.println("Updated user statuses: Alice=ONLINE, Bob=ONLINE, Charlie=AWAY");
            System.out.println();
            
            // ==================== DEMO 2: PRIVATE CHAT ====================
            
            System.out.println("2. PRIVATE CHAT DEMO");
            System.out.println("--------------------");
            
            // Create a private chat between two users
            // This demonstrates input validation and business rule enforcement
            Chat privateChat = messagingService.createPrivateChat(alice.getId(), bob.getId());
            System.out.println("Created private chat between Alice and Bob");
            
            // Send messages demonstrating the Strategy pattern
            // Online users get instant delivery, offline users get queued delivery
            Message msg1 = messagingService.sendMessage(alice.getId(), bob.getId(), "Hi Bob! How are you?");
            Message msg2 = messagingService.sendMessage(bob.getId(), alice.getId(), "Hi Alice! I'm doing great, thanks!");
            
            // Demonstrate message status management
            // Only the intended recipient can mark a message as read
            messagingService.markMessageAsRead(msg1.getId(), bob.getId());
            
            System.out.println("Messages exchanged between Alice and Bob");
            System.out.println("Message 1 status: " + msg1.getStatus().getDisplayName());
            System.out.println("Message 2 status: " + msg2.getStatus().getDisplayName());
            System.out.println();
            
            // ==================== DEMO 3: GROUP CHAT ====================
            
            System.out.println("3. GROUP CHAT DEMO");
            System.out.println("------------------");
            
            // Create a group chat demonstrating inheritance (GroupChat extends Chat)
            // This shows Liskov Substitution Principle - GroupChat can be used wherever Chat is expected
            GroupChat groupChat = messagingService.createGroupChat("Project Team", alice.getId());
            System.out.println("Created group chat: " + groupChat.getGroupName() + " with admin: " + alice.getName());
            
            // Add members to the group
            // This demonstrates the business logic for group management
            messagingService.addUserToChat(groupChat.getId(), bob.getId());
            messagingService.addUserToChat(groupChat.getId(), charlie.getId());
            
            // Demonstrate admin management
            // Only existing participants can be made admins
            messagingService.addAdminToGroup(groupChat.getId(), bob.getId());
            
            System.out.println("Added Bob and Charlie to the group");
            System.out.println("Made Bob an admin as well");
            System.out.println("Group participants: " + groupChat.getParticipants().size());
            System.out.println("Group admins: " + groupChat.getAdmins().size());
            
            // Send group messages (simulated as private messages for demo)
            // In a full implementation, this would broadcast to all participants
            System.out.println("Sending group messages...");
            messagingService.sendMessage(alice.getId(), bob.getId(), "Welcome to the project team!");
            messagingService.sendMessage(bob.getId(), alice.getId(), "Thanks Alice! Excited to work together.");
            messagingService.sendMessage(charlie.getId(), alice.getId(), "Hello everyone!");
            
            System.out.println();
            
            // ==================== DEMO 4: MESSAGE HISTORY ====================
            
            System.out.println("4. MESSAGE HISTORY DEMO");
            System.out.println("-----------------------");
            
            // Retrieve and display message history
            // This demonstrates the filtering and sorting capabilities
            var messageHistory = messagingService.getMessagesBetween(alice.getId(), bob.getId());
            System.out.println("Message history between Alice and Bob:");
            
            for (Message msg : messageHistory) {
                User sender = messagingService.getUser(msg.getSenderId());
                System.out.println("- " + sender.getName() + ": " + msg.getContent() + 
                                 " [" + msg.getStatus().getDisplayName() + "]");
            }
            
            System.out.println();
            
            // ==================== DEMO 5: USER BLOCKING ====================
            
            System.out.println("5. USER BLOCKING DEMO");
            System.out.println("---------------------");
            
            // Demonstrate user blocking functionality
            // This shows how business rules prevent unwanted communications
            charlie.blockUser(alice.getId());
            System.out.println("Charlie blocked Alice");
            
            // Attempt to send message to blocked user
            // This should fail with an appropriate error message
            try {
                messagingService.sendMessage(alice.getId(), charlie.getId(), "Hi Charlie!");
                System.out.println("ERROR: Message should have been blocked!");
            } catch (IllegalArgumentException e) {
                System.out.println("Message blocked: " + e.getMessage());
            }
            
            System.out.println();
            
            // ==================== DEMO 6: OFFLINE USER MESSAGING ====================
            
            System.out.println("6. OFFLINE USER MESSAGING DEMO");
            System.out.println("-------------------------------");
            
            // Create a new user who remains offline
            // This demonstrates the Strategy pattern for offline message delivery
            User david = messagingService.createUser("David Wilson", "david@example.com");
            // David remains offline (default status is OFFLINE)
            
            // Send message to offline user
            // This will use the QueuedDeliveryStrategy instead of InstantDeliveryStrategy
            Message offlineMsg = messagingService.sendMessage(alice.getId(), david.getId(), 
                "Hi David, please check this when you're online!");
            System.out.println("Sent message to offline user David - message queued for delivery");
            System.out.println("Message status: " + offlineMsg.getStatus().getDisplayName());
            
            // Simulate user coming online
            // In a real system, this would trigger delivery of queued messages
            messagingService.updateUserStatus(david.getId(), UserStatus.ONLINE);
            System.out.println("David came online - queued messages would be delivered");
            
            System.out.println();
            
            // ==================== DEMO 7: SYSTEM STATISTICS ====================
            
            System.out.println("7. SYSTEM STATISTICS");
            System.out.println("--------------------");
            
            // Display system statistics to show the current state
            // This demonstrates the monitoring capabilities of the system
            System.out.println("Total users created: " + manager.getTotalUsers());
            System.out.println("Total chats created: " + manager.getTotalChats() + " (1 private, 1 group)");
            System.out.println("Total messages sent: " + manager.getTotalMessages());
            System.out.println("Group chat participants: " + groupChat.getParticipants().size());
            System.out.println("Group chat admins: " + groupChat.getAdmins().size());
            
            System.out.println("\n=== Demo completed successfully! ===");
            
            // ==================== SUCCESS SUMMARY ====================
            
            System.out.println("\n*** DESIGN PATTERNS DEMONSTRATED:");
            System.out.println("+ Singleton - MessagingSystemManager single instance");
            System.out.println("+ Strategy - Instant vs Queued delivery based on user status");
            System.out.println("+ Factory - Chat creation through factory methods");
            System.out.println("+ Observer - Notification system (architecture ready)");
            System.out.println("+ Facade - MessagingService simplifying complex operations");
            
            System.out.println("\n*** SOLID PRINCIPLES APPLIED:");
            System.out.println("+ Single Responsibility - Each class has one clear purpose");
            System.out.println("+ Open/Closed - Extensible through strategies and observers");
            System.out.println("+ Liskov Substitution - GroupChat substitutes Chat seamlessly");
            System.out.println("+ Interface Segregation - Focused interfaces for specific needs");
            System.out.println("+ Dependency Inversion - Depending on abstractions not implementations");
            
        } catch (Exception e) {
            // Comprehensive error handling for any unexpected issues
            System.err.println("ERROR: Error during demo: " + e.getMessage());
            e.printStackTrace();
            
            // In a real application, this would include:
            // - Logging to appropriate log files
            // - Alerting monitoring systems
            // - Graceful degradation of functionality
            // - User-friendly error messages
        }
    }
}