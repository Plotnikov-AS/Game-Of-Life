package ru.gameoflife.service;

import ru.gameoflife.config.*;
import ru.gameoflife.model.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import static java.util.Objects.*;
import static ru.gameoflife.config.Configuration.*;
import static ru.gameoflife.constants.Constants.*;

public interface SaveLoadService {
    static void saveGame(GameOfLife gameOfLife, String path) throws IOException {
        if (isNull(gameOfLife)) {
            throw new IllegalArgumentException();
        }
        SaveGame saveGame = convertGameToSave(gameOfLife);
        if (directoryIsNotExist(PATH_TO_SAVE_GAME)) {
            Files.createDirectory(Paths.get(PATH_TO_SAVE_GAME));
        }
        FileOutputStream fileOut = new FileOutputStream(path);
        ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
        objectOut.writeObject(saveGame);
        objectOut.close();
    }

    static SaveGame convertGameToSave(GameOfLife gameOfLife) {
        List<Cell> cells = gameOfLife.getGeneration().getCells();
        Configuration configuration = getConfig();
        boolean[][] aliveCells = new boolean[GRID_HEIGHT][GRID_WIDTH];
        boolean[][] bornCells = new boolean[GRID_HEIGHT][GRID_WIDTH];
        boolean[][] dyingCells = new boolean[GRID_HEIGHT][GRID_WIDTH];
        cells.forEach(cell -> {
            if (nonNull(cell)) {
                if (cell.isAlive()) {
                    aliveCells[cell.getRowIdx()][cell.getColIdx()] = true;
                } else if (cell.isBorn()) {
                    bornCells[cell.getRowIdx()][cell.getColIdx()] = true;
                } else if (cell.isDying()) {
                    dyingCells[cell.getRowIdx()][cell.getColIdx()] = true;
                }
            }
        });
        Map<String, Long> gameStatistic = gameOfLife.getGeneration().getStatistic();
        return new SaveGame(aliveCells, bornCells, dyingCells, configuration, gameStatistic);
    }

    static GameOfLife loadGame(File saveFile) {
        try {
            FileInputStream fileInput = new FileInputStream(saveFile);
            ObjectInputStream objInput = new ObjectInputStream(fileInput);
            SaveGame savedGame = (SaveGame) objInput.readObject();
            return convertSaveToGame(savedGame);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    static GameOfLife convertSaveToGame(SaveGame saveGame) {
        restoreConfig(saveGame.getConfiguration());
        Grid grid = new Grid();
        List<Cell> cells = restoreCells(grid, saveGame);
        Generation generation = new Generation(grid, cells, saveGame.getGameStatistic());
        return new GameOfLife(grid, generation);
    }

    static List<Cell> restoreCells(Grid grid, SaveGame saveGame) {
        Cell[][] cells = grid.getCells();
        List<Cell> cellList = new ArrayList<>();
        for (int row = 0; row < cells.length; row++) {
            for (int col = 0; col < cells[row].length; col++) {
                Cell cell = cells[row][col];
                boolean isAlive = saveGame.getAliveCells()[row][col];
                cell.setAlive(isAlive);
                boolean isBorn = saveGame.getBornCells()[row][col];
                cell.setBorn(isBorn);
                boolean isDying = saveGame.getDyingCells()[row][col];
                cell.setDying(isDying);
                if (isAlive || isBorn || isDying) {
                    cellList.add(cell);
                }
            }
        }
        return cellList;
    }

    static void restoreConfig(Configuration savedConfig) {
        Configuration currentConfig = getConfig();
        currentConfig.setGridConfig(savedConfig.getGridConfig());
        currentConfig.setGameConfig(savedConfig.getGameConfig());
    }

    static boolean isDirectoryExist(String path) {
        File directory = new File(path);
        return directory.exists();
    }

    static boolean directoryIsNotExist(String path) {
        return !isDirectoryExist(path);
    }
}
