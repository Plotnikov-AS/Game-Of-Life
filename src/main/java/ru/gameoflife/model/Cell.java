package ru.gameoflife.model;

import javafx.beans.property.*;
import lombok.*;

@Builder
@Getter
public class Cell {
    private final int rowIdx;
    private final int colIdx;
    private final BooleanProperty aliveProperty = new SimpleBooleanProperty();
    private final BooleanProperty dyingProperty = new SimpleBooleanProperty();
    private final BooleanProperty bornProperty = new SimpleBooleanProperty();
    private final BooleanProperty deadProperty = new SimpleBooleanProperty();

    public boolean isAlive() {
        return getAliveProperty().get();
    }

    public void setAlive(boolean isAlive) {
        getAliveProperty().set(isAlive);
    }

    public void toggleAlive() {
        getAliveProperty().set(!isAlive());
    }

    public boolean isDying() {
        return dyingProperty.get();
    }

    public void setDying(boolean isDying) {
        getDyingProperty().set(isDying);
    }

    public boolean isBorn() {
        return bornProperty.get();
    }

    public void setBorn(boolean isBorn) {
        getBornProperty().set(isBorn);
    }

    public boolean isDead() {
        return deadProperty.get();
    }

    public void setDead(boolean isDead) {
        getDeadProperty().set(isDead);
    }

    public void resetToDefault() {
        setDead(true);
        setBorn(false);
        setAlive(false);
        setDying(false);
    }
}
