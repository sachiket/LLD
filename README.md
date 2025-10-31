# 🚀 Low Level Design (LLD)

[![Java](https://img.shields.io/badge/Java-8%2B-orange.svg)](https://www.oracle.com/java/)
[![Design Patterns](https://img.shields.io/badge/Design%20Patterns-Gang%20of%20Four-blue.svg)](https://en.wikipedia.org/wiki/Design_Patterns)
[![SOLID Principles](https://img.shields.io/badge/SOLID-Principles-green.svg)](https://en.wikipedia.org/wiki/SOLID)
[![Interview Ready](https://img.shields.io/badge/Interview-Ready-red.svg)](https://github.com/sachiket/LLD)

## 📋 Table of Contents
1. [Repository Overview](#repository-overview)
2. [Projects Included](#projects-included)
3. [Design Patterns Demonstrated](#design-patterns-demonstrated)
4. [SOLID Principles Implementation](#solid-principles-implementation)
5. [Quick Start Guide](#quick-start-guide)
6. [Interview Preparation](#interview-preparation)
7. [Project Structure](#project-structure)
8. [How to Use This Repository](#how-to-use-this-repository)
9. [Contributing](#contributing)

---

## 🏗️ Repository Overview

This repository contains **10 comprehensive Low Level Design (LLD) projects** implemented in Java, specifically designed for **FAANG technical interviews**. Each project demonstrates multiple design patterns, OOP principles, and best practices commonly asked in system design interviews.

### 🎯 **Purpose**
- **Interview Preparation**: Ready-to-use implementations for 45-90 minute technical interviews
- **Design Pattern Mastery**: Practical examples of Gang of Four design patterns
- **SOLID Principles**: Real-world application of all five SOLID principles
- **Clean Code**: Production-ready code with proper documentation
- **Scalability**: Extensible architectures ready for follow-up questions

### 🏆 **Key Highlights**
- ✅ **10 Complete Systems** with full documentation
- ✅ **20+ Design Patterns** implemented across projects
- ✅ **SOLID Principles** demonstrated in every project
- ✅ **Thread-Safe Operations** where applicable
- ✅ **Comprehensive Documentation** with interview Q&A
- ✅ **Step-by-Step Guides** for implementation
- ✅ **Extension Points** for advanced discussions

---

## 📚 Projects Included

### 1. 🎬 **BookMyShow System**
**Location**: [`BookMyShow/`](BookMyShow/)

**Core Features**:
- Movie booking and seat management
- Multi-theater, multi-screen support
- Dynamic pricing strategies
- Real-time notifications

**Design Patterns**: Strategy, Factory, Observer, Singleton  
**Key Concepts**: Booking lifecycle, seat allocation, pricing algorithms  
**Interview Focus**: Concurrency, scalability, payment integration

---

### 2. 🍕 **Food Delivery Service**
**Location**: [`FoodDeliveryService/`](FoodDeliveryService/)

**Core Features**:
- Multi-user system (Customer, Restaurant Owner, Delivery Partner)
- Restaurant and menu management
- Complete order lifecycle management
- Smart delivery partner assignment
- Location-based services

**Design Patterns**: Strategy, Singleton, Factory, Service Layer
**Key Concepts**: Order processing, delivery assignment, location services, user management
**Interview Focus**: Multi-role systems, location algorithms, order state management

---

### 3. 💬 **Messaging Service**
**Location**: [`MessagingService/`](MessagingService/)

**Core Features**:
- WhatsApp-like messaging system
- Private and group chat functionality
- Message delivery strategies
- User status management

**Design Patterns**: Strategy, Factory, Observer, Singleton  
**Key Concepts**: Message delivery, group management, real-time communication  
**Interview Focus**: Real-time systems, message queuing, scalability

---

### 4. 🚗 **Parking Lot Service**
**Location**: [`ParkingLotService/`](ParkingLotService/)

**Core Features**:
- Multi-vehicle parking system
- Dynamic fee calculation
- Real-time slot monitoring
- Multi-floor architecture

**Design Patterns**: Strategy, Factory, Observer, Singleton  
**Key Concepts**: Slot allocation, fee calculation, capacity management  
**Interview Focus**: Optimization algorithms, concurrent access, payment processing

---

### 5. 🏢 **Elevator Control System**
**Location**: [`ElevatorControlSystem/`](ElevatorControlSystem/)

**Core Features**:
- Multi-elevator management
- Intelligent scheduling algorithms
- Capacity management
- Thread-safe operations

**Design Patterns**: Strategy, Factory, Singleton, Service Layer  
**Key Concepts**: Elevator scheduling, movement optimization, request handling  
**Interview Focus**: Algorithm optimization, concurrency, system efficiency

---

### 6. 📋 **Task Scheduler**
**Location**: [`TaskScheduler/`](TaskScheduler/)

**Core Features**:
- Complete task lifecycle management
- Priority-based scheduling
- User assignment and tracking
- Overdue detection

**Design Patterns**: Factory, Observer, Singleton, Service Layer  
**Key Concepts**: Task management, priority queues, notification systems  
**Interview Focus**: Scheduling algorithms, dependency management, scalability

---

### 7. 🚕 **Ride Booking Service**
**Location**: [`RideBookingService/`](RideBookingService/)

**Core Features**:
- Uber-like ride booking system
- Driver-rider matching
- Dynamic pricing strategies
- Location-based services

**Design Patterns**: Strategy, Service Layer  
**Key Concepts**: Geolocation, matching algorithms, pricing strategies  
**Interview Focus**: Real-time matching, location services, surge pricing

---

### 8. ⚡ **Rate Limiter Service**
**Location**: [`RateLimiterService/`](RateLimiterService/)

**Core Features**:
- Token bucket algorithm
- Request rate limiting
- Configurable limits
- Thread-safe operations

**Design Patterns**: Service Layer  
**Key Concepts**: Rate limiting, token bucket, API protection  
**Interview Focus**: System protection, algorithm implementation, performance

---

### 9. 💰 **Splitwise**
**Location**: [`Splitwise/`](Splitwise/)

**Core Features**:
- Expense splitting system
- Multiple split strategies
- Debt calculation and settlement
- User management

**Design Patterns**: Strategy, Service Layer  
**Key Concepts**: Expense management, debt calculation, settlement algorithms  
**Interview Focus**: Financial calculations, algorithm optimization, data consistency

### 10. 🎥 **Video Streaming Service**
**Location**: [`VideoStreamingService/`](VideoStreamingService/)

**Core Features**:
- YouTube-like video streaming platform
- Multi-role user system (Viewer, Creator, Admin)
- Channel creation and management
- Content upload (Video, Audio, Image) with visibility controls
- Subscription and engagement system
- Search and discovery algorithms
- Real-time analytics and trending content

**Design Patterns**: Factory, Singleton, Strategy, Service Layer
**Key Concepts**: Content management, search algorithms, engagement tracking, analytics
**Interview Focus**: Content discovery, recommendation systems, scalability, real-time features

---

## 🎯 Design Patterns Demonstrated

### **Creational Patterns**
| Pattern | Projects | Use Case |
|---------|----------|----------|
| **Factory** | BookMyShow, FoodDeliveryService, MessagingService, ParkingLot, ElevatorControl, TaskScheduler, VideoStreaming | Object creation with consistent initialization |
| **Singleton** | BookMyShow, FoodDeliveryService, MessagingService, ParkingLot, ElevatorControl, TaskScheduler, VideoStreaming | Global state management and thread safety |

### **Behavioral Patterns**
| Pattern | Projects | Use Case |
|---------|----------|----------|
| **Strategy** | BookMyShow, FoodDeliveryService, MessagingService, ParkingLot, ElevatorControl, RideBooking, Splitwise, VideoStreaming | Algorithm encapsulation and interchangeability |
| **Observer** | BookMyShow, MessagingService, ParkingLot, TaskScheduler | Event-driven notifications and loose coupling |

### **Architectural Patterns**
| Pattern | Projects | Use Case |
|---------|----------|----------|
| **Service Layer** | All Projects | Business logic encapsulation and clean API |
| **Repository** | Mentioned in extensions | Data access abstraction |

---

## ⚖️ SOLID Principles Implementation

### **Single Responsibility Principle (SRP)** ✅
- Each class has **one reason to change**
- Clear separation of concerns across all projects
- Example: `Seat` manages seat state, `PricingStrategy` calculates prices

### **Open/Closed Principle (OCP)** ✅
- **Open for extension, closed for modification**
- Strategy pattern enables adding new algorithms without changing existing code
- Example: Adding new pricing strategies, scheduling algorithms

### **Liskov Substitution Principle (LSP)** ✅
- **Subtypes are substitutable for their base types**
- All strategy implementations are interchangeable
- Example: Any `PricingStrategy` can replace another

### **Interface Segregation Principle (ISP)** ✅
- **Clients depend only on interfaces they use**
- Focused, specific interfaces throughout all projects
- Example: `BookingEventObserver` only has booking-related methods

### **Dependency Inversion Principle (DIP)** ✅
- **Depend on abstractions, not concretions**
- High-level modules depend on interfaces
- Example: Services depend on strategy interfaces, not concrete implementations

---

## 🚀 Quick Start Guide

### **Prerequisites**
- **Java 8+** installed
- **IDE** (IntelliJ IDEA, Eclipse, VS Code)
- **Git** for version control

### **Clone Repository**
```bash
git clone https://github.com/sachiket/LLD.git
cd LLD
```

### **Run Any Project**
```bash
# Navigate to project directory
cd BookMyShow

# Compile Java files
javac -d out src/**/*.java

# Run the main class
java -cp out Main
```

### **Project Structure**
Each project follows a consistent structure:
```
ProjectName/
├── src/
│   ├── enums/          # Type-safe constants
│   ├── models/         # Core domain entities
│   ├── services/       # Business logic layer
│   ├── strategies/     # Strategy pattern implementations
│   ├── factories/      # Factory pattern implementations
│   ├── observers/      # Observer pattern implementations
│   ├── managers/       # Singleton managers
│   └── Main.java       # Demo application
├── README.md           # Comprehensive project guide
├── INTERVIEW_STRATEGY.md    # Interview preparation
├── SIMPLE_START_GUIDE.md    # Step-by-step implementation
├── INTERVIEW_CHEAT_SHEET.md # Quick reference
├── FLOW_DIAGRAMS.md         # Visual architecture
└── AGENTS.md               # AI-powered preparation
```

---

## 🎤 Interview Preparation

### **Time-Based Project Selection**

#### **45-60 Minutes** ⏰
- **ElevatorControlSystem**: Core scheduling algorithms
- **ParkingLotService**: Slot allocation and fee calculation
- **TaskScheduler**: Priority management and notifications

#### **60-90 Minutes** ⏰
- **BookMyShow**: Complete booking system with multiple patterns
- **MessagingService**: Real-time communication system
- **RideBookingService**: Location-based matching system
- **VideoStreamingService**: YouTube-like platform with content management

#### **30-45 Minutes** ⏰
- **RateLimiterService**: Algorithm-focused implementation
- **Splitwise**: Financial calculation system

### **Common Interview Questions Covered**

#### **System Design Questions**
- "Design a movie booking system like BookMyShow"
- "Design a food delivery system like Swiggy/Zomato"
- "Design a messaging system like WhatsApp"
- "Design a parking lot management system"
- "Design an elevator control system"
- "Design a task scheduling system"
- "Design a ride-sharing system like Uber"
- "Design a video streaming platform like YouTube"

#### **Algorithm Questions**
- "Implement rate limiting using token bucket"
- "Design expense splitting algorithms"
- "Implement elevator scheduling algorithms"
- "Design seat allocation algorithms"

### **What Interviewers Look For**
1. **Design Pattern Knowledge**: Practical application of patterns
2. **SOLID Principles**: Real-world implementation
3. **Scalability Thinking**: How to extend and scale systems
4. **Code Quality**: Clean, readable, maintainable code
5. **Problem-Solving**: Handling edge cases and constraints

---

## 📁 Project Structure

```
LLD/
├── BookMyShow/                 # Movie booking system
├── FoodDeliveryService/        # Food delivery system
├── MessagingService/           # WhatsApp-like messaging
├── ParkingLotService/          # Parking management system
├── ElevatorControlSystem/      # Elevator control system
├── TaskScheduler/              # Task management system
├── RideBookingService/         # Uber-like ride booking
├── RateLimiterService/         # API rate limiting
├── Splitwise/                  # Expense splitting system
├── VideoStreamingService/      # YouTube-like video platform
├── README.md                   # This file
└── .gitignore                  # Git ignore rules
```

---

## 🎯 How to Use This Repository

### **For Interview Preparation**
1. **Choose a project** based on your interview timeline
2. **Study the README** for comprehensive understanding
3. **Review design patterns** and SOLID principles implementation
4. **Practice explaining** the architecture and design decisions
5. **Prepare for follow-up questions** using the Q&A sections

### **For Learning Design Patterns**
1. **Start with simpler projects** (RateLimiter, Splitwise)
2. **Progress to complex systems** (BookMyShow, MessagingService)
3. **Compare implementations** across different projects
4. **Practice extending** systems with new features

### **For System Design Practice**
1. **Implement from scratch** using the step-by-step guides
2. **Modify existing implementations** to add new features
3. **Practice explaining** trade-offs and design decisions
4. **Prepare for scaling discussions** using extension points

---

## 🔧 Extension Ideas

### **Database Integration**
- Add Repository pattern with JPA/Hibernate
- Implement data persistence across all projects
- Add database migration scripts

### **REST API Development**
- Add Spring Boot controllers
- Implement RESTful endpoints
- Add API documentation with Swagger

### **Real-time Features**
- Add WebSocket support for live updates
- Implement real-time notifications
- Add event streaming with Kafka

### **Microservices Architecture**
- Break down monoliths into microservices
- Add service discovery and load balancing
- Implement distributed system patterns

---

## 🤝 Contributing

### **How to Contribute**
1. **Fork** the repository
2. **Create** a feature branch (`git checkout -b feature/new-project`)
3. **Commit** your changes (`git commit -am 'Add new LLD project'`)
4. **Push** to the branch (`git push origin feature/new-project`)
5. **Create** a Pull Request

### **Contribution Guidelines**
- Follow the existing project structure
- Include comprehensive documentation
- Add interview Q&A sections
- Implement multiple design patterns
- Ensure SOLID principles compliance

---

*Last Updated: October 2025*  
*Repository Maintained by: [Sachiket Behera](https://github.com/sachiket)*