package ru.gameoflife.model;

import javafx.animation.*;
import javafx.beans.property.*;
import javafx.event.*;
import javafx.util.*;
import lombok.*;

import static java.util.Objects.*;

@Getter
public class GameOfLife {
    private final Grid grid;
    private final Generation generation;
    private final ObjectProperty<Speed> speed = new SimpleObjectProperty<>(Speed.SLOW);
    private Timeline timeline;

    public GameOfLife() {
        grid = new Grid();
        generation = new Generation(grid);

        updateTimeline();
        addSpeedPropertyListener();
    }

    private void updateTimeline() {
        Duration duration = new Duration(speed.get().getMilliseconds());
        EventHandler<ActionEvent> eventHandler = event -> next();
        KeyFrame keyFrame = new KeyFrame(duration, eventHandler);
        timeline = new Timeline(keyFrame);
        timeline.setCycleCount(Animation.INDEFINITE);
    }

    public void next() {
        generation.nextGeneration();
        generation.incrementGenerationNum();
    }

    private void addSpeedPropertyListener() {
        speed.addListener((observable, oldValue, newValue) -> {
            boolean shouldPlay = timeline.getStatus() == Animation.Status.RUNNING;
            pause();
            updateTimeline();
            if (shouldPlay) {
                play();
            }
        });
    }

    public void play() {
        timeline.play();
    }

    public void pause() {
        timeline.pause();
    }

    public void setSpeed(Speed speed) {
        if (isNull(speed))
            throw new IllegalArgumentException();

        this.speed.set(speed);
    }

    public void reset() {
        pause();
        generation.resetGeneration();
    }
}
