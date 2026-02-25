package com.busticket.service;

import com.busticket.model.Booking;
import com.busticket.model.Bus;
import com.busticket.model.Passenger;
import com.busticket.model.Route;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class BookingService {

    private static BookingService instance;

    public static BookingService getInstance() {
        if (instance == null) instance = new BookingService();
        return instance;
    }


    private final Map<String, Bus>     buses    = new LinkedHashMap<>();
    private final Map<String, Booking> bookings = new LinkedHashMap<>();


    private static final String ADMIN_USER = "group07";
    private static final String ADMIN_PASS = "Password123";


    private BookingService() {
        seedSampleData();
    }

    private void seedSampleData() {
        Route r1 = new Route("Colombo", "Kandy", 150);
        Route r2 = new Route("Matara", "Colombo", 200);
        Route r3 = new Route("Kandy", "Kurunegala", 350);
        Route r4 = new Route("Matale", "Anuradhapura", 600);

        Bus b1 = new Bus("BUS001", "VT Express", 40, r1, LocalTime.of(6, 0), 350);
        Bus b2 = new Bus("BUS002", "Disanayake Express", 36, r1, LocalTime.of(14, 30), 450);
        Bus b3 = new Bus("BUS003", "Amila Travels", 45, r2, LocalTime.of(7, 0), 300);
        Bus b4 = new Bus("BUS004", "JDK Super Line", 50, r3, LocalTime.of(22, 0), 550);
        Bus b5 = new Bus("BUS005", "Ananda Star Line", 40, r4, LocalTime.of(18, 0), 700);


        b1.bookSeat(1); b1.bookSeat(2); b1.bookSeat(5);
        b3.bookSeat(10); b3.bookSeat(11);

        Arrays.asList(b1, b2, b3, b4, b5).forEach(b -> buses.put(b.getBusId(), b));
    }



    public List<Bus> searchBuses(String from, String to, LocalDate date) {
        return buses.values().stream()
                .filter(b -> b.getRoute().getStartLocation().equalsIgnoreCase(from.trim())
                          && b.getRoute().getDestination().equalsIgnoreCase(to.trim()))
                .collect(Collectors.toList());
    }

    public void addBus(Bus bus) {
        buses.put(bus.getBusId(), bus);
    }

    public Collection<Bus> getAllBuses() {
        return buses.values();
    }

    public Bus getBusById(String busId) {
        return buses.get(busId);
    }

    public boolean busIdExists(String busId) {
        return buses.containsKey(busId);
    }


    public Booking createBooking(Bus bus, Passenger passenger,
                                 LocalDate date, int seatNumber) {
        if (bus.isSeatBooked(seatNumber)) return null;
        boolean reserved = bus.bookSeat(seatNumber);
        if (!reserved) return null;

        Booking booking = new Booking(bus, passenger, date, seatNumber);
        bookings.put(booking.getBookingId(), booking);
        return booking;
    }


    public Booking cancelBooking(String bookingId) {
        Booking booking = bookings.get(bookingId.trim().toUpperCase());
        if (booking == null || booking.isCancelled()) return null;
        booking.cancelBooking();
        return booking;
    }

    public Booking getBookingById(String id) {
        return bookings.get(id.trim().toUpperCase());
    }

    public Collection<Booking> getAllBookings() {
        return bookings.values();
    }


    public boolean authenticateAdmin(String user, String pass) {
        return ADMIN_USER.equals(user) && ADMIN_PASS.equals(pass);
    }



    public List<String> getAllSources() {
        return buses.values().stream()
                .map(b -> b.getRoute().getStartLocation())
                .distinct().sorted().collect(Collectors.toList());
    }

    public List<String> getAllDestinations() {
        return buses.values().stream()
                .map(b -> b.getRoute().getDestination())
                .distinct().sorted().collect(Collectors.toList());
    }
}
