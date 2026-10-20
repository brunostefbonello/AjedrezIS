package chess.core.setup;

import chess.core.model.Color;
import chess.core.model.Piece;
import chess.core.model.PieceType;
import chess.core.movement.CombinedMovement;
import chess.core.movement.DiagonalMovement;
import chess.core.movement.StraightMovement;

/**
 * Único lugar donde se decide qué estrategia lleva cada tipo de pieza.
 * Así nadie arma por error una torre con movimiento diagonal.
 *
 * Pendientes: knight() y pawn() (Integrante C), king() (Integrante D).
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

    public static Piece queen(Color color) {
        return new Piece(color, PieceType.QUEEN,
                new CombinedMovement(new StraightMovement(), new DiagonalMovement()));
    }
}
