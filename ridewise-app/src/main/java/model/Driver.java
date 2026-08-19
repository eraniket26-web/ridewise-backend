package model;

import util.GenerateDriverId;

import java.util.ArrayList;
import java.util.List;

public class Driver extends Person{

    private final String driverId;
    private Location location;
    private boolean available;
    private final List<Ride> rides = new ArrayList<>();

    public Driver() {
        location = new Location();
        driverId = GenerateDriverId.getDriverId();
    }

    public Driver(String driverId, String name, long contactNo, Location location){
        this.driverId = driverId;
        super(name,contactNo);
        this.location = (location!= null) ? location : new Location();
    }

    public String getDriverId() {
        return driverId;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "Driver{" +
                "name= " + getName() +
                "contact no= " + getContactNo() +
                "driverId='" + driverId + '\'' +
                ", location=" + location +
                ", available=" + available +
                '}';
    }

    public void addRide(Ride ride) {
        rides.add(ride);
    }

    public long getCompletedRideCount() {
        return rides.stream()
                .filter(ride -> ride.getStatus() == RideStatus.COMPLETED)
                .count();
    }

    public List<Ride> getRides(){
        return rides;
    }



}
