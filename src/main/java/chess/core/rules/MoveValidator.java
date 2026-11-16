package chess.core.rules;

import chess.core.model.Board;
import chess.core.model.Piece;
import chess.core.model.Position;

// Solo reglas comunes; turno y geometría van aparte
public class MoveValidator {

    public boolean isValidMove(Board board, Position from, Position to) {
        if (from.equals(to)) {
            return false;
        }

        Piece piece = board.getPiece(from);
        if (piece == null) {
            return false;
        }

        if (piece.isAlly(board.getPiece(to))) {
            return false;
        }

        return piece.canMoveTo(from, to, board);
    }
}
