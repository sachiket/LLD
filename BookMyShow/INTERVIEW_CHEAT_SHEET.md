# 📝 BookMyShow Interview Cheat Sheet

## 🚀 Quick Reference for Live Coding

### **Core Models (5 minutes)**
```java
// Movie.java
public class Movie {
    private final String movieId;
    private final String title;
    private final MovieGenre genre;
    private final int durationInMinutes;
    private final double rating;
}

// Theater.java -> Screen.java -> Seat.java
public class Theater {
    private final String theaterId;
    private final String name;
    private final List<Screen> screens;
}
```

### **Key Enums**
```java
public enum SeatType { REGULAR, PREMIUM, VIP }
public enum BookingStatus { PENDING, CONFIRMED, CANCELLED, EXPIRED }
public enum MovieGenre { ACTION, COMEDY, DRAMA, THRILLER }
```

---

## 🎯 Design Patterns Quick Implementation

### **1. Strategy Pattern (Pricing)**
```java
public interface PricingStrategy {
    double calculatePrice(Show show, List<Seat> seats);
}

public class StandardPricingStrategy implements PricingStrategy {
    @Override
    public double calculatePrice(Show show, List<Seat> seats) {
        double total = 0.0;
        for (Seat seat : seats) {
            total += show.getBasePrice() * getSeatMultiplier(seat.getSeatType());
        }
        return total;
    }
}
```

### **2. Factory Pattern (Booking Creation)**
```java
public class BookingFactory {
    public static Booking createBooking(String userId, Show show, List<Seat> seats, double amount) {
        String bookingId = "BMS-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return new Booking(bookingId, userId, show, seats, amount);
    }
}
```

### **3. Observer Pattern (Notifications)**
```java
public interface BookingEventObserver {
    void onBookingCreated(Booking booking);
    void onBookingConfirmed(Booking booking);
}

// In Service Implementation
private void notifyBookingCreated(Booking booking) {
    for (BookingEventObserver observer : observers) {
        observer.onBookingCreated(booking);
    }
}
```

### **4. Singleton Pattern (Theater Manager)**
```java
public class TheaterManager {
    private static TheaterManager instance;
    
    public static synchronized TheaterManager getInstance() {
        if (instance == null) {
            instance = new TheaterManager();
        }
        return instance;
    }
}
```

---

## ⚖️ SOLID Principles Examples

### **Single Responsibility**
```java
// ✅ Good - Each class has one responsibility
public class Seat { /* Only manages seat state */ }
public class PricingStrategy { /* Only calculates prices */ }
public class BookingFactory { /* Only creates bookings */ }
```

### **Open/Closed**
```java
// ✅ Easy to extend without modifying existing code
public class WeekendPricingStrategy implements PricingStrategy {
    @Override
    public double calculatePrice(Show show, List<Seat> seats) {
        return new StandardPricingStrategy().calculatePrice(show, seats) * 1.5;
    }
}
```

### **Dependency Inversion**
```java
// ✅ Depend on abstractions
public class BookMyShowServiceImpl {
    private final PricingStrategy pricingStrategy; // Interface, not concrete class
    private final List<BookingEventObserver> observers; // Interface
}
```

---

## 🔧 Core Service Methods

### **Booking Creation**
```java
@Override
public Booking createBooking(String userId, String showId, List<String> seatIds) {
    Show show = findShowById(showId);
    List<Seat> seats = validateAndGetSeats(show, seatIds);
    double totalAmount = pricingStrategy.calculatePrice(show, seats);
    
    Booking booking = BookingFactory.createBooking(userId, show, seats, totalAmount);
    bookings.put(booking.getBookingId(), booking);
    notifyBookingCreated(booking);
    
    return booking;
}
```

### **Seat Validation**
```java
private List<Seat> validateAndGetSeats(Show show, List<String> seatIds) {
    List<Seat> seats = new ArrayList<>();
    for (String seatId : seatIds) {
        Seat seat = show.getScreen().findSeatById(seatId);
        if (seat == null) throw new IllegalArgumentException("Seat not found: " + seatId);
        if (!seat.isAvailable()) throw new IllegalArgumentException("Seat not available: " + seatId);
        seats.add(seat);
    }
    return seats;
}
```

---

## 🎤 Common Interview Questions & Answers

