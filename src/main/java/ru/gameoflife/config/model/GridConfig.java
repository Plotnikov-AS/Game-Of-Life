package ru.gameoflife.config.model;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

@NoArgsConstructor
@Setter
@Getter
public class GridConfig {
    @JsonProperty
    private Integer cellSize;
    @JsonProperty
    private Integer width;
    @JsonProperty
    private Integer height;


}
