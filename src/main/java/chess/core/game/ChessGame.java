package chess.core.game;

import chess.core.commands.MoveCommand;
import chess.core.commands.MoveHistory;
import chess.core.model.Board;
import chess.core.model.Color;
import chess.core.model.Piece;
import chess.core.model.Position;
import chess.core.ports.IGameService;
import chess.core.ports.MoveResult;
import chess.core.rules.MoveValidator;
import chess.core.state.IGameState;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Implementa el puerto de entrada. Une las tres piezas del núcleo:
 * el estado dice de quién es el turno, el validador dice si el movimiento es legal,
 * y el historial lo ejecuta y lo puede deshacer.
 *
 * Todas las dependencias entran por constructor, incluido el estado inicial:
 * así esta clase no conoce WhiteTurn y se puede testear con un estado falso.
 */
public class ChessGame implements IGameService {

    private final Board board;
    private final MoveValidator validator;
    private final MoveHistory history;

    private IGameState state;
    private final Deque<IGameState> previousStates = new ArrayDeque<>();

    public ChessGame(Board board,
                     MoveValidator validator,
                     MoveHistory history,
                     IGameState initialState) {
        this.board = board;
        this.validator = validator;
        this.history = history;
        this.state = initialState;
    }

    @Override
    public MoveResult move(Position from, Position to) {
        if (state.isOver()) {
            return MoveResult.GAME_OVER;
        }

        Piece piece = board.getPiece(from);
        if (piece != null && !state.canMove(piece.color())) {
            return MoveResult.NOT_YOUR_TURN;
        }

        if (!validator.isValidMove(board, from, to)) {
            return MoveResult.INVALID_MOVE;
        }

        history.execute(new MoveCommand(board, from, to));
        previousStates.push(state);                   // para que el undo devuelva el turno
        state = state.next();
        return MoveResult.OK;
    }

    @Override
    public boolean undo() {
        if (!history.undo()) {
            return false;
        }
        state = previousStates.pop();
        return true;
    }

    @Override
    public Piece pieceAt(Position position) {
        return board.getPiece(position);
    }

    @Override
    public Color currentTurn() {
        if (state.isOver()) {
            return null;
        }
        return state.canMove(Color.WHITE) ? Color.WHITE : Color.BLACK;
    }

    @Override
    public boolean isOver() {
        return state.isOver();
    }
}
