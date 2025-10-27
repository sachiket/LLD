package enums;

/**
 * Enum representing the type of chat conversation in the messaging system.
 * 
 * This enum defines the different types of conversations that can exist:
 * - PRIVATE: One-on-one conversation between two users
 * - GROUP: Multi-participant conversation with admin controls
 * 
 * Design Pattern: Type-safe enumeration pattern
 * SOLID Principle: Open/Closed - Easy to extend with new chat types
 * 
 * @author MessagingSystem
 * @version 1.0
 */
public enum ChatType {
    /** 
     * Private chat between exactly two participants.
     * No admin controls, both participants have equal rights.
     */
    PRIVATE,
    
    /** 
     * Group chat with multiple participants.
     * Has admin controls and group-specific features.
     */
    GROUP
}
