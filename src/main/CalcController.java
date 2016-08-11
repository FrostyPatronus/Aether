package main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.Effect;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

import java.util.prefs.Preferences;

/**
 * Created by Timothy on 1/9/2016.
 */
public class CalcController extends Main implements Initializable {

    public Button backButton;

    public void setPrefs(){
        prefs.put("bkl", bookLength.getText());
        prefs.put("ppd", pagesPerDay.getText());
        prefs.put("finspan", timeSpan.getText());

        prefs.putDouble("tpp", getBotText(tppMins, tppSecs));
        prefs.putDouble("ded", getBotText(dppHours, dppMinutes));

        prefs.putBoolean("finSel", finSpanTog.isSelected());
        prefs.putBoolean("ppdSel", ppdTog.isSelected());
        prefs.putBoolean("tppSel", tppTog.isSelected());
        prefs.putBoolean("dppSel", tDedTog.isSelected());
        prefs.putBoolean("showTimerSel", showTime.isSelected());
    }
    //BACK ACTIONS
    public void backAction() {
        MediaControl.clickInit();
        setPrefs();
        Main.stage.setScene(Main.mainMenu);
    }

    Effect glow;

    public void backHover() {
        glow = new Bloom();
        MediaControl.hoverInit();
        backButton.setEffect(glow);
    }

    public void backExit() {
        backButton.setEffect(null);
    }

    public void checkSound() {
        MediaControl.hoverInit();
        textFieldHandler();
    }

    Model model = new Model();

    @FXML
    CheckBox decimals;

    public void textFieldHandler() {
        try {
            calculate();
        } catch (NumberFormatException e) {
        }
    }

    double bkLength, finSpan, ppd, tpp, ded;

    @FXML
    TextField bookLength;
    @FXML
    TextField timeSpan;
    @FXML
    TextField pagesPerDay;
    @FXML
    TextField pagesRead;
    @FXML
    SplitPane splitPane;
    @FXML
    TextField dppHours;
    @FXML
    TextField dppMinutes;
    @FXML
    TextField tppMins;
    @FXML
    TextField tppSecs;

    Preferences prefs = Preferences.userRoot().node(this.getClass().getName());

    @FXML
    Text optionalText;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        stage.setOnCloseRequest(event ->{
            setPrefs();
            System.exit(0);
        });
        checkNumber(bookLength, timeSpan, pagesPerDay, pagesRead, dppHours, dppMinutes, tppSecs, tppMins);

        decimals.setSelected(prefs.getBoolean("accurate", false));
        model.setAccurate(decimals.isSelected());

