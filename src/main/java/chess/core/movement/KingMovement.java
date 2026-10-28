package chess.core.movement;

import chess.core.model.Board;
import chess.core.model.MovementStrategy;
import chess.core.model.Piece;
import chess.core.model.Position;

public class KingMovement implements MovementStrategy {

    @Override
    public boolean canMove(Piece piece, Position from, Position to, Board board) {
        int deltaRow = Math.abs(to.row() - from.row());
        int deltaCol = Math.abs(to.col() - from.col());

        // El rey se mueve exactamente 1 casilla en cualquier dirección
        return deltaRow <= 1 && deltaCol <= 1;
    }
}