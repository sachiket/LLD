# 🔄 Parking Lot System - Flow Diagrams

## 📋 Table of Contents
1. [System Architecture Flow](#system-architecture-flow)
2. [Vehicle Parking Flow](#vehicle-parking-flow)
3. [Vehicle Unparking Flow](#vehicle-unparking-flow)
4. [Design Patterns Interaction](#design-patterns-interaction)
5. [Class Relationship Diagram](#class-relationship-diagram)
6. [Observer Pattern Flow](#observer-pattern-flow)
7. [Strategy Pattern Flow](#strategy-pattern-flow)

---

## 🏗️ System Architecture Flow

```mermaid
graph TB
    A[Main Application] --> B[ParkingLotManager]
    A --> C[ParkingLotService]
    
    B --> D[ParkingLot 1]
    B --> E[ParkingLot 2]
    B --> F[ParkingLot N]
    
    C --> G[ParkingLotServiceImpl]
    G --> H[ParkingDisplayService]
    G --> I[FeeCalculationStrategy]
    G --> J[ParkingTicketFactory]
    G --> K[Observer List]
    
    D --> L[Floor 1]
    D --> M[Floor 2]
    L --> N[ParkingSlot 1]
    L --> O[ParkingSlot 2]
    M --> P[ParkingSlot 3]
    M --> Q[ParkingSlot 4]
    
    style A fill:#e1f5fe
    style G fill:#f3e5f5
    style D fill:#e8f5e8
```

---

## 🚗 Vehicle Parking Flow

```mermaid
sequenceDiagram
    participant U as User/Main
    participant PS as ParkingLotService
    participant PL as ParkingLot
    participant F as Floor
    participant S as ParkingSlot
    participant TF as TicketFactory
    participant O as Observer
    
    U->>PS: parkVehicle(vehicle)
    PS->>PL: getFloors()
    
    loop For each floor
        PS->>F: getAvailableSlotFor(vehicleType)
        F->>S: isAvailable() && canFitVehicle()
        alt Slot Available
            S-->>F: return slot
            F-->>PS: return slot
        else No Slot
            F-->>PS: return null
        end
    end
    
    alt Slot Found
        PS->>S: park(vehicle)
        S->>S: setAvailable(false)
        S->>S: setParkedVehicle(vehicle)
        PS->>TF: createTicket(vehicle, slot)
        TF-->>PS: return ticket
        PS->>O: notifyVehicleParked(ticket)
        PS-->>U: return ticket
    else No Slot Found
        PS->>O: notifyParkingFull()
        PS-->>U: return null
    end
```

---

## 🚪 Vehicle Unparking Flow

```mermaid
sequenceDiagram
    participant U as User/Main
    participant PS as ParkingLotService
    participant T as ParkingTicket
    participant S as ParkingSlot
    participant FS as FeeStrategy
    participant O as Observer
    
    U->>PS: unparkVehicle(ticketId)
    PS->>PS: validateTicket(ticketId)
    
    alt Valid Ticket
        PS->>T: setExitTime(now)
        PS->>S: unpark()
        S->>S: setAvailable(true)
        S->>S: setParkedVehicle(null)
        
        PS->>FS: calculateFee(ticket)
        FS->>T: getEntryTime()
        FS->>T: getExitTime()
        FS->>T: getVehicle().getType()
        FS-->>PS: return fee
        
        PS->>O: notifyVehicleUnparked(ticket, fee)
        PS->>O: notifySlotAvailable()
        PS-->>U: return fee
    else Invalid Ticket
        PS-->>U: return 0.0
    end
```

---

## 🎯 Design Patterns Interaction

```mermaid
graph LR
    subgraph "Strategy Pattern"
        A[FeeCalculationStrategy] --> B[HourlyFeeStrategy]
        A --> C[PeakHourFeeStrategy]
        A --> D[FlatRateFeeStrategy]
    end
    
    subgraph "Factory Pattern"
        E[ParkingTicketFactory] --> F[createTicket]
        F --> G[generateTicketId]
    end
    
    subgraph "Observer Pattern"
        H[ParkingEventObserver] --> I[ConsoleNotificationObserver]
        H --> J[EmailNotificationObserver]
        H --> K[SMSNotificationObserver]
    end
    
    subgraph "Singleton Pattern"
        L[ParkingLotManager] --> M[getInstance]
        M --> N[SingleInstance]
    end
    
    subgraph "Service Layer"
        O[ParkingLotServiceImpl]
    end
    
    O --> A
    O --> E
    O --> H
    O --> L
    
    style A fill:#ffeb3b
    style E fill:#4caf50
    style H fill:#2196f3
    style L fill:#ff9800
```

---

## 📊 Class Relationship Diagram

```mermaid
classDiagram
    class ParkingLot {
        -String name
        -List floors
        +addFloor(Floor)
        +getFloors()
        +findAvailableSlot(VehicleType)
    }
    
    class Floor {
        -int floorNumber
        -List slots
        +addSlot(ParkingSlot)
        +getAvailableSlotFor(VehicleType)
    }
    
    class ParkingSlot {
        -int slotNumber
        -SlotSize size
        -boolean isAvailable
        -Vehicle parkedVehicle
        +park(Vehicle)
        +unpark()
        +canFitVehicle(VehicleType)
    }
    
    class Vehicle {
        -String licenseNumber
        -VehicleType type
    }
    
    class ParkingTicket {
        -String ticketId
        -Vehicle vehicle
        -ParkingSlot slot
        -LocalDateTime entryTime
        -LocalDateTime exitTime
    }
    
    class ParkingLotServiceImpl {
        -ParkingLot parkingLot
        -Map activeTickets
        -FeeCalculationStrategy feeStrategy
        -List observers
        +parkVehicle(Vehicle)
        +unparkVehicle(String)
    }
    
    class FeeCalculationStrategy {
        <<interface>>
        +calculateFee(ParkingTicket)
    }
    
    class HourlyFeeCalculationStrategy {
        +calculateFee(ParkingTicket)
    }
    
    class ParkingEventObserver {
        <<interface>>
        +onVehicleParked(ParkingTicket)
        +onVehicleUnparked(ParkingTicket, double)
    }
    
    class ConsoleNotificationObserver {
        +onVehicleParked(ParkingTicket)
        +onVehicleUnparked(ParkingTicket, double)
    }
    
    ParkingLot "1" --> "*" Floor
    Floor "1" --> "*" ParkingSlot
    ParkingSlot "0..1" --> "0..1" Vehicle
    ParkingTicket "1" --> "1" Vehicle
    ParkingTicket "1" --> "1" ParkingSlot
    ParkingLotServiceImpl "1" --> "1" ParkingLot
    ParkingLotServiceImpl "1" --> "*" ParkingTicket
    ParkingLotServiceImpl "1" --> "1" FeeCalculationStrategy
    ParkingLotServiceImpl "1" --> "*" ParkingEventObserver
    FeeCalculationStrategy <|.. HourlyFeeCalculationStrategy
    ParkingEventObserver <|.. ConsoleNotificationObserver
```

---

## 👁️ Observer Pattern Flow

```mermaid
graph TD
    A[ParkingLotServiceImpl] --> B[Event Occurs]
    
    B --> C{Event Type}
    C -->|Vehicle Parked| D[notifyVehicleParked]
    C -->|Vehicle Unparked| E[notifyVehicleUnparked]
    C -->|Parking Full| F[notifyParkingFull]
    C -->|Slot Available| G[notifySlotAvailable]
    
    D --> H[Observer1Console]
    D --> I[Observer2Email]
    D --> J[Observer3SMS]
    
    E --> H
    E --> I
    E --> J
    
    F --> H
    F --> I
    F --> J
    
    G --> H
    G --> I
    G --> J
    
    H --> K[Console Output]
    I --> L[Send Email]
    J --> M[Send SMS]
    
    style A fill:#e3f2fd
    style H fill:#c8e6c9
    style I fill:#fff3e0
    style J fill:#fce4ec
```

---

## ⚡ Strategy Pattern Flow

```mermaid
graph TD
    A[ParkingLotServiceImpl] --> B[unparkVehicle called]
    B --> C[Need to calculate fee]
    C --> D[FeeCalculationStrategy]
    
    D --> E{Strategy Type}
    E -->|Hourly| F[HourlyFeeCalculationStrategy]
    E -->|Peak Hour| G[PeakHourFeeCalculationStrategy]
    E -->|Flat Rate| H[FlatRateFeeCalculationStrategy]
    E -->|Weekend| I[WeekendFeeCalculationStrategy]
    
    F --> J[Calculate: duration × hourly_rate]
    G --> K[Calculate: duration × hourly_rate × 1.5]
    H --> L[Calculate: fixed_amount]
    I --> M[Calculate: duration × weekend_rate]
    
    J --> N[Return Fee]
    K --> N
    L --> N
    M --> N
    
    N --> O[Display Fee to User]
    
    style D fill:#fff9c4
    style F fill:#c8e6c9
    style G fill:#ffcdd2
    style H fill:#e1f5fe
    style I fill:#f3e5f5
```

---

## 🔄 Complete System Flow (High Level)

```mermaid
graph TB
    subgraph "User Interface Layer"
        A[Main Application]
    end
    
    subgraph "Service Layer"
        B[ParkingLotService]
        C[ParkingDisplayService]
    end
    
    subgraph "Business Logic Layer"
        D[ParkingLotServiceImpl]
        E[FeeCalculationStrategy]
        F[ParkingTicketFactory]
    end
    
    subgraph "Model Layer"
        G[ParkingLot]
        H[Floor]
        I[ParkingSlot]
        J[Vehicle]
        K[ParkingTicket]
    end
    
    subgraph "Observer Layer"
        L[ParkingEventObserver]
        M[ConsoleNotificationObserver]
    end
    
    subgraph "Management Layer"
        N[ParkingLotManager]
    end
    
    A --> B
    A --> C
    B --> D
    D --> E
    D --> F
    D --> G
    D --> L
    G --> H
    H --> I
    I --> J
    F --> K
    L --> M
    A --> N
    N --> G
    
    style A fill:#e8f5e8
    style D fill:#e3f2fd
    style G fill:#fff3e0
    style L fill:#fce4ec
    style N fill:#f3e5f5
```

---

## 🎯 Interview Flow Diagram

```mermaid
graph LR
    A[Start Interview] --> B[Explain Architecture]
    B --> C[Show Design Patterns]
    C --> D[Demonstrate SOLID]
    D --> E[Live Coding Demo]
    E --> F[Extension Discussion]
    F --> G[Q&A Session]
    
    B --> B1[System Overview<br/>Multi-floor, Multi-vehicle]
    C --> C1[Strategy Pattern<br/>Fee Calculation]
    C --> C2[Factory Pattern<br/>Ticket Creation]
    C --> C3[Observer Pattern<br/>Notifications]
    C --> C4[Singleton Pattern<br/>Manager]
    
    D --> D1[SRP: Single Responsibility]
    D --> D2[OCP: Open/Closed]
    D --> D3[LSP: Liskov Substitution]
    D --> D4[ISP: Interface Segregation]
    D --> D5[DIP: Dependency Inversion]
    
    E --> E1[Park Vehicle Demo]
    E --> E2[Unpark Vehicle Demo]
    E --> E3[Show Notifications]
    
    F --> F1[Add New Vehicle Type]
    F --> F2[Add New Pricing Strategy]
    F --> F3[Add Payment System]
    
    style A fill:#c8e6c9
    style G fill:#ffcdd2
```

---

## 📝 Usage Instructions

### For Interviews:
1. **Start with System Architecture Flow** - gives high-level overview
2. **Show Vehicle Parking/Unparking Flows** - demonstrates core functionality
3. **Explain Design Patterns Interaction** - shows pattern knowledge
4. **Use Class Relationship Diagram** - explains OOP concepts
5. **Reference specific flows** when answering questions

### For Development:
1. Use these diagrams to understand system flow
2. Reference when adding new features
3. Show to team members for system understanding
4. Use as documentation for future maintenance

### Mermaid Rendering:
- These diagrams use Mermaid syntax
- Can be rendered in GitHub, GitLab, or any Mermaid-compatible viewer
- For local viewing, use Mermaid Live Editor or VS Code Mermaid extension

---

**💡 Pro Tip**: During interviews, draw simplified versions of these diagrams on a whiteboard to demonstrate your understanding of system architecture and design patterns!