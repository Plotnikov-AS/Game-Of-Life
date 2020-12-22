package ru.gameoflife.controller;

import javafx.beans.property.*;
import javafx.collections.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import ru.gameoflife.model.Cell;
import ru.gameoflife.model.*;

import static java.util.Objects.*;
import static javafx.scene.input.MouseEvent.*;
import static javafx.scene.layout.GridPane.*;
import static ru.gameoflife.config.Configuration.*;
import static ru.gameoflife.constants.Constants.Style.*;

public class MainController {


    public ToggleButton slowToggleButton;
    public ToggleButton mediumToggleButton;
    public ToggleButton fastToggleButton;
    public ToggleButton fastestToggleButton;
    public ToggleButton playToggleButton;
    public ToggleButton pauseToggleButton;
    public Label generationNumberLabel;
    public AnchorPane rootPane;
    public GridPane gridPane;
    public Label lblCellSize;
    public Label lblRows;
    public Label lblColumns;
    public Label lblNumOfColonies;
    public Label lblMaxNumOfCells;
    public Label lblMinSpaceBetweenCells;
    public Label lblNeighRadius;
    public Label lblCellCountToBorn;
    public Label lblAliveCells;
    public Label lblDyingCells;
    public Label lblBornCells;

    private GameOfLife gameOfLife;

    public void initialize() {
        initPlayPauseButtons();
        initSpeedButtons();
        initConfigLabels();
    }

    private void initGameStatisticLabels() {
        lblAliveCells.textProperty().bind(gameOfLife.getGeneration().aliveCellsCountProperty().asString());
        lblDyingCells.textProperty().bind(gameOfLife.getGeneration().dyingCellsCountProperty().asString());
        lblBornCells.textProperty().bind(gameOfLife.getGeneration().bornCellsCountProperty().asString());
    }

    private void initConfigLabels() {
        lblCellSize.setText(getConfig().getGridConfig().getCellSize().toString());
        lblRows.setText(getConfig().getGridConfig().getHeight().toString());
        lblColumns.setText(getConfig().getGridConfig().getWidth().toString());
        lblNumOfColonies.setText(getConfig().getGameConfig().getColoniesCount().toString());
        lblMaxNumOfCells.setText(getConfig().getGameConfig().getMaxCellNumber().toString());
        lblMinSpaceBetweenCells.setText(getConfig().getGameConfig().getMinSpaceBetweenCells().toString());
        lblNeighRadius.setText(getConfig().getGameConfig().getNeighboursCellRadius().toString());
        lblCellCountToBorn.setText(getConfig().getGameConfig().getCellsCountToBornNew().toString());
    }

    private void setGenerationNumberLabel() {
        generationNumberLabel.textProperty().bind(gameOfLife.getGeneration().generationNumProperty().asString());
    }

    private void initPlayPauseButtons() {
        ToggleGroup toggleGroup = new ToggleGroup();
        toggleGroup.getToggles().addAll(playToggleButton, pauseToggleButton);
        pauseToggleButton.setSelected(true);
    }

    private void initSpeedButtons() {
        ToggleGroup toggleGroup = new ToggleGroup();
        toggleGroup.getToggles().addAll(slowToggleButton, mediumToggleButton, fastToggleButton, fastestToggleButton);
        slowToggleButton.setSelected(true);
    }

    private void initGridPane() {
        Grid grid = gameOfLife.getGrid();
        int gridWidth = grid.getWidth();
        int gridHeight = grid.getHeight();

        for (int rowIdx = 0; rowIdx < gridHeight; rowIdx++) {
            for (int colIdx = 0; colIdx < gridWidth; colIdx++) {
                addCellPane(rowIdx, colIdx);
            }
        }
    }

    private void addCellPane(int rowIdx, int colIdx) {
        Pane cellPane = new Pane();
        addCellPaneStyle(cellPane);
        addAlivePropertyListener(rowIdx, colIdx, cellPane);
        addClickEventHandler(rowIdx, colIdx, cellPane);
        setAliveStyle(cellPane, gameOfLife.getGrid().getCell(rowIdx, colIdx).isAlive());
        gridPane.add(cellPane, colIdx, rowIdx);
    }

    private void addClickEventHandler(int rowIdx, int colIdx, Pane cellPane) {
        Cell cell = gameOfLife.getGrid().getCell(rowIdx, colIdx);
        cellPane.addEventHandler(MOUSE_CLICKED, event -> cell.toggleAlive());
    }

    private void addAlivePropertyListener(int rowIdx, int colIdx, Pane cellPane) {
        BooleanProperty aliveProperty = gameOfLife.getGrid().getCell(rowIdx, colIdx).getAliveProperty();
        aliveProperty.addListener(((observable, oldValue, newValue) -> setAliveStyle(cellPane, newValue)));
    }

    private void setAliveStyle(Pane cellPane, boolean isAlive) {
        ObservableList<String> styleClass = cellPane.getStyleClass();
        if (isAlive) {
            styleClass.add(ALIVE_STYLE_CLASS);
        } else {
            styleClass.remove(ALIVE_STYLE_CLASS);
        }
    }

    private void addCellPaneStyle(Pane cellPane) {
        int cellSize = getConfig().getGridConfig().getCellSize();
        cellPane.setPrefSize(cellSize, cellSize);
        setFillHeight(cellPane, true);
        setFillWidth(cellPane, true);
        cellPane.getStyleClass().add(CELL_PANE_STYLE_CLASS);
    }

    public void setGameOfLife(GameOfLife gameOfLife) {
        if (isNull(gameOfLife))
            throw new IllegalArgumentException();

        this.gameOfLife = gameOfLife;
        setGenerationNumberLabel();
        initGridPane();
        initGameStatisticLabels();
    }

    public void startGame() {
        gameOfLife.play();
    }

    public void pauseGame() {
        gameOfLife.pause();
    }

    public void restartGame() {
        gridPane.getChildren().clear();
        initGridPane();
        gameOfLife.reset();
        pauseToggleButton.setSelected(true);
    }

    public void slowToggleButtonAction() {
        gameOfLife.setSpeed(Speed.SLOW);
    }

    public void mediumToggleButtonAction() {
        gameOfLife.setSpeed(Speed.MEDIUM);
    }

    public void fastToggleButtonAction() {
        gameOfLife.setSpeed(Speed.FAST);
    }

    public void fastestToggleButtonAction() {
        gameOfLife.setSpeed(Speed.FASTEST);
    }
}
