package chess.core.model;

public class Board {

    private final Piece[][] squares = new Piece[8][8];

    public Piece getPiece(Position position) {
        return squares[position.row()][position.col()];
    }

    public void setPiece(Position position, Piece piece) {
        squares[position.row()][position.col()] = piece;
    }

    public boolean isEmpty(Position position) {
        return getPiece(position) == null;
    }
}