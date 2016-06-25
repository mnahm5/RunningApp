package com.example.mnahm5.runningapp;

import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;

public class CalculateRunData {
    public String getStopWatchTime() {
        return "00:00:00";
    }

    public double getDistance(LatLng start, LatLng end) {
        final int R = 6371; // Radius of the earth
        double lat1 = start.latitude;
        double lat2 = start.latitude;
        double lon1 = end.longitude;
        double lon2 = end.longitude;
        Double latDistance = Math.toRadians(lat2 - lat1);
        Double lonDistance = Math.toRadians(lon2 - lon1);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c * 1000; // convert to meters
    }

    public double getCaloriesBurned(double distance, int time, int weight) {
        double speed = getSpeedInKmHr(distance/time);
        double caloriesBurned;
        if (speed > 8) {
            caloriesBurned = weight*speed*1.023;
        }
        else {
            caloriesBurned = weight*speed*0.723;
        }
        return caloriesBurned;
    }

    public double getSpeedInKmHr(double speedInMS) {
        return speedInMS*3600/1000;
    }

    public float round(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00#");
        return Float.parseFloat(decimalFormat.format(value));
    }
}
