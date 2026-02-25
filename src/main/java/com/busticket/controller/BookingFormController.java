package com.busticket.controller;

import com.busticket.model.Booking;
import com.busticket.model.Bus;
import com.busticket.model.Passenger;
import com.busticket.service.BookingService;
import com.busticket.util.AlertHelper;
import com.busticket.util.SceneManager;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.time.LocalDate;


public class BookingFormController {

    @FXML private Label     summaryLabel;
    @FXML private TextField nameField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private Label     nameError;
    @FXML private Label     phoneError;
    @FXML private Label     emailError;

    private Bus            bus;
    private LocalDate      travelDate;
    private int            seatNumber;
    private final BookingService service = BookingService.getInstance();


    public void initData(Bus bus, LocalDate date, int seatNumber) {
        this.bus        = bus;
        this.travelDate = date;
        this.seatNumber = seatNumber;
        summaryLabel.setText(
                bus.getBusName() + "  |  " +
                        bus.getRoute().getStartLocation() + " → " + bus.getRoute().getDestination() +
                        "  |  Seat " + seatNumber + "  |  Rs." + (int) bus.getFarePerSeat()
        );

        nameField.focusedProperty().addListener((o, old, focused) -> {
            if (!focused) validateName();
        });
        phoneField.focusedProperty().addListener((o, old, focused) -> {
            if (!focused) validatePhone();
        });
        emailField.focusedProperty().addListener((o, old, focused) -> {
            if (!focused) validateEmail();
        });
    }


    private boolean validateName() {
        if (nameField.getText().trim().isEmpty()) {
            nameError.setText("Name is required.");
            return false;
        }
        nameError.setText("");
        return true;
    }

    private boolean validatePhone() {
        if (!phoneField.getText().matches("\\d{10}")) {
            phoneError.setText("Enter a valid 10-digit phone number.");
            return false;
        }
        phoneError.setText("");
        return true;
    }

    private boolean validateEmail() {
        String email = emailField.getText();
        if (!email.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$")) {
            emailError.setText("Enter a valid email address.");
            return false;
        }
        emailError.setText("");
        return true;
    }


    @FXML
    private void onConfirmBooking() {
        boolean ok = validateName() & validatePhone() & validateEmail();
        if (!ok) return;

        Passenger passenger = new Passenger(
                nameField.getText().trim(),
                phoneField.getText().trim(),
                emailField.getText().trim()
        );

        String error = passenger.validateContact();
        if (error != null) {
            AlertHelper.showError("Validation Error", error);
            return;
        }

        Booking booking = service.createBooking(bus, passenger, travelDate, seatNumber);
        if (booking == null) {
            AlertHelper.showError("Booking Failed",
                    "Seat " + seatNumber + " was just taken. Please select another seat.");
            SceneManager.navigateTo("SearchBus.fxml");
            return;
        }

        showBookingConfirmation(booking);
        SceneManager.navigateTo("Home.fxml");
    }


    private void showBookingConfirmation(Booking booking) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Booking Confirmed!");

        VBox content = new VBox(16);
        content.setPadding(new Insets(24, 28, 8, 28));
        content.setAlignment(Pos.CENTER_LEFT);
        content.setPrefWidth(400);

        Text tick = new Text("✅  Booking Confirmed!");
        tick.setStyle("-fx-font-size: 17px; -fx-font-weight: bold; -fx-fill: #16a34a;");

        Label idLabel = new Label("Booking ID");
        idLabel.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #475569;");

        TextField idField = new TextField(booking.getBookingId());
        idField.setEditable(false);       // read-only but still selectable
        idField.setStyle(
                "-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #4f46e5;" +
                        "-fx-background-color: #eef2ff; -fx-border-color: #c7d2fe;" +
                        "-fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 8 12;"
        );
        idField.setPrefWidth(220);

        // Copy button
        Button copyBtn = new Button("Copy");
        copyBtn.setStyle(
                "-fx-background-color: #4f46e5; -fx-text-fill: white;" +
                        "-fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 14; -fx-cursor: hand;"
        );
        copyBtn.setOnAction(e -> {
            idField.selectAll();
            idField.copy();
            copyBtn.setText("✓ Copied!");
            copyBtn.setStyle(
                    "-fx-background-color: #16a34a; -fx-text-fill: white;" +
                            "-fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 14;"
            );
        });

        HBox idRow = new HBox(10, idField, copyBtn);
        idRow.setAlignment(Pos.CENTER_LEFT);

        Label hint = new Label("You can also click the field and press Ctrl+A, Ctrl+C to copy.");
        hint.setStyle("-fx-font-size: 11px; -fx-text-fill: #94a3b8;");
        hint.setWrapText(true);

        String summary = booking.getSummary()
                .replaceFirst("Booking ID : .*\\n", ""); // remove ID line (shown above)
        TextArea summaryArea = new TextArea(summary);
        summaryArea.setEditable(false);
        summaryArea.setWrapText(true);
        summaryArea.setPrefRowCount(7);
        summaryArea.setStyle(
                "-fx-font-size: 13px; -fx-background-color: #f8fafc;" +
                        "-fx-border-color: #e2e8f0; -fx-border-radius: 8; -fx-background-radius: 8;"
        );

        content.getChildren().addAll(tick, idLabel, idRow, hint, summaryArea);

        // ── OK button ─────────────────────────────────────────────────────
        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(okButton);
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().setPrefWidth(460);

        // Auto-select the ID when dialog opens so user sees it's selectable
        dialog.setOnShown(e -> {
            idField.requestFocus();
            idField.selectAll();
        });

        dialog.showAndWait();
    }

    @FXML
    private void onBack() {
        SceneManager.navigateTo("SearchBus.fxml");
    }
}
