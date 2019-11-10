package com.arnab.controller;

import com.arnab.App;
import javafx.fxml.FXML;

public class MainWindowController {

    @FXML private void textToPDFConverter() throws Exception{
        App.setRoot("text_to_pdf");
    }
}
