package com.busticket.controller;

import com.busticket.util.SceneManager;
import javafx.fxml.FXML;


public class HomeController {

    @FXML
    private void onSearchBus() {
        SceneManager.navigateTo("SearchBus.fxml");
    }

    @FXML
    private void onCancelTicket() {
        SceneManager.navigateTo("CancelBooking.fxml");
    }

    @FXML
    private void onAdminLogin() {
        SceneManager.navigateTo("AdminLogin.fxml");
    }
}
