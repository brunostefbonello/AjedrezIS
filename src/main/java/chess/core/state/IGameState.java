package chess.core.state;

import chess.core.model.Color;

/**
 * Contrato de los estados de la partida (patrón State).
 * Las implementaciones (WhiteTurn, BlackTurn, GameOver) son del Integrante D.
 */
public interface IGameState {

    /** ¿Le toca mover a este color en esta fase? */
    boolean canMove(Color color);

    /** Estado al que se pasa después de un movimiento válido. */
    IGameState next();

    boolean isOver();
}
