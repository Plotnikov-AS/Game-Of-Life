package ru.gameoflife.builder;

import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;
import ru.gameoflife.*;
import ru.gameoflife.controller.*;

import java.io.*;

public interface ViewBuilder {

    static Controller createView(String viewPath, String windowTitle, Integer width, Integer height) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(Main.class.getResource(viewPath));
            Scene scene = new Scene(fxmlLoader.load(), width, height);
            Stage stage = new Stage();
            stage.setTitle(windowTitle);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
            return fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
