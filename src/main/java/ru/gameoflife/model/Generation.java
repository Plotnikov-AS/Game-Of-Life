package ru.gameoflife.model;

import javafx.beans.property.*;

import java.util.*;

import static java.lang.Math.*;
import static java.util.Objects.*;
import static java.util.stream.Collectors.*;
import static ru.gameoflife.constants.Constants.*;
import static ru.gameoflife.constants.Constants.Direction.*;
import static ru.gameoflife.random.RandomExtensions.*;

public class Generation {
    private final ReadOnlyLongWrapper generationNum = new ReadOnlyLongWrapper();
    private final ReadOnlyLongWrapper aliveCellsCount = new ReadOnlyLongWrapper();
    private final ReadOnlyLongWrapper dyingCellsCount = new ReadOnlyLongWrapper();
    private final ReadOnlyLongWrapper bornCellsCount = new ReadOnlyLongWrapper();

    private final Grid grid;
    private final List<Cell> cells = new ArrayList<>();

    public Generation(Grid grid) {
        this.grid = grid;
        bornFirstGeneration();
    }

    private void bornFirstGeneration() {
        cells.clear();
        for (int colonyIdx = 0; colonyIdx < COLONIES_COUNT; colonyIdx++) {
            cells.addAll(createRandomColony());
        }
        countAliveCells();
    }

    //TODO refact!
    private List<Cell> createRandomColony() {
        List<Cell> colony = new ArrayList<>();
        Integer randomRow = getRandomRowIdx();
        Integer randomCol = getRandomColIdx();
        while (colony.size() < nextInt(5, CELLS_IN_COLONY * 2)) {
            Cell cell = grid.getCell(randomRow, randomCol);
            colony.add(cell);
            if (cell.isAlive()
                    || grid.getCountOfAliveNeighsInRadius(cell.getRowIdx(), cell.getColIdx(), DISTANCE_BETWEEN_COLONIES, colony) > 0) {
                colony.remove(cell);
                if (colony.isEmpty()) {
                    randomRow = getRandomRowIdx();
                    randomCol = getRandomColIdx();
                } else {
                    Map<String, Integer> newCoords = move(randomRow, randomCol);
                    randomRow = newCoords.get(ROW);
                    randomCol = newCoords.get(COLUMN);
                }
                continue;
            }
            cell.setAlive(true);
            cell.setDead(false);
            Map<String, Integer> newCoords = move(randomRow, randomCol);
            randomRow = newCoords.get(ROW);
            randomCol = newCoords.get(COLUMN);
        }
        return colony;
    }

    private Map<String, Integer> move(Integer row, Integer col) {
        Map<String, Integer> result = new HashMap<>();
        int nextVal;
        Direction direction = getRandomDirection();
        switch (direction) {
            case UP:
                nextVal = --row;
                row = max(nextVal, 0);
                break;
            case DOWN:
                nextVal = ++row;
                row = min(nextVal, GRID_HEIGHT - 1);
                break;
            case LEFT:
                nextVal = --col;
                col = max(nextVal, 0);
                break;
            case RIGHT:
                nextVal = ++col;
                col = min(nextVal, GRID_WIDTH - 1);
                break;
        }
        result.put(ROW, row);
        result.put(COLUMN, col);
        return result;
    }

    public void nextGeneration() {
        killDyingCells();
        bornCells();
        findDyingCells();
        findCellsToBorn();
    }

    private void findCellsToBorn() {
        List<Cell> cellsToBorn = new ArrayList<>();
        for (Cell[] cellsRow : grid.getCells()) {
            for (Cell cell : cellsRow) {
                int countOfNeighs = grid.getCountOfAliveNeighsInRadius(cell.getRowIdx(), cell.getColIdx(), NEIGHBOURS_RADIUS);
                if (countOfNeighs == NEIGHBOURS_TO_BORN) {
                    cellsToBorn.add(cell);
                }
            }
        }
        for (Cell cell : cellsToBorn) {
            cell.setBorn(true);
            cell.setDead(false);
            incrementBornCells();
            cells.add(cell);
        }
    }

