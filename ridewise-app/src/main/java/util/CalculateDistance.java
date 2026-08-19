package util;

public class CalculateDistance {

    private CalculateDistance() { }

    private static final double EARTH_RADIUS_KM = 6371.0;

    public static double calculateDistanceInKm(double riderLat,
                                               double riderLon ,
                                               double driverLat,
                                               double driverLon){



        double lat1Rad = Math.toRadians(riderLat);
        double lon1Rad = Math.toRadians(riderLon);
        double lat2Rad = Math.toRadians(driverLat);
        double lon2Rad = Math.toRadians(driverLon);

        double deltaLat = lat2Rad - lat1Rad;
        double deltaLon = lon2Rad - lon1Rad;

        // Haversine formula calculation
        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
                + Math.cos(lat1Rad) * Math.cos(lat2Rad)
                * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));


        return EARTH_RADIUS_KM * c;
    }


}
