package main.com.ps.models;

import main.com.ps.enums.RideStatus;

public class Driver {
    private String id;
    private String name;
    private Location currentLocation;
    private boolean isAvailable;

    public Driver(String id, String name, Location currentLocation) {
        this.id = id;
        this.name = name;
        this.currentLocation = currentLocation;
        this.isAvailable = true;
    }

    public void acceptRide(Ride ride) {
        if (!isAvailable) {
            throw new IllegalStateException("Driver is not available.");
        }

        if (ride.getStatus() != RideStatus.REQUESTED) {
            throw new IllegalArgumentException("Ride is not in REQUESTED state.");
        }

        ride.setDriver(this);
        ride.setStatus(RideStatus.ACCEPTED);
        this.isAvailable = false;

        System.out.println(name + " accepted the ride.");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}