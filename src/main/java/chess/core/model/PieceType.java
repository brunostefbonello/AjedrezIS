package chess.core.model;

// Rey vale 0: capturarlo termina la partida
public enum PieceType {
    PAWN(1),
    KNIGHT(3),
    BISHOP(3),
    ROOK(5),
    QUEEN(9),
    KING(0);

    private final int value;

    PieceType(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
