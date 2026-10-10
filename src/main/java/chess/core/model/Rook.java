package chess.core.model;

public class Rook extends Piece {

    public Rook(Color color) {
        super(color);
    }

    // Se mueve en línea recta (misma fila o misma columna) sin saltar piezas.
    @Override
    public boolean canMoveTo(Position from, Position to, Board board) {
        boolean straight = from.row() == to.row() || from.col() == to.col();
        return straight && board.isPathClear(from, to);
    }
}
