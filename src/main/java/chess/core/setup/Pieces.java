package chess.core.setup;

import chess.core.model.Color;
import chess.core.model.Piece;
import chess.core.model.PieceType;
import chess.core.movement.KingMovement;
import chess.core.movement.CombinedMovement;
import chess.core.movement.DiagonalMovement;
import chess.core.movement.KnightMovement;
import chess.core.movement.PawnMovement;
import chess.core.movement.StraightMovement;

// Único lugar que asigna estrategia a cada tipo
public final class Pieces {

    private Pieces() {
    }

    public static Piece rook(Color color) {
        return new Piece(color, PieceType.ROOK, new StraightMovement());
    }

    public static Piece bishop(Color color) {
        return new Piece(color, PieceType.BISHOP, new DiagonalMovement());
    }

    public static Piece queen(Color color) {
        return new Piece(color, PieceType.QUEEN,
                new CombinedMovement(new StraightMovement(), new DiagonalMovement()));
    }

    public static Piece knight(Color color) {
        return new Piece(color, PieceType.KNIGHT, new KnightMovement());
    }

    public static Piece pawn(Color color) {
        return new Piece(color, PieceType.PAWN, new PawnMovement());
    }

    public static Piece king(Color color) {
        return new Piece(color, PieceType.KING, new KingMovement());
    }
}
