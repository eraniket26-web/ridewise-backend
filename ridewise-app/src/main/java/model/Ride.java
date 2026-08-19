package model;

import util.GenerateRideId;

public class Ride {

    private final String rideId;
    private Rider rider;
    private Driver driver;
    private RideStatus status;
    private double distance;
    private VehicleType vehicleType;
    private Location destination;
    private FareReceipt fareReceipt;

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

    public FareReceipt getFareReceipt() {
        return fareReceipt;
    }

    public void setFareReceipt(FareReceipt fareReceipt) {
        this.fareReceipt = fareReceipt;
    }

    @Override
    public String toString() {
        return "Ride{" +
                "rideId='" + rideId + '\'' +
                ", rider=" + rider +
                ", driver=" + driver +
                ", status=" + status +
                ", distance=" + distance +
                ", vehicleType=" + vehicleType +
                ", destination=" + destination +
                ", fareReceipt=" + fareReceipt +
                '}';
    }
}
