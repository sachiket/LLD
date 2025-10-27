package main.com.ps;

import main.com.ps.models.Driver;
import main.com.ps.models.Location;
import main.com.ps.models.Ride;
import main.com.ps.models.Rider;
import main.com.ps.services.RideManager;
import main.com.ps.strategies.matching.NearestDriverStrategy;
import main.com.ps.strategies.pricing.NormalPricing;

public class Main {
    public static void main(String[] args) {
        // --- 1. Setup ---
        RideManager rideManager = RideManager.getInstance();

        // Create drivers with locations and register them
        Driver d1 = new Driver("D1", "Alice", new Location(12.96, 77.60));
        Driver d2 = new Driver("D2", "Bob", new Location(13.01, 77.62));
        Driver d3 = new Driver("D3", "Eve", new Location(12.95, 77.61));  // This one is very close

        // Register them with RideManager
        rideManager.registerDriver(d1);
        rideManager.registerDriver(d2);
        rideManager.registerDriver(d3);

        // --- Set Pricing and Matching Strategies ---
        rideManager.setPricingStrategy(new NormalPricing());
        rideManager.setRideMatchingStrategy(new NearestDriverStrategy());

        // --- 2. Rider Requests Ride ---
        Rider rider = new Rider("R1", "Charlie");

        Location from = new Location(12.95, 77.61); // pickup (same as D3)
        Location to = new Location(13.02, 77.64);   // drop

        try {
            Ride ride = rider.requestRide(from, to);

            // --- 3. Output Ride Info ---
            System.out.println("\n--- Ride Details ---");
            System.out.println("Ride ID     : " + ride.getId());
            System.out.println("Rider       : " + rider.getName());
            System.out.println("Driver      : " + ride.getDriver().getName());
            System.out.println("From        : (" + from.getLatitude() + ", " + from.getLongitude() + ")");
            System.out.println("To          : (" + to.getLatitude() + ", " + to.getLongitude() + ")");
            System.out.println("Status      : " + ride.getStatus());
            System.out.printf ("Price (INR) : ₹%.2f\n", ride.getPrice());

        } catch (IllegalStateException e) {
            System.err.println("❌ Ride could not be created: " + e.getMessage());
        }
    }
}
