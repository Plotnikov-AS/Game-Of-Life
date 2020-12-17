package ru.gameoflife.model;

import javafx.beans.property.*;

public class Cell {
    private final BooleanProperty aliveProperty = new SimpleBooleanProperty();

    public boolean isAlive() {
        return aliveProperty().get();
    }

    public void setAlive(boolean isAlive) {
        aliveProperty().set(isAlive);
    }

    public void toggleAlive() {
        setAlive(!isAlive());
    }

    public BooleanProperty aliveProperty() {
        return aliveProperty;
    }
}
