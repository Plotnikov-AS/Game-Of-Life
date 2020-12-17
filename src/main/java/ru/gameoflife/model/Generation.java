package ru.gameoflife.model;

import javafx.beans.property.*;

import java.util.*;

import static ru.gameoflife.constants.Constants.*;

public class Generation {
    private final ReadOnlyLongWrapper generationNum = new ReadOnlyLongWrapper();
    private final Grid grid;
    private Map<Integer, Colony> colonies = new HashMap<>();

    public Generation(Grid grid) {
        this.grid = grid;
        createRandomGeneration(grid);
    }

    private void createRandomGeneration(Grid grid) {
        for (int colonyIdx = 0; colonyIdx < COLONIES_COUNT; colonyIdx++) {
            Colony newColony = new Colony(grid);
            colonies.put(colonyIdx, newColony);
        }
    }

    private void nextGeneration() {
        //todo
    }

    public long getGenerationNum() {
        return generationNum.get();
    }

    public void incrementGenerationNum() {
        generationNum.set(getGenerationNum() + 1);
    }

    public void resetGeneration() {
        generationNum.set(1);
        createRandomGeneration(grid);
    }

    public ReadOnlyLongProperty generationNumProperty() {
        return generationNum.getReadOnlyProperty();
    }
}
