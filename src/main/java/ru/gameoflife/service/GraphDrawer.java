package ru.gameoflife.service;

import javafx.scene.chart.*;

import java.util.*;

import static ru.gameoflife.constants.Constants.*;

public interface GraphDrawer {
    static void nextStatePopulationChart(Long generationNum, Map<String, Long> statistic, LineChart<Long, Long> populationChart) {
        XYChart.Series<Long, Long> aliveData = populationChart.getData().stream()
                .filter(data -> ALIVE.equalsIgnoreCase(data.getName()))
                .findAny()
                .orElse(new XYChart.Series<>());
        XYChart.Series<Long, Long> dyingData = populationChart.getData().stream()
                .filter(data -> DYING.equalsIgnoreCase(data.getName()))
                .findAny()
                .orElse(new XYChart.Series<>());
        XYChart.Series<Long, Long> bornData = populationChart.getData().stream()
                .filter(data -> BORN.equalsIgnoreCase(data.getName()))
                .findAny()
                .orElse(new XYChart.Series<>());

        aliveData.setName(ALIVE);
        aliveData.getData().add(new XYChart.Data<>(generationNum, statistic.get(ALIVE)));
//        aliveData.getNode().setStyle(ALIVE_LINE_STYLE_CLASS);
        checkLineLength(aliveData);

        dyingData.setName(DYING);
        dyingData.getData().add(new XYChart.Data<>(generationNum, statistic.get(DYING)));
//        dyingData.getNode().setStyle(DYING_LINE_STYLE_CLASS);
        checkLineLength(dyingData);

        bornData.setName(BORN);
        bornData.getData().add(new XYChart.Data<>(generationNum, statistic.get(BORN)));
//        bornData.getNode().setStyle(BORN_LINE_STYLE_CLASS);
        checkLineLength(bornData);


        if (!populationChart.getData().contains(aliveData)) {
            populationChart.getData().add(aliveData);
        }
        if (!populationChart.getData().contains(dyingData)) {
            populationChart.getData().add(dyingData);
        }
        if (!populationChart.getData().contains(bornData)) {
            populationChart.getData().add(bornData);
        }
    }

    static void checkLineLength(XYChart.Series<Long, Long> data) {
        if (data.getData().size() > MAX_GRAPH_LINE_LENGTH) {
            data.getData().remove(0);
        }
    }
}
