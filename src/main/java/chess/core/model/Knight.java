package chess.core.model;

/**
 * Caballo: se mueve en "L" (dos casillas en una dirección y una en la perpendicular).
 * Es la única pieza que salta sobre otras, por eso no consulta si el camino está libre.
 *
 * Precondición (la garantiza MoveValidator): en {@code from} está esta pieza,
 * from != to y en {@code to} no hay un aliado.
 */
public class Knight extends Piece {

    public Knight(Color color) {
        super(color);
    }

    @Override
    public boolean canMoveTo(Position from, Position to, Board board) {
        int rowDistance = Math.abs(to.row() - from.row());
        int colDistance = Math.abs(to.col() - from.col());

        return (rowDistance == 2 && colDistance == 1)
                || (rowDistance == 1 && colDistance == 2);
    }
}

