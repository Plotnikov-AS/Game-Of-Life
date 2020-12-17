package ru.gameoflife;

import javafx.application.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;
import ru.gameoflife.controller.*;
import ru.gameoflife.model.*;

import java.io.*;

import static ru.gameoflife.constants.Constants.*;

public class Main extends Application {
    private final GameOfLife gameOfLife;
    private Stage primaryStage;
    private FXMLLoader fxmlLoader;
    private Parent view;

    public Main() {
        this(new GameOfLife());
    }

    public Main(GameOfLife gameOfLife) {
        this.gameOfLife = gameOfLife;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        initPrimaryStage(primaryStage);
        initFxmlLoader();
        initView();
        initController();
        showScene();
    }

    private void showScene() {
        primaryStage.setScene(new Scene(view));
        primaryStage.show();
    }

    private void initController() {
        MainController controller = fxmlLoader.getController();
        controller.setGameOfLife(gameOfLife);
    }

    private void initView() throws IOException {
        view = fxmlLoader.load();
    }

    private void initFxmlLoader() {
        fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(Main.class.getResource(VIEW_PATH));
    }

    private void initPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle(APP_NAME);
        this.primaryStage.setOnCloseRequest(event -> System.exit(0));
        this.primaryStage.setResizable(false);
        this.primaryStage.sizeToScene();
    }


}
