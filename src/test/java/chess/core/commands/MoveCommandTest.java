package chess.core.commands;

import chess.core.model.Board;
import chess.core.model.Color;
import chess.core.model.Piece;
import chess.core.model.Position;
import chess.core.model.PieceType;
import chess.core.model.ScoreBoard;
import chess.doubles.StubMovement;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MoveCommandTest {

    @Test
    void executeMovesPieceAndEmptiesOrigin() {
        // Arrange
        Board board = new Board();
        Piece piece = StubMovement.piece(Color.WHITE, true);
        board.setPiece(new Position(1, 4), piece);
        MoveCommand command = new MoveCommand(board, new Position(1, 4), new Position(3, 4), new ScoreBoard());

        // Act
        command.execute();

        // Assert: pieza en destino, origen vacío
        assertSame(piece, board.getPiece(new Position(3, 4)));
        assertNull(board.getPiece(new Position(1, 4)));
    }

    @Test
    void executeMarksPieceAsMoved() {
        // Arrange
        Board board = new Board();
        Piece piece = StubMovement.piece(Color.WHITE, true);
        board.setPiece(new Position(1, 4), piece);
        MoveCommand command = new MoveCommand(board, new Position(1, 4), new Position(3, 4), new ScoreBoard());

        // Act
        command.execute();

        // Assert: marcada como movida
        assertTrue(piece.hasMoved());
    }

    @Test
    void undoRestoresBoardAfterCapture() {
        // Arrange
        Board board = new Board();
        board.setPiece(new Position(3, 3), StubMovement.piece(Color.WHITE, true));
        board.setPiece(new Position(4, 4), StubMovement.piece(Color.BLACK, true));   // hay captura
        Map<Position, Piece> before = snapshot(board);
        MoveCommand command = new MoveCommand(board, new Position(3, 3), new Position(4, 4), new ScoreBoard());

        // Act
        command.execute();
        Map<Position, Piece> afterExecute = snapshot(board);
        command.undo();

        // Assert: execute cambia, undo revierte todo
        assertNotEquals(before, afterExecute);
        assertEquals(before, snapshot(board));
    }

    @Test
    void undoOfFirstMoveResetsMovedFlag() {
        // Arrange
        Board board = new Board();
        Piece piece = StubMovement.piece(Color.WHITE, true);
        board.setPiece(new Position(1, 4), piece);
        MoveCommand command = new MoveCommand(board, new Position(1, 4), new Position(3, 4), new ScoreBoard());

        // Act
        command.execute();
        command.undo();

        // Assert: vuelve a "nunca se movió"
        assertFalse(piece.hasMoved());
    }

    @Test
    void undoOfLaterMoveKeepsMovedFlag() {
        // Arrange: ya se movió a1 -> a4; ahora a4 -> a7
        Board board = new Board();
        Piece piece = StubMovement.piece(Color.WHITE, true);
        board.setPiece(new Position(0, 0), piece);
        new MoveCommand(board, new Position(0, 0), new Position(3, 0), new ScoreBoard()).execute();
        MoveCommand second = new MoveCommand(board, new Position(3, 0), new Position(6, 0), new ScoreBoard());

        // Act
        second.execute();
        second.undo();

        // Assert: vuelve a a4 y sigue marcada
        assertSame(piece, board.getPiece(new Position(3, 0)));
        assertTrue(piece.hasMoved());
    }

    @Test
    void undoBeforeExecuteFails() {
        // Arrange
        Board board = new Board();
        MoveCommand command = new MoveCommand(board, new Position(1, 4), new Position(3, 4), new ScoreBoard());

        // Act + Assert: excepción, nada que deshacer
        assertThrows(IllegalStateException.class, command::undo);
    }

    @Test
    void captureAddsValueOfCapturedPieceToMover() {
        // Arrange
        Board board = new Board();
        ScoreBoard score = new ScoreBoard();
        board.setPiece(new Position(0, 0), StubMovement.piece(Color.WHITE, true));
        board.setPiece(new Position(0, 7), StubMovement.piece(Color.BLACK, PieceType.ROOK, true));
        MoveCommand command = new MoveCommand(board, new Position(0, 0), new Position(0, 7), score);

        // Act
        command.execute();

        // Assert: 5 para blancas (torre), 0 negras
        assertEquals(5, score.score(Color.WHITE));
        assertEquals(0, score.score(Color.BLACK));
    }

    @Test
    void undoOfCaptureGivesPointsBack() {
        // Arrange
        Board board = new Board();
        ScoreBoard score = new ScoreBoard();
        board.setPiece(new Position(0, 0), StubMovement.piece(Color.WHITE, true));
        board.setPiece(new Position(0, 7), StubMovement.piece(Color.BLACK, PieceType.QUEEN, true));
        MoveCommand command = new MoveCommand(board, new Position(0, 0), new Position(0, 7), score);

        // Act
        command.execute();
        command.undo();

        // Assert: 0, el undo devuelve los puntos
        assertEquals(0, score.score(Color.WHITE));
    }

    @Test
    void moveWithoutCaptureDoesNotScore() {
        // Arrange
        Board board = new Board();
        ScoreBoard score = new ScoreBoard();
        board.setPiece(new Position(1, 4), StubMovement.piece(Color.WHITE, true));
        MoveCommand command = new MoveCommand(board, new Position(1, 4), new Position(3, 4), score);

        // Act
        command.execute();

        // Assert: 0 puntos, sin captura
        assertEquals(0, score.score(Color.WHITE));
        assertNull(command.capturedPiece());
    }

    // Comparable por valor porque Position es record
    private static Map<Position, Piece> snapshot(Board board) {
        Map<Position, Piece> pieces = new HashMap<>();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Position position = new Position(row, col);
                Piece piece = board.getPiece(position);
                if (piece != null) {
                    pieces.put(position, piece);
                }
            }
        }
        return pieces;
    }
}
