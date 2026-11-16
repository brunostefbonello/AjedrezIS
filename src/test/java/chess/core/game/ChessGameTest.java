package chess.core.game;

import chess.core.commands.MoveHistory;
import chess.core.model.Board;
import chess.core.model.Color;
import chess.core.model.Piece;
import chess.core.model.PieceType;
import chess.core.model.Position;
import chess.core.model.ScoreBoard;
import chess.core.ports.MoveResult;
import chess.core.rules.MoveValidator;
import chess.core.state.IGameState;
import chess.doubles.FakeTurnState;
import chess.doubles.StubMovement;
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
        return newGame(board, new ScoreBoard(), initialState);
    }

    private ChessGame newGame(Board board, ScoreBoard score, IGameState initialState) {
        return new ChessGame(board, new MoveValidator(), new MoveHistory(), score, initialState);
    }

    @Test
    void validMoveReturnsOkAndMovesThePiece() {
        // Arrange
        Board board = new Board();
        Piece piece = StubMovement.piece(Color.WHITE, true);
        board.setPiece(E2, piece);
        ChessGame game = newGame(board, new FakeTurnState(Color.WHITE));

        // Act
        MoveResult result = game.move(E2, E4);

        // Assert: OK y pieza en e4
        assertEquals(MoveResult.OK, result);
        assertSame(piece, game.pieceAt(E4));
        assertNull(game.pieceAt(E2));
    }

    @Test
    void validMovePassesTheTurn() {
        // Arrange
        Board board = new Board();
        board.setPiece(E2, StubMovement.piece(Color.WHITE, true));
        ChessGame game = newGame(board, new FakeTurnState(Color.WHITE));

        // Act
        game.move(E2, E4);

        // Assert: turno de las negras
        assertEquals(Color.BLACK, game.currentTurn());
    }

    @Test
    void movingOpponentPieceReturnsNotYourTurn() {
        // Arrange
        Board board = new Board();
        board.setPiece(E7, StubMovement.piece(Color.BLACK, true));
        ChessGame game = newGame(board, new FakeTurnState(Color.WHITE));

        // Act
        MoveResult result = game.move(E7, E5);

        // Assert: NOT_YOUR_TURN, tablero sin cambios
        assertEquals(MoveResult.NOT_YOUR_TURN, result);
        assertNull(game.pieceAt(E5));
    }

    @Test
    void illegalMoveReturnsInvalidMoveAndKeepsTurn() {
        // Arrange
        Board board = new Board();
        board.setPiece(E2, StubMovement.piece(Color.WHITE, false));   // la pieza dice que no
        ChessGame game = newGame(board, new FakeTurnState(Color.WHITE));

        // Act
        MoveResult result = game.move(E2, E4);

        // Assert: INVALID_MOVE, sigue turno de blancas
        assertEquals(MoveResult.INVALID_MOVE, result);
        assertEquals(Color.WHITE, game.currentTurn());
    }

    @Test
    void undoRestoresBoardAndTurn() {
        // Arrange
        Board board = new Board();
        Piece piece = StubMovement.piece(Color.WHITE, true);
        board.setPiece(E2, piece);
        ChessGame game = newGame(board, new FakeTurnState(Color.WHITE));
        game.move(E2, E4);

        // Act
        boolean result = game.undo();

        // Assert: pieza en e2, turno de blancas
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

        // Assert: false, turno intacto
        assertFalse(result);
        assertEquals(Color.WHITE, game.currentTurn());
    }

    @Test
    void noMovesAllowedWhenGameIsOver() {
        // Arrange
        Board board = new Board();
        board.setPiece(E2, StubMovement.piece(Color.WHITE, true));
        ChessGame game = newGame(board, new FakeTurnState(Color.WHITE, true));

        // Act
        MoveResult result = game.move(E2, E4);

        // Assert: GAME_OVER, sin turno ni movimiento
        assertEquals(MoveResult.GAME_OVER, result);
        assertTrue(game.isOver());
        assertNull(game.currentTurn());
        assertNull(game.pieceAt(E4));
    }

    @Test
    void captureIsReflectedInScore() {
        // Arrange
        Board board = new Board();
        board.setPiece(E4, StubMovement.piece(Color.WHITE, true));
        board.setPiece(E5, StubMovement.piece(Color.BLACK, PieceType.KNIGHT, true));
        ChessGame game = newGame(board, new FakeTurnState(Color.WHITE));

        // Act
        game.move(E4, E5);

        // Assert: 3 para blancas (caballo)
        assertEquals(3, game.score(Color.WHITE));
        assertEquals(0, game.score(Color.BLACK));
    }

    @Test
    void capturingTheKingEndsTheGameAndCapturerWins() {
        // Arrange: negras van ganando en puntos
        Board board = new Board();
        ScoreBoard score = new ScoreBoard();
        score.add(Color.BLACK, 20);
        board.setPiece(E4, StubMovement.piece(Color.WHITE, true));
        board.setPiece(E5, StubMovement.piece(Color.BLACK, PieceType.KING, true));
        ChessGame game = newGame(board, score, new FakeTurnState(Color.WHITE));

        // Act
        game.move(E4, E5);

        // Assert: terminada, ganan blancas, sin turno
        assertTrue(game.isOver());
        assertEquals(Color.WHITE, game.winner());
        assertNull(game.currentTurn());
    }

    @Test
    void undoOfKingCaptureReopensTheGame() {
        // Arrange
        Board board = new Board();
        board.setPiece(E4, StubMovement.piece(Color.WHITE, true));
        board.setPiece(E5, StubMovement.piece(Color.BLACK, PieceType.KING, true));
        ChessGame game = newGame(board, new FakeTurnState(Color.WHITE));
        game.move(E4, E5);

        // Act
        game.undo();

        // Assert: en curso, sin ganador, turno de blancas
        assertFalse(game.isOver());
        assertNull(game.winner());
        assertEquals(Color.WHITE, game.currentTurn());
    }

    @Test
    void whenTurnsRunOutHigherScoreWins() {
        // Arrange: turnos ya agotados
        ScoreBoard score = new ScoreBoard();
        score.add(Color.WHITE, 4);
        score.add(Color.BLACK, 9);
        ChessGame game = newGame(new Board(), score, new FakeTurnState(Color.WHITE, true));

        // Act
        Color winner = game.winner();

        // Assert: ganan negras, más puntos
        assertEquals(Color.BLACK, winner);
    }

    @Test
    void whenTurnsRunOutWithEqualScoreItIsADraw() {
        // Arrange
        ScoreBoard score = new ScoreBoard();
        score.add(Color.WHITE, 6);
        score.add(Color.BLACK, 6);
        ChessGame game = newGame(new Board(), score, new FakeTurnState(Color.WHITE, true));

        // Act
        Color winner = game.winner();

        // Assert: null con partida terminada, empate
        assertTrue(game.isOver());
        assertNull(winner);
    }

    @Test
    void noWinnerWhileGameIsInProgress() {
        // Arrange
        ScoreBoard score = new ScoreBoard();
        score.add(Color.WHITE, 10);
        ChessGame game = newGame(new Board(), score, new FakeTurnState(Color.WHITE));

        // Act
        Color winner = game.winner();

        // Assert: null, la partida sigue
        assertNull(winner);
    }
}
