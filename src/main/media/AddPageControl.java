package main.media;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import main.BookModel;
import main.InfoGatherControl;
import main.MyBookController;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by Timothy on 1/20/2016.
 */
public class AddPageControl implements Initializable{

    public static int readPages;
    @FXML
    Button addBook;
    @FXML
    TextField textField;
    @FXML
    Label labelPrompt;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        labelPrompt.setText(formatPPD(MyBookController.selected.getPagesPerDay()));
    }

    private String formatPPD(int PPD){
        if (PPD <= 1){
            return "(You needed to read " + PPD + " page per day)";
        }else {
            return "(You needed to read " + PPD + " pages per day)";
        }
    }

    public void mouseHover() {
        addBook.setId("button-highlight");
    }

    public void mouseExit() {
        addBook.setId(null);
    }

    public void addBook(){
        try {
            readPages = Integer.parseInt(textField.getText());
        }catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please enter a number");
            alert.setHeaderText("Error");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("You read " + readPages + " pages today");
        alert.setContentText("Confirm?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            MyBookController.selected.getReadPagesList().add(readPages);
            MyBookController.selected.setPagesRead(MyBookController.selected.getPagesRead() + readPages);
            try {
                MyBookController.saveBooks(MyBookController.wrap());
            }catch (Exception e){
                e.printStackTrace();
            }
            System.out.println("CLOSE DAMMIT");
        }

    }

}
