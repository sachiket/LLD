# 🎯 SIMPLE START GUIDE - BookMyShow Interview

## 🚀 **DON'T PANIC! Start Here:**

### **Your Opening Line (30 seconds):**
*"I'll design a movie booking system step by step. Let me start with the basic entities and build up gradually."*

---

## 📝 **STEP 1: Draw Basic Entities (2 minutes)**

**On whiteboard/screen, draw:**
```
[Movie] ----shown in----> [Show] ----has----> [Screen] ----contains----> [Seat]
                                      |
                                   [Theater]
```

**Say:** *"These are my core entities. Let me code them..."*

### **Code Step 1:**
```java
// Start with just this!
class Movie {
    String movieId;
    String title;
    String genre; // "ACTION", "COMEDY", "DRAMA"
    int durationInMinutes;
}

class Seat {
    String seatId;
    String seatType; // "REGULAR", "PREMIUM", "VIP"
    boolean isAvailable = true;
}

class Screen {
    String screenId;
    String name;
    List<Seat> seats = new ArrayList<>();
}

class Theater {
    String theaterId;
    String name;
    List<Screen> screens = new ArrayList<>();
}
```

**Say:** *"This covers the basics. Now let me add shows and bookings..."*

---

## 📝 **STEP 2: Add Show & Booking (3 minutes)**

```java
class Show {
    String showId;
    Movie movie;
    Screen screen;
    Theater theater;
    LocalDateTime startTime;
    double basePrice;
    
    Show(String showId, Movie movie, Screen screen, Theater theater, 
         LocalDateTime startTime, double basePrice) {
        this.showId = showId;
        this.movie = movie;
        this.screen = screen;
        this.theater = theater;
        this.startTime = startTime;
        this.basePrice = basePrice;
    }
}

class Booking {
    String bookingId;
    String userId;
    Show show;
    List<Seat> bookedSeats;
    double totalAmount;
    String status = "PENDING"; // "PENDING", "CONFIRMED", "CANCELLED"
    
    void confirmBooking() {
        this.status = "CONFIRMED";
        for (Seat seat : bookedSeats) {
            seat.isAvailable = false;
        }
    }
}
```

**Say:** *"Great! This works. Now let me add the core booking functionality..."*

---

## 📝 **STEP 3: Add Core Methods (3 minutes)**

```java
class Screen {
    // ... previous code ...
    
    List<Seat> getAvailableSeats() {
        List<Seat> available = new ArrayList<>();
        for (Seat seat : seats) {
            if (seat.isAvailable) {
                available.add(seat);
            }
        }
        return available;
    }
    
    Seat findSeatById(String seatId) {
        for (Seat seat : seats) {
            if (seat.seatId.equals(seatId)) {
                return seat;
            }
        }
        return null;
    }
}

class BookMyShowService {
    List<Movie> movies = new ArrayList<>();
    List<Theater> theaters = new ArrayList<>();
    List<Show> shows = new ArrayList<>();
    Map<String, Booking> bookings = new HashMap<>();
    
    Booking createBooking(String userId, String showId, List<String> seatIds) {
        Show show = findShowById(showId);
        List<Seat> requestedSeats = new ArrayList<>();
        
        // Validate seats
        for (String seatId : seatIds) {
            Seat seat = show.screen.findSeatById(seatId);
            if (seat == null || !seat.isAvailable) {
                System.out.println("Seat not available: " + seatId);
                return null;
            }
            requestedSeats.add(seat);
        }
        
        // Calculate price and create booking
        double totalAmount = calculatePrice(show, requestedSeats);
        String bookingId = "BMS-" + System.currentTimeMillis();
        Booking booking = new Booking(bookingId, userId, show, requestedSeats, totalAmount);
        bookings.put(bookingId, booking);
        
        System.out.println("Booking created: " + bookingId);
        return booking;
    }
    
    double calculatePrice(Show show, List<Seat> seats) {
        double total = 0;
        for (Seat seat : seats) {
            double multiplier = seat.seatType.equals("VIP") ? 2.0 : 
                               seat.seatType.equals("PREMIUM") ? 1.5 : 1.0;
            total += show.basePrice * multiplier;
        }
        return total;
    }
}
```

**Say:** *"Perfect! This works. Now let me make it more professional with enums..."*

---

## 📝 **STEP 4: Add Enums & Clean Up (2 minutes)**

```java
enum SeatType { REGULAR, PREMIUM, VIP }
enum BookingStatus { PENDING, CONFIRMED, CANCELLED }
enum MovieGenre { ACTION, COMEDY, DRAMA, THRILLER }

class Movie {
    String movieId;
    String title;
    MovieGenre genre;
    int durationInMinutes;
    double rating;
    
    Movie(String movieId, String title, MovieGenre genre, int duration, double rating) {
        this.movieId = movieId;
        this.title = title;
        this.genre = genre;
        this.durationInMinutes = duration;
        this.rating = rating;
    }
}

class Seat {
    String seatId;
    SeatType seatType;
    boolean isAvailable = true;
    
    Seat(String seatId, SeatType seatType) {
        this.seatId = seatId;
        this.seatType = seatType;
    }
    
    void book() {
        this.isAvailable = false;
    }
    
    void release() {
        this.isAvailable = true;
    }
}
```

