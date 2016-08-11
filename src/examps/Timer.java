package examps;

import javafx.scene.control.Label;

import java.util.concurrent.TimeUnit;

/**
 * Created by Timothy on 1/7/2016.
 */
public class Timer {

    private boolean stop = true;

    public void timerStart(Label label) {
        stop = true;
        for (int index = 1; ; index++) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println(index);
            if (!stop) break;
        }
    }

    public void timerStop() {
        stop = false;
    }
}
