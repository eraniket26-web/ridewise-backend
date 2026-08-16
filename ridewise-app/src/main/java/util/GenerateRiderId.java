package util;

public class GenerateRiderId {

    private GenerateRiderId() { }

    private static int riderIdCounter = 0;

    public static String getRiderId(){
        return String.format("R%04d",++riderIdCounter);
    }
}