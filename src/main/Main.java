package main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    static Stage stage;
    static final int X = 800, Y = 500;
    Parent root;
    static Scene mainMenu;
    public static final Image ICON = new Image(Main.class.getResourceAsStream("icon.png"));

    @Override
    public void start(Stage primaryStage) throws Exception{
        Main.stage = primaryStage;
        root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        primaryStage.setTitle("Aether - v1.0");
        primaryStage.centerOnScreen();
        primaryStage.setResizable(false);
        mainMenu = new Scene(root, X, Y);
        primaryStage.setScene(mainMenu);
        primaryStage.getIcons().add(ICON);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
