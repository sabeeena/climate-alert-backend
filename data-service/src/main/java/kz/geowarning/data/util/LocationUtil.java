package kz.geowarning.data.util;

public class LocationUtil {
    private static final double EARTH_RADIUS_KM = 6371.0;

    private static double toRadians(double degrees) {
        return degrees * (Math.PI / 180);
    }

    // Calculates the Haversine distance between two points
    public static double haversine(double lat1, double lon1, double lat2, double lon2) {
        double dLat = toRadians(lat2 - lat1);
        double dLon = toRadians(lon2 - lon1);
        double rLat1 = toRadians(lat1);
        double rLat2 = toRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(rLat1) * Math.cos(rLat2) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }

    public static boolean isWithinRange(double userLat, double userLon, double quakeLat, double quakeLon, double rangeKm) {
        double distance = haversine(userLat, userLon, quakeLat, quakeLon);
        return distance <= rangeKm;
    }
}
