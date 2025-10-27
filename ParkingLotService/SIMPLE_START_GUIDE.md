# 🎯 SIMPLE START GUIDE - Parking Lot Interview

## 🚀 **DON'T PANIC! Start Here:**

### **Your Opening Line (30 seconds):**
*"I'll design a parking lot system step by step. Let me start with the basic entities and build up gradually."*

---

## 📝 **STEP 1: Draw Basic Entities (2 minutes)**

**On whiteboard/screen, draw:**
```
[Vehicle] ----parks in----> [ParkingSlot] ----belongs to----> [ParkingLot]
```

**Say:** *"These are my core entities. Let me code them..."*

### **Code Step 1:**
```java
// Start with just this!
class Vehicle {
    String licenseNumber;
    String type; // "BIKE", "CAR", "TRUCK"
}

class ParkingSlot {
    int slotNumber;
    String size; // "SMALL", "MEDIUM", "LARGE"
    boolean isAvailable = true;
    Vehicle parkedVehicle = null;
}

class ParkingLot {
    List<ParkingSlot> slots = new ArrayList<>();
}
```

**Say:** *"This covers the basics. Now let me add the core functionality..."*

---

## 📝 **STEP 2: Add Core Methods (3 minutes)**

```java
class ParkingSlot {
    // ... previous code ...
    
    boolean canFit(Vehicle vehicle) {
        if (vehicle.type.equals("BIKE")) return true;
        if (vehicle.type.equals("CAR")) return size.equals("MEDIUM") || size.equals("LARGE");
        if (vehicle.type.equals("TRUCK")) return size.equals("LARGE");
        return false;
    }
    
    void park(Vehicle vehicle) {
        this.parkedVehicle = vehicle;
        this.isAvailable = false;
    }
    
    void unpark() {
        this.parkedVehicle = null;
        this.isAvailable = true;
    }
}

class ParkingLot {
    List<ParkingSlot> slots = new ArrayList<>();
    
    boolean parkVehicle(Vehicle vehicle) {
        for (ParkingSlot slot : slots) {
            if (slot.isAvailable && slot.canFit(vehicle)) {
                slot.park(vehicle);
                System.out.println("Vehicle parked in slot " + slot.slotNumber);
                return true;
            }
        }
        System.out.println("No available slot found!");
        return false;
    }
}
```

**Say:** *"Great! This works. Now let me make it more professional..."*

---

## 📝 **STEP 3: Add Enums & Clean Up (2 minutes)**

```java
enum VehicleType { BIKE, CAR, TRUCK }
enum SlotSize { SMALL, MEDIUM, LARGE }

class Vehicle {
    String licenseNumber;
    VehicleType type;
    
    Vehicle(String licenseNumber, VehicleType type) {
        this.licenseNumber = licenseNumber;
        this.type = type;
    }
}

class ParkingSlot {
    int slotNumber;
    SlotSize size;
    boolean isAvailable = true;
    Vehicle parkedVehicle = null;
    
    ParkingSlot(int slotNumber, SlotSize size) {
        this.slotNumber = slotNumber;
        this.size = size;
    }
    
    boolean canFit(VehicleType type) {
        switch (type) {
            case BIKE: return true;
            case CAR: return size == SlotSize.MEDIUM || size == SlotSize.LARGE;
            case TRUCK: return size == SlotSize.LARGE;
            default: return false;
        }
    }
}
```

**Say:** *"Much cleaner with enums. Now let me add floors for scalability..."*

---

## 📝 **STEP 4: Add Floors (2 minutes)**

```java
class Floor {
    int floorNumber;
    List<ParkingSlot> slots = new ArrayList<>();
    
    Floor(int floorNumber) {
        this.floorNumber = floorNumber;
    }
    
    void addSlot(ParkingSlot slot) {
        slots.add(slot);
    }
    
    ParkingSlot findAvailableSlot(VehicleType vehicleType) {
        for (ParkingSlot slot : slots) {
            if (slot.isAvailable && slot.canFit(vehicleType)) {
                return slot;
            }
        }
        return null;
    }
}

class ParkingLot {
    String name;
    List<Floor> floors = new ArrayList<>();
    
    ParkingLot(String name) {
        this.name = name;
    }
    
    void addFloor(Floor floor) {
        floors.add(floor);
    }
    
    boolean parkVehicle(Vehicle vehicle) {
        for (Floor floor : floors) {
            ParkingSlot slot = floor.findAvailableSlot(vehicle.type);
            if (slot != null) {
                slot.park(vehicle);
                System.out.println("Vehicle " + vehicle.licenseNumber + 
                                 " parked on floor " + floor.floorNumber + 
                                 " slot " + slot.slotNumber);
                return true;
            }
        }
        System.out.println("No available slot for " + vehicle.type);
        return false;
    }
}
```

**Say:** *"Perfect! Now it supports multiple floors. Let me add tickets and fees..."*

---

## 📝 **STEP 5: Add Tickets & Fees (3 minutes)**

