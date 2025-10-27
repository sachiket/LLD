package main.com.ps.services;

import main.com.ps.enums.RideStatus;
import main.com.ps.models.*;
import main.com.ps.strategies.matching.NearestDriverStrategy;
import main.com.ps.strategies.matching.RideMatchingStrategy;
import main.com.ps.strategies.pricing.NormalPricing;
import main.com.ps.strategies.pricing.PricingStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RideManager {
    private static RideManager instance;
    private final List<Driver> allDrivers;
    private PricingStrategy pricingStrategy;
    private RideMatchingStrategy rideMatchingStrategy;

    private RideManager() {
        allDrivers = new ArrayList<>();
        pricingStrategy = new NormalPricing();
        rideMatchingStrategy = new NearestDriverStrategy();
    }

    public static RideManager getInstance() {
        if (instance == null) instance = new RideManager();
        return instance;
    }

    public void registerDriver(Driver driver) {
        allDrivers.add(driver);
    }

    public Ride createRide(Rider rider, Location from, Location to) {
        Driver driver = rideMatchingStrategy.findDriver(rider, from, allDrivers);

        if (driver == null) {
            throw new IllegalStateException("No available driver found.");
        }

        Ride ride = new Ride(UUID.randomUUID().toString(), rider, null, from, to);
        ride.setStatus(RideStatus.REQUESTED);

        // Calculate price here, AFTER ride is created
        double price = pricingStrategy.calculatePrice(ride);
        ride.setPrice(price);

        driver.acceptRide(ride); // Driver accepts and updates status, assignment

        System.out.println("Ride created: " + ride.getId() + " by " + rider.getName());
        return ride;
    }

    public void setPricingStrategy(NormalPricing normalPricing) {
        this.pricingStrategy = normalPricing;
    }

    public void setRideMatchingStrategy(NearestDriverStrategy nearestDriverStrategy) {
        rideMatchingStrategy = nearestDriverStrategy;
    }
}
