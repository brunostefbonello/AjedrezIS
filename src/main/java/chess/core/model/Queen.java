package chess.core.model;

public class Queen extends Piece {

    public Queen(Color color) {
        super(color);
    }

    // Combina torre y alfil: línea recta o diagonal, sin saltar piezas.
    @Override
    public boolean canMoveTo(Position from, Position to, Board board) {
        boolean straight = from.row() == to.row() || from.col() == to.col();
        boolean diagonal = Math.abs(to.row() - from.row()) == Math.abs(to.col() - from.col());
        return (straight || diagonal) && board.isPathClear(from, to);
    }
}
