package chess.core.game;

import chess.core.commands.MoveCommand;
import chess.core.commands.MoveHistory;
import chess.core.model.Board;
import chess.core.model.Color;
import chess.core.model.Piece;
import chess.core.model.PieceType;
import chess.core.model.Position;
import chess.core.model.ScoreBoard;
import chess.core.ports.IGameService;
import chess.core.ports.MoveResult;
import chess.core.rules.MoveValidator;
import chess.core.state.IGameState;

import java.util.ArrayDeque;
import java.util.Deque;

// Rey capturado gana; al agotar turnos, gana más puntos
public class ChessGame implements IGameService {

    private final Board board;
    private final MoveValidator validator;
    private final MoveHistory history;
    private final ScoreBoard scoreBoard;

    private IGameState state;
    private final Deque<IGameState> previousStates = new ArrayDeque<>();
    private Color kingCapturedBy;                     // null mientras ningún rey fue capturado

    public ChessGame(Board board,
                     MoveValidator validator,
                     MoveHistory history,
                     ScoreBoard scoreBoard,
                     IGameState initialState) {
        this.board = board;
        this.validator = validator;
        this.history = history;
        this.scoreBoard = scoreBoard;
        this.state = initialState;
    }

    @Override
    public MoveResult move(Position from, Position to) {
        if (isOver()) {
            return MoveResult.GAME_OVER;
        }

        Piece piece = board.getPiece(from);
        if (piece != null && !state.canMove(piece.color())) {
            return MoveResult.NOT_YOUR_TURN;
        }

        if (!validator.isValidMove(board, from, to)) {
            return MoveResult.INVALID_MOVE;
        }

        MoveCommand command = new MoveCommand(board, from, to, scoreBoard);
        history.execute(command);
        previousStates.push(state);                   // para que el undo devuelva el turno
        state = state.next();

        Piece captured = command.capturedPiece();
        if (captured != null && captured.type() == PieceType.KING) {
            kingCapturedBy = piece.color();
        }
        return MoveResult.OK;
    }

    @Override
    public boolean undo() {
        if (!history.undo()) {
            return false;
        }
        state = previousStates.pop();
        // el rey solo pudo caer en el último movimiento
        kingCapturedBy = null;
        return true;
    }

    @Override
    public Piece pieceAt(Position position) {
        return board.getPiece(position);
    }

    @Override
    public Color currentTurn() {
        if (isOver()) {
            return null;
        }
        return state.canMove(Color.WHITE) ? Color.WHITE : Color.BLACK;
    }

    @Override
    public boolean isOver() {
        return kingCapturedBy != null || state.isOver();
    }

    @Override
    public int score(Color color) {
        return scoreBoard.score(color);
    }

    @Override
    public Color winner() {
        if (!isOver()) {
            return null;
        }
        if (kingCapturedBy != null) {
            return kingCapturedBy;
        }
        int white = scoreBoard.score(Color.WHITE);
        int black = scoreBoard.score(Color.BLACK);
        if (white == black) {
            return null;                              // empate
        }
        return white > black ? Color.WHITE : Color.BLACK;
    }
}
