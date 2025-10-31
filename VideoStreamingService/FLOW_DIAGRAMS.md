# 🔄 Video Streaming Service - Flow Diagrams

## 📋 **System Flow Overview**

This document provides visual representations of key flows in the Video Streaming Service system.

---

## 🎯 **1. User Registration & Channel Creation Flow**

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   User Visits   │───▶│  Register User  │───▶│   User Created  │
│    Platform     │    │   (VIEWER)      │    │   (VIEWER)      │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                                        │
                                                        ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Channel       │◀───│  Create First   │◀───│   User Wants    │
│   Created       │    │    Channel      │    │  to Upload      │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │
         ▼                       ▼
┌─────────────────┐    ┌─────────────────┐
│  User Role      │    │   Channel       │
│ VIEWER→CREATOR  │    │  Registered     │
└─────────────────┘    └─────────────────┘
```

---

## 🎥 **2. Content Upload Flow**

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Creator       │───▶│   Validate      │───▶│   Content       │
│  Uploads File   │    │   Ownership     │    │   Factory       │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                │                       │
                                ▼                       ▼
                       ┌─────────────────┐    ┌─────────────────┐
                       │   Check User    │    │   Generate      │
                       │   is Channel    │    │   Content ID    │
                       │     Owner       │    │   & Metadata    │
                       └─────────────────┘    └─────────────────┘
                                                        │
                                                        ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Update        │◀───│   Register      │◀───│   Initialize    │
│  Channel Stats  │    │   Content       │    │   Defaults      │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

---

## 🔍 **3. Content Discovery Flow**

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   User Enters   │───▶│   Search        │───▶│   Filter        │
│    Keyword      │    │   Strategy      │    │   Public        │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                │                       │
                                ▼                       ▼
                       ┌─────────────────┐    ┌─────────────────┐
                       │   Basic Search  │    │   Match Title   │
                       │   Algorithm     │    │  & Description  │
                       └─────────────────┘    └─────────────────┘
                                                        │
                                                        ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Return        │◀───│   Sort by       │◀───│   Calculate     │
│   Results       │    │   Engagement    │    │   Relevance     │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

---

## 📺 **4. Home Page Generation Flow**

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   User Visits   │───▶│   Get User      │───▶│   Check         │
│   Home Page     │    │ Subscriptions   │    │ Subscriptions   │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                │                       │
                                ▼                       ▼
                       ┌─────────────────┐    ┌─────────────────┐
                       │   Has Subs?     │    │   No Subs?      │
                       │      YES        │    │      NO         │
                       └─────────────────┘    └─────────────────┘
                                │                       │
                                ▼                       ▼
                       ┌─────────────────┐    ┌─────────────────┐
                       │   Get Content   │    │   Show          │
                       │   from Subbed   │    │   Trending      │
                       │    Channels     │    │   Content       │
                       └─────────────────┘    └─────────────────┘
                                │                       │
                                ▼                       │
                       ┌─────────────────┐              │
                       │   Sort by       │              │
                       │  Creation Date  │              │
                       └─────────────────┘              │
                                │                       │
                                ▼                       ▼
                       ┌─────────────────────────────────────────┐
                       │         Return Home Page Content        │
                       └─────────────────────────────────────────┘
```

---

## 👥 **5. Subscription Flow**

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   User Clicks   │───▶│   Validate      │───▶│   Check Not     │
│   Subscribe     │    │   User &        │    │   Already       │
└─────────────────┘    │   Channel       │    │   Subscribed    │
                       └─────────────────┘    └─────────────────┘
                                │                       │
                                ▼                       ▼
                       ┌─────────────────┐    ┌─────────────────┐
                       │   Both Exist?   │    │   Not Already   │
                       │      YES        │    │   Subscribed?   │
                       └─────────────────┘    └─────────────────┘
                                │                       │
                                ▼                       ▼
                       ┌─────────────────┐    ┌─────────────────┐
                       │   Add Channel   │    │   Add User to   │
                       │   to User's     │    │   Channel's     │
                       │ Subscriptions   │    │  Subscribers    │
                       └─────────────────┘    └─────────────────┘
                                │                       │
                                ▼                       ▼
                       ┌─────────────────┐    ┌─────────────────┐
                       │   Update        │    │   Subscription  │
                       │  Channel Stats  │    │   Successful    │
                       └─────────────────┘    └─────────────────┘
```

---

## 💬 **6. Comment & Engagement Flow**

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   User Views    │───▶│   Increment     │───▶│   Update        │
│    Content      │    │   View Count    │    │ Channel Views   │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │
         ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   User Likes/   │───▶│   Update        │───▶│   Affect        │
│   Dislikes      │    │   Content       │    │   Trending      │
└─────────────────┘    │   Counters      │    │   Algorithm     │
         │              └─────────────────┘    └─────────────────┘
         ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   User Adds     │───▶│   Create        │───▶│   Add Comment   │
│    Comment      │    │   Comment       │    │   to Content    │
└─────────────────┘    │    Object       │    └─────────────────┘
                       └─────────────────┘
```

