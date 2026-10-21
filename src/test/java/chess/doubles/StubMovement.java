package chess.doubles;

import chess.core.model.Board;
import chess.core.model.Color;
import chess.core.model.MovementStrategy;
import chess.core.model.Piece;
import chess.core.model.PieceType;
import chess.core.model.Position;

/**
 * STUB: estrategia que responde siempre lo mismo, sin ninguna lógica.
 * Sirve para testear lo que rodea a las piezas (validador, comandos, partida)
 * o para poner "algo" en el camino, sin depender de las estrategias reales.
 */
public class StubMovement implements MovementStrategy {

    private final boolean canMove;

    public StubMovement(boolean canMove) {
        this.canMove = canMove;
    }

    @Override
    public boolean canMove(Piece piece, Position from, Position to, Board board) {
        return canMove;
    }

    /** Atajo: una pieza cualquiera (peón, vale 1) con movimiento stub. */
    public static Piece piece(Color color, boolean canMove) {
        return piece(color, PieceType.PAWN, canMove);
    }

    /** Atajo con tipo explícito, para tests de puntaje o de captura del rey. */
    public static Piece piece(Color color, PieceType type, boolean canMove) {
        return new Piece(color, type, new StubMovement(canMove));
    }
}
