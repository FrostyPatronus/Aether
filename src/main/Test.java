package main;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.awt.print.Book;
import java.io.*;
import java.util.List;
import java.util.prefs.Preferences;

/**
 * Created by Timothy on 1/9/2016.
 */
public class Test{

    public static void main(String[] args) {
        /*BookModel model = new BookModel("J00n", 100, 10, 90);
        BookModel mo0del = new BookModel("Jn", 100, 10, 90);
        BookWrapper wrapper = new BookWrapper();
        wrapper.addBooks(model);
        wrapper.addBooks(mo0del);
        save(wrapper);*/
        System.out.println(load().getBooks().get(0).getReadPagesList());
    }


    public static void save(BookWrapper wrapper){
        try {
            ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream("Save.bin"));
            stream.writeObject(wrapper);
            stream.close();
        }catch (Exception e){
            e.printStackTrace();}
    }

    public static BookWrapper load(){
        try {
            ObjectInputStream stream = new ObjectInputStream(new FileInputStream("Save.bin"));
            BookWrapper temp = (BookWrapper)stream.readObject();
            temp.getBooks().get(0).update();
            stream.close();
            return temp;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
