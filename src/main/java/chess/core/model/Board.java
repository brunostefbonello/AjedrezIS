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

    // true si todas las casillas ESTRICTAMENTE entre from y to están vacías.
    // Solo tiene sentido para líneas rectas y diagonales: para cualquier otra
    // dirección devuelve false (no hay un "camino" que recorrer).
    public boolean isPathClear(Position from, Position to) {
        int rowDiff = to.row() - from.row();
        int colDiff = to.col() - from.col();
        boolean straight = rowDiff == 0 || colDiff == 0;
        boolean diagonal = Math.abs(rowDiff) == Math.abs(colDiff);
        if (!straight && !diagonal) {
            return false;
        }

        int rowStep = Integer.signum(rowDiff);
        int colStep = Integer.signum(colDiff);
        int row = from.row() + rowStep;
        int col = from.col() + colStep;
        while (row != to.row() || col != to.col()) {
            if (squares[row][col] != null) {
                return false;
            }
            row += rowStep;
            col += colStep;
        }
        return true;
    }
}