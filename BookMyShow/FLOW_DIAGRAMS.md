# 🔄 BookMyShow System Flow Diagrams

## 📋 Quick Navigation
- [System Architecture Flow](#system-architecture-flow)
- [Booking Process Flow](#booking-process-flow)
- [Design Patterns Flow](#design-patterns-flow)
- [Data Flow Diagrams](#data-flow-diagrams)
- [Error Handling Flow](#error-handling-flow)

---

## 🏗️ System Architecture Flow

```mermaid
graph TD
    A[User] --> B[BookMyShowService]
    B --> C[Movie Management]
    B --> D[Theater Management]
    B --> E[Booking Management]
    
    C --> F[Movie Repository]
    D --> G[Theater Repository]
    E --> H[Booking Repository]
    
    E --> I[PricingStrategy]
    E --> J[BookingFactory]
    E --> K[BookingObserver]
    
    L[TheaterManager] --> D
    
    subgraph "Core Models"
        M[Movie]
        N[Theater]
        O[Screen]
        P[Seat]
        Q[Show]
        R[Booking]
    end
```

---

## 🎫 Booking Process Flow

### **Happy Path Flow:**
```
1. User Views Movies
   ↓
2. User Selects Movie
   ↓
3. System Shows Available Shows
   ↓
4. User Selects Show
   ↓
5. System Displays Seat Layout
   ↓
6. User Selects Seats
   ↓
7. System Validates Seat Availability
   ↓
8. System Calculates Price (Strategy Pattern)
   ↓
9. System Creates Booking (Factory Pattern)
   ↓
10. System Notifies Observers (Observer Pattern)
    ↓
11. User Confirms Booking
    ↓
12. System Updates Seat Status
    ↓
13. System Sends Confirmation
```

### **Detailed Booking Flow:**
```mermaid
sequenceDiagram
    participant U as User
    participant S as BookMyShowService
    participant PS as PricingStrategy
    participant BF as BookingFactory
    participant O as Observer
    participant Seat as Seat

    U->>S: createBooking(userId, showId, seatIds)
    S->>S: findShowById(showId)
    S->>S: validateSeats(show, seatIds)
    S->>Seat: isAvailable()
    Seat-->>S: true/false
    S->>PS: calculatePrice(show, seats)
    PS-->>S: totalAmount
    S->>BF: createBooking(userId, show, seats, amount)
    BF-->>S: booking
    S->>O: onBookingCreated(booking)
    S-->>U: booking

    U->>S: confirmBooking(bookingId)
    S->>Seat: book()
    S->>O: onBookingConfirmed(booking)
    S-->>U: success
```

---

## 🎯 Design Patterns Flow

### **Strategy Pattern (Pricing):**
```mermaid
graph LR
    A[BookMyShowService] --> B[PricingStrategy Interface]
    B --> C[StandardPricingStrategy]
    B --> D[WeekendPricingStrategy]
    B --> E[HolidayPricingStrategy]
    
    C --> F[Calculate: Base Price × Seat Multiplier]
    D --> G[Calculate: Base Price × 1.5 × Seat Multiplier]
    E --> H[Calculate: Base Price × 2.0 × Seat Multiplier]
```

### **Factory Pattern (Booking Creation):**
```mermaid
graph TD
    A[Service Layer] --> B[BookingFactory]
    B --> C[Generate Booking ID]
    B --> D[Create Booking Object]
    B --> E[Set Initial Status]
    B --> F[Return Booking]
    
    C --> G[BMS-XXXXXXXX]
    D --> H[new Booking(...)]
    E --> I[Status: PENDING]
```

### **Observer Pattern (Notifications):**
```mermaid
graph TD
    A[Booking Event] --> B[BookMyShowService]
    B --> C[Notify All Observers]
    
    C --> D[ConsoleNotificationObserver]
    C --> E[EmailNotificationObserver]
    C --> F[SMSNotificationObserver]
    
    D --> G[Print to Console]
    E --> H[Send Email]
    F --> I[Send SMS]
```

### **Singleton Pattern (Theater Manager):**
```mermaid
graph TD
    A[Multiple Clients] --> B[TheaterManager.getInstance()]
    B --> C{Instance Exists?}
    C -->|No| D[Create New Instance]
    C -->|Yes| E[Return Existing Instance]
    D --> F[Single TheaterManager Instance]
    E --> F
```

---

## 📊 Data Flow Diagrams

### **Level 0: Context Diagram**
```mermaid
graph TD
    A[User] --> B[BookMyShow System]
    B --> A
    C[Admin] --> B
    B --> C
    D[Payment Gateway] --> B
    B --> D
```

### **Level 1: System Breakdown**
```mermaid
graph TD
    A[User Input] --> B[Movie Management]
    A --> C[Show Management]
    A --> D[Booking Management]
    
    B --> E[Movie Database]
    C --> F[Show Database]
    D --> G[Booking Database]
    
    D --> H[Pricing Engine]
    D --> I[Notification System]
    D --> J[Seat Management]
```

### **Level 2: Booking Process Detail**
```mermaid
graph TD
    A[Seat Selection] --> B[Validate Availability]
    B --> C[Calculate Price]
    C --> D[Create Booking]
    D --> E[Update Seat Status]
    E --> F[Send Notifications]
    
    B --> G[Seat Repository]
    C --> H[Pricing Strategy]
    D --> I[Booking Factory]
    F --> J[Observer Pattern]
```

---

## ⚠️ Error Handling Flow

### **Seat Booking Error Flow:**
```mermaid
graph TD
    A[User Selects Seats] --> B{Seats Available?}
    B -->|Yes| C[Proceed with Booking]
    B -->|No| D[Show Error Message]
    
    C --> E{Payment Successful?}
    E -->|Yes| F[Confirm Booking]
    E -->|No| G[Release Seats]
    
    F --> H[Send Confirmation]
    G --> I[Show Payment Error]
    D --> J[Suggest Alternative Seats]
```

### **Concurrent Booking Handling:**
```mermaid
sequenceDiagram
    participant U1 as User 1
    participant U2 as User 2
    participant S as Service
    participant Seat as Seat

    U1->>S: selectSeat(seatId)
    U2->>S: selectSeat(seatId)
    
    S->>Seat: checkAvailability()
    Seat-->>S: available
    
    S->>Seat: reserveSeat(U1)
    Seat-->>S: reserved for U1
    
    S->>Seat: reserveSeat(U2)
    Seat-->>S: already reserved
    
    S-->>U1: seat reserved
    S-->>U2: seat not available
```

---

## 🔄 State Transition Diagrams

### **Seat State Transitions:**
```mermaid
stateDiagram-v2
    [*] --> Available
    Available --> Reserved : User selects seat
    Reserved --> Available : Reservation timeout
    Reserved --> Booked : Payment confirmed
    Booked --> Available : Booking cancelled
    Available --> Maintenance : Admin action
    Maintenance --> Available : Maintenance complete
```

### **Booking State Transitions:**
```mermaid
stateDiagram-v2
    [*] --> Pending
    Pending --> Confirmed : Payment successful
    Pending --> Expired : Timeout reached
    Pending --> Cancelled : User cancellation
    Confirmed --> Cancelled : Cancellation allowed
    Expired --> [*]
    Cancelled --> [*]
```

---

## 🎬 Show Management Flow

### **Show Creation Process:**
```mermaid
graph TD
    A[Admin Creates Show] --> B[Select Movie]
    B --> C[Select Theater & Screen]
    C --> D[Set Show Time]
    D --> E[Set Base Price]
    E --> F[Validate No Conflicts]
    F --> G{Conflicts Found?}
    G -->|Yes| H[Show Error & Retry]
    G -->|No| I[Create Show]
    I --> J[Notify System]
    H --> C
```

### **Show Availability Check:**
```mermaid
graph TD
    A[User Requests Shows] --> B[Filter by Movie]
    B --> C[Filter by Date/Time]
    C --> D[Check Seat Availability]
    D --> E[Calculate Pricing]
    E --> F[Return Show List]
    
    D --> G{Any Seats Available?}
    G -->|No| H[Mark as Sold Out]
    G -->|Yes| I[Show Available Seats Count]
```

---

## 🏢 Multi-Theater Architecture

### **Theater Management Flow:**
```mermaid
graph TD
    A[TheaterManager Singleton] --> B[Theater 1]
    A --> C[Theater 2]
    A --> D[Theater N]
    
    B --> E[Screen 1A]
    B --> F[Screen 1B]
    
    C --> G[Screen 2A]
    C --> H[Screen 2B]
    
    E --> I[Seats 1A]
    F --> J[Seats 1B]
    G --> K[Seats 2A]
    H --> L[Seats 2B]
```

### **Cross-Theater Show Search:**
```mermaid
sequenceDiagram
    participant U as User
    participant S as Service
    participant TM as TheaterManager
    participant T1 as Theater1
    participant T2 as Theater2

    U->>S: searchShows(movieId, city)
    S->>TM: getAllTheaters(city)
    TM-->>S: [Theater1, Theater2]
    
    S->>T1: getShowsForMovie(movieId)
    T1-->>S: [Show1, Show2]
    
    S->>T2: getShowsForMovie(movieId)
    T2-->>S: [Show3, Show4]
    
    S-->>U: [Show1, Show2, Show3, Show4]
```

---

## 📱 Integration Points

### **External System Integration:**
```mermaid
graph TD
    A[BookMyShow Core] --> B[Payment Gateway]
    A --> C[Email Service]
    A --> D[SMS Service]
    A --> E[Analytics Service]
    
    B --> F[Credit Card Processing]
    B --> G[Digital Wallet]
    
    C --> H[SMTP Server]
    D --> I[SMS Gateway]
    E --> J[Data Warehouse]
```

**These diagrams provide a comprehensive visual understanding of the BookMyShow system architecture and flows, perfect for interview discussions!**