package com.busticket.controller;

import com.busticket.model.Bus;
import com.busticket.service.BookingService;
import com.busticket.util.AlertHelper;
import com.busticket.util.SceneManager;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class SearchBusController implements Initializable {

    @FXML private ComboBox<String> sourceCombo;
    @FXML private ComboBox<String> destCombo;
    @FXML private DatePicker       datePicker;
    @FXML private Button           searchBtn;
    @FXML private Button           selectBtn;
    @FXML private Label            statusLabel;

    @FXML private TableView<Bus>             busTable;
    @FXML private TableColumn<Bus, String>   colBusName;
    @FXML private TableColumn<Bus, String>   colDeparture;
    @FXML private TableColumn<Bus, Integer>  colAvailable;
    @FXML private TableColumn<Bus, String>   colFare;

    private final BookingService service = BookingService.getInstance();


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sourceCombo.setItems(FXCollections.observableArrayList(service.getAllSources()));
        destCombo.setItems(FXCollections.observableArrayList(service.getAllDestinations()));
        datePicker.setValue(LocalDate.now());

        colBusName.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getBusName()));
        colDeparture.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getDepartureTime().toString()));
        colAvailable.setCellValueFactory(d ->
                new SimpleIntegerProperty(d.getValue().getAvailableSeats()).asObject());
        colFare.setCellValueFactory(d ->
                new SimpleStringProperty("Rs." + (int) d.getValue().getFarePerSeat()));

        selectBtn.setDisable(true);
        busTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, old, selected) -> selectBtn.setDisable(selected == null));
    }


    @FXML
    private void onSearch() {
        String from = sourceCombo.getValue();
        String to   = destCombo.getValue();
        LocalDate date = datePicker.getValue();

        if (from == null || to == null || date == null) {
            AlertHelper.showError("Missing Input", "Please select source, destination, and date.");
            return;
        }
        if (from.equalsIgnoreCase(to)) {
            AlertHelper.showError("Invalid Route", "Source and destination cannot be the same.");
            return;
        }
        if (date.isBefore(LocalDate.now())) {
            AlertHelper.showError("Invalid Date", "Travel date cannot be in the past.");
            return;
        }

        List<Bus> results = service.searchBuses(from, to, date);
        busTable.setItems(FXCollections.observableArrayList(results));

        if (results.isEmpty()) {
            statusLabel.setText("No buses found for this route.");
        } else {
            statusLabel.setText(results.size() + " bus(es) found.");
        }
    }

    @FXML
    private void onSelectBus() {
        Bus selected = busTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        if (!selected.checkAvailability()) {
            AlertHelper.showError("Sold Out", "No seats available on this bus.");
            return;
        }

        FXMLLoader loader = SceneManager.switchScene("SeatSelection.fxml");
        SeatSelectionController ctrl = loader.getController();
        ctrl.initData(selected, datePicker.getValue());
    }

    @FXML
    private void onBack() {
        SceneManager.navigateTo("Home.fxml");
    }
}
