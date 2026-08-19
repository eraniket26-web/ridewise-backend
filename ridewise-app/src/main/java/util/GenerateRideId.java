package util;

public class GenerateRideId {

    private GenerateRideId() { }

    private static int rideIdCounter = 0;

    public static String getRideId(){
        return String.format("RIDE%04d",++rideIdCounter);
    }
}
