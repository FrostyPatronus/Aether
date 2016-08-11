package main;

import javafx.scene.control.Button;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Timothy on 1/9/2016.
 */
public class MediaControl {

    public static Media buttonHover = new javafx.scene.media.Media(new File("C:\\Users\\Timothy\\IdeaProjects\\Hermes\\src\\main\\media\\ButtonHover.mp3").toURI().toString()),
            buttonClick = new javafx.scene.media.Media(new File("C:\\Users\\Timothy\\IdeaProjects\\Hermes\\src\\main\\media\\ButtonClick.wav").toURI().toString());

    public static MediaPlayer hover = new MediaPlayer(buttonHover), click = new MediaPlayer(buttonClick);

    Effect glow = new Glow();

    public void setButtonMedia(Button... buttons) {
        for (Button button : buttons) {
            button.setOnMouseEntered(event -> {
                button.setEffect(glow);
                button.setId("button-highlight");
                hoverInit();
            });
            button.setOnMouseExited(event -> {
                button.setId("button-regular");
                button.setEffect(null);
            });
        }
    }

    public static void clickInit(){
        click.stop();
        click.play();
    }

    public static void hoverInit(){
        hover.stop();
        hover.play();
    }
}

