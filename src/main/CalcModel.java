package main;

import com.sun.istack.internal.Nullable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Created by Timothy on 1/9/2016.
 */

public class CalcModel {

    private IntegerProperty finishSpan; //How many DAYS to finish a book?
    private IntegerProperty bookLength; //How many pages are in a book?
    private IntegerProperty timePerPage; //How much time can you read per page in SECONDS?
    private DoubleProperty timeDedication; //How much time are you willing to read this book per day in MINUTES
    private DoubleProperty pagePerDay; //How many pages must you read to finish the book?

    //LONG TERM
    public void ppd(int finSpan, int bkLength){
        this.ppdAndDed(finSpan, bkLength, 0);
    }

    public void ppdAndDed (int finSpan, int bkLength, int tpp){
        finishSpan = new SimpleIntegerProperty(finSpan);
        bookLength = new SimpleIntegerProperty(bkLength);
        timePerPage = new SimpleIntegerProperty(tpp);

        pagePerDay = new SimpleDoubleProperty(Math.ceil((double) getBookLength() / getFinishSpan()));
        double temp = Math.ceil( pagePerDay.get() * (tpp / 60));
        timeDedication = new SimpleDoubleProperty(temp);
        //outputs PPD, hours of commitment per day
    }

    //SHORT TERM
    public void ppdAndFinSpan(int tpp, int bkLength, double ded){
        timeDedication = new SimpleDoubleProperty(ded);
        bookLength = new SimpleIntegerProperty(bkLength);
        timePerPage = new SimpleIntegerProperty(tpp);

        pagePerDay.set((Math.ceil((double) timePerPage.get() / (timePerPage.get() / 60))));
        //outputs ppd and finspan
    }


    //Getters and Setters
    //FINISH SPAN
    public int getFinishSpan() {
        return finishSpan.get();
    }

    public IntegerProperty finishSpanProperty() {
        return finishSpan;
    }

    public void setFinishSpan(int finishSpan) {
        this.finishSpan.set(finishSpan);
    }

    //BOOK LENGTH
    public int getBookLength() {
        return bookLength.get();
    }

    public IntegerProperty bookLengthProperty() {
        return bookLength;
    }

    public void setBookLength(int bookLength) {
        this.bookLength.set(bookLength);
    }

    //TIME PER PAGE
    public int getTimePerPage() {
        return timePerPage.get();
    }

    public IntegerProperty timePerPageProperty() {
        return timePerPage;
    }

    public void setTimePerPage(int timePerPage) {
        this.timePerPage.set(timePerPage);
    }

    //TIME DEDICATION
    public double getTimeDedication() {
        return timeDedication.get();
    }

    public DoubleProperty timeDedicationProperty() {
        return timeDedication;
    }

    public void setTimeDedication(double timeDedication) {
        this.timeDedication.set(timeDedication);
    }

    //Page Per Day
    public double getPagePerDay() {
        return pagePerDay.get();
    }

    public DoubleProperty pagePerDayProperty() {
        return pagePerDay;
    }

    public void setPagePerDay(double pagePerDay) {
        this.pagePerDay.set(pagePerDay);
    }
}
