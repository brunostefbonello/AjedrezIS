package chess.core.commands;

import chess.core.model.Board;
import chess.core.model.Piece;
import chess.core.model.Position;

/**
 * Mueve una pieza de {@code from} a {@code to} y sabe revertirlo.
 *
 * No valida nada: asume que el movimiento ya fue validado antes de crearlo.
 * El undo solo puede revertir lo que execute() se acordó de guardar.
 */
public class MoveCommand implements ICommand {

    private final Board board;
    private final Position from;
    private final Position to;

    // Memoria del comando: se llena en execute() y la usa undo()
    private Piece movedPiece;
    private Piece capturedPiece;        // null si no hubo captura
    private boolean wasFirstMove;
    private boolean executed;

    public MoveCommand(Board board, Position from, Position to) {
        this.board = board;
        this.from = from;
        this.to = to;
    }

    @Override
    public void execute() {
        movedPiece = board.getPiece(from);
        if (movedPiece == null) {
            throw new IllegalStateException("No piece at " + from);
        }

        capturedPiece = board.getPiece(to);           // guardo ANTES de pisarla
        wasFirstMove = !movedPiece.hasMoved();        // guardo ANTES de marcarla

        board.setPiece(to, movedPiece);
        board.setPiece(from, null);
        movedPiece.markAsMoved();

        executed = true;
    }

    @Override
    public void undo() {
        if (!executed) {
            throw new IllegalStateException("Cannot undo a command that was not executed");
        }

        board.setPiece(from, movedPiece);
        board.setPiece(to, capturedPiece);            // si era null, la casilla vuelve a quedar vacía

        if (wasFirstMove) {
            movedPiece.resetMoved();                  // solo si ESTE fue su primer movimiento
        }

        executed = false;
    }
}
