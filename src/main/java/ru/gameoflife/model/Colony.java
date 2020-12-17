package ru.gameoflife.model;

import lombok.*;

import java.util.*;
import java.util.concurrent.*;

import static ru.gameoflife.constants.Constants.*;

@Getter
public class Colony {
    private List<Cell> cells = new ArrayList<>();
    private final int maxCellNumber = RANDOM.nextInt(MAX_CELLS_IN_COLONY);

    public Colony(Grid grid) {
        createRandomColony(grid);
    }

    private void createRandomColony(Grid grid) {
        int randomRow = RANDOM.nextInt(grid.getHeight());
        int randomCol = RANDOM.nextInt(grid.getWidth());
        while (cells.size() < maxCellNumber) {
            bornCell(grid, randomRow, randomCol);
            randomRow = moveUpDown(randomRow, grid);
            randomCol = moveLeftRight(randomCol, grid);
        }
    }

    private void bornCell(Grid grid, Integer randomRow, Integer randomCol) {
        Cell cell = grid.getCell(randomRow, randomCol);
        if (cell.isAlive()) return;

        // TODO: 18.12.2020 check for neighbors
        cell.setAlive(true);
        cells.add(cell);
    }

    private int moveLeftRight(int value, Grid grid) {
        int newVal = ThreadLocalRandom.current().nextInt(value - 1, value + 2);
        if (newVal == value) {
            moveLeftRight(newVal, grid);
        }
        if (newVal < 0) {
            newVal = grid.getWidth() - 1;
        } else if (newVal >= grid.getWidth()) {
            newVal = 0;
        }
        return newVal;
    }

    private int moveUpDown(int value, Grid grid) {
        int newVal = ThreadLocalRandom.current().nextInt(value - 1, value + 2);
        if (newVal == value) {
            moveUpDown(newVal, grid);
        }
        if (newVal < 0) {
            newVal = grid.getHeight() - 1;
        } else if (newVal >= grid.getHeight()) {
            newVal = 0;
        }
        return newVal;
    }
}
