package com.busticket.model;


public class Route {

    private String startLocation;
    private String destination;
    private double distanceKm;

    public Route(String startLocation, String destination, double distanceKm) {
        this.startLocation = startLocation;
        this.destination = destination;
        this.distanceKm = distanceKm;
    }


    public String getStartLocation() { return startLocation; }
    public void setStartLocation(String startLocation) { this.startLocation = startLocation; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public double getDistanceKm() { return distanceKm; }
    public void setDistanceKm(double distanceKm) { this.distanceKm = distanceKm; }

    @Override
    public String toString() {
        return startLocation + " → " + destination + " (" + distanceKm + " km)";
    }
}
