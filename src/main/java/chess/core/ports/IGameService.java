package chess.core.ports;

import chess.core.model.Color;
import chess.core.model.Piece;
import chess.core.model.Position;

// Puerto de entrada: lo único que ven los adaptadores
public interface IGameService {

    MoveResult move(Position from, Position to);

    /** @return false si no había nada para deshacer. */
    boolean undo();

    /** @return null si la casilla está vacía. */
    Piece pieceAt(Position position);

    /** @return null si la partida terminó. */
    Color currentTurn();

    boolean isOver();

    int score(Color color);

    /** @return null si sigue en curso o hay empate; ver isOver(). */
    Color winner();
}
