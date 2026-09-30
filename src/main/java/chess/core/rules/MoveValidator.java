package chess.core.rules;

import chess.core.model.Board;
import chess.core.model.Piece;
import chess.core.model.Position;

/**
 * Responde una sola pregunta: ¿es válido mover la pieza de {@code from} a {@code to},
 * dado este tablero?
 *
 * Función pura: mismo tablero + mismo movimiento = mismo resultado. No modifica nada.
 *
 * Solo aplica las reglas que valen para TODAS las piezas. La geometría de cada pieza
 * la resuelve su propio {@code canMoveTo}. El turno lo resuelve el estado de la partida.
 */
public class MoveValidator {

    public boolean isValidMove(Board board, Position from, Position to) {
        if (from.equals(to)) {
            return false;                       // quedarse en el lugar no es un movimiento
        }

        Piece piece = board.getPiece(from);
        if (piece == null) {
            return false;                       // no hay nada que mover
        }

        if (piece.isAlly(board.getPiece(to))) {
            return false;                       // no se puede caer sobre una pieza propia
        }

        // Superados los chequeos generales, decide la pieza.
        return piece.canMoveTo(from, to, board);
    }
}
