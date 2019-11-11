package com.arnab.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.jfoenix.controls.*;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Spinner;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class TextToPdfController implements Initializable {

    @FXML private StackPane stackPane;
    @FXML private ToggleGroup fontStyleGroup;
    @FXML private ToggleGroup paperSizeGroup;
    @FXML private Spinner<Integer> fontSizeSpinner;
    @FXML private JFXButton textFileSelectorButton;
    @FXML private JFXButton destinationFolderSelectorButton;
    @FXML private ImageView sourceFileSelectedImage;
    @FXML private ImageView destinationFolderSelectedImage;

    private File sourceFile;
    private File destinationFolder;
    private HashMap<String, Integer> fontStyleMap;
    private HashMap<String, Rectangle> pageSizeMap;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fontStyleMap = new HashMap<>();
        pageSizeMap = new HashMap<>();

        fontStyleMap.put("Normal", Font.NORMAL);
        fontStyleMap.put("Bold", Font.BOLD);
        fontStyleMap.put("Italic", Font.ITALIC);
        fontStyleMap.put("Bold Italic", Font.BOLDITALIC);

        pageSizeMap.put("A3", PageSize.A3);
        pageSizeMap.put("A4", PageSize.A4);
        pageSizeMap.put("Letter", PageSize.LETTER);
        pageSizeMap.put("Legal", PageSize.LEGAL);
    }

    @FXML private void selectSourceFile(){
        var layout = new JFXDialogLayout();

        File selectedFile = new FileChooser().showOpenDialog(new Stage());
        if(selectedFile == null || !selectedFile.isFile() || !selectedFile.canRead()){
            layout.setBody(new Text("Error while opening file!"));
            new JFXDialog(stackPane, layout, JFXDialog.DialogTransition.CENTER);
            return;
        }
        textFileSelectorButton.setText("Text file selected");
        sourceFileSelectedImage.setVisible(true);
        sourceFile = selectedFile;

    }

    @FXML private void selectDestinationFolder(){
        var layout = new JFXDialogLayout();
        File selectedDestination = new DirectoryChooser().showDialog(new Stage());
        if(selectedDestination == null || !selectedDestination.isDirectory() || !selectedDestination.canRead()){
            layout.setBody(new Text("Error "));
            new JFXDialog(stackPane, layout, JFXDialog.DialogTransition.CENTER).show();
            return;
        }
        destinationFolder = selectedDestination;
        destinationFolderSelectorButton.setText("Destination folder selected");
        destinationFolderSelectedImage.setVisible(true);
    }

    @FXML private void generatePDF() {
        var layout = new JFXDialogLayout();
        if(sourceFile == null){
            layout.setBody(new Text("please select source file!"));
            new JFXDialog(stackPane, layout, JFXDialog.DialogTransition.CENTER).show();
            return;
        }
        if(destinationFolder == null){
            layout.setBody(new Text("please select destination folder!"));
            new JFXDialog(stackPane, layout, JFXDialog.DialogTransition.CENTER).show();
            return;
        }

        layout.setBody(new Text("PDF generation in progress..."));
        var dialog = new JFXDialog(stackPane, layout, JFXDialog.DialogTransition.BOTTOM);
        dialog.show();
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(event -> dialog.close());
        delay.play();
        new Thread(() -> {
            String pageSize = ((JFXRadioButton) paperSizeGroup.getSelectedToggle()).getText();
            String fontStyle = ((JFXRadioButton) fontStyleGroup.getSelectedToggle()).getText();

            String pdfFilePath = destinationFolder.getAbsolutePath() + "/" + sourceFile.getName() + ".pdf";

            Document pdfDoc = new Document(pageSizeMap.get(pageSize));
            try{
                PdfWriter.getInstance(pdfDoc, new FileOutputStream(pdfFilePath))
                        .setPdfVersion(PdfWriter.PDF_VERSION_1_7);
                pdfDoc.open();
                Font font = new Font();
                font.setStyle(fontStyleMap.get(fontStyle));
                font.setSize(fontSizeSpinner.getValue());
                pdfDoc.add(new Paragraph("\n"));
                try(BufferedReader reader = new BufferedReader(new FileReader(sourceFile))){
                    String line;
                    while((line = reader.readLine()) != null){
                        Paragraph paragraph = new Paragraph(line + "\n", font);
                        paragraph.setAlignment(Element.ALIGN_JUSTIFIED);
                        pdfDoc.add(paragraph);
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            } finally {
                pdfDoc.close();
            }
        }).start();
    }

}
