package ru.gameoflife.config;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.dataformat.yaml.*;
import lombok.*;
import ru.gameoflife.config.model.*;

import java.io.*;

import static ru.gameoflife.constants.Constants.*;

@Getter
@Setter
@NoArgsConstructor
public class Configuration {
    @Getter
    private static final Configuration config = init();
    @JsonProperty("grid")
    private GridConfig gridConfig;
    @JsonProperty
    private GameConfig gameConfig;

    private static Configuration init() {
        File file = new File(CONFIG_PATH);
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        try {
            return objectMapper.readValue(file, Configuration.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
