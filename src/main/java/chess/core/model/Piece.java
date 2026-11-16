package chess.core.model;

// final: varía por estrategia, no por herencia. Crear con Pieces
public final class Piece {

    private final Color color;
    private final PieceType type;
    private final MovementStrategy movement;
    private boolean hasMoved;

    public Piece(Color color, PieceType type, MovementStrategy movement) {
        this.color = color;
        this.type = type;
        this.movement = movement;
    }

    public Color color() {
        return color;
    }

    public PieceType type() {
        return type;
    }

    /** Puntos para quien la captura. */
    public int value() {
        return type.value();
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

    public boolean canMoveTo(Position from, Position to, Board board) {
        return movement.canMove(this, from, to, board);
    }
}
