package ru.gameoflife.model;

import lombok.*;
import ru.gameoflife.config.*;

import java.io.*;
import java.util.*;

@AllArgsConstructor
@Value
public class SaveGame implements Serializable {
    boolean[][] aliveCells;
    boolean[][] bornCells;
    boolean[][] dyingCells;
    Configuration configuration;
    Map<String, Long> gameStatistic;
}
