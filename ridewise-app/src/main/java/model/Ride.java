package model;

import util.GenerateRideId;

public class Ride {

    private String rideId;
    private Rider rider;
    private Driver driver;
    private RideStatus status;
    private double distance;
    private VehicleType vehicleType;
    private Location destination;

    public Ride() {this.rideId = GenerateRideId.getRideId();}

    public Ride(String rideId, Rider rider, Driver driver, RideStatus status, double distance) {
        this.rideId = rideId;
        this.rider = rider;
        this.driver = driver;
        this.status = status;
        this.distance = distance;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public RideStatus getStatus() {
        return status;
    }

    public void setStatus(RideStatus status) {
        this.status = status;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Rider getRider() {
        return rider;
    }

    public void setRider(Rider rider) {
        this.rider = rider;
    }

    public String getRideId() {
        return rideId;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Location getDestination() {
        return destination;
    }

    public void setDestination(Location destination) {
        this.destination = destination;
    }
}
