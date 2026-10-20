package chess.core.setup;

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
}
