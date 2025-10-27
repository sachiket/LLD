package main.com.ps.models;

import main.com.ps.services.RideManager;

public class Rider {
    private String id;
    private String name;

    public Rider(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Ride requestRide(Location from, Location to) {
        return RideManager.getInstance().createRide(this, from, to);
    }
}
