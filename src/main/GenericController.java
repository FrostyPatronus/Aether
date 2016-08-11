package main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

/**
 * Created by Timothy on 1/7/2016.
 */

public class GenericController extends Main {

    @FXML
    Button myBooks;
    @FXML
    Button quickCalculation;
    @FXML
    Button helpButton;

    MediaControl media = new MediaControl();

    public void sceneStart() {
        media.setButtonMedia(myBooks, quickCalculation, helpButton);
    }

    public void bookAction() {
        MediaControl.clickInit();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("MyBooks.fxml"));
            stage.setScene(new Scene(root, Main.X, Main.Y));
        }catch (Exception e){
        }

    }

    public void calcAction() {
        MediaControl.clickInit();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Bang.fxml"));
            stage.setScene(new Scene(root, Main.X, Main.Y));
        }catch (Exception e){}

    }

    public void helpAction() {
        MediaControl.clickInit();
    }
}