---

## 🏗️ **7. System Architecture Flow**

```
┌─────────────────────────────────────────────────────────────────┐
│                        CLIENT REQUEST                           │
└─────────────────────────────────────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│                     SERVICE LAYER                               │
│              VideoStreamingServiceImpl                          │
└─────────────────────────────────────────────────────────────────┘
                                │
                ┌───────────────┼───────────────┐
                ▼               ▼               ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   UserManager   │    │ ContentManager  │    │   Factories     │
│   (Singleton)   │    │   (Singleton)   │    │ (UserFactory,   │
└─────────────────┘    └─────────────────┘    │ ContentFactory) │
         │                       │              └─────────────────┘
         ▼                       ▼                       │
┌─────────────────┐    ┌─────────────────┐              ▼
│   User & Channel│    │   Content &     │    ┌─────────────────┐
│   Operations    │    │   Search Ops    │    │   Object        │
└─────────────────┘    └─────────────────┘    │   Creation      │
         │                       │              └─────────────────┘
         ▼                       ▼
┌─────────────────┐    ┌─────────────────┐
│   Data Models   │    │   Search        │
│ (User, Channel) │    │   Strategy      │
└─────────────────┘    └─────────────────┘
```

---

## 🔄 **8. Design Pattern Interactions**

```
┌─────────────────────────────────────────────────────────────────┐
│                      FACTORY PATTERN                            │
│   UserFactory.createUser() → ContentFactory.createContent()     │
└─────────────────────────────────────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│                    SINGLETON PATTERN                            │
│     UserManager.getInstance() ↔ ContentManager.getInstance()    │
└─────────────────────────────────────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│                    STRATEGY PATTERN                             │
│   SearchStrategy → BasicSearchStrategy (Pluggable Algorithms)   │
└─────────────────────────────────────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│                   SERVICE LAYER PATTERN                         │
│        VideoStreamingService (Interface + Implementation)       │
└─────────────────────────────────────────────────────────────────┘
```

---

## 📊 **9. Data Flow & State Management**

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   User Action   │───▶│   Service       │───▶│   Manager       │
│  (Subscribe)    │    │   Layer         │    │   Layer         │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                │                       │
                                ▼                       ▼
                       ┌─────────────────┐    ┌─────────────────┐
                       │   Validation    │    │   Business      │
                       │   & Routing     │    │   Logic         │
                       └─────────────────┘    └─────────────────┘
                                                        │
                                                        ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Response      │◀───│   Update        │◀───│   Model         │
│   to Client     │    │   Statistics    │    │   Updates       │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

---

## 🎯 **10. Trending Algorithm Flow**

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Get All       │───▶│   Filter        │───▶│   Calculate     │
│   Content       │    │   Public        │    │   Engagement    │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                │                       │
                                ▼                       ▼
                       ┌─────────────────┐    ┌─────────────────┐
                       │   Only Public   │    │   Score =       │
                       │   Visibility    │    │ Views + Likes   │
                       └─────────────────┘    │  - Dislikes     │
                                              └─────────────────┘
                                                        │
                                                        ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Return Top    │◀───│   Sort by       │◀───│   Rank All      │
│   10 Trending   │    │   Score DESC    │    │   Content       │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

---

## 🔧 **Key Design Decisions**

### **1. Singleton Managers**
- **Why**: Centralized state management
- **Benefit**: Single source of truth for data
- **Trade-off**: Potential bottleneck, but suitable for demo

### **2. Strategy Pattern for Search**
- **Why**: Multiple search algorithms possible
- **Benefit**: Easy to extend with ML-based search
- **Trade-off**: Slight complexity increase

### **3. Factory Pattern for Creation**
- **Why**: Consistent object creation with validation
- **Benefit**: Centralized creation logic
- **Trade-off**: Additional abstraction layer

### **4. Service Layer**
- **Why**: Clean API separation
- **Benefit**: Easy to test and extend
- **Trade-off**: Additional layer of indirection

---

## 🚀 **Scalability Considerations**

### **Current State → Future State**

```
┌─────────────────┐         ┌─────────────────┐
│   Single App    │   →     │  Microservices  │
│   (Monolith)    │         │   Architecture  │
└─────────────────┘         └─────────────────┘
         │                           │
         ▼                           ▼
┌─────────────────┐         ┌─────────────────┐
│   In-Memory     │   →     │   Database      │
│   Storage       │         │   + Caching     │
└─────────────────┘         └─────────────────┘
         │                           │
         ▼                           ▼
┌─────────────────┐         ┌─────────────────┐
│   Basic Search  │   →     │   Elasticsearch │
│   Algorithm     │         │   + ML Ranking  │
└─────────────────┘         └─────────────────┘
```

This flow-based approach demonstrates the system's logical progression and makes it easy to explain during interviews!