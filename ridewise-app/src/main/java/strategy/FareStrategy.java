package strategy;

import model.Ride;
import model.VehicleType;

public interface FareStrategy {

    double calculateFare(Ride ride);
}
