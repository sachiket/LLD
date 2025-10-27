package factories;

import enums.ChatType;
import models.Chat;
import models.GroupChat;

/**
 * Factory pattern for creating different types of chats
 * Demonstrates Factory pattern and encapsulates object creation logic
 */
public class ChatFactory {
    
    public static Chat createChat(ChatType type, String groupName) {
        switch (type) {
            case PRIVATE:
                return new Chat(ChatType.PRIVATE);
            case GROUP:
                if (groupName == null || groupName.trim().isEmpty()) {
                    throw new IllegalArgumentException("Group name is required for group chats");
                }
                return new GroupChat(groupName);
            default:
                throw new IllegalArgumentException("Unsupported chat type: " + type);
        }
    }
    
    public static Chat createPrivateChat() {
        return createChat(ChatType.PRIVATE, null);
    }
    
    public static GroupChat createGroupChat(String groupName) {
        return (GroupChat) createChat(ChatType.GROUP, groupName);
    }
}