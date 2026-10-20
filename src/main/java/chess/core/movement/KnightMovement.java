package chess.core.movement;

import chess.core.model.Board;
import chess.core.model.MovementStrategy;
import chess.core.model.Piece;
import chess.core.model.Position;

/**
 * Caballo: se mueve en "L" (dos casillas en una dirección y una en la perpendicular).
 * Es la única pieza que salta sobre otras, por eso no consulta si el camino está libre.
 *
 * Precondición (la garantiza MoveValidator): en {@code from} está la pieza,
 * from != to y en {@code to} no hay un aliado.
 */
public class KnightMovement implements MovementStrategy {

    @Override
    public boolean canMove(Piece piece, Position from, Position to, Board board) {
        int rowDistance = Math.abs(to.row() - from.row());
        int colDistance = Math.abs(to.col() - from.col());

        return (rowDistance == 2 && colDistance == 1)
                || (rowDistance == 1 && colDistance == 2);
    }
}
