package com.busticket.controller;

import com.busticket.model.Booking;
import com.busticket.service.BookingService;
import com.busticket.util.AlertHelper;
import com.busticket.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class CancelBookingController {

    @FXML private TextField bookingIdField;
    @FXML private Label     resultLabel;

    private final BookingService service = BookingService.getInstance();

    @FXML
    private void onCancel() {
        String id = bookingIdField.getText().trim();
        if (id.isEmpty()) {
            resultLabel.setText("⚠ Please enter a Booking ID.");
            resultLabel.getStyleClass().setAll("result-error");
            return;
        }

        Booking booking = service.getBookingById(id);
        if (booking == null) {
            resultLabel.setText("✗ Booking ID not found: " + id);
            resultLabel.getStyleClass().setAll("result-error");
            return;
        }
        if (booking.isCancelled()) {
            resultLabel.setText("⚠ This booking was already cancelled.");
            resultLabel.getStyleClass().setAll("result-error");
            return;
        }

        boolean confirmed = AlertHelper.showConfirm("Confirm Cancellation",
            "Cancel booking for " + booking.getPassenger().getName() + "?\n" +
            "Bus: " + booking.getBusName() + "  |  Seat: " + booking.getSeatNumber());

        if (!confirmed) return;

        service.cancelBooking(id);
        resultLabel.setText("✓ Booking " + id + " cancelled successfully.");
        resultLabel.getStyleClass().setAll("result-success");
        bookingIdField.clear();
    }

    @FXML
    private void onBack() {
        SceneManager.navigateTo("Home.fxml");
    }
}
