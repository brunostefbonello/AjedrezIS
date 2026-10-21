package chess.core.model;

/**
 * Una pieza del tablero: color, tipo (con su valor) y una estrategia de movimiento.
 *
 * Es {@code final} a propósito: las piezas ya no se diferencian por herencia,
 * sino por la MovementStrategy que reciben (composición).
 * Para crearlas usar {@code chess.core.setup.Pieces}.
 */
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

    /** Puntos que gana el rival al capturar esta pieza. */
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

    /** Delega en la estrategia: la pieza no sabe cómo se mueve, se lo pregunta a su movimiento. */
    public boolean canMoveTo(Position from, Position to, Board board) {
        return movement.canMove(this, from, to, board);
    }
}
