package managers;

import models.ParkingLot;
import java.util.HashMap;
import java.util.Map;

/**
 * Singleton class to manage multiple parking lots
 * Implements Singleton Design Pattern
 */
public class ParkingLotManager {
    private static ParkingLotManager instance;
    private final Map<String, ParkingLot> parkingLots;
    
    private ParkingLotManager() {
        this.parkingLots = new HashMap<>();
    }
    
    /**
     * Gets the singleton instance of ParkingLotManager
     * Thread-safe implementation using synchronized method
     * @return The singleton instance
     */
    public static synchronized ParkingLotManager getInstance() {
        if (instance == null) {
            instance = new ParkingLotManager();
        }
        return instance;
    }
    
    /**
     * Adds a parking lot to the manager
     * @param parkingLot The parking lot to add
     */
    public void addParkingLot(ParkingLot parkingLot) {
        parkingLots.put(parkingLot.getName(), parkingLot);
    }
    
    /**
     * Gets a parking lot by name
     * @param name The name of the parking lot
     * @return The parking lot or null if not found
     */
    public ParkingLot getParkingLot(String name) {
        return parkingLots.get(name);
    }
    
    /**
     * Removes a parking lot from the manager
     * @param name The name of the parking lot to remove
     * @return The removed parking lot or null if not found
     */
    public ParkingLot removeParkingLot(String name) {
        return parkingLots.remove(name);
    }
    
    /**
     * Gets all parking lot names
     * @return Set of parking lot names
     */
    public java.util.Set<String> getAllParkingLotNames() {
        return parkingLots.keySet();
    }
    
    /**
     * Gets the total number of parking lots managed
     * @return Number of parking lots
     */
    public int getTotalParkingLots() {
        return parkingLots.size();
    }
}