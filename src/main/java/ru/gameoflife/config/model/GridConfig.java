package ru.gameoflife.config.model;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import java.io.*;

@NoArgsConstructor
@Setter
@Getter
public class GridConfig implements Serializable {
    @JsonProperty
    private Integer cellSize;
    @JsonProperty
    private Integer width;
    @JsonProperty
    private Integer height;


}
