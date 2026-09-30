package chess.core.ports;

import chess.core.model.Color;
import chess.core.model.Piece;
import chess.core.model.Position;

/**
 * Puerto de entrada: lo único que un adaptador (la consola, o mañana una UI)
 * necesita conocer para jugar. El núcleo no sabe quién lo llama.
 */
public interface IGameService {

    MoveResult move(Position from, Position to);

    /** @return false si no había ningún movimiento para deshacer. */
    boolean undo();

    /** @return la pieza en esa casilla, o null si está vacía. */
    Piece pieceAt(Position position);

    /** @return el color al que le toca mover, o null si la partida terminó. */
    Color currentTurn();

    boolean isOver();
}
