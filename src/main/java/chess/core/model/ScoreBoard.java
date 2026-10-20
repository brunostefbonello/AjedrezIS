package chess.core.model;

import java.util.EnumMap;
import java.util.Map;

/** Puntos acumulados por cada color al capturar piezas. */
public class ScoreBoard {

    private final Map<Color, Integer> points = new EnumMap<>(Color.class);

    public int score(Color color) {
        return points.getOrDefault(color, 0);
    }

    public void add(Color color, int amount) {
        points.put(color, score(color) + amount);
    }

    /** Lo usa el undo de una captura. */
    public void subtract(Color color, int amount) {
        points.put(color, score(color) - amount);
    }
}
