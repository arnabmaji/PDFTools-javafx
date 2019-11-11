package com.arnab.controller;

import com.arnab.App;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {

    @FXML private JFXButton textToPdfButton;
    @FXML private JFXButton pdfToTextButton;
    private HashMap<JFXButton, String> toolsMap;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        toolsMap = new HashMap<>();
        toolsMap.put(textToPdfButton, "text_to_pdf");
        toolsMap.put(pdfToTextButton, "pdf_to_text");
    }

    @FXML private void showTool(ActionEvent event) throws Exception{
        String toolFxml = toolsMap.get((JFXButton) event.getSource());
        App.setRoot(toolFxml);
    }
}
