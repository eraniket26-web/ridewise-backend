package util;

public class GenerateDriverId {

   private GenerateDriverId() { }

    private static int driverIdCounter = 0;

    public static String getDriverId() {
        return String.format("DRIVER%04d", ++driverIdCounter);

    }
}
