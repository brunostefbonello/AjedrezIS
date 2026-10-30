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

/**
 * Único lugar donde se decide qué estrategia lleva cada tipo de pieza.
 * Así nadie arma por error una torre con movimiento diagonal.
 *
 * Cada integrante agrega acá el método de sus piezas, por ejemplo:
 *
 *     public static Piece rook(Color color) {
 *         return new Piece(color, PieceType.ROOK, new StraightMovement());
 *     }
 *
 * Pendiente: king() (Integrante D).
 */
public final class Pieces {

    private Pieces() {
    }

    public static Piece rook(Color color) {
        return new Piece(color, PieceType.ROOK, new StraightMovement());
    }

    public static Piece bishop(Color color) {
        return new Piece(color, PieceType.BISHOP, new DiagonalMovement());
    }

    // La reina no tiene clase propia: combina los movimientos de la torre y el alfil.
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
