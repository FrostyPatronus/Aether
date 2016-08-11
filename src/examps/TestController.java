package examps;

import examps.Timer;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class TestController {

    public DatePicker datePicker;
    public Label showDiff;
    LocalDate fromTime, toTime;
    long finalDifference;

    /*public void datePickerOnAction() {
        fromTime = LocalDate.now();
        toTime = datePicker.getValue();

        finalDifference = ChronoUnit.DAYS.between(fromTime, toTime);

        if (finalDifference >= 1){
            showDiff.setStyle("-fx-text-fill: black");
            showDiff.setText(Long.toString(finalDifference));}
        else{
            showDiff.setStyle("-fx-text-fill: red");
            showDiff.setText("Error");}
    }*/

    public Label timer;
    Timer stopWatch = new Timer();

    public void startTimer(){
        stopWatch.timerStart(timer);
    }

    public void stopTimer(){
        stopWatch.timerStop();
    }

}
