package ru.gameoflife.model;

import javafx.beans.property.*;
import lombok.*;

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
    @Getter
    private List<Cell> cells = new ArrayList<>();

    public Generation(Grid grid) {
        this.grid = grid;
        bornFirstGeneration();
    }

    public Generation(Grid grid, List<Cell> cells, Map<String, Long> gameStatistic) {
        this.grid = grid;
        this.cells = cells;
        setAliveCellsCount(gameStatistic.get(ALIVE));
        setBornCellsCount(gameStatistic.get(BORN));
        setDyingCellsCount(gameStatistic.get(DYING));
        setGenerationNum(gameStatistic.get(GENERATION_NUM));
    }

    private void bornFirstGeneration() {
        cells.clear();
        for (int colonyIdx = 0; colonyIdx < COLONIES_COUNT; colonyIdx++) {
            cells.addAll(createRandomColony());
        }
        countAliveCells();
    }

    private List<Cell> createRandomColony() {
        List<Cell> colony = new ArrayList<>();
        Map<String, Integer> coords = new HashMap<>();
        coords.put(ROW, getRandomRowIdx());
        coords.put(COLUMN, getRandomColIdx());
        while (colony.size() < nextInt(5, CELLS_IN_COLONY * 2)) {
            Cell cell = grid.getCell(coords.get(ROW), coords.get(COLUMN));
            colony.add(cell);
            if (isCellValid(cell, colony)) {
                cell.setAlive(true);
                cell.setDead(false);
                move(coords);
            } else {
                removeCellAndMoveNext(colony, cell, coords);
            }
        }
        return colony;
    }

    private boolean isCellValid(Cell cell, List<Cell> colony) {
        if (isNull(cell) || cell.isAlive()) {
            return false;
        }

        int countOfAliveNeighs = grid.getCountOfAliveNeighsInRadius(cell, DISTANCE_BETWEEN_COLONIES, colony);
        return countOfAliveNeighs <= 0;
    }

    private void removeCellAndMoveNext(List<Cell> colony, Cell cell, Map<String, Integer> coords) {
        colony.remove(cell);
        if (colony.isEmpty()) {
            coords.replace(ROW, getRandomRowIdx());
            coords.replace(COLUMN, getRandomColIdx());
        } else {
            move(coords);
        }
    }

    private void move(Map<String, Integer> coords) {
        int row = coords.get(ROW);
        int col = coords.get(COLUMN);
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
        coords.replace(ROW, row);
        coords.replace(COLUMN, col);
    }

    public void nextGeneration() {
        killDyingCells();
        bornCells();
        findDyingCells();
        findCellsToBorn();
    }

    public void killDyingCells() {
        List<Cell> dyingCells = cells.stream()
                .filter(cell -> nonNull(cell) && cell.isDying())
                .collect(toList());

        for (Cell cell : dyingCells) {
            cell.setAlive(false);
            cell.setDying(false);
            cell.setDead(true);
            cells.remove(cell);
        }
        dyingCellsCount.set(0);
        aliveCellsCount.set(getAliveCellsNum() - dyingCells.size());
    }

    public void bornCells() {
        for (Cell cell : cells) {
            if (cell.isBorn()) {
                cell.setBorn(false);
                cell.setAlive(true);
            }
        }
        bornCellsCount.set(0);
        countAliveCells();
    }

    public void findDyingCells() {
        List<Cell> dyingCells = new ArrayList<>();
        for (Cell cell : cells) {
            if (cell.isDead()) continue;

            int cellNeighsCount = grid.getCountOfAliveNeighsInRadius(cell, NEIGHBOURS_RADIUS);
            if (cellNeighsCount > MAX_NEIGHBOURS || cellNeighsCount < MIN_NEIGHBOURS) {
                cell.setDying(true);
                dyingCells.add(cell);
            }
        }
        dyingCellsCount.set(dyingCells.size());
    }

    private void findCellsToBorn() {
        List<Cell> cellsToBorn = new ArrayList<>();
        for (Cell[] cellsRow : grid.getCells()) {
            for (Cell cell : cellsRow) {
                int countOfNeighs = grid.getCountOfAliveNeighsInRadius(cell, NEIGHBOURS_RADIUS);
                if (countOfNeighs == NEIGHBOURS_TO_BORN) {
                    cellsToBorn.add(cell);
                }
            }
        }

        for (Cell cell : cellsToBorn) {
            cell.setBorn(true);
            cell.setDead(false);
            if (!cells.contains(cell)) {
                cells.add(cell);
            }
        }
        bornCellsCount.set(cellsToBorn.size());
    }

    private void killAll() {
        for (Cell cell : cells) {
            cell.setAlive(false);
            cell.setBorn(false);
            cell.setDying(false);
            cell.setDead(true);
        }
        cells.clear();
    }

    public void resetGeneration() {
        generationNum.set(1);
        killAll();
        resetDyingCellsCount();
        resetBornCellsCount();
        bornFirstGeneration();
        countAliveCells();
    }

    private void countAliveCells() {
        int aliveCells = (int) cells.stream()
                .filter(cell -> nonNull(cell) && cell.isAlive())
                .count();
        aliveCellsCount.set(aliveCells);
    }

    private void resetDyingCellsCount() {
        dyingCellsCount.set(0);
    }

    private void resetBornCellsCount() {
        bornCellsCount.set(0);
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

    public void incrementGenerationNum() {
        generationNum.set(getGenerationNum() + 1);
    }

    private void setGenerationNum(Long generationNum) {
        this.generationNum.set(generationNum);
    }

    private void setAliveCellsCount(Long aliveCellsCount) {
        this.aliveCellsCount.set(aliveCellsCount);
    }

    private void setDyingCellsCount(Long dyingCellsCount) {
        this.dyingCellsCount.set(dyingCellsCount);
    }

    private void setBornCellsCount(Long bornCellsCount) {
        this.bornCellsCount.set(bornCellsCount);
    }

}
