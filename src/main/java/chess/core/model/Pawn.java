package chess.core.model;

/**
 * Peón: avanza distinto de como captura.
 * - Avanza una casilla hacia adelante, solo si el destino está vacío.
 * - En su primer movimiento puede avanzar dos, si las dos casillas están vacías.
 * - Captura una casilla en diagonal hacia adelante, solo si hay un enemigo.
 *
 * Las blancas avanzan sumando row; las negras, restando.
 * No incluye captura al paso ni promoción (fuera de alcance por ahora).
 *
 * Precondición (la garantiza MoveValidator): en {@code from} está esta pieza,
 * from != to y en {@code to} no hay un aliado. A diferencia del resto de las piezas,
 * el peón sí mira el destino porque su avance y su captura son distintos.
 */
public class Pawn extends Piece {

    public Pawn(Color color) {
        super(color);
    }

    @Override
    public boolean canMoveTo(Position from, Position to, Board board) {
        int direction = (color() == Color.WHITE) ? 1 : -1;
        int rowDelta = to.row() - from.row();
        int colDelta = to.col() - from.col();

        if (colDelta == 0) {
            return canAdvance(from, to, board, direction, rowDelta);
        }
        return canCapture(to, board, direction, rowDelta, colDelta);
    }

    private boolean canAdvance(Position from, Position to, Board board, int direction, int rowDelta) {
        if (!board.isEmpty(to)) {
            return false;                           // avanzando no se captura: el destino debe estar vacío
        }
        if (rowDelta == direction) {
            return true;                            // una casilla
        }
        if (rowDelta == 2 * direction && !hasMoved()) {
            Position between = new Position(from.row() + direction, from.col());
            return board.isEmpty(between);          // dos casillas: la del medio también debe estar libre
        }
        return false;
    }

    private boolean canCapture(Position to, Board board, int direction, int rowDelta, int colDelta) {
        if (rowDelta != direction || Math.abs(colDelta) != 1) {
            return false;                           // la captura es una diagonal hacia adelante
        }
        Piece target = board.getPiece(to);
        return target != null && !isAlly(target);   // tiene que haber un enemigo
    }
}