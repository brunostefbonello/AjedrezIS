package chess.core.setup;

import chess.core.model.Color;
import chess.core.model.Piece;
import chess.core.model.PieceType;
import chess.core.movement.KnightMovement;
import chess.core.movement.PawnMovement;

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
 * Pendientes: rook(), bishop(), queen() (Integrante B),
 *             knight(), pawn() (Integrante C), king() (Integrante D).
 */
public final class Pieces {

    private Pieces() {
    }

    public static Piece knight(Color color) {
        return new Piece(color, PieceType.KNIGHT, new KnightMovement());
    }

    public static Piece pawn(Color color) {
        return new Piece(color, PieceType.PAWN, new PawnMovement());
    }
}
