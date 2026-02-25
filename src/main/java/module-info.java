module com.busticket {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.busticket to javafx.fxml;
    opens com.busticket.controller to javafx.fxml;
    opens com.busticket.model to javafx.base;

    exports com.busticket;
    exports com.busticket.controller;
    exports com.busticket.model;
    exports com.busticket.service;
    exports com.busticket.util;
}
