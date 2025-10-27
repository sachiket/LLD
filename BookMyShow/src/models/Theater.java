package models;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a theater in the BookMyShow system
 * Similar to ParkingLot in the reference implementation
 */
public class Theater {
    private final String theaterId;
    private final String name;
    private final String location;
    private final List<Screen> screens;

    public Theater(String theaterId, String name, String location) {
        this.theaterId = theaterId;
        this.name = name;
        this.location = location;
        this.screens = new ArrayList<>();
    }

    /**
     * Adds a screen to the theater
     */
    public void addScreen(Screen screen) {
        this.screens.add(screen);
    }

    /**
     * Finds a screen by its ID
     */
    public Screen findScreenById(String screenId) {
        for (Screen screen : screens) {
            if (screen.getScreenId().equals(screenId)) {
                return screen;
            }
        }
        return null;
    }

    /**
     * Gets total available seats across all screens
     */
    public int getTotalAvailableSeats() {
        int total = 0;
        for (Screen screen : screens) {
            total += screen.getAvailableSeatsCount();
        }
        return total;
    }

    /**
     * Gets total seats across all screens
     */
    public int getTotalSeats() {
        int total = 0;
        for (Screen screen : screens) {
            total += screen.getTotalSeats();
        }
        return total;
    }

    // Getters
    public String getTheaterId() {
        return theaterId;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public List<Screen> getScreens() {
        return screens;
    }

    @Override
    public String toString() {
        return String.format("Theater{id='%s', name='%s', location='%s', screens=%d, totalSeats=%d}", 
                           theaterId, name, location, screens.size(), getTotalSeats());
    }
}