package ru.gameoflife.controller;

import javafx.collections.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import lombok.*;
import ru.gameoflife.model.*;
import ru.gameoflife.service.*;

import java.io.*;

import static java.util.Objects.*;
import static javafx.collections.FXCollections.*;
import static ru.gameoflife.constants.Constants.*;

public class LoadController implements Controller {
    public AnchorPane rootPane;
    public ChoiceBox<File> chbSelectFile;
    public Button btnLoad;

    @Setter
    private MainController mainController;

    public void initialize() {
        initChbSelectFile();
    }

    private void initChbSelectFile() {
        File saveDirectory = new File(PATH_TO_SAVE_GAME);
        File[] saveFiles = saveDirectory.listFiles();
        if (isNull(saveFiles) || saveFiles.length == 0) {
            throw new RuntimeException();
        }
        ObservableList<File> saveNames = observableArrayList(saveFiles);
        chbSelectFile.setItems(saveNames);
    }

    public void loadSave() {
        if (chbSelectFile.getItems().isEmpty()) {
            throw new RuntimeException();
        }
        GameOfLife gameOfLife = SaveLoadService.loadGame(chbSelectFile.getValue());
        mainController.setGameOfLife(gameOfLife);
    }
}
