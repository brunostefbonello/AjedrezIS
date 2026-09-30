package chess.core.model;

public abstract class Piece {

    private final Color color;
    private boolean hasMoved;

    protected Piece(Color color) {
        this.color = color;
    }

    public Color color() {
        return color;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public void markAsMoved() {
        this.hasMoved = true;
    }

    public void resetMoved() {
        this.hasMoved = false;
    }

    public boolean isAlly(Piece other) {
        return other != null && other.color == this.color;
    }

    public abstract boolean canMoveTo(Position from, Position to, Board board);
}
