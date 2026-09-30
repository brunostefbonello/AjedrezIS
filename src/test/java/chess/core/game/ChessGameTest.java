package chess.core.game;

import chess.core.commands.MoveHistory;
import chess.core.model.Board;
import chess.core.model.Color;
import chess.core.model.Piece;
import chess.core.model.Position;
import chess.core.ports.MoveResult;
import chess.core.rules.MoveValidator;
import chess.core.state.IGameState;
import chess.doubles.FakeTurnState;
import chess.doubles.StubPiece;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ChessGameTest {

    private static final Position E2 = new Position(1, 4);
    private static final Position E4 = new Position(3, 4);
    private static final Position E7 = new Position(6, 4);
    private static final Position E5 = new Position(4, 4);

    private ChessGame newGame(Board board, IGameState initialState) {
        return new ChessGame(board, new MoveValidator(), new MoveHistory(), initialState);
    }

    @Test
    void validMoveReturnsOkAndMovesThePiece() {
        // Arrange
        Board board = new Board();
        Piece piece = new StubPiece(Color.WHITE, true);
        board.setPiece(E2, piece);
        ChessGame game = newGame(board, new FakeTurnState(Color.WHITE));

        // Act
        MoveResult result = game.move(E2, E4);

        // Assert — espero OK y la pieza en e4
        assertEquals(MoveResult.OK, result);
        assertSame(piece, game.pieceAt(E4));
        assertNull(game.pieceAt(E2));
    }

    @Test
    void validMovePassesTheTurn() {
        // Arrange
        Board board = new Board();
        board.setPiece(E2, new StubPiece(Color.WHITE, true));
        ChessGame game = newGame(board, new FakeTurnState(Color.WHITE));

        // Act
        game.move(E2, E4);

        // Assert — espero que ahora le toque a las negras
        assertEquals(Color.BLACK, game.currentTurn());
    }

    @Test
    void movingOpponentPieceReturnsNotYourTurn() {
        // Arrange
        Board board = new Board();
        board.setPiece(E7, new StubPiece(Color.BLACK, true));
        ChessGame game = newGame(board, new FakeTurnState(Color.WHITE));

        // Act
        MoveResult result = game.move(E7, E5);

        // Assert — espero NOT_YOUR_TURN y el tablero sin cambios
        assertEquals(MoveResult.NOT_YOUR_TURN, result);
        assertNull(game.pieceAt(E5));
    }

    @Test
    void illegalMoveReturnsInvalidMoveAndKeepsTurn() {
        // Arrange
        Board board = new Board();
        board.setPiece(E2, new StubPiece(Color.WHITE, false));   // la pieza dice que no
        ChessGame game = newGame(board, new FakeTurnState(Color.WHITE));

        // Act
        MoveResult result = game.move(E2, E4);

        // Assert — espero INVALID_MOVE y que siga siendo turno de las blancas
        assertEquals(MoveResult.INVALID_MOVE, result);
        assertEquals(Color.WHITE, game.currentTurn());
    }

    @Test
    void undoRestoresBoardAndTurn() {
        // Arrange
        Board board = new Board();
        Piece piece = new StubPiece(Color.WHITE, true);
        board.setPiece(E2, piece);
        ChessGame game = newGame(board, new FakeTurnState(Color.WHITE));
        game.move(E2, E4);

        // Act
        boolean result = game.undo();

        // Assert — espero la pieza de vuelta en e2 y el turno otra vez de las blancas
        assertTrue(result);
        assertSame(piece, game.pieceAt(E2));
        assertNull(game.pieceAt(E4));
        assertEquals(Color.WHITE, game.currentTurn());
    }

    @Test
    void undoWithNoMovesReturnsFalse() {
        // Arrange
        ChessGame game = newGame(new Board(), new FakeTurnState(Color.WHITE));

        // Act
        boolean result = game.undo();

        // Assert — espero false y el turno intacto
        assertFalse(result);
        assertEquals(Color.WHITE, game.currentTurn());
    }

    @Test
    void noMovesAllowedWhenGameIsOver() {
        // Arrange
        Board board = new Board();
        board.setPiece(E2, new StubPiece(Color.WHITE, true));
        ChessGame game = newGame(board, new FakeTurnState(Color.WHITE, true));

        // Act
        MoveResult result = game.move(E2, E4);

        // Assert — espero GAME_OVER, sin turno y sin mover nada
        assertEquals(MoveResult.GAME_OVER, result);
        assertTrue(game.isOver());
        assertNull(game.currentTurn());
        assertNull(game.pieceAt(E4));
    }
}
