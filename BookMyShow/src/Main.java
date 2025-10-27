import models.*;
import enums.*;
import services.BookMyShowServiceImpl;
import observers.BookingEventObserver;
import observers.ConsoleNotificationObserver;
import managers.TheaterManager;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Main class demonstrating BookMyShow system functionality
 * Shows all design patterns and SOLID principles in action
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("[DEMO] Welcome to BookMyShow System Demo!");
        System.out.println("=" + "=".repeat(60));
        
        // Step 1: Setup the system
        BookMyShowServiceImpl bookingService = setupSystem();
        
        // Step 2: Display available movies
        System.out.println("\n[STEP 1] Display Available Movies");
        bookingService.displayMovies();
        
        // Step 3: Display shows for a specific movie
        System.out.println("\n[STEP 2] Display Shows for Avengers");
        bookingService.displayShows("MOV001");
        
        // Step 4: Display seat layout
        System.out.println("\n[STEP 3] Display Seat Layout for Show");
        bookingService.displaySeatLayout("SHOW001");
        
        // Step 5: Create bookings
        System.out.println("\n[STEP 4] Create Bookings");
        demonstrateBookings(bookingService);
        
        // Step 6: Display updated seat layout
        System.out.println("\n[STEP 5] Display Updated Seat Layout");
        bookingService.displaySeatLayout("SHOW001");
        
        // Step 7: Show booking details
        System.out.println("\n[STEP 6] Display Booking Details");
        // Display a sample booking (will show "Booking not found" which is expected)
        bookingService.displayBookingDetails("SAMPLE-BOOKING");
        
        System.out.println("\n[SUCCESS] Demo completed successfully!");
        System.out.println("=" + "=".repeat(60));
    }
    
    /**
     * Sets up the complete BookMyShow system with sample data
     */
    private static BookMyShowServiceImpl setupSystem() {
        // Create service with observer
        BookMyShowServiceImpl service = new BookMyShowServiceImpl();
        ConsoleNotificationObserver observer = new ConsoleNotificationObserver();
        service.addObserver((BookingEventObserver) observer);
        
        // Create movies
        Movie avengers = new Movie("MOV001", "Avengers: Endgame", 
                "Epic superhero finale", 180, MovieGenre.ACTION, "English", 8.4);
        Movie joker = new Movie("MOV002", "Joker", 
                "Psychological thriller", 122, MovieGenre.THRILLER, "English", 8.5);
        Movie parasite = new Movie("MOV003", "Parasite", 
                "Korean dark comedy", 132, MovieGenre.DRAMA, "Korean", 8.6);
        
        service.addMovie(avengers);
        service.addMovie(joker);
        service.addMovie(parasite);
        
        // Create theaters and screens
        Theater pvr = new Theater("TH001", "PVR Cinemas", "Mumbai");
        Screen screen1 = new Screen("SCR001", "Screen 1", 10, 15);
        Screen screen2 = new Screen("SCR002", "Screen 2", 8, 12);
        pvr.addScreen(screen1);
        pvr.addScreen(screen2);
        
        Theater inox = new Theater("TH002", "INOX", "Delhi");
        Screen screen3 = new Screen("SCR003", "IMAX Screen", 12, 20);
        inox.addScreen(screen3);
        
        service.addTheater(pvr);
        service.addTheater(inox);
        
        // Add theaters to manager (Singleton pattern)
        TheaterManager theaterManager = TheaterManager.getInstance();
        theaterManager.addTheater(pvr);
        theaterManager.addTheater(inox);
        
        // Create shows
        LocalDateTime now = LocalDateTime.now();
        
        Show show1 = new Show("SHOW001", avengers, screen1, pvr, 
                now.plusHours(2), 250.0);
        Show show2 = new Show("SHOW002", joker, screen2, pvr, 
                now.plusHours(4), 200.0);
        Show show3 = new Show("SHOW003", parasite, screen3, inox, 
                now.plusHours(3), 300.0);
        
        service.addShow(show1);
        service.addShow(show2);
        service.addShow(show3);
        
        return service;
    }
    
    /**
     * Demonstrates booking functionality with different scenarios
     */
    private static void demonstrateBookings(BookMyShowServiceImpl service) {
        try {
            // Scenario 1: Successful booking
            System.out.println("\n[SCENARIO 1] Successful Booking");
            List<String> seats1 = Arrays.asList("SCR001-R5-C7", "SCR001-R5-C8");
            Booking booking1 = service.createBooking("USER001", "SHOW001", seats1);
            service.confirmBooking(booking1.getBookingId());
            
            // Display this booking's details
            System.out.println("\n[DETAILS] Booking Details for Scenario 1:");
            service.displayBookingDetails(booking1.getBookingId());
            
            // Scenario 2: Multiple seat booking
            System.out.println("\n[SCENARIO 2] Multiple Seat Booking");
            List<String> seats2 = Arrays.asList("SCR001-R3-C5", "SCR001-R3-C6", 
                                              "SCR001-R3-C7", "SCR001-R3-C8");
            Booking booking2 = service.createBooking("USER002", "SHOW001", seats2);
            service.confirmBooking(booking2.getBookingId());
            
            // Scenario 3: Booking cancellation
            System.out.println("\n[SCENARIO 3] Booking Cancellation");
            List<String> seats3 = Arrays.asList("SCR001-R7-C10", "SCR001-R7-C11");
            Booking booking3 = service.createBooking("USER003", "SHOW001", seats3);
            service.confirmBooking(booking3.getBookingId());
            System.out.println("Cancelling booking...");
            service.cancelBooking(booking3.getBookingId());
            
            // Scenario 4: Try to book already booked seats (will fail)
            System.out.println("\n[SCENARIO 4] Attempt to Book Already Booked Seats");
            try {
                List<String> seats4 = Arrays.asList("SCR001-R5-C7", "SCR001-R5-C8");
                service.createBooking("USER004", "SHOW001", seats4);
            } catch (IllegalArgumentException e) {
                System.out.println("[X] Booking failed as expected: " + e.getMessage());
            }
            
        } catch (Exception e) {
            System.out.println("[X] Error during booking: " + e.getMessage());
        }
    }
}