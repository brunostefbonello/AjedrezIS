package chess.core.model;

public class Bishop extends Piece {

    public Bishop(Color color) {
        super(color);
    }

    // Se mueve en diagonal (misma distancia en filas y columnas) sin saltar piezas.
    @Override
    public boolean canMoveTo(Position from, Position to, Board board) {
        boolean diagonal = Math.abs(to.row() - from.row()) == Math.abs(to.col() - from.col());
        return diagonal && board.isPathClear(from, to);
    }
}
