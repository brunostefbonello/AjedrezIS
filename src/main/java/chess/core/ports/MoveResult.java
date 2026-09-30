package chess.core.ports;

/** Resultado de pedirle un movimiento al núcleo. */
public enum MoveResult {
    OK,
    INVALID_MOVE,
    NOT_YOUR_TURN,
    GAME_OVER
}
