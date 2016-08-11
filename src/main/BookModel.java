package main;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

/**
 * Created by Timothy on 1/19/2016.
 */
public class BookModel implements Serializable{
    //Primitives
    public String bookNameSer;
    public int bookLengthSer;
    public int finishSpanSer;
    public int pagesPerDaySer;
    public int timePerPageSer;
    public int readTimePerDaySer;
    public int pagesReadSer;
    public int debtSer;
    public int daysPastSer;
    public double progressSer;
    public boolean isBehindSer;
    public LocalDate dateCreatedSer;
    public LocalDate dateFinishSer;
    public ArrayList<Integer> readPagesListSer = new ArrayList<>();

    //Properties
    transient private StringProperty bookName;
    transient private IntegerProperty bookLength;
    transient private IntegerProperty finishSpan;
    transient private IntegerProperty pagesPerDay;
    transient private IntegerProperty timePerPage;
    transient private IntegerProperty readTimePerDay;

    transient private IntegerProperty pagesRead;
    transient private IntegerProperty debt;
    transient private IntegerProperty daysPast;

    transient private DoubleProperty progress;

    transient private BooleanProperty isBehind;
    transient private ObjectProperty<LocalDate> dateCreated;
    transient private ObjectProperty<LocalDate> dateFinish;

    transient private ObservableList<Integer> readPagesList;
    transient static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy / dd / MM");

    public BookModel(String bookName, int bookLength, int finishSpan, int timePerPage) {
        dateCreatedSer = LocalDate.now();
        bookNameSer = bookName;
        bookLengthSer = bookLength;
        finishSpanSer = finishSpan;
        timePerPageSer = timePerPage;
        dateFinishSer = dateCreatedSer.plusDays((long) finishSpan);
        pagesPerDaySer = calculatePagePerDay(bookLengthSer, finishSpanSer);
        readTimePerDaySer = calculateDedication(pagesPerDaySer, timePerPageSer);
        pagesReadSer = 0;
        daysPastSer = calculateDaysPast(dateCreatedSer);
        debtSer = calculateDebt(pagesReadSer, pagesPerDaySer, daysPastSer);
        isBehindSer = calculateIsBehind(debtSer);
        readPagesListSer.add(0, 0);
        progressSer = 0;
        update();
    }

    public void update(){
        dateCreated = new SimpleObjectProperty<>(dateCreatedSer);
        bookName = new SimpleStringProperty(bookNameSer);
        bookLength = new SimpleIntegerProperty(bookLengthSer);
        finishSpan = new SimpleIntegerProperty(finishSpanSer);
        timePerPage = new SimpleIntegerProperty(timePerPageSer);
        dateFinish = new SimpleObjectProperty<>(dateFinishSer);
        pagesPerDay = new SimpleIntegerProperty(pagesPerDaySer);
        readTimePerDay = new SimpleIntegerProperty(readTimePerDaySer);
        pagesRead = new SimpleIntegerProperty(pagesReadSer);
        daysPast = new SimpleIntegerProperty(daysPastSer);
        debt = new SimpleIntegerProperty(debtSer);
        isBehind = new SimpleBooleanProperty(isBehindSer);
        readPagesList = FXCollections.observableArrayList(readPagesListSer);
        progress = new SimpleDoubleProperty(progressSer);

        pagesRead.addListener((observable, oldValue, newValue) -> {
            setPagesRead((int)newValue);
            pagesReadSer = getPagesRead();
            setDaysPast(getDaysPast() + 1);
            daysPastSer = getDaysPast();
            setProgress(calculateProgress(getBookLength(), getPagesRead()));
            progressSer = getProgress();
            setPagesPerDay(calculatePagePerDay(calculateNewBookLength(getBookLength(), getPagesRead())
                    , getFinishSpan()));
            pagesPerDaySer = getPagesPerDay();
            setDebt(calculateDebt(getPagesRead(), getPagesPerDay(), getDaysPast()));
            debtSer = getDebt();
            readPagesListSer = new ArrayList<>(getReadPagesList());
            System.out.println("YOU UPDATED ME");
        });
    }

