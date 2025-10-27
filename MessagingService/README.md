# Enhanced Messaging System - Low Level Design

## Overview

A comprehensive WhatsApp-like messaging system demonstrating advanced software engineering principles, design patterns, and SOLID principles. Built for FAANG-level technical interviews with step-by-step development approach.

## Key Features

- **Real-time Messaging**: Private and group chat functionality
- **User Management**: Online/offline status, contact management, user blocking
- **Message Delivery**: Strategy-based delivery with instant and queued mechanisms
- **Group Administration**: Admin controls, member management
- **Message Status Tracking**: Sent, delivered, read status with timestamps
- **Design Patterns**: Strategy, Factory, Observer, Singleton patterns implemented
- **SOLID Principles**: All five principles demonstrated throughout the codebase

## Architecture Overview

```
MessagingService/
├── src/
│   ├── enums/           # Type-safe constants
│   ├── models/          # Core domain entities
│   ├── strategies/      # Strategy pattern implementations
│   ├── factories/       # Factory pattern for object creation
│   ├── observers/       # Observer pattern for notifications
│   ├── managers/        # Singleton system manager
│   ├── services/        # Service layer with business logic
│   └── Main.java        # Demo application
└── docs/                # Comprehensive documentation
```

## Design Patterns Implemented

### 1. Strategy Pattern
- **MessageDeliveryStrategy**: Different delivery mechanisms
- **InstantDeliveryStrategy**: For online users
- **QueuedDeliveryStrategy**: For offline users

### 2. Factory Pattern
- **ChatFactory**: Creates different types of chats (Private/Group)

### 3. Observer Pattern
- **MessageEventObserver**: Notification system for message events
- **ConsoleNotificationObserver**: Console-based notifications

### 4. Singleton Pattern
- **MessagingSystemManager**: Centralized system management

## SOLID Principles Demonstrated

### Single Responsibility Principle (SRP)
- Each class has one reason to change
- `User` manages user data, `Message` handles message data
- Separate services for different concerns

### Open/Closed Principle (OCP)
- Strategy pattern allows adding new delivery mechanisms
- Observer pattern enables new notification types
- Factory pattern supports new chat types

### Liskov Substitution Principle (LSP)
- `GroupChat` extends `Chat` without breaking functionality
- All strategy implementations are interchangeable

### Interface Segregation Principle (ISP)
- Focused interfaces like `MessageDeliveryStrategy`
- Clients depend only on methods they use

### Dependency Inversion Principle (DIP)
- High-level modules depend on abstractions
- Service implementations depend on interfaces

## Core Models

### User
- Unique ID, name, email, status management
- Contact list and blocking functionality
- Business logic for status changes

### Message
- Immutable message data with status tracking
- Delivery and read timestamp management
- Type-safe message types (TEXT, IMAGE, FILE)

### Chat & GroupChat
- Participant management and message history
- Group-specific features (admins, group name)
- Inheritance demonstrating LSP

## Quick Start

```java
// Initialize system
MessagingService service = new MessagingServiceImpl();

// Create users
User alice = service.createUser("Alice", "alice@example.com");
User bob = service.createUser("Bob", "bob@example.com");

// Create private chat
Chat chat = service.createPrivateChat(alice.getId(), bob.getId());

// Send message
Message msg = service.sendMessage(alice.getId(), bob.getId(), "Hello!");

// Create group chat
GroupChat group = service.createGroupChat("Team", alice.getId());
service.addUserToChat(group.getId(), bob.getId());
```

## Interview Readiness

This implementation demonstrates:
- **System Design**: Scalable architecture with clear separation of concerns
- **Design Patterns**: Multiple patterns working together cohesively
- **SOLID Principles**: All principles applied practically
- **Error Handling**: Comprehensive validation and exception handling
- **Testing Strategy**: Easily testable with dependency injection
- **Scalability**: Ready for distributed system extensions

## Time Complexity

- **Send Message**: O(1) - Direct hash map operations
- **Get Messages**: O(n log n) - Sorting by timestamp
- **User Operations**: O(1) - Hash map lookups
- **Group Operations**: O(m) - Where m is number of participants

## Space Complexity

- **Users**: O(n) - Where n is number of users
- **Messages**: O(m) - Where m is total messages
- **Chats**: O(c) - Where c is number of chats

## Extension Points

- **Database Integration**: Replace in-memory storage
- **WebSocket Support**: Real-time message delivery
- **Message Encryption**: End-to-end encryption
- **File Sharing**: Multimedia message support
- **Push Notifications**: Mobile notification system

## Built With

- **Java 8+**: Core language features
- **Design Patterns**: Gang of Four patterns
- **SOLID Principles**: Clean architecture principles
- **Concurrent Collections**: Thread-safe operations

---

*This implementation showcases production-ready code suitable for FAANG company technical interviews, demonstrating both theoretical knowledge and practical application of software engineering principles.*