**Say:** *"Much cleaner with enums. Now let me add screen initialization..."*

---

## 📝 **STEP 5: Add Screen Layout (2 minutes)**

```java
class Screen {
    String screenId;
    String name;
    List<Seat> seats = new ArrayList<>();
    int totalRows;
    int seatsPerRow;
    
    Screen(String screenId, String name, int totalRows, int seatsPerRow) {
        this.screenId = screenId;
        this.name = name;
        this.totalRows = totalRows;
        this.seatsPerRow = seatsPerRow;
        initializeSeats();
    }
    
    void initializeSeats() {
        for (int row = 1; row <= totalRows; row++) {
            for (int col = 1; col <= seatsPerRow; col++) {
                String seatId = screenId + "-R" + row + "-C" + col;
                SeatType seatType = determineSeatType(row);
                seats.add(new Seat(seatId, seatType));
            }
        }
    }
    
    SeatType determineSeatType(int row) {
        if (row <= totalRows / 3) return SeatType.REGULAR;
        else if (row <= 2 * totalRows / 3) return SeatType.PREMIUM;
        else return SeatType.VIP;
    }
    
    void displayLayout() {
        System.out.println("Screen: " + name);
        for (int row = 1; row <= totalRows; row++) {
            System.out.print("Row " + row + ": ");
            for (int col = 1; col <= seatsPerRow; col++) {
                String seatId = screenId + "-R" + row + "-C" + col;
                Seat seat = findSeatById(seatId);
                System.out.print(seat.isAvailable ? "[O]" : "[X]");
            }
            System.out.println();
        }
    }
}
```

**Say:** *"Excellent! Now it shows seat layouts. Let me add design patterns to make it more professional..."*

---

## 📝 **STEP 6: Add Design Patterns (5 minutes)**

### **Strategy Pattern for Pricing:**
```java
interface PricingStrategy {
    double calculatePrice(Show show, List<Seat> seats);
}

class StandardPricingStrategy implements PricingStrategy {
    public double calculatePrice(Show show, List<Seat> seats) {
        double total = 0;
        for (Seat seat : seats) {
            double multiplier = switch (seat.seatType) {
                case REGULAR -> 1.0;
                case PREMIUM -> 1.5;
                case VIP -> 2.0;
            };
            total += show.basePrice * multiplier;
        }
        return total;
    }
}
```

### **Factory Pattern for Bookings:**
```java
class BookingFactory {
    static Booking createBooking(String userId, Show show, List<Seat> seats, double amount) {
        String bookingId = "BMS-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return new Booking(bookingId, userId, show, seats, amount);
    }
}
```

### **Observer Pattern for Notifications:**
```java
interface BookingObserver {
    void onBookingCreated(Booking booking);
    void onBookingConfirmed(Booking booking);
}

class ConsoleNotificationObserver implements BookingObserver {
    public void onBookingCreated(Booking booking) {
        System.out.println("Booking created: " + booking.bookingId + 
                          " for movie: " + booking.show.movie.title);
    }
    
    public void onBookingConfirmed(Booking booking) {
        System.out.println("Booking confirmed: " + booking.bookingId);
    }
}
```

### **Service Layer:**
```java
class BookMyShowService {
    List<Movie> movies = new ArrayList<>();
    List<Show> shows = new ArrayList<>();
    Map<String, Booking> bookings = new HashMap<>();
    PricingStrategy pricingStrategy = new StandardPricingStrategy();
    List<BookingObserver> observers = new ArrayList<>();
    
    void addObserver(BookingObserver observer) {
        observers.add(observer);
    }
    
    Booking createBooking(String userId, String showId, List<String> seatIds) {
        Show show = findShowById(showId);
        List<Seat> seats = validateSeats(show, seatIds);
        double amount = pricingStrategy.calculatePrice(show, seats);
        
        Booking booking = BookingFactory.createBooking(userId, show, seats, amount);
        bookings.put(booking.bookingId, booking);
        
        notifyObservers(booking, "CREATED");
        return booking;
    }
    
    void confirmBooking(String bookingId) {
        Booking booking = bookings.get(bookingId);
        booking.confirmBooking();
        notifyObservers(booking, "CONFIRMED");
    }
}
```

**Say:** *"Now it's much more extensible and follows design patterns!"*

---

## 🎯 **Your Interview Mindset:**

### **Think Step by Step:**
1. **"What are the main entities?"** → Movie, Theater, Screen, Seat, Show, Booking
2. **"What are the main operations?"** → View Movies, Book Seats, Confirm Booking
3. **"How can I make it scalable?"** → Multiple Theaters, Service Layer
4. **"How can I make it flexible?"** → Design Patterns

### **Always Explain:**
- *"I'm starting simple and building up..."*
- *"Let me add this for better organization..."*
- *"This pattern will make it more flexible..."*
- *"I can extend this easily for new requirements..."*

### **If Asked About Extensions:**
- **New seat type?** *"Just add to enum and update pricing strategy"*
- **Different pricing?** *"Create new PricingStrategy implementation"*
- **Payment processing?** *"Add PaymentStrategy pattern"*
- **Multiple cities?** *"Extend TheaterManager with city support"*

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