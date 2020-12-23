package ru.gameoflife.config.model;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import java.io.*;

@NoArgsConstructor
@Setter
@Getter
public class GameConfig implements Serializable {
    @JsonProperty
    private Integer maxCellNumber;
    @JsonProperty
    private Integer coloniesCount;
    @JsonProperty
    private Integer minSpaceBetweenCells;
    @JsonProperty
    private Integer neighboursCellRadius;
    @JsonProperty
    private Integer maxNeighbours;
    @JsonProperty
    private Integer minNeighbours;
    @JsonProperty
    private Integer cellsCountToBornNew;
    @JsonProperty
    private Integer minNeighboursToBornColony;
}
