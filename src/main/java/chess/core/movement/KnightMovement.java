package chess.core.movement;

import chess.core.model.Board;
import chess.core.model.MovementStrategy;
import chess.core.model.Piece;
import chess.core.model.Position;

// Salta piezas: no consulta isPathClear
public class KnightMovement implements MovementStrategy {

    @Override
    public boolean canMove(Piece piece, Position from, Position to, Board board) {
        int rowDistance = Math.abs(to.row() - from.row());
        int colDistance = Math.abs(to.col() - from.col());

        return (rowDistance == 2 && colDistance == 1)
                || (rowDistance == 1 && colDistance == 2);
    }
}
