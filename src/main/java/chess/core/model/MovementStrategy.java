package chess.core.model;

// Strategy: solo geometría; MoveValidator ya filtró lo general
public interface MovementStrategy {

    boolean canMove(Piece piece, Position from, Position to, Board board);
}
