package ru.gameoflife.random;

import java.util.concurrent.*;

import static ru.gameoflife.constants.Constants.*;

public class RandomExtensions {
    private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    public static int getRandomRowIdx() {
        return RANDOM.nextInt(0, GRID_HEIGHT);
    }

    public static int getRandomColIdx() {
        return RANDOM.nextInt(0, GRID_WIDTH);
    }

    public static int nextInt(int start, int bound) {
        return RANDOM.nextInt(start, bound);
    }

    public static int nextInt(int bound) {
        return RANDOM.nextInt(0, bound);
    }
}
