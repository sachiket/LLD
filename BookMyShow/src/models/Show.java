package models;

import java.time.LocalDateTime;

/**
 * Represents a movie show at a specific time in a specific screen
 * Links Movie, Screen, and timing together
 */
public class Show {
    private final String showId;
    private final Movie movie;
    private final Screen screen;
    private final Theater theater;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final double basePrice;

    public Show(String showId, Movie movie, Screen screen, Theater theater, 
                LocalDateTime startTime, double basePrice) {
        this.showId = showId;
        this.movie = movie;
        this.screen = screen;
        this.theater = theater;
        this.startTime = startTime;
        this.endTime = startTime.plusMinutes(movie.getDurationInMinutes());
        this.basePrice = basePrice;
    }

    /**
     * Checks if the show is currently active (between start and end time)
     */
    public boolean isActive() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(startTime) && now.isBefore(endTime);
    }

    /**
     * Checks if the show is upcoming
     */
    public boolean isUpcoming() {
        return LocalDateTime.now().isBefore(startTime);
    }

    /**
     * Checks if the show is completed
     */
    public boolean isCompleted() {
        return LocalDateTime.now().isAfter(endTime);
    }

    // Getters
    public String getShowId() {
        return showId;
    }

    public Movie getMovie() {
        return movie;
    }

    public Screen getScreen() {
        return screen;
    }

    public Theater getTheater() {
        return theater;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public double getBasePrice() {
        return basePrice;
    }

    @Override
    public String toString() {
        return String.format("Show{id='%s', movie='%s', screen='%s', theater='%s', startTime=%s, basePrice=%.2f}", 
                           showId, movie.getTitle(), screen.getName(), theater.getName(), startTime, basePrice);
    }
}