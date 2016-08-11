package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Timothy on 1/20/2016.
 */

public class BookWrapper implements Serializable{

    private ArrayList<BookModel> books = new ArrayList<>();

    public ArrayList<BookModel> getBooks() {
        return books;
    }

    public void setBooks(ArrayList<BookModel> books) {
        this.books = books;
    }

    public void addBooks(BookModel books){
        getBooks().add(books);
    }

    public void saySomething(){
        System.out.println(books);
    }
}
