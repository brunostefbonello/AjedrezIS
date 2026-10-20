package chess.core.movement;

import chess.core.model.Board;
import chess.core.model.MovementStrategy;
import chess.core.model.Piece;
import chess.core.model.Position;

/** Línea recta (misma fila o misma columna) sin saltar piezas. La usan la torre y la reina. */
public class StraightMovement implements MovementStrategy {

    @Override
    public boolean canMove(Piece piece, Position from, Position to, Board board) {
        boolean straight = from.row() == to.row() || from.col() == to.col();
        return straight && board.isPathClear(from, to);
    }
}
