package com.busticket.controller;

import com.busticket.model.Booking;
import com.busticket.model.Bus;
import com.busticket.model.Route;
import com.busticket.service.BookingService;
import com.busticket.util.AlertHelper;
import com.busticket.util.SceneManager;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;


public class AdminPanelController implements Initializable {

    @FXML private TextField busIdField;
    @FXML private TextField busNameField;
    @FXML private TextField seatsField;
    @FXML private TextField sourceField;
    @FXML private TextField destinationField;
    @FXML private TextField distanceField;
    @FXML private TextField departureField;   // format HH:mm
    @FXML private TextField fareField;

    @FXML private TableView<Booking>            bookingTable;
    @FXML private TableColumn<Booking, String>  colId;
    @FXML private TableColumn<Booking, String>  colPassenger;
    @FXML private TableColumn<Booking, String>  colBus;
    @FXML private TableColumn<Booking, String>  colRoute;
    @FXML private TableColumn<Booking, Integer> colSeat;
    @FXML private TableColumn<Booking, String>  colDate;
    @FXML private TableColumn<Booking, String>  colStatus;

    private final BookingService service = BookingService.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getBookingId()));
        colPassenger.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getPassengerName()));
        colBus.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getBusName()));
        colRoute.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getRouteDisplay()));
        colSeat.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getSeatNumber()).asObject());
        colDate.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDateDisplay()));
        colStatus.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getStatusDisplay()));

        refreshBookings();
    }


    @FXML
    private void onAddBus() {
        String busId  = busIdField.getText().trim().toUpperCase();
        String name   = busNameField.getText().trim();
        String seats  = seatsField.getText().trim();
        String from   = sourceField.getText().trim();
        String to     = destinationField.getText().trim();
        String dist   = distanceField.getText().trim();
        String dep    = departureField.getText().trim();
        String fare   = fareField.getText().trim();

        if (busId.isEmpty() || name.isEmpty() || seats.isEmpty() || from.isEmpty()
                || to.isEmpty() || dist.isEmpty() || dep.isEmpty() || fare.isEmpty()) {
            AlertHelper.showError("Missing Fields", "All fields are required.");
            return;
        }
        if (service.busIdExists(busId)) {
            AlertHelper.showError("Duplicate ID", "Bus ID already exists: " + busId);
            return;
        }

        int    totalSeats;
        double distance, fareAmt;
        LocalTime departureTime;
        try {
            totalSeats    = Integer.parseInt(seats);
            distance      = Double.parseDouble(dist);
            fareAmt       = Double.parseDouble(fare);
            departureTime = LocalTime.parse(dep, DateTimeFormatter.ofPattern("HH:mm"));
        } catch (NumberFormatException e) {
            AlertHelper.showError("Invalid Number", "Seats, distance, and fare must be numbers.");
            return;
        } catch (DateTimeParseException e) {
            AlertHelper.showError("Invalid Time", "Departure time must be in HH:mm format (e.g. 14:30).");
            return;
        }

        Route route = new Route(from, to, distance);
        Bus bus = new Bus(busId, name, totalSeats, route, departureTime, fareAmt);
        service.addBus(bus);

        AlertHelper.showInfo("Success", "Bus added: " + name);
        clearAddBusForm();
    }

    private void clearAddBusForm() {
        busIdField.clear(); busNameField.clear(); seatsField.clear();
        sourceField.clear(); destinationField.clear(); distanceField.clear();
        departureField.clear(); fareField.clear();
    }


    @FXML
    private void onRefreshBookings() {
        refreshBookings();
    }

    private void refreshBookings() {
        bookingTable.setItems(
            FXCollections.observableArrayList(service.getAllBookings())
        );
    }


    @FXML
    private void onLogout() {
        SceneManager.navigateTo("Home.fxml");
    }
}
