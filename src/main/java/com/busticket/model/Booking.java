package com.busticket.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;


public class Booking {

    private final String bookingId;
    private final Bus bus;
    private final Passenger passenger;
    private final LocalDate travelDate;
    private final int seatNumber;
    private boolean cancelled;

    public Booking(Bus bus, Passenger passenger, LocalDate travelDate, int seatNumber) {
        this.bus        = bus;
        this.passenger  = passenger;
        this.travelDate = travelDate;
        this.seatNumber = seatNumber;
        this.cancelled  = false;
        this.bookingId  = generateBookingId();
    }


    private String generateBookingId() {
        String uid = UUID.randomUUID().toString().toUpperCase().replace("-", "");
        return "BUS-" + uid.substring(0, 8);
    }

    public boolean cancelBooking() {
        if (cancelled) return false;
        cancelled = true;
        bus.releaseSeat(seatNumber);
        return true;
    }

    public String getSummary() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM yyyy");
        return String.format(
            "Booking ID : %s%n" +
            "Passenger  : %s%n" +
            "Bus        : %s%n" +
            "Route      : %s → %s%n" +
            "Date       : %s%n" +
            "Seat No    : %d%n" +
            "Fare       : Rs.%.0f%n" +
            "Status     : %s",
            bookingId,
            passenger.getName(),
            bus.getBusName(),
            bus.getRoute().getStartLocation(),
            bus.getRoute().getDestination(),
            travelDate.format(fmt),
            seatNumber,
            bus.getFarePerSeat(),
            cancelled ? "CANCELLED" : "CONFIRMED"
        );
    }


    public String getBookingId()    { return bookingId; }
    public Bus getBus()             { return bus; }
    public Passenger getPassenger() { return passenger; }
    public LocalDate getTravelDate(){ return travelDate; }
    public int getSeatNumber()      { return seatNumber; }
    public boolean isCancelled()    { return cancelled; }

    public String getPassengerName()    { return passenger.getName(); }
    public String getBusName()          { return bus.getBusName(); }
    public String getRouteDisplay()     { return bus.getRoute().getStartLocation() + " → " + bus.getRoute().getDestination(); }
    public String getDateDisplay()      { return travelDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy")); }
    public String getStatusDisplay()    { return cancelled ? "Cancelled" : "Confirmed"; }
}
