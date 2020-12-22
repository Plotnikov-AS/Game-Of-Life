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
    private FXMLLoader viewFxmlLoader;
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
        initFxmlLoaders();
        initView();
        initControllers();
        showScene();
    }

    private void showScene() {
        primaryStage.setScene(new Scene(view));
        primaryStage.show();
    }

    private void initControllers() {
        MainController controller = viewFxmlLoader.getController();
        controller.setGameOfLife(gameOfLife);
    }

    private void initView() throws IOException {
        view = viewFxmlLoader.load();
    }

    private void initFxmlLoaders() {
        viewFxmlLoader = new FXMLLoader();
        viewFxmlLoader.setLocation(Main.class.getResource(VIEW_PATH));
    }

    private void initPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle(APP_NAME);
        this.primaryStage.setOnCloseRequest(event -> System.exit(0));
        this.primaryStage.setResizable(false);
        this.primaryStage.sizeToScene();
    }


}
