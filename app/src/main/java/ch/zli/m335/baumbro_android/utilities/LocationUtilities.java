package ch.zli.m335.baumbro_android.utilities;


import java.util.HashMap;

public class LocationUtilities {

    // Adapted Haversine Formula https://www.geeksforgeeks.org/haversine-formula-to-find-distance-between-two-points-on-a-sphere/
    public static HashMap<String, Float> calculateQueryValues(double longitude, double latitude, int distance) {
        // Earth's mean radius in meters
        final double earthRadius = 6371000;

        final double degreesToMeters = earthRadius * Math.PI / 180;

        final double deltaDegrees = distance / degreesToMeters;

        double minLatitude = latitude - deltaDegrees;
        double maxLatitude = latitude + deltaDegrees;
        double minLongitude = longitude - deltaDegrees / Math.cos(Math.toRadians(latitude));
        double maxLongitude = longitude + deltaDegrees / Math.cos(Math.toRadians(latitude));

        HashMap<String, Float> maxMinValues = new HashMap<String, Float>();

        maxMinValues.put("minLongitude", (float) minLongitude);
        maxMinValues.put("maxLongitude", (float) maxLongitude);
        maxMinValues.put("minLatitude", (float) minLatitude);
        maxMinValues.put("maxLatitude", (float) maxLatitude);

        return maxMinValues;
    }
}