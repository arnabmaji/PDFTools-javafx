package com.arnab.controller;

import com.arnab.App;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.tools.ExportXFDF;

import java.io.File;
import java.io.PrintWriter;

public class PdfToTextController {

    @FXML private StackPane stackPane;
    @FXML private ImageView sourceFileSelectedImageView;
    @FXML private ImageView destinationFolderSelectedImageView;
    @FXML private JFXTextField fileExtensionTextField;
    @FXML private JFXButton sourceFileSelectorButton;
    @FXML private JFXButton destinationFolderSelectorButton;


    private File selectedFile;
    private File selectedDestinationFolder;

    @FXML
    private void selectSourceFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if(selectedFile == null || !selectedFile.isFile() || !selectedFile.canRead()){
            var layout = new JFXDialogLayout();
            layout.setBody(new Text("Error while opening file!"));
            new JFXDialog(stackPane, layout, JFXDialog.DialogTransition.BOTTOM).show();
            return;
        }
        this.selectedFile = selectedFile;
        sourceFileSelectedImageView.setVisible(true);
        sourceFileSelectorButton.setText("file selected");
    }

    @FXML
    private void selectDestinationFolder(){
        File selectedDestinationFolder = new DirectoryChooser().showDialog(new Stage());
        if(selectedDestinationFolder == null || !selectedDestinationFolder.isDirectory() || !selectedDestinationFolder.canWrite()){
            var layout = new JFXDialogLayout();
            layout.setBody(new Text("Error while selecting folder!"));
            new JFXDialog(stackPane, layout, JFXDialog.DialogTransition.BOTTOM).show();
            return;
        }
        this.selectedDestinationFolder = selectedDestinationFolder;
        destinationFolderSelectedImageView.setVisible(true);
        destinationFolderSelectorButton.setText("folder selected");
    }


    @FXML
    private void generateText() {
        var layout = new JFXDialogLayout();
        if (selectedFile == null) {
            layout.setBody(new Text("Select Source File"));
            new JFXDialog(stackPane, layout, JFXDialog.DialogTransition.BOTTOM).show();
            return;
        }

        String fileExtension = fileExtensionTextField.getText();
        if(fileExtension.isBlank()){
            layout.setBody(new Text("file extension can't be blank!"));
            new JFXDialog(stackPane, layout, JFXDialog.DialogTransition.BOTTOM).show();
            return;
        }

        if (selectedDestinationFolder == null) {
            layout.setBody(new Text("Select Destination Folder"));
            new JFXDialog(stackPane, layout, JFXDialog.DialogTransition.BOTTOM).show();
            return;
        }

        layout.setBody(new Text("File generation in progress..."));
        var dialog = new JFXDialog(stackPane, layout, JFXDialog.DialogTransition.BOTTOM);
        dialog.show();
        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(event -> {
            dialog.close();
            try{
                App.setRoot("main_window");
            } catch (Exception e){
                e.printStackTrace();
            }
        });
        delay.play();

        new Thread(() -> {
            try{
                String fileNameWithExtension = selectedFile.getName() + "." + fileExtension;
                File file = selectedFile;
                String parsedText;
                PDFParser parser = new PDFParser(new RandomAccessFile(file,"r"));
                parser.parse();
                COSDocument document = parser.getDocument();
                PDFTextStripper stripper = new PDFTextStripper();
                PDDocument pdDocument = new PDDocument(document);
                parsedText = stripper.getText(pdDocument);
                PrintWriter writer = new PrintWriter(selectedDestinationFolder.getAbsolutePath() + "/" + fileNameWithExtension);
                writer.write(parsedText);
                writer.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }).start();
    }

}
