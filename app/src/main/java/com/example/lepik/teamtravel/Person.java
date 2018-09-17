package com.example.lepik.teamtravel;

import java.io.Serializable;

import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

public class Person implements Serializable{
    private String id;
    private String email;
    private String password;
    private String firstName;
    private String secondName;
    private double latitudeCoord;
    private double longitudeCoord;

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public Double getLatitudeCoord() {
        return latitudeCoord;
    }

    public void setLatitudeCoord(Double latitudeCoord) {
        this.latitudeCoord = latitudeCoord;
    }

    public Double getLongitudeCoord() {
        return longitudeCoord;
    }

    public void setLongitudeCoord(Double longitudeCoord) {
        this.longitudeCoord = longitudeCoord;
    }

    public String getID() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double calcDistanceFrom(double latitude, double longitude) {
        //считает расстояние в метрах между двумя точками по алгоритму из Википедии(ортодромия)
        //https://is.gd/dUuAFx
        final double meridianDegreeLength = 111134.856;

        double distance;
        double angleBetween;
        double angleCos;

        angleCos = sin(toRadians(latitudeCoord))* sin(toRadians(latitude));
        angleCos += cos(toRadians(latitudeCoord))*cos(toRadians(latitude))*cos(toRadians(longitude - longitudeCoord));
        angleBetween = acos(angleCos);

        distance = 6371300 * angleBetween;

        return distance;
    }

}

