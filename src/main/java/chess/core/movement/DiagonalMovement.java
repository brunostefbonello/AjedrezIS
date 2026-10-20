package chess.core.movement;

import chess.core.model.Board;
import chess.core.model.MovementStrategy;
import chess.core.model.Piece;
import chess.core.model.Position;

/** Diagonal (misma distancia en filas y columnas) sin saltar piezas. La usan el alfil y la reina. */
public class DiagonalMovement implements MovementStrategy {

    @Override
    public boolean canMove(Piece piece, Position from, Position to, Board board) {
        boolean diagonal = Math.abs(to.row() - from.row()) == Math.abs(to.col() - from.col());
        return diagonal && board.isPathClear(from, to);
    }
}
