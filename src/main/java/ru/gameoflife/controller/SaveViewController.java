package ru.gameoflife.controller;

import javafx.scene.control.*;
import lombok.*;
import ru.gameoflife.model.*;

import java.io.*;
import java.nio.file.*;

import static java.util.Objects.*;
import static ru.gameoflife.builder.ViewBuilder.*;
import static ru.gameoflife.constants.Constants.*;
import static ru.gameoflife.constants.Constants.ErrorMessages.*;

@Setter
public class SaveViewController implements Controller {
    public Button btnSave;
    public TextField txtFileName;
    public Label lblSaveResult;
    private GameOfLife gameOfLife;

    public void saveGame() {
        try {
            if (isNull(txtFileName.getText()) || txtFileName.getText().isEmpty()) {
                ErrorController errorController = (ErrorController) createView(ERROR_VIEW_PATH, ERROR_VIEW_WINDOW_NAME, 350, 200);
                errorController.setErrorMsg(EMPTY_SAVE_NAME);
                lblSaveResult.setDisable(false);
                lblSaveResult.setText(FAILED);
                return;
            }
            String path = PATH_TO_SAVE_GAME + txtFileName.getText() + ".save";
            if (directoryIsNotExist(path)) {
                Files.createDirectory(Paths.get(PATH_TO_SAVE_GAME));
            }
            FileOutputStream fileOut = new FileOutputStream(path);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(gameOfLife);
            objectOut.close();
            lblSaveResult.setDisable(false);
            lblSaveResult.setText(SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            ErrorController errorController = (ErrorController) createView(ERROR_VIEW_PATH, ERROR_VIEW_WINDOW_NAME, 500, 200);
            errorController.setErrorMsg(e.getMessage());
        }
    }

    private boolean isDirectoryExist(String path) {
        File directory = new File(path);
        return directory.exists();
    }

    private boolean directoryIsNotExist(String path) {
        return !isDirectoryExist(path);
    }

}
