package ru.gameoflife.model;

import javafx.beans.property.*;

import java.util.*;

import static java.lang.Math.*;
import static java.util.Objects.*;
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
        setAliveCellsCount();
    }

    private List<Cell> createRandomColony() {
        List<Cell> colony = new ArrayList<>();
        Integer randomRow = getRandomRowIdx();
        Integer randomCol = getRandomColIdx();
        int emptyIterations = 0;
        while (colony.size() < RANDOM_MAX_CELLS_IN_COLONY && cells.size() < MAX_CELLS_NUMBER) {
            Cell cell = grid.getCell(randomRow, randomCol);
            colony.add(cell);
            if (cell.isAlive()) {
                colony.remove(cell);
                emptyIterations++;
                if (emptyIterations > 10) {
                    break;
                }
                Map<String, Integer> newCoords = move(randomRow, randomCol);
                randomRow = newCoords.get(ROW);
                randomCol = newCoords.get(COLUMN);
                continue;
            }
            int neighboursCount = grid.getCountOfAliveNeighsInRadius(cell.getRowIdx(), cell.getColIdx(), DISTANCE_BETWEEN_COLONIES, colony);
            if (neighboursCount > 0) {
                colony.remove(cell);
                emptyIterations++;
                if (emptyIterations > 10) {
                    break;
                }
                Map<String, Integer> newCoords = move(randomRow, randomCol);
                randomRow = newCoords.get(ROW);
                randomCol = newCoords.get(COLUMN);
                continue;
            }
            cell.setAlive(true);
            emptyIterations = 0;
            Map<String, Integer> newCoords = move(randomRow, randomCol);
            randomRow = newCoords.get(ROW);
            randomCol = newCoords.get(COLUMN);
        }
        printColony(colony);
        return colony;
    }

    private void printColony(List<Cell> colony) {
        System.out.println("===========================");
        for (int i = 0; i < colony.size(); i++) {
            System.out.println("Cell №" + i);
            System.out.println("Row: " + colony.get(i).getRowIdx());
            System.out.println("Column: " + colony.get(i).getColIdx());
        }
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
        setDyingCells();
        findCellsToBorn();
        setAliveCellsCount();
        setBornCellsCount();
        setDyingCellsCount();
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
        }
    }

    public void setDyingCells() {
        for (Cell cell : cells) {
            if (cell.isDead()) continue;

            int cellNeighsCount = grid.getCountOfAliveNeighsInRadius(cell.getRowIdx(), cell.getColIdx(), NEIGHBOURS_RADIUS);
            if (cellNeighsCount > MAX_NEIGHBOURS || cellNeighsCount < MIN_NEIGHBOURS) {
                cell.setDying(true);
            }
        }
    }

    public void killDyingCells() {
        for (Cell cell : cells) {
            if (cell.isDying()) {
                cell.setAlive(false);
                cell.setDying(false);
                cell.setDead(true);
            }
        }
    }

    public void bornCells() {
        for (Cell cell : cells) {
            if (cell.isBorn()) {
                cell.setAlive(true);
                cell.setBorn(false);
            }
        }
    }

    private void killAll() {
        for (Cell cell : cells) {
            cell.setAlive(false);
            cell.setBorn(false);
            cell.setDying(false);
            cell.setDead(false);
        }
    }

    public void resetGeneration() {
        generationNum.set(1);
        killAll();
        setDyingCellsCount();
        setBornCellsCount();
        bornFirstGeneration();
    }

    private void setAliveCellsCount() {
        int aliveCells = (int) cells.stream()
                .filter(cell -> nonNull(cell) && cell.isAlive())
                .count();
        aliveCellsCount.set(aliveCells);
    }

    private void setDyingCellsCount() {
        int dyingCells = (int) cells.stream()
                .filter(cell -> nonNull(cell) && cell.isDying())
                .count();
        dyingCellsCount.set(dyingCells);
    }

    private void setBornCellsCount() {
        int bornCells = (int) cells.stream()
                .filter(cell -> nonNull(cell) && cell.isBorn())
                .count();
        bornCellsCount.set(bornCells);
    }

    public long getGenerationNum() {
        return generationNum.get();
    }

    public void incrementGenerationNum() {
        generationNum.set(getGenerationNum() + 1);
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
}
