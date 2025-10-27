package enums;

/**
 * Enum representing the type of content in a message.
 * 
 * This enum defines the different types of content that can be sent in messages:
 * - TEXT: Plain text messages
 * - IMAGE: Image attachments
 * - FILE: File attachments
 * 
 * Design Pattern: Type-safe enumeration pattern
 * SOLID Principle: Open/Closed - Easy to extend with new message types (VIDEO, AUDIO, etc.)
 * 
 * @author MessagingSystem
 * @version 1.0
 */
public enum MessageType {
    /** 
     * Plain text message content.
     * Most common type of message in the system.
     */
    TEXT,
    
    /** 
     * Image attachment message.
     * Contains image data or reference to image file.
     */
    IMAGE,
    
    /** 
     * File attachment message.
     * Contains file data or reference to uploaded file.
     */
    FILE
}