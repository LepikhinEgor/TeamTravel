package com.example.lepik.teamtravel;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

public class PersonLocation {
    private String id;
    private double lastLatitude;
    private double lastLongitude;
    private long date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLastLatitude() {
        return lastLatitude;
    }

    public void setLastLatitude(double lastLatitude) {
        this.lastLatitude = lastLatitude;
    }

    public double getLastLongitude() {
        return lastLongitude;
    }

    public void setLastLongitude(double lastLongitude) {
        this.lastLongitude = lastLongitude;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getDateString() {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS", Locale.getDefault());

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);

        return formatter.format(calendar.getTime());
    }

    public double calcDistanceFrom(double latitude, double longitude) {
        //считает расстояние в метрах между двумя точками по алгоритму из Википедии(ортодромия)
        //https://is.gd/dUuAFx

        final int earthRadius = 6371300;

        double distance;
        double angleBetween;
        double angleCos;

        angleCos = sin(toRadians(lastLatitude))* sin(toRadians(latitude));
        angleCos += cos(toRadians(lastLatitude))*cos(toRadians(latitude))*cos(toRadians(longitude - lastLongitude));
        angleBetween = acos(angleCos);

        distance = earthRadius * angleBetween;

        return distance;
    }

    public static String getDifferenceWithCurrTimeStr(long date) {
        long currentTime = System.currentTimeMillis();
        long difference = currentTime - date;

        if (difference < 1000)
            return  "1 s. ago";

        if (difference < 60*1000)
            return Long.toString(difference/1000) + " s. ago";

        if (difference < 60*60*1000)
            return Long.toString(difference/(1000*60)) + " m. ago";

        if (difference < 60*60*24*1000)
            return Long.toString(difference/(1000*60*60)) + " h. ago";

        if (difference < 60*60*24*1000*2)
            return "day ago";

        return Long.toString(difference/(1000*60*60*24)) + " days ago";
    }
}
