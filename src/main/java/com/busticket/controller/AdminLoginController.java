package com.busticket.controller;

import com.busticket.service.BookingService;
import com.busticket.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


public class AdminLoginController {

    @FXML private TextField     usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label         errorLabel;

    private final BookingService service = BookingService.getInstance();

    @FXML
    private void onLogin() {
        String user = usernameField.getText().trim();
        String pass = passwordField.getText();

        if (service.authenticateAdmin(user, pass)) {
            SceneManager.navigateTo("AdminPanel.fxml");
        } else {
            errorLabel.setText("Invalid username or password.");
        }
    }

    @FXML
    private void onBack() {
        SceneManager.navigateTo("Home.fxml");
    }
}
