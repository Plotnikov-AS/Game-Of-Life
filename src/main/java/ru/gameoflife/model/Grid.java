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
                cells[rowIdx][colIdx] = Cell.builder()
                        .rowIdx(rowIdx)
                        .colIdx(colIdx)
                        .build();
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

    public void clear() {
        for (int rowIndex = 0; rowIndex < height; rowIndex++) {
            for (int columnIndex = 0; columnIndex < width; columnIndex++) {
                getCell(rowIndex, columnIndex).setAlive(false);
            }
        }
    }

    public int getCountOfAliveNeighsInRadius(int originRowIdx, int originColIdx, int radius) {
        return getCountOfAliveNeighsInRadius(originRowIdx, originColIdx, radius, emptyList());
    }

    public int getCountOfAliveNeighsInRadius(int originRowIdx, int originColIdx, int radius, List<Cell> colony) {
        int maxNeighRowIdx = getMaxNeighCoordIdxInRadius(originRowIdx, height, radius);
        int minNeighRowIdx = getMinNeighCoordIdxInRadius(originRowIdx, radius);
        int maxNeighColIdx = getMaxNeighCoordIdxInRadius(originColIdx, width, radius);
        int minNeighColIdx = getMinNeighCoordIdxInRadius(originColIdx, radius);

        int firstRow = max((originRowIdx - radius), 0);
        int firstCol = max((originColIdx - radius), 0);
        if (firstRow < minNeighRowIdx || firstCol < minNeighColIdx) {
            throw new IllegalArgumentException(format(ROUND_TOO_BIG_ERROR, originRowIdx, originColIdx, radius));
        }

        int countOfAliveNeighs = 0;
        for (int row = firstRow; row <= maxNeighRowIdx; row++) {
            for (int col = firstCol; col <= maxNeighColIdx; col++) {
                if (row == originRowIdx && col == originColIdx)
                    continue;

                Cell cell = getCell(row, col);
                if (colony.contains(cell)) {
                    continue;
                }
                if (cell.isNotDead()) {
                    countOfAliveNeighs++;
                }
            }
        }
        return countOfAliveNeighs;
    }


    private int getMaxNeighCoordIdxInRadius(int originIdx, int directionMaxVal, int radius) {
        int maxVal = originIdx + radius;
        if (maxVal >= directionMaxVal) {
            maxVal = directionMaxVal - 1;
        }
        return maxVal;
    }

    private int getMinNeighCoordIdxInRadius(int originIdx, int radius) {
        int minVal = originIdx - radius;
        if (minVal < 0) {
            minVal = 0;
        }
        return minVal;
    }
}
