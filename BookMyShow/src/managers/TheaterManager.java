package managers;

import models.Theater;
import java.util.HashMap;
import java.util.Map;

/**
 * Singleton class to manage multiple theaters
 * Implements Singleton Design Pattern - similar to ParkingLotManager
 */
public class TheaterManager {
    private static TheaterManager instance;
    private final Map<String, Theater> theaters;
    
    private TheaterManager() {
        this.theaters = new HashMap<>();
    }
    
    /**
     * Gets the singleton instance of TheaterManager
     * Thread-safe implementation using synchronized method
     */
    public static synchronized TheaterManager getInstance() {
        if (instance == null) {
            instance = new TheaterManager();
        }
        return instance;
    }
    
    /**
     * Adds a theater to the manager
     */
    public void addTheater(Theater theater) {
        theaters.put(theater.getTheaterId(), theater);
    }
    
    /**
     * Gets a theater by ID
     */
    public Theater getTheater(String theaterId) {
        return theaters.get(theaterId);
    }
    
    /**
     * Removes a theater from the manager
     */
    public Theater removeTheater(String theaterId) {
        return theaters.remove(theaterId);
    }
    
    /**
     * Gets all theater IDs
     */
    public java.util.Set<String> getAllTheaterIds() {
        return theaters.keySet();
    }
    
    /**
     * Gets the total number of theaters managed
     */
    public int getTotalTheaters() {
        return theaters.size();
    }
}