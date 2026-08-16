package util;

public class GenerateRideId {

    private GenerateRideId() { }

    private static int rideIdCounter = 0;

    public static String getRideId(){
        return String.format("R%04d",++rideIdCounter);
    }
}
