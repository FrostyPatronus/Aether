package main;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Observable;
import java.util.Optional;
import java.util.prefs.Preferences;

/**
 * Created by Timothy on 1/19/2016.
 */
public class InfoGatherControl extends Main {
    @FXML
    TextField bookName,
            bookLength,
            tppMins,
            tppSecs,
            finSpan;
    @FXML
    Button startTimer,
            clear,
            addBook;
    @FXML
    DatePicker datePicker;
    @FXML
    CheckBox showTimer;

    Preferences prefs = Preferences.userRoot().node(this.getClass().getName());

    public void initialize() {
        showTimer.setSelected(prefs.getBoolean("showTimer?", true));
        TextField[] fields = {bookLength, tppMins, tppSecs, finSpan};
        for (TextField textField : fields) {
            textField.textProperty().addListener((v, oldValue, newValue) -> {
                if (newValue.isEmpty()) return;
                if (newValue.substring(newValue.length() - 1).equals("d") ||
                        newValue.substring(newValue.length() - 1).equals("f") ||
                        newValue.length() == 5)
                    textField.setText(oldValue);
                try {
                    Double.valueOf(newValue);
                } catch (Exception e) {
                    textField.setText(oldValue);
                }
            });
        }

        datePicker.valueProperty().addListener(((observable, oldValue, newValue) -> {
            if (ChronoUnit.DAYS.between(LocalDate.now(), newValue) < 0) return;
            finSpan.setText(Long.toString(ChronoUnit.DAYS.between(LocalDate.now(), newValue)));
        }));

        finSpan.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty() || newValue.equals("0")) return;
            datePicker.setValue(LocalDate.now().plus(Long.parseLong(newValue), ChronoUnit.DAYS));
        });

        tppSecs.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) return;
            if(Integer.parseInt(newValue) >= 60){
                int value = Integer.parseInt(newValue);
                tppMins.setText(String.valueOf(parse(tppMins) + (int)Math.floor((double)value / 60)));
                tppSecs.setText(String.valueOf(value % 60));
            }
        });

        tppMins.textProperty().addListener(((observable, oldValue, newValue) -> {
            if(Integer.parseInt(newValue) >= 60){
                tppMins.setText(oldValue);
            }
        }));
}

    public void addBook() {
        MediaControl.hoverInit();
        if (isEmpty(bookName, bookLength, finSpan) || (isEmpty(tppMins) && isEmpty(tppSecs) ||
        timer.isRunning())){
            Alert error = new Alert(Alert.AlertType.ERROR);
            if (timer.isRunning()) {
                error.setHeaderText("Your timer is running");
            } else {
                error.setHeaderText("You forgot to fill in a field.");
            }
            error.setTitle("Oops!");
            error.showAndWait();
        }
        else {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setHeaderText("Book Name: " + bookName.getText()
                    + "\nBook Length: " + bookLength.getText()
                    + "\nCompletion Date: " + datePicker.getValue().format(BookModel.DATE_FORMAT)
                    + "\nTime per Page: " + getTPP());
            Optional<ButtonType> result = confirm.showAndWait();
            if (result.get() == ButtonType.OK){
                BookModel temp = new BookModel(bookName.getText(),
                        parse(bookLength),
                        (int)ChronoUnit.DAYS.between(LocalDate.now(), datePicker.getValue()),
                        getTppValue(tppMins, tppSecs));
                MyBookController.bookData.add(temp);

            }
        }
    }

    public String getTPP(){
        return String.format("%d minutes, %d seconds", parse(tppMins), parse(tppSecs));
    }

    public int getTppValue(TextField mins, TextField secs){
        return (parse(mins) * 60) + parse(secs);
    }

    int time;
    private ActionListener start = e -> {
        if (showTimer.isSelected()){
            tppSecs.setText(String.valueOf(++time));
        }
        else{
            tppSecs.clear();
            tppMins.clear();
            time++;
        }
    };
    Timer timer = new Timer(1000, start);
    boolean isStarting;

    public void startTimer() {
        MediaControl.hoverInit();
        if (!isStarting){
            tppSecs.clear();
            tppMins.clear();
            time = 0;
            timer.start();
            startTimer.setText("Stop Timer");
            startTimer.setStyle("-fx-background-color: #801600");
        }else {
            timer.stop();
            startTimer.setText("Start Timer");
            startTimer.setStyle(null);
            tppSecs.setText(String.valueOf(time));
        }
        isStarting = !isStarting;

    }

    public void clickShowTimer(){
        prefs.putBoolean("showTimer?", showTimer.isSelected());
    }

    public void clear() {
        MediaControl.hoverInit();
        TextField[] fields = {bookName, bookLength, tppMins, tppSecs, finSpan};
        if (timer.isRunning()){
            timer.stop();
            isStarting = true;
            startTimer();
        }
        for(TextField field : fields){
            field.clear();
        }
    }

    public void mouseHover(MouseEvent mouse) {
        Button button = (Button) mouse.getSource();
        if (button == clear) clear.setStyle("-fx-border-color: #e9ff00");
        else button.setId("button-highlight");
    }

    public void mouseExit(MouseEvent mouse) {
        Button button = (Button) mouse.getSource();
        if (button == clear) clear.setStyle(null);
        else button.setId(null);
    }

    public boolean isEmpty(TextField... textFields){
        boolean check = false;
        for (TextField textField : textFields) {
            if (textField != bookName)
                check = textField.getText().isEmpty() || Integer.parseInt(textField.getText()) == 0 ;
            else
                check = textField.getText().isEmpty();
            if (check) break;
        }
        return check;
    }

    public int parse(TextField field){
        if (field.getText().isEmpty()) return 0;
        return Integer.parseInt(field.getText());
    }
}
