package com.busticket.model;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;


public class Bus {

    private final String busId;
    private String busName;
    private int totalSeats;
    private Route route;
    private LocalTime departureTime;
    private double farePerSeat;

    private final Set<Integer> bookedSeats = new HashSet<>();

    public Bus(String busId, String busName, int totalSeats,
               Route route, LocalTime departureTime, double farePerSeat) {
        this.busId = busId;
        this.busName = busName;
        this.totalSeats = totalSeats;
        this.route = route;
        this.departureTime = departureTime;
        this.farePerSeat = farePerSeat;
    }


    public String getDetails() {
        return String.format("[%s] %s | %s | Dep: %s | Seats: %d/%d | Rs.%.0f",
                busId, busName, route,
                departureTime.format(DateTimeFormatter.ofPattern("hh:mm a")),
                getAvailableSeats(), totalSeats, farePerSeat);
    }

    public int getAvailableSeats() {
        return totalSeats - bookedSeats.size();
    }

    public boolean checkAvailability() {
        return getAvailableSeats() > 0;
    }

    public boolean bookSeat(int seatNumber) {
        if (seatNumber < 1 || seatNumber > totalSeats) return false;
        return bookedSeats.add(seatNumber);
    }

    public boolean releaseSeat(int seatNumber) {
        return bookedSeats.remove(seatNumber);
    }

    public boolean isSeatBooked(int seatNumber) {
        return bookedSeats.contains(seatNumber);
    }


    public String getBusId()               { return busId; }
    public String getBusName()             { return busName; }
    public void setBusName(String n)       { this.busName = n; }
    public int getTotalSeats()             { return totalSeats; }
    public void setTotalSeats(int s)       { this.totalSeats = s; }
    public Route getRoute()                { return route; }
    public void setRoute(Route r)          { this.route = r; }
    public LocalTime getDepartureTime()    { return departureTime; }
    public void setDepartureTime(LocalTime t) { this.departureTime = t; }
    public double getFarePerSeat()         { return farePerSeat; }
    public void setFarePerSeat(double f)   { this.farePerSeat = f; }
    public Set<Integer> getBookedSeats()   { return bookedSeats; }

    @Override
    public String toString() { return getDetails(); }
}
