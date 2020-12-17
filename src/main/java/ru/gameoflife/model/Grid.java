package ru.gameoflife.model;

import lombok.*;

import static ru.gameoflife.config.Configuration.*;

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
                cells[rowIdx][colIdx] = new Cell();
            }
        }
        return cells;
    }

    public Cell getCell(int rowIndex, int columnIndex) {
        return cells[rowIndex][columnIndex];
    }

    public void clear() {
        for (int rowIndex = 0; rowIndex < height; rowIndex++) {
            for (int columnIndex = 0; columnIndex < width; columnIndex++) {
                getCell(rowIndex, columnIndex).setAlive(false);
            }
        }
    }
}
