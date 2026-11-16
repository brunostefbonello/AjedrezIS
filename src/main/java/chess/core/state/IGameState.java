package chess.core.state;

import chess.core.model.Color;

// Patrón State de la partida
public interface IGameState {

    boolean canMove(Color color);

    /** Estado tras un movimiento válido. */
    IGameState next();

    boolean isOver();
}