### **Q: "How would you handle concurrent bookings?"**
```java
// Option 1: Synchronized methods
public synchronized boolean bookSeat(String seatId) { }

// Option 2: Database-level locking
@Transactional
public Booking createBooking(...) {
    // Use SELECT FOR UPDATE in database
}

// Option 3: Optimistic locking
public class Seat {
    private int version; // JPA @Version annotation
}
```

### **Q: "How would you add payment processing?"**
```java
public interface PaymentStrategy {
    boolean processPayment(double amount, PaymentDetails details);
}

public class CreditCardPaymentStrategy implements PaymentStrategy { }
public class DigitalWalletPaymentStrategy implements PaymentStrategy { }
```

### **Q: "How would you implement seat reservation timeout?"**
```java
public class Seat {
    private SeatState state; // AVAILABLE, RESERVED, BOOKED
    private LocalDateTime reservationExpiry;
    
    public boolean isExpired() {
        return state == SeatState.RESERVED && 
               LocalDateTime.now().isAfter(reservationExpiry);
    }
}
```

---

## 🚀 Quick Extensions

### **Add New Seat Type (2 minutes)**
```java
// 1. Add to enum
public enum SeatType { REGULAR, PREMIUM, VIP, RECLINER }

// 2. Update pricing
private double getSeatMultiplier(SeatType seatType) {
    switch (seatType) {
        case RECLINER: return 3.0;
        // ... existing cases
    }
}
```

### **Add Holiday Pricing (3 minutes)**
```java
public class HolidayPricingStrategy implements PricingStrategy {
    @Override
    public double calculatePrice(Show show, List<Seat> seats) {
        double basePrice = new StandardPricingStrategy().calculatePrice(show, seats);
        return basePrice * 2.0; // Double price on holidays
    }
}
```

### **Add Email Notifications (5 minutes)**
```java
public class EmailNotificationObserver implements BookingEventObserver {
    @Override
    public void onBookingConfirmed(Booking booking) {
        // Send confirmation email
        System.out.println("Email sent to user: " + booking.getUserId());
    }
}
```

---

## 🎯 Demo Script

### **Step 1: Show Movies**
```java
service.displayMovies();
// Shows: Avengers, Joker, Parasite with ratings and genres
```

### **Step 2: Show Available Shows**
```java
service.displayShows("MOV001");
// Shows: Theater, Screen, Time, Available Seats
```

### **Step 3: Display Seat Layout**
```java
service.displaySeatLayout("SHOW001");
// Shows: 10x15 grid with [O] available, [X] booked
```

### **Step 4: Create Booking**
```java
List<String> seats = Arrays.asList("SCR001-R5-C7", "SCR001-R5-C8");
Booking booking = service.createBooking("USER001", "SHOW001", seats);
service.confirmBooking(booking.getBookingId());
// Shows: Observer notifications, pricing calculation
```

---

## 💡 Pro Tips for Live Coding

### **Start with This Template**
```java
public class Main {
    public static void main(String[] args) {
        // 1. Setup system
        BookMyShowServiceImpl service = new BookMyShowServiceImpl();
        
        // 2. Add sample data
        setupSampleData(service);
        
        // 3. Demonstrate functionality
        demonstrateBooking(service);
    }
}
```

### **Time-Saving Shortcuts**
- Use `Arrays.asList()` for quick list creation
- Use `String.format()` for clean output
- Use `LocalDateTime.now().plusHours(2)` for future shows
- Use `UUID.randomUUID().toString().substring(0, 8)` for IDs

### **Error Handling Pattern**
```java
try {
    // Main operation
} catch (IllegalArgumentException e) {
    System.out.println("Error: " + e.getMessage());
    return false;
}
```

---

## 🏆 Success Checklist

### **Must Have (60% score)**
- ✅ Basic models (Movie, Theater, Screen, Seat, Booking)
- ✅ Core booking functionality
- ✅ At least 1 design pattern
- ✅ Working demo

### **Should Have (80% score)**
- ✅ 2-3 design patterns
- ✅ SOLID principles demonstrated
- ✅ Error handling
- ✅ Observer notifications

### **Nice to Have (95% score)**
- ✅ All 4 design patterns
- ✅ Multiple pricing strategies
- ✅ Comprehensive demo
- ✅ Extension examples

**Remember**: Quality over quantity. Better to have fewer features implemented well than many features implemented poorly!