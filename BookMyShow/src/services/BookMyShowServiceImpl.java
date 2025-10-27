package services;

import models.*;
import enums.SeatType;
import strategies.PricingStrategy;
import strategies.StandardPricingStrategy;
import factories.BookingFactory;
import observers.BookingEventObserver;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of BookMyShowService
 * Similar to ParkingLotServiceImpl in the reference implementation
 */
public class BookMyShowServiceImpl implements BookMyShowService {
    
    private final List<Movie> movies;
    private final List<Theater> theaters;
    private final List<Show> shows;
    private final Map<String, Booking> bookings;
    private final PricingStrategy pricingStrategy;
    private final List<BookingEventObserver> observers;

    public BookMyShowServiceImpl() {
        this.movies = new ArrayList<>();
        this.theaters = new ArrayList<>();
        this.shows = new ArrayList<>();
        this.bookings = new HashMap<>();
        this.pricingStrategy = new StandardPricingStrategy();
        this.observers = new ArrayList<>();
    }

    // Observer pattern methods
    public void addObserver(BookingEventObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(BookingEventObserver observer) {
        observers.remove(observer);
    }

    private void notifyBookingCreated(Booking booking) {
        for (BookingEventObserver observer : observers) {
            observer.onBookingCreated(booking);
        }
    }

    private void notifyBookingConfirmed(Booking booking) {
        for (BookingEventObserver observer : observers) {
            observer.onBookingConfirmed(booking);
        }
    }

    private void notifyBookingCancelled(Booking booking) {
        for (BookingEventObserver observer : observers) {
            observer.onBookingCancelled(booking);
        }
    }

    // Setup methods
    public void addMovie(Movie movie) {
        movies.add(movie);
    }

    public void addTheater(Theater theater) {
        theaters.add(theater);
    }

    public void addShow(Show show) {
        shows.add(show);
    }

    // Movie operations
    @Override
    public List<Movie> getAllMovies() {
        return new ArrayList<>(movies);
    }

    @Override
    public List<Movie> searchMoviesByTitle(String title) {
        return movies.stream()
                .filter(movie -> movie.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Show operations
    @Override
    public List<Show> getShowsForMovie(String movieId) {
        return shows.stream()
                .filter(show -> show.getMovie().getMovieId().equals(movieId))
                .filter(Show::isUpcoming)
                .collect(Collectors.toList());
    }

    @Override
    public List<Show> getShowsForTheater(String theaterId) {
        return shows.stream()
                .filter(show -> show.getTheater().getTheaterId().equals(theaterId))
                .filter(Show::isUpcoming)
                .collect(Collectors.toList());
    }

    @Override
    public List<Show> getShowsForDate(LocalDateTime date) {
        return shows.stream()
                .filter(show -> show.getStartTime().toLocalDate().equals(date.toLocalDate()))
                .filter(Show::isUpcoming)
                .collect(Collectors.toList());
    }

    // Booking operations
    @Override
    public Booking createBooking(String userId, String showId, List<String> seatIds) {
        Show show = findShowById(showId);
        if (show == null) {
            throw new IllegalArgumentException("Show not found: " + showId);
        }

        List<Seat> requestedSeats = new ArrayList<>();
        for (String seatId : seatIds) {
            Seat seat = show.getScreen().findSeatById(seatId);
            if (seat == null) {
                throw new IllegalArgumentException("Seat not found: " + seatId);
            }
            if (!seat.isAvailable()) {
                throw new IllegalArgumentException("Seat not available: " + seatId);
            }
            requestedSeats.add(seat);
        }

        double totalAmount = pricingStrategy.calculatePrice(show, requestedSeats);
        Booking booking = BookingFactory.createBooking(userId, show, requestedSeats, totalAmount);
        
        bookings.put(booking.getBookingId(), booking);
        notifyBookingCreated(booking);
        
        return booking;
    }

    @Override
    public boolean confirmBooking(String bookingId) {
        Booking booking = bookings.get(bookingId);
        if (booking == null) {
            return false;
        }

        if (booking.isExpired()) {
            booking.expire();
            return false;
        }

        booking.confirm();
        notifyBookingConfirmed(booking);
        
        // Check if show is fully booked
        if (booking.getShow().getScreen().getAvailableSeatsCount() == 0) {
            for (BookingEventObserver observer : observers) {
                observer.onShowFullyBooked(booking.getShow());
            }
        }
        
        return true;
    }

    @Override
    public boolean cancelBooking(String bookingId) {
        Booking booking = bookings.get(bookingId);
        if (booking == null) {
            return false;
        }

        booking.cancel();
        notifyBookingCancelled(booking);
        
        // Notify seats available
        for (BookingEventObserver observer : observers) {
            observer.onSeatsAvailable(booking.getShow());
        }
        
        return true;
    }

    @Override
    public Booking getBookingById(String bookingId) {
        return bookings.get(bookingId);
    }

    // Seat operations
    @Override
    public List<Seat> getAvailableSeats(String showId) {
        Show show = findShowById(showId);
        if (show == null) {
            return new ArrayList<>();
        }
        return show.getScreen().getAllAvailableSeats();
    }

    @Override
    public List<Seat> getAvailableSeatsByType(String showId, SeatType seatType) {
        Show show = findShowById(showId);
        if (show == null) {
            return new ArrayList<>();
        }
        return show.getScreen().getAvailableSeats(seatType);
    }

    // Display operations
    @Override
    public void displayMovies() {
        System.out.println("\n[MOVIES] Available Movies:");
        System.out.println("=" + "=".repeat(50));
        for (Movie movie : movies) {
            System.out.println("[MOVIE] " + movie.getTitle() + " (" + movie.getGenre() + ")");
            System.out.println("    Duration: " + movie.getDurationInMinutes() + " mins | Rating: " + movie.getRating());
            System.out.println("    " + movie.getDescription());
            System.out.println();
        }
    }

    @Override
    public void displayShows(String movieId) {
        List<Show> movieShows = getShowsForMovie(movieId);
        if (movieShows.isEmpty()) {
            System.out.println("No shows available for this movie.");
            return;
        }

        Movie movie = findMovieById(movieId);
        System.out.println("\n[SHOWS] Shows for: " + movie.getTitle());
        System.out.println("=" + "=".repeat(50));
        
        for (Show show : movieShows) {
            System.out.println("[THEATER] " + show.getTheater().getName() + " - " + show.getScreen().getName());
            System.out.println("   Time: " + show.getStartTime());
            System.out.println("   Base Price: ₹" + show.getBasePrice());
            System.out.println("   Available Seats: " + show.getScreen().getAvailableSeatsCount());
            System.out.println();
        }
    }

    @Override
    public void displaySeatLayout(String showId) {
        Show show = findShowById(showId);
        if (show == null) {
            System.out.println("Show not found.");
            return;
        }

        Screen screen = show.getScreen();
        System.out.println("\n[LAYOUT] Seat Layout - " + screen.getName());
        System.out.println("Movie: " + show.getMovie().getTitle());
        System.out.println("Time: " + show.getStartTime());
        System.out.println("=" + "=".repeat(50));
        System.out.println("[O] Available | [X] Booked");
        System.out.println();

        for (int row = 1; row <= screen.getTotalRows(); row++) {
            System.out.printf("Row %2d: ", row);
            for (int col = 1; col <= screen.getSeatsPerRow(); col++) {
                String seatId = String.format("%s-R%d-C%d", screen.getScreenId(), row, col);
                Seat seat = screen.findSeatById(seatId);
                if (seat != null) {
                    String symbol = seat.isAvailable() ? "[O]" : "[X]";
                    System.out.print(symbol + " ");
                }
            }
            System.out.println();
        }
    }

    @Override
    public void displayBookingDetails(String bookingId) {
        Booking booking = bookings.get(bookingId);
        if (booking == null) {
            System.out.println("Booking not found.");
            return;
        }

        System.out.println("\n[BOOKING] Booking Details");
        System.out.println("=" + "=".repeat(50));
        System.out.println("Booking ID: " + booking.getBookingId());
        System.out.println("User ID: " + booking.getUserId());
        System.out.println("Movie: " + booking.getShow().getMovie().getTitle());
        System.out.println("Theater: " + booking.getShow().getTheater().getName());
        System.out.println("Screen: " + booking.getShow().getScreen().getName());
        System.out.println("Show Time: " + booking.getShow().getStartTime());
        System.out.println("Seats: " + booking.getNumberOfSeats());
        System.out.println("Total Amount: ₹" + String.format("%.2f", booking.getTotalAmount()));
        System.out.println("Status: " + booking.getStatus());
        System.out.println("Booking Time: " + booking.getBookingTime());
    }

    // Helper methods
    private Show findShowById(String showId) {
        return shows.stream()
                .filter(show -> show.getShowId().equals(showId))
                .findFirst()
                .orElse(null);
    }

    private Movie findMovieById(String movieId) {
        return movies.stream()
                .filter(movie -> movie.getMovieId().equals(movieId))
                .findFirst()
                .orElse(null);
    }
}