package com.busticket.controller;

import com.busticket.model.Bus;
import com.busticket.util.AlertHelper;
import com.busticket.util.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;


public class SeatSelectionController {

    @FXML private Label    busInfoLabel;
    @FXML private Label    selectedSeatLabel;
    @FXML private Button   proceedBtn;
    @FXML private GridPane seatGrid;

    private Bus       bus;
    private LocalDate travelDate;
    private int       selectedSeat = -1;

    private static final String STYLE_AVAILABLE = "seat-available";
    private static final String STYLE_BOOKED    = "seat-booked";
    private static final String STYLE_SELECTED  = "seat-selected";



    public void initData(Bus bus, LocalDate date) {
        this.bus        = bus;
        this.travelDate = date;
        busInfoLabel.setText(bus.getBusName() + "  |  " +
                bus.getRoute().getStartLocation() + " → " + bus.getRoute().getDestination() +
                "  |  Rs." + (int) bus.getFarePerSeat());
        selectedSeatLabel.setText("No seat selected");
        proceedBtn.setDisable(true);
        buildSeatGrid();
    }


    private void buildSeatGrid() {
        seatGrid.getChildren().clear();
        int total  = bus.getTotalSeats();
        int cols   = 4; // 2 seats | aisle | 2 seats layout
        int rows   = (int) Math.ceil(total / (double) cols);

        int seatNum = 1;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (seatNum > total) break;

                int gridCol = (col < 2) ? col : col + 1;

                Button seatBtn = createSeatButton(seatNum);
                seatGrid.add(seatBtn, gridCol, row);
                seatNum++;
            }
        }
    }

    private Button createSeatButton(int seatNum) {
        Button btn = new Button(String.valueOf(seatNum));
        btn.setPrefSize(52, 44);
        btn.getStyleClass().add("seat-btn");

        if (bus.isSeatBooked(seatNum)) {
            btn.getStyleClass().add(STYLE_BOOKED);
            btn.setDisable(true);
        } else {
            btn.getStyleClass().add(STYLE_AVAILABLE);
            btn.setOnAction(e -> onSeatClicked(seatNum, btn));
        }
        return btn;
    }

    private Button lastSelected = null;

    private void onSeatClicked(int seatNum, Button btn) {
        if (lastSelected != null) {
            lastSelected.getStyleClass().remove(STYLE_SELECTED);
            lastSelected.getStyleClass().add(STYLE_AVAILABLE);
        }

        selectedSeat = seatNum;
        btn.getStyleClass().remove(STYLE_AVAILABLE);
        btn.getStyleClass().add(STYLE_SELECTED);
        lastSelected = btn;

        selectedSeatLabel.setText("Selected: Seat " + seatNum);
        proceedBtn.setDisable(false);
    }


    @FXML
    private void onProceed() {
        if (selectedSeat == -1) {
            AlertHelper.showError("No Seat", "Please select a seat first.");
            return;
        }
        FXMLLoader loader = SceneManager.switchScene("BookingForm.fxml");
        BookingFormController ctrl = loader.getController();
        ctrl.initData(bus, travelDate, selectedSeat);
    }

    @FXML
    private void onBack() {
        SceneManager.navigateTo("SearchBus.fxml");
    }
}
