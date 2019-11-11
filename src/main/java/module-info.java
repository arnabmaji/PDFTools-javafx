module com.arnab {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jfoenix;
    requires itextpdf;
    requires pdfbox.tools;
    requires pdfbox;
    requires pdfbox.debugger;

    opens com.arnab to javafx.fxml;
    opens com.arnab.controller;
    opens com.arnab.fxml;
    exports com.arnab;
}
