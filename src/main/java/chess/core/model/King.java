package chess.core.model;

public class King extends Piece {

    public King(Color color) {
        super(color);
    }

    @Override
    public boolean canMoveTo(Position from, Position to, Board board) {
        int deltaRow = Math.abs(to.row() - from.row());
        int deltaCol = Math.abs(to.col() - from.col());

        // El rey se mueve exactamente 1 casilla en horizontal, vertical o diagonal
        return deltaRow <= 1 && deltaCol <= 1;
    }
}
