package model;

import java.time.Instant;
import java.util.Objects;

public class FareReceipt {

    private String rideId;
    private double amount;
    private Instant generatedAt;

    public FareReceipt() { }

    public FareReceipt(String rideId, double amount, Instant generatedAt) {
        this.rideId = rideId;
        this.amount = amount;
        this.generatedAt = (generatedAt != null) ? generatedAt : Instant.now();
    }

    public String getRideId() {
        return rideId;
    }

    public void setRideId(String rideId) {
        this.rideId = rideId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Instant getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(Instant generatedAt) {
        this.generatedAt = generatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FareReceipt that = (FareReceipt) o;
        return Objects.equals(rideId, that.rideId) &&
                Objects.equals(amount, that.amount) &&
                Objects.equals(generatedAt, that.generatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rideId, amount, generatedAt);
    }

    @Override
    public String toString() {
        return "FareReceipt{" +
                "rideId='" + rideId + '\'' +
                ", amount=" + amount +
                ", generatedAt=" + generatedAt +
                '}';
    }


}
