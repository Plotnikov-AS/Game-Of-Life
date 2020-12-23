package ru.gameoflife.constants;

import java.util.*;

import static ru.gameoflife.config.Configuration.*;
import static ru.gameoflife.random.RandomExtensions.*;

public interface Constants {
    String APP_NAME = "Game of Life";
    String SAVE_GAME_WINDOW_NAME = "Save game";
    String LOAD_GAME_WINDOW_NAME = "Load game";
    String ERROR_VIEW_WINDOW_NAME = "Error";
    String CONFIG_PATH = "src/main/resources/сonfig.yml";
    String MAIN_VIEW_PATH = "/view/view.fxml";
    String SAVE_VIEW_PATH = "/view/saveView.fxml";
    String LOAD_VIEW_PATH = "/view/loadView.fxml";
    String ERROR_VIEW_PATH = "/view/errorView.fxml";
    String PATH_TO_SAVE_GAME = "saved/";

    String ROW = "row";
    String COLUMN = "column";
    String SUCCESS = "Success";
    String FAILED = "Failed";
    String ALIVE = "alive";
    String BORN = "born";
    String DYING = "dying";
    String GENERATION_NUM = "generationNum";

    int COLONIES_COUNT = getConfig().getGameConfig().getColoniesCount();
    int MAX_CELLS_NUMBER = getConfig().getGameConfig().getMaxCellNumber();
    int CELLS_IN_COLONY = MAX_CELLS_NUMBER / COLONIES_COUNT;
    int DISTANCE_BETWEEN_COLONIES = getConfig().getGameConfig().getMinSpaceBetweenCells();
    int NEIGHBOURS_TO_BORN = getConfig().getGameConfig().getCellsCountToBornNew();
    int MAX_NEIGHBOURS = getConfig().getGameConfig().getMaxNeighbours();
    int MIN_NEIGHBOURS = getConfig().getGameConfig().getMinNeighbours();
    int NEIGHBOURS_RADIUS = getConfig().getGameConfig().getNeighboursCellRadius();
    int GRID_HEIGHT = getConfig().getGridConfig().getHeight();
    int GRID_WIDTH = getConfig().getGridConfig().getWidth();

    interface ErrorMessages {
        String CELL_NOT_EXIST_ON_COORD = "Cell not exist on coordinates row: %s, col: %s";
        String ROUND_TOO_BIG_ERROR = "Round is too big!%nCell coordinates: row %s, col %s;%nRound offset: %s";
        String EMPTY_SAVE_NAME = "Save name is empty. Please enter save name";
    }

    interface Style {
        String CELL_PANE_STYLE_CLASS = "cell-pane";
        String ALIVE_STYLE_CLASS = "alive";
    }

    enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT;
        public static Direction getRandomDirection() {
            List<Direction> directions = new ArrayList<>();
            directions.add(UP);
            directions.add(DOWN);
            directions.add(LEFT);
            directions.add(RIGHT);
            return directions.get(nextInt(directions.size()));
        }

    }

}
