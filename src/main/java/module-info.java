module com.arnab {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jfoenix;

    opens com.arnab to javafx.fxml;
    opens com.arnab.controller;
    opens com.arnab.fxml;
    exports com.arnab;
}
