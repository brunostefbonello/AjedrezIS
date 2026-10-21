package chess.core.movement;

import chess.core.model.Board;
import chess.core.model.Color;
import chess.core.model.MovementStrategy;
import chess.core.model.Piece;
import chess.core.model.Position;

/**
 * Peón: avanza distinto de como captura.
 * - Avanza una casilla hacia adelante, solo si el destino está vacío.
 * - En su primer movimiento puede avanzar dos, si las dos casillas están vacías.
 * - Captura una casilla en diagonal hacia adelante, solo si hay un enemigo.
 *
 * Las blancas avanzan sumando row; las negras, restando.
 * No incluye captura al paso ni promoción (fuera de alcance por ahora).
 *
 * Precondición (la garantiza MoveValidator): en {@code from} está la pieza,
 * from != to y en {@code to} no hay un aliado. A diferencia del resto de las piezas,
 * el peón sí mira el destino porque su avance y su captura son distintos.
 */
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
            return false;                           // avanzando no se captura: el destino debe estar vacío
        }
        if (rowDelta == direction) {
            return true;                            // una casilla
        }
        if (rowDelta == 2 * direction && !piece.hasMoved()) {
            Position between = new Position(from.row() + direction, from.col());
            return board.isEmpty(between);          // dos casillas: la del medio también debe estar libre
        }
        return false;
    }

    private boolean canCapture(Piece piece, Position to, Board board,
                               int direction, int rowDelta, int colDelta) {
        if (rowDelta != direction || Math.abs(colDelta) != 1) {
            return false;                           // la captura es una diagonal hacia adelante
        }
        Piece target = board.getPiece(to);
        return target != null && !piece.isAlly(target);   // tiene que haber un enemigo
    }
}
