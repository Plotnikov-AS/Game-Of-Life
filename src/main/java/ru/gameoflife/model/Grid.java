package ru.gameoflife.model;

import lombok.*;
import ru.gameoflife.exception.*;

import java.util.*;

import static java.lang.Math.max;
import static java.lang.String.*;
import static java.util.Collections.*;
import static java.util.Objects.*;
import static ru.gameoflife.config.Configuration.*;
import static ru.gameoflife.constants.Constants.ErrorMessages.*;

@Getter
public class Grid {
    private final int width;
    private final int height;
    private final int totalGridSize;

    private final Cell[][] cells;

    public Grid() {
        this.width = getConfig().getGridConfig().getWidth();
        this.height = getConfig().getGridConfig().getHeight();
        totalGridSize = height * width;
        cells = createCells(height, width);
    }

    private Cell[][] createCells(int gridHeight, int gridWidth) {
        Cell[][] cells = new Cell[gridHeight][gridWidth];
        for (int rowIdx = 0; rowIdx < gridHeight; rowIdx++) {
            for (int colIdx = 0; colIdx < gridWidth; colIdx++) {
                Cell cell = Cell.builder()
                        .rowIdx(rowIdx)
                        .colIdx(colIdx)
                        .build();
                cell.setDead(true);
                cells[rowIdx][colIdx] = cell;
            }
        }
        return cells;
    }

    public Cell getCell(int rowIndex, int columnIndex) {
        Cell cell;
        try {
            cell = cells[rowIndex][columnIndex];
            if (isNull(cell))
                throw new NotFoundException(format(CELL_NOT_EXIST_ON_COORD, rowIndex, columnIndex));
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new NotFoundException(format(CELL_NOT_EXIST_ON_COORD, rowIndex, columnIndex));
        }
        return cell;
    }

    public int getCountOfAliveNeighsInRadius(Cell cell, int radius) {
        return getCountOfAliveNeighsInRadius(cell, radius, emptyList());
    }

    public int getCountOfAliveNeighsInRadius(Cell cell, int radius, List<Cell> colony) {
        int cellRow = cell.getRowIdx();
        int cellCol = cell.getColIdx();
        int maxRow = getMaxCoordInRadius(cellRow, height, radius);
        int minRow = getMinCoordInRadius(cellRow, radius);
        int maxCol = getMaxCoordInRadius(cellCol, width, radius);
        int minCol = getMinCoordInRadius(cellCol, radius);

        int firstRow = max((cellRow - radius), 0);
        int firstCol = max((cellCol - radius), 0);
        if (firstRow < minRow || firstCol < minCol) {
            throw new IllegalArgumentException(format(ROUND_TOO_BIG_ERROR, cellRow, cellCol, radius));
        }

        int countOfAliveNeighs = 0;
        for (int row = firstRow; row <= maxRow; row++) {
            for (int col = firstCol; col <= maxCol; col++) {
                if (row == cellRow && col == cellCol)
                    continue;

                Cell neighbour = getCell(row, col);
                if (colony.contains(neighbour)) {
                    continue;
                }
                if (neighbour.isAlive() || neighbour.isDying()) {
                    countOfAliveNeighs++;
                }
            }
        }
        return countOfAliveNeighs;
    }


    private int getMaxCoordInRadius(int originIdx, int directionMaxVal, int radius) {
        int maxVal = originIdx + radius;
        if (maxVal >= directionMaxVal) {
            maxVal = directionMaxVal - 1;
        }
        return maxVal;
    }

    private int getMinCoordInRadius(int originIdx, int radius) {
        int minVal = originIdx - radius;
        if (minVal < 0) {
            minVal = 0;
        }
        return minVal;
    }
}