```java
class ParkingTicket {
    String ticketId;
    Vehicle vehicle;
    ParkingSlot slot;
    LocalDateTime entryTime;
    LocalDateTime exitTime;
    
    ParkingTicket(String ticketId, Vehicle vehicle, ParkingSlot slot) {
        this.ticketId = ticketId;
        this.vehicle = vehicle;
        this.slot = slot;
        this.entryTime = LocalDateTime.now();
    }
}

class ParkingLot {
    // ... previous code ...
    Map<String, ParkingTicket> activeTickets = new HashMap<>();
    
    ParkingTicket parkVehicle(Vehicle vehicle) {
        for (Floor floor : floors) {
            ParkingSlot slot = floor.findAvailableSlot(vehicle.type);
            if (slot != null) {
                slot.park(vehicle);
                String ticketId = "TICKET-" + System.currentTimeMillis();
                ParkingTicket ticket = new ParkingTicket(ticketId, vehicle, slot);
                activeTickets.put(ticketId, ticket);
                System.out.println("Vehicle parked. Ticket: " + ticketId);
                return ticket;
            }
        }
        System.out.println("No available slot!");
        return null;
    }
    
    double unparkVehicle(String ticketId) {
        ParkingTicket ticket = activeTickets.get(ticketId);
        if (ticket == null) {
            System.out.println("Invalid ticket!");
            return 0.0;
        }
        
        ticket.exitTime = LocalDateTime.now();
        ticket.slot.unpark();
        activeTickets.remove(ticketId);
        
        // Simple fee calculation
        long hours = Duration.between(ticket.entryTime, ticket.exitTime).toHours();
        if (hours == 0) hours = 1; // Minimum 1 hour
        
        double rate = 0;
        switch (ticket.vehicle.type) {
            case BIKE: rate = 10; break;
            case CAR: rate = 20; break;
            case TRUCK: rate = 40; break;
        }
        
        double fee = hours * rate;
        System.out.println("Fee: Rs." + fee);
        return fee;
    }
}
```

**Say:** *"Excellent! Now I have a working system. Let me add some design patterns to make it more professional..."*

---

## 📝 **STEP 6: Add Design Patterns (5 minutes)**

### **Strategy Pattern for Fees:**
```java
interface FeeCalculationStrategy {
    double calculateFee(ParkingTicket ticket);
}

class HourlyFeeStrategy implements FeeCalculationStrategy {
    public double calculateFee(ParkingTicket ticket) {
        long hours = Duration.between(ticket.entryTime, ticket.exitTime).toHours();
        if (hours == 0) hours = 1;
        
        double rate = switch (ticket.vehicle.type) {
            case BIKE -> 10;
            case CAR -> 20;
            case TRUCK -> 40;
        };
        return hours * rate;
    }
}
```

### **Factory Pattern for Tickets:**
```java
class TicketFactory {
    static ParkingTicket createTicket(Vehicle vehicle, ParkingSlot slot) {
        String ticketId = "TICKET-" + UUID.randomUUID().toString().substring(0, 8);
        return new ParkingTicket(ticketId, vehicle, slot);
    }
}
```

### **Service Layer:**
```java
class ParkingService {
    ParkingLot parkingLot;
    FeeCalculationStrategy feeStrategy;
    
    ParkingService(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
        this.feeStrategy = new HourlyFeeStrategy();
    }
    
    ParkingTicket parkVehicle(Vehicle vehicle) {
        // Use parkingLot.parkVehicle() logic here
    }
    
    double unparkVehicle(String ticketId) {
        // Use feeStrategy.calculateFee() here
    }
}
```

**Say:** *"Now it's much more extensible and follows design patterns!"*

---

## 🎯 **Your Interview Mindset:**

### **Think Step by Step:**
1. **"What are the main entities?"** → Vehicle, Slot, Lot
2. **"What are the main operations?"** → Park, Unpark, Calculate Fee
3. **"How can I make it scalable?"** → Add Floors, Service Layer
4. **"How can I make it flexible?"** → Design Patterns

### **Always Explain:**
- *"I'm starting simple and building up..."*
- *"Let me add this for better organization..."*
- *"This pattern will make it more flexible..."*
- *"I can extend this easily for new requirements..."*

### **If Asked About Extensions:**
- **New vehicle type?** *"Just add to enum and update canFit() logic"*
- **Different pricing?** *"Create new FeeCalculationStrategy implementation"*
- **Reservations?** *"Add reservation state to ParkingSlot"*
- **Payments?** *"Add PaymentStrategy pattern"*

---

## ✅ **Success Checklist:**

- ✅ Started simple with basic classes
- ✅ Built functionality incrementally  
- ✅ Explained reasoning at each step
- ✅ Added design patterns naturally
- ✅ Showed extensibility
- ✅ Handled edge cases
- ✅ Demonstrated clean code principles

**Remember: It's not about perfect code, it's about showing your thought process!**