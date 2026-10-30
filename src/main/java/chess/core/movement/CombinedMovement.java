package chess.core.movement;

import chess.core.model.Board;
import chess.core.model.MovementStrategy;
import chess.core.model.Piece;
import chess.core.model.Position;

import java.util.List;

/**
 * Se puede mover si CUALQUIERA de las estrategias lo permite.
 * La reina es Straight + Diagonal: reutiliza las dos en vez de repetir su lógica.
 */
public class CombinedMovement implements MovementStrategy {

    private final List<MovementStrategy> options;

    public CombinedMovement(MovementStrategy... options) {
        this.options = List.of(options);
    }

    @Override
    public boolean canMove(Piece piece, Position from, Position to, Board board) {
        for (MovementStrategy option : options) {
            if (option.canMove(piece, from, to, board)) {
                return true;
            }
        }
        return false;
    }
}
