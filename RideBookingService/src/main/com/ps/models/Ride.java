package main.com.ps.models;

import main.com.ps.enums.RideStatus;

public class Ride {
    private String id;
    private Rider rider;
    private Driver driver;
    private Location from;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private Location to;
    private RideStatus status;
    private double price;

    public Ride(String id, Rider rider, Driver driver, Location from, Location to) {
        this.id = id;
        this.rider = rider;
        this.driver = driver;
        this.from = from;
        this.to = to;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public RideStatus getStatus() {
        return status;
    }

    public void setStatus(RideStatus status) {
        this.status = status;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Location getFrom() {
        return from;
    }

    public void setFrom(Location from) {
        this.from = from;
    }

    public Location getTo() {
        return to;
    }

    public void setTo(Location to) {
        this.to = to;
    }
}