    public void findDyingCells() {
        for (Cell cell : cells) {
            if (cell.isDead()) continue;

            int cellNeighsCount = grid.getCountOfAliveNeighsInRadius(cell.getRowIdx(), cell.getColIdx(), NEIGHBOURS_RADIUS);
            if (cellNeighsCount > MAX_NEIGHBOURS || cellNeighsCount < MIN_NEIGHBOURS) {
                cell.setDying(true);
                incrementDyingCells();
            }
        }
    }

    public void killDyingCells() {
        List<Cell> dyingCells = cells.stream()
                .filter(cell -> nonNull(cell) && cell.isDying())
                .collect(toList());
        for (Cell cell : dyingCells) {
            cell.setAlive(false);
            cell.setDying(false);
            cell.setDead(true);
            decrementDyingCells();
            decrementAliveCells();
            cells.remove(cell);
        }
    }

    public void bornCells() {
        for (Cell cell : cells) {
            if (cell.isBorn()) {
                cell.setAlive(true);
                cell.setBorn(false);
                decrementBornCells();
                incrementAliveCells();
            }
        }
    }

    private void killAll() {
        for (Cell cell : cells) {
            cell.setAlive(false);
            cell.setBorn(false);
            cell.setDying(false);
            cell.setDead(true);
            resetAliveCount();
            resetBornCount();
            resetDyingCount();
        }
    }

    public void resetGeneration() {
        generationNum.set(1);
        killAll();
        countDyingCells();
        countBornCells();
        bornFirstGeneration();
    }

    private void countAliveCells() {
        int aliveCells = (int) cells.stream()
                .filter(cell -> nonNull(cell) && cell.isAlive())
                .count();
        aliveCellsCount.set(aliveCells);
    }

    private void countDyingCells() {
        int dyingCells = (int) cells.stream()
                .filter(cell -> nonNull(cell) && cell.isDying())
                .count();
        dyingCellsCount.set(dyingCells);
    }

    private void countBornCells() {
        int bornCells = (int) cells.stream()
                .filter(cell -> nonNull(cell) && cell.isBorn())
                .count();
        bornCellsCount.set(bornCells);
    }

    public void incrementGenerationNum() {
        generationNum.set(getGenerationNum() + 1);
    }

    private void incrementDyingCells() {
        dyingCellsCount.set(getDyingCellsNum() + 1);
    }

    private void incrementBornCells() {
        bornCellsCount.set(getBornCellsNum() + 1);
    }

    private void incrementAliveCells() {
        aliveCellsCount.set(getAliveCellsNum() + 1);
    }

    private void decrementAliveCells() {
        aliveCellsCount.set(getAliveCellsNum() - 1);
    }

    private void decrementDyingCells() {
        dyingCellsCount.set(getDyingCellsNum() - 1);
    }

    private void decrementBornCells() {
        bornCellsCount.set(getBornCellsNum() - 1);
    }

    public ReadOnlyLongProperty generationNumProperty() {
        return generationNum.getReadOnlyProperty();
    }

    public ReadOnlyLongProperty aliveCellsCountProperty() {
        return aliveCellsCount.getReadOnlyProperty();
    }

    public ReadOnlyLongProperty dyingCellsCountProperty() {
        return dyingCellsCount.getReadOnlyProperty();
    }

    public ReadOnlyLongProperty bornCellsCountProperty() {
        return bornCellsCount.getReadOnlyProperty();
    }

    public long getDyingCellsNum() {
        return dyingCellsCount.get();
    }

    public long getBornCellsNum() {
        return bornCellsCount.get();
    }

    public long getAliveCellsNum() {
        return aliveCellsCount.get();
    }

    public long getGenerationNum() {
        return generationNum.get();
    }

    private void resetAliveCount() {
        aliveCellsCount.set(0);
    }

    private void resetDyingCount() {
        dyingCellsCount.set(0);
    }

    private void resetBornCount() {
        bornCellsCount.set(0);
    }

}