    public static int calculateNewBookLength(int bookLength, int pagesRead){
        return bookLength - pagesRead;
    }
    public static int calculatePagePerDay(int length, int finspan){
        return (int)Math.ceil((double)length / finspan);
    }
    public static int calculateDedication(int ppd, int tpp){
        return (int)Math.ceil((double)(ppd*tpp)/60);
    }

    public static int calculateDaysPast(LocalDate dateCreated){
        return (int)ChronoUnit.DAYS.between(dateCreated, LocalDate.now());
    }

    public static int calculateDebt(int pagesRead, int ppd, int daysPast){
        return pagesRead -(ppd * daysPast);
    }

    public static boolean calculateIsBehind(int debt){
        return debt < 0;
    }

    public static double calculateProgress(int bookLength, int pagesRead){
        return (double) pagesRead / bookLength;
    }

    public String getBookName() {
        return bookName.get();
    }

    public StringProperty bookNameProperty() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName.set(bookName);
    }

    public int getBookLength() {
        return bookLength.get();
    }

    public IntegerProperty bookLengthProperty() {
        return bookLength;
    }

    public void setBookLength(int bookLength) {
        this.bookLength.set(bookLength);
    }

    public int getFinishSpan() {
        return finishSpan.get();
    }

    public IntegerProperty finishSpanProperty() {
        return finishSpan;
    }

    public void setFinishSpan(int finishSpan) {
        this.finishSpan.set(finishSpan);
    }

    public int getPagesPerDay() {
        return pagesPerDay.get();
    }

    public IntegerProperty pagesPerDayProperty() {
        return pagesPerDay;
    }

    public void setPagesPerDay(int pagesPerDay) {
        //progressSer = pagesPerDay;
        this.pagesPerDay.set(pagesPerDay);
    }

    public int getTimePerPage() {
        return timePerPage.get();
    }

    public IntegerProperty timePerPageProperty() {
        return timePerPage;
    }

    public void setTimePerPage(int timePerPage) {
        this.timePerPage.set(timePerPage);
    }

    public int getReadTimePerDay() {
        return readTimePerDay.get();
    }

    public IntegerProperty readTimePerDayProperty() {
        return readTimePerDay;
    }

    public void setReadTimePerDay(int readTimePerDay) {
        this.readTimePerDay.set(readTimePerDay);
    }

    public int getPagesRead() {
        return pagesRead.get();
    }

    public IntegerProperty pagesReadProperty() {
        return pagesRead;
    }

    public void setPagesRead(int pagesRead) {
        //this.progressSer = pagesRead;
        this.pagesRead.set(pagesRead);
    }

    public int getDebt() {
        return debt.get();
    }

    public IntegerProperty debtProperty() {
        return debt;
    }

    public void setDebt(int debt) {
        //debtSer = debt;
        this.debt.set(debt);
    }

    public int getDaysPast() {
        return daysPast.get();
    }

    public IntegerProperty daysPastProperty() {
        return daysPast;
    }

    public void setDaysPast(int daysPast) {
        //daysPastSer = daysPast;
        this.daysPast.set(daysPast);
    }

    public double getProgress() {
        return progress.get();
    }

    public DoubleProperty progressProperty() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress.set(progress);
        //progressSer = getProgress();
    }

    public boolean getIsBehind() {
        return isBehind.get();
    }

    public BooleanProperty isBehindProperty() {
        return isBehind;
    }

    public void setIsBehind(boolean isBehind) {
        this.isBehind.set(isBehind);
    }

    public LocalDate getDateCreated() {
        return dateCreated.get();
    }

    public ObjectProperty<LocalDate> dateCreatedProperty() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated.set(dateCreated);
    }

    public LocalDate getDateFinish() {
        return dateFinish.get();
    }

    public ObjectProperty<LocalDate> dateFinishProperty() {
        return dateFinish;
    }

    public void setDateFinish(LocalDate dateFinish) {
        this.dateFinish.set(dateFinish);
    }

    public ObservableList<Integer> getReadPagesList() {
        return readPagesList;
    }

    public void setReadPagesList(ObservableList<Integer> readPagesList) {
        this.readPagesList = readPagesList;
    }

    public static DateTimeFormatter getDateFormat() {
        return DATE_FORMAT;
    }
}
