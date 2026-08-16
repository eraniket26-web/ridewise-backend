package model;

import util.GenerateRiderId;

public class Rider extends Person {

    private final String riderId;
    private Location location;

    public Rider() {
        location = new Location();
        this.riderId = GenerateRiderId.getRiderId();
    }

    public Rider(String name, long contactNo, Location location){
        super(name,contactNo);
        this.riderId = GenerateRiderId.getRiderId();
        this.location = (location!= null) ? location : new Location();

    }

    public String getRiderId() {
        return riderId;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }


    @Override
    public String toString() {
        return "Rider{" +
                "riderId='" + riderId + '\'' +
                ", location=" + location +
                ", name=" + getName() +
                ", contact=" + getContactNo() +
                '}';
    }
}
