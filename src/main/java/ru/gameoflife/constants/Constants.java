package ru.gameoflife.constants;

import java.util.*;

import static ru.gameoflife.config.Configuration.*;

public interface Constants {
    String APP_NAME = "Game of Life";
    String CONFIG_PATH = "src/main/resources/сonfig.yml";
    String VIEW_PATH = "/view/view.fxml";
    Random RANDOM = new Random();

    int COLONIES_COUNT = getConfig().getGameConfig().getColoniesCount();
    int MAX_CELLS_IN_COLONY = getConfig().getGameConfig().getMaxCellNumber() / COLONIES_COUNT;

    interface ErrorMessages {
        String CELL_NOT_EXIST_ON_COORD = "Cell not exist on coordinates x: %s, y: %s";
    }

    interface Style {
        String CELL_PANE_STYLE_CLASS = "cell-pane";
        String ALIVE_STYLE_CLASS = "alive";
    }

}
