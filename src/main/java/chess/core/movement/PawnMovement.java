package chess.core.movement;

import chess.core.model.Board;
import chess.core.model.Color;
import chess.core.model.MovementStrategy;
import chess.core.model.Piece;
import chess.core.model.Position;

// Sin captura al paso ni promoción (fuera de alcance)
public class PawnMovement implements MovementStrategy {

    @Override
    public boolean canMove(Piece piece, Position from, Position to, Board board) {
        int direction = (piece.color() == Color.WHITE) ? 1 : -1;
        int rowDelta = to.row() - from.row();
        int colDelta = to.col() - from.col();

        if (colDelta == 0) {
            return canAdvance(piece, from, to, board, direction, rowDelta);
        }
        return canCapture(piece, to, board, direction, rowDelta, colDelta);
    }

    private boolean canAdvance(Piece piece, Position from, Position to, Board board,
                               int direction, int rowDelta) {
        if (!board.isEmpty(to)) {
            return false;                           // avanzando no se captura
        }
        if (rowDelta == direction) {
            return true;
        }
        if (rowDelta == 2 * direction && !piece.hasMoved()) {
            Position between = new Position(from.row() + direction, from.col());
            return board.isEmpty(between);
        }
        return false;
    }

    private boolean canCapture(Piece piece, Position to, Board board,
                               int direction, int rowDelta, int colDelta) {
        if (rowDelta != direction || Math.abs(colDelta) != 1) {
            return false;                           // solo diagonal hacia adelante
        }
        Piece target = board.getPiece(to);
        return target != null && !piece.isAlly(target);
    }
}
