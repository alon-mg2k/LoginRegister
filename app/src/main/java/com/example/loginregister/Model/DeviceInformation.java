package com.example.loginregister.Model;

public class DeviceInformation {

    private String name, state, datetime, username;
    private String distance, stepCounter, personCounter;
    private String co2, x, y, z;

    public DeviceInformation() {

    }

    public DeviceInformation(String name, String state, String datetime, String distance, String stepCounter,String username, String co2, String x, String y, String z, String personCounter) {
        this.co2 = co2;
        this.datetime = datetime;
        this.distance = distance;
        this.name = name;
        this.state = state;
        this.personCounter = personCounter;
        this.stepCounter = stepCounter;
        this.username = username;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String dist) {
        this.distance = dist;
    }

    public String getStepCounter() {
        return stepCounter;
    }

    public void setStepCounter(String stepCounter) {
        this.stepCounter = stepCounter;
    }

    public String getCo2() {
        return co2;
    }

    public void setCo2(String co2) {
        this.co2 = co2;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getZ() {
        return z;
    }

    public void setZ(String z) {
        this.z = z;
    }

    public String getPersonCounter() { return personCounter; }

    public void setPersonCounter(String personCounter) { this.personCounter = personCounter; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }
}