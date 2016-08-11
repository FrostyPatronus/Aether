package main;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by Timothy on 1/17/2016.
 */
public class MyBookController extends Main{
    @FXML
    public TableView<BookModel> tableView;
    @FXML
    private TableColumn<BookModel, String> bookNameColumn;
    @FXML
    private TableColumn<BookModel, Integer> debtColumn;
    @FXML
    private TableColumn<BookModel, Integer> ppdColumn;

    @FXML
    private Button deleteButton,
            editButton;

    @FXML
    private ProgressBar progressBar;
    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private Label bookLength,
            finSpan,
            startDate,
            readTime,
            pagesRead,
            pagePerDay,
            debt,
            daysPast,
            bookName;

    public static ObservableList<BookModel> bookData;
    public static BookWrapper wrapper;

    public static BookModel selected;

    @FXML
    private void initialize() {
        File file = new File("Save.bin");
        if (!file.exists()) {
            try {
                file.createNewFile();
                saveBooks(new BookWrapper());
                System.out.println("I MADE A FILE");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        bookNameColumn.setCellValueFactory(param -> param.getValue().bookNameProperty());
        debtColumn.setCellValueFactory(param -> param.getValue().debtProperty().asObject());
        ppdColumn.setCellValueFactory(param -> param.getValue().pagesPerDayProperty().asObject());
        progressBar.progressProperty().bindBidirectional(progressIndicator.progressProperty());

        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->{
            setBook(newValue);
            System.out.println(newValue.getPagesRead());
            selected = newValue;
            lineChart.getData().clear();
            lineChart.setTitle(newValue.getBookName());
            lineChart.getData().add(plotLine(newValue.getReadPagesList()));
        });

        try {
            if (loadBooks() != null) {
                wrapper = loadBooks();
                bookData = FXCollections.observableArrayList(wrapper.getBooks());
                tableView.setItems(bookData);
                tableView.getSelectionModel().select(0);
                setListener(bookData);
            }
            System.out.printf("Good path");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Overall Catch bruh");
        }

        bookData.addListener(new ListChangeListener<BookModel>() {
            @Override
            public void onChanged(Change<? extends BookModel> c) {
                BookWrapper wrapper = new BookWrapper();
                wrapper.setBooks(new ArrayList<>(bookData));
                try {
                    saveBooks(wrapper);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                setBook(selected);
                setListener(bookData);
            }
        });

        if (bookData.isEmpty()) emptyFields();
    }

    public void setListener(ObservableList<BookModel> models){
        for(BookModel model : models){
            model.pagesReadProperty().addListener((observable, oldValue, newValue) -> {
                setBook(selected);
                lineChart.getData().clear();
                lineChart.getData().add(plotLine(model.getReadPagesList()));
                System.out.println("It works i guess");
            });
        }
    }

    public XYChart.Series<String, Integer> plotLine(ObservableList<Integer> readPageList){
        XYChart.Series<String, Integer> line = new XYChart.Series<>();
        int x = 0;
        for (int read : readPageList) {
            line.getData().add(new XYChart.Data<>("Day " + x, read));
            x++;
        }
        return line;
    }

    public static void saveBooks(BookWrapper wrapper) throws IOException {
        ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream("Save.bin"));
        stream.writeObject(wrapper);
        stream.close();
    }

    public void onSelectionChanged(){
        try{
            setBook(selected);
        }catch (NullPointerException e){
            System.out.println("ignore this lol");
        }
    }

    public static BookWrapper loadBooks() throws IOException, ClassNotFoundException {
        try {
            ObjectInputStream stream = new ObjectInputStream(new FileInputStream("Save.bin"));
            BookWrapper wrap = (BookWrapper) stream.readObject();
            for (BookModel wrapper : wrap.getBooks())
                wrapper.update();
            stream.close();
            return wrap;
        } catch (EOFException eofe) {
            return null;
        }
    }

    public static BookWrapper wrap(){
        wrapper.getBooks().clear();
        wrapper.setBooks(new ArrayList<>(bookData));
        return wrapper;
    }

    public static Stage stage1;

    @FXML
    public void addRow() throws IOException {
        stage = infoGather("Information Harvester");
        stage.showAndWait();
    }

    Stage infoGather(String message) throws IOException {
        Stage dialogueStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("InfoGather.fxml"));
        dialogueStage.setScene(new Scene(root));
        dialogueStage.setTitle(message);
        dialogueStage.initModality(Modality.APPLICATION_MODAL);
        dialogueStage.getIcons().add(Main.ICON);
        dialogueStage.initOwner(stage);
        dialogueStage.setResizable(false);
        return dialogueStage;
    }

    private void emptyFields() {
        bookName.setText("Book Name");
        finSpan.setText("");
        bookLength.setText("");
        startDate.setText("");
        readTime.setText("00:00");
        pagesRead.setText("");
        pagePerDay.setText("");
        debt.setText("");
        daysPast.setText("");
        progressIndicator.setProgress(0);
    }

    public void setBook(BookModel book) {
        bookName.setText(book.getBookName());
        finSpan.setText(book.getDateFinish().format(BookModel.DATE_FORMAT));
        bookLength.setText(String.valueOf(book.getBookLength()));
        startDate.setText(book.getDateCreated().format(BookModel.DATE_FORMAT));
        readTime.setText(formatDed(book.getReadTimePerDay()));
        pagesRead.setText(String.valueOf(book.getPagesRead()));
        pagePerDay.setText(String.valueOf(book.getPagesPerDay()));
        debt.setText(String.valueOf(book.getDebt()));
        daysPast.setText(String.valueOf(book.getDaysPast()));
        progressIndicator.setProgress(book.getProgress());
    }

    public void deleteAction() {
        int index = tableView.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            bookData.remove(index);
        }
    }

    public String formatDed(int time) {
        if (time > 60)
            return (Math.floor(time / 60) == 1) ? (int) Math.floor(time / 60) + " hour : " + time % 60 + " minutes" :
                    (int) Math.floor(time / 60) + " hours : " + time % 60 + " minutes";
        else
            return (String.valueOf(time).length() == 1) ? "00 : 0" + time : "00 : " + time;
    }

    @FXML
    LineChart<String, Integer> lineChart;

    @FXML
    public void addPage() throws Exception{
        if(!tableView.getSelectionModel().isEmpty()) {
            Stage dialogueStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("BookRead.fxml"));
            dialogueStage.setScene(new Scene(root));
            dialogueStage.initModality(Modality.APPLICATION_MODAL);
            dialogueStage.getIcons().add(Main.ICON);
            dialogueStage.initOwner(stage);
            dialogueStage.setResizable(false);
            dialogueStage.showAndWait();
        }
    }
}