        tDedTog.selectedProperty().addListener((observable, oldValue, newValue) -> {
            dppHours.setEditable(oldValue);
            dppMinutes.setEditable(oldValue);

            tppMins.setEditable(newValue);
            tppSecs.setEditable(newValue);
        });
        pagesRead.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (Double.parseDouble(newValue) >= parse(bookLength)) pagesRead.setText(oldValue);
            }catch (Exception e){}
        });
        finSpanTog.selectedProperty().addListener((observable, oldValue, newValue) -> {
            timeSpan.setEditable(oldValue);
            pagesPerDay.setEditable(newValue);
            if (newValue){
                timeSpan.setId("text-field-selected");
                pagesPerDay.setId(null);
            }
            else{
                timeSpan.setId(null);
                pagesPerDay.setId("text-field-selected");
            }
        });
        dppHours.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (Double.parseDouble(newValue) >= 24) dppHours.setText(oldValue);
            }catch (Exception e){}
        });
        timeSpan.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty() || newValue.equals("0")) return;
            //datePicker.setValue(LocalDate.now().plus(Long.parseLong(newValue), ChronoUnit.DAYS));
        });

        pagesPerDay.setId("text-field-selected");
        pagesPerDay.setId("text-field-selected");
        setColor(tppTog, tppMins, tppSecs);
        setColor(tDedTog, dppHours, dppMinutes);

        bookLength.setText(prefs.get("bkl", ""));
        pagesPerDay.setText(prefs.get("ppd", ""));
        timeSpan.setText(prefs.get("finspan", ""));

        setBotText(prefs.getDouble("tpp", 0), tppMins, tppSecs);
        setBotText(prefs.getDouble("ded", 0), dppHours, dppMinutes);

        finSpanTog.setSelected(prefs.getBoolean("finSel", false));
        ppdTog.setSelected(prefs.getBoolean("ppdSel", true));
        tppTog.setSelected(prefs.getBoolean("tppSel", false));
        tDedTog.setSelected(prefs.getBoolean("dppSel", false));
        showTime.setSelected(prefs.getBoolean("showTimerSel", true));

        if (isSelected(ppdTog)){
            pagesPerDay.setEditable(false);
        } else timeSpan.setEditable(false);

        if (isDownSelected()){
            if (isSelected(tppTog)){
                tppMins.setEditable(false);
                tppSecs.setEditable(false);
            }else{
                dppHours.setEditable(false);
                dppMinutes.setEditable(false);
            }
        }
    }

    private void setColor (CheckBox box, TextField large, TextField small){
        Bloom bloom = new Bloom();
        box.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                large.setId("text-right-selected");
                small.setId("text-right-selected");
            }else{
                large.setId("text-field-irregular");
                small.setId("text-field-irregular");
            }
            if (isDownSelected()){
                optionalText.setFill(Paint.valueOf("#e9ff00"));
                optionalText.setEffect(bloom);
            }
            else{
                optionalText.setFill(Paint.valueOf("white"));
                optionalText.setEffect(null);
            }
        });
    }

    public void sceneInit() {
        splitPane.lookupAll(".split-pane-divider").stream().forEach(d -> d.setMouseTransparent(true));
    }

    public void checkNumber(TextField... textFields) {
        for (TextField value : textFields) {
            value.textProperty().addListener((v, oldValue, newValue) -> {
                if (newValue.isEmpty()) return;
                if (newValue.substring(newValue.length() - 1).equals("d") ||
                        newValue.substring(newValue.length() - 1).equals("f")
                        && value != pagesRead)
                    value.setText(oldValue);
                try {
                    Double.valueOf(newValue);
                } catch (Exception e) {
                    value.setText(oldValue);
                }
                if (!(value == tppMins ||
                        value == tppSecs ||
                        value == dppMinutes ||
                        value == dppHours)) textFieldHandler();
            });
        }
    }
    public void calculate() {
        if (isEmpty(bookLength)) return;
        bkLength = parse(bookLength) - parse(pagesRead);
        model.setBookLength(bkLength);
        LocalDate date = LocalDate.now();
        if (!isEmpty(timeSpan) && !isDownSelected() && isSelected(ppdTog)) {
            finSpan = parse(timeSpan);
            model.setFinSpan(finSpan);
            pagesPerDay.setText(model.setPpd(bkLength, finSpan));
        } else if (!isEmpty(pagesPerDay) && !isDownSelected() && isSelected(finSpanTog)) {
            ppd = parse(pagesPerDay);
            timeSpan.setText(model.setFinspan(bkLength, ppd));
        }

        datePicker.setValue(date.plus((long) model.getFinSpan(), ChronoUnit.DAYS));
        timeSpan.positionCaret(timeSpan.getText().length());

        if (isDownSelected()) {
            if (!isEmpty(timeSpan) && isSelected(ppdTog)) {
                finSpan = parse(timeSpan);
                ppd = Double.parseDouble(model.setPpd(bkLength, finSpan));
                pagesPerDay.setText(model.setPpd(bkLength, finSpan));
            } else if (!isEmpty(pagesPerDay) && isSelected(finSpanTog)) {
                ppd = parse(pagesPerDay);
                finSpan = Double.parseDouble(model.setFinspan(bkLength, ppd));
                timeSpan.setText(model.setFinspan(bkLength, ppd));
            }

            if ((!isEmpty(dppMinutes) || !isEmpty(dppHours)) && isSelected(tppTog)) {
                ded = getBotText(dppHours, dppMinutes);
                model.setTpp(ded, ppd);
                setBotText(model.getTpp(), tppMins, tppSecs);
            }
            if ((!(isEmpty(tppMins)) || !isEmpty(tppSecs)) && isSelected(tDedTog)) {
                tpp = getBotText(tppMins, tppSecs);
                model.setDed(ppd, tpp);
                setBotText(model.getDed(), dppHours, dppMinutes);
            }
        }
    }

    boolean isStarting;
    double time;

    @FXML
    Button timerButt;
    @FXML
    CheckBox showTime;

    private ActionListener start = e -> {
        if(showTime.isSelected()){
            setBotText(++time, tppMins, tppSecs);
            textFieldHandler();
        }
        else{
            try {
                if (!isEmpty(tppMins, tppSecs)){
                    tppMins.clear();
                    tppSecs.clear();
                }
                else if (!tppMins.getText().isEmpty())tppMins.clear();
                else if (!tppSecs.getText().isEmpty()) tppSecs.clear();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            ++time;
        }
    };
    Timer timer = new Timer(1000, start);

    public void startTimer() {
        if (isSelected(tppTog) || !isDownSelected()){
            tppTog.setSelected(false);
            tDedTog.setSelected(true);
        }
        isStarting = !isStarting;
        if (isStarting) {
            timer.start();
            timerButt.setStyle("-fx-background-color: #801600");
            timerButt.setText("Stop timer");
            tppTog.setDisable(true);
        } else {
            timer.stop();
            setBotText(time, tppMins, tppSecs);
            tppTog.setDisable(false);
            time = 0;
            timerButt.setStyle(null);
            timerButt.setText("Start Timer");
        }
    }

    private void setBotText(double time, TextField field1, TextField field2) {
        double field1Value = (int) Math.floor(time / 60);
        double field2Value = (int) time % 60;
        field1.setText(String.valueOf((int) Math.ceil(field1Value)));
        field2.setText(String.valueOf((int) Math.ceil(field2Value)));
        if (field2Value >= 60) {
            field1.setText(String.valueOf((int) (field1Value + Math.floor(field2Value / 60))));
            field2.setText(String.valueOf((int) parse(field2) % 60));
        }
    }

    private double getBotText(TextField botField1, TextField botField2) {
        if (parse(botField2) >= 60) {
            botField1.setText(String.valueOf((int) (parse(botField1) + Math.floor(parse(botField2) / 60))));
            botField2.setText(String.valueOf((int) parse(botField2) % 60));
        }
        if (isEmpty(botField1)) return parse(botField2);
        else if (isEmpty(botField2)) return parse(botField1) * 60;
        else return (parse(botField1) * 60) + parse(botField2);
    }

    public Button clearButton;

    public void clear() {
        isStarting = true;
        startTimer();
        TextField[] textFields = {bookLength, timeSpan, pagesPerDay, tppMins, tppSecs, dppHours, dppMinutes};
        for (TextField textField : textFields) {
            textField.clear();
        }
        pagesRead.setText("0");
        ppdTog.setSelected(true);
        tppTog.setSelected(false);
        tDedTog.setSelected(false);
    }

    public void buttonEnter(MouseEvent event) {
        MediaControl.hoverInit();
        Button button = (Button) event.getSource();
        button.setId("button-highlight");
    }

    public void buttonExit(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setId("button-regular");
    }

    private double parse(TextField field) {
        if (isEmpty(field)) {
            return 0;
        }
        return Double.parseDouble(field.getText());
    }

    public static boolean isEmpty(TextField... textFields) {
        boolean check = true;
        for (TextField field : textFields) {
            check = field.getText().isEmpty();
            if (!check) break;
        }
        return check;
    }

    public void setAccurate() {
        MediaControl.hoverInit();
        model.setAccurate(decimals.isSelected());
        prefs.putBoolean("accurate", model.isAccurate());
        textFieldHandler();
    }

    @FXML
    public void select(MouseEvent event) {
        TextField field = (TextField) event.getSource();
        field.requestFocus();
        field.positionCaret(0);
        field.selectAll();
    }

    @FXML
    DatePicker datePicker;

    long finalDiff;

    public void datePickerOnAction() {
        LocalDate fromTime = LocalDate.now();
        LocalDate toTime = datePicker.getValue();
        finalDiff = ChronoUnit.DAYS.between(fromTime, toTime);
        if (finalDiff >= 1) {
            timeSpan.setText(Long.toString(finalDiff));
        } else {
            timeSpan.clear();
        }
    }

    @FXML
    ToggleGroup topGroup;
    @FXML
    RadioButton finSpanTog, ppdTog;
    @FXML
    CheckBox tppTog, tDedTog;

    public void downControl(ActionEvent event) {
        MediaControl.hoverInit();
        CheckBox check = (CheckBox) event.getSource();
        if (check == tppTog) {
            tppSecs.setEditable(false);
            tppMins.setEditable(false);
            if (isSelected(tDedTog)) tDedTog.setSelected(false);
            //tDedTog.setSelected(false);
        } else if (check == tDedTog) {
            dppHours.setEditable(false);
            dppMinutes.setEditable(false);
            if (isSelected(tppTog)) tppTog.setSelected(false);
        }
    }

    private boolean isSelected(CheckBox checkBox) {
        return checkBox.isSelected();
    }

    private boolean isSelected(RadioButton radioButton) {
        return radioButton.isSelected();
    }

    public boolean isDownSelected() {
        return (isSelected(tppTog) || isSelected(tDedTog));
    }

    @FXML Button helpButton;
    public void helpAction(){

    }
}
