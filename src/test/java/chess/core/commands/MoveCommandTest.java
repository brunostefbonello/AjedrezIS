package chess.core.commands;

import chess.core.model.Board;
import chess.core.model.Color;
import chess.core.model.Piece;
import chess.core.model.Position;
import chess.doubles.StubPiece;
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
        Piece piece = new StubPiece(Color.WHITE, true);
        board.setPiece(new Position(1, 4), piece);
        MoveCommand command = new MoveCommand(board, new Position(1, 4), new Position(3, 4));

        // Act
        command.execute();

        // Assert — espero la pieza en el destino y el origen vacío
        assertSame(piece, board.getPiece(new Position(3, 4)));
        assertNull(board.getPiece(new Position(1, 4)));
    }

    @Test
    void executeMarksPieceAsMoved() {
        // Arrange
        Board board = new Board();
        Piece piece = new StubPiece(Color.WHITE, true);
        board.setPiece(new Position(1, 4), piece);
        MoveCommand command = new MoveCommand(board, new Position(1, 4), new Position(3, 4));

        // Act
        command.execute();

        // Assert — espero que la pieza quede marcada como movida
        assertTrue(piece.hasMoved());
    }

    @Test
    void undoRestoresBoardAfterCapture() {
        // Arrange
        Board board = new Board();
        board.setPiece(new Position(3, 3), new StubPiece(Color.WHITE, true));
        board.setPiece(new Position(4, 4), new StubPiece(Color.BLACK, true));   // hay captura
        Map<Position, Piece> before = snapshot(board);
        MoveCommand command = new MoveCommand(board, new Position(3, 3), new Position(4, 4));

        // Act
        command.execute();
        Map<Position, Piece> afterExecute = snapshot(board);
        command.undo();

        // Assert — espero que el execute haya cambiado algo y que el undo lo revierta del todo
        assertNotEquals(before, afterExecute);
        assertEquals(before, snapshot(board));
    }

    @Test
    void undoOfFirstMoveResetsMovedFlag() {
        // Arrange
        Board board = new Board();
        Piece piece = new StubPiece(Color.WHITE, true);
        board.setPiece(new Position(1, 4), piece);
        MoveCommand command = new MoveCommand(board, new Position(1, 4), new Position(3, 4));

        // Act
        command.execute();
        command.undo();

        // Assert — espero que la pieza vuelva a figurar como "nunca se movió"
        assertFalse(piece.hasMoved());
    }

    @Test
    void undoOfLaterMoveKeepsMovedFlag() {
        // Arrange — la pieza ya se movió antes (a1 -> a4), ahora va de a4 a a7
        Board board = new Board();
        Piece piece = new StubPiece(Color.WHITE, true);
        board.setPiece(new Position(0, 0), piece);
        new MoveCommand(board, new Position(0, 0), new Position(3, 0)).execute();
        MoveCommand second = new MoveCommand(board, new Position(3, 0), new Position(6, 0));

        // Act
        second.execute();
        second.undo();

        // Assert — espero que vuelva a a4 y SIGA marcada: su primer movimiento no se deshizo
        assertSame(piece, board.getPiece(new Position(3, 0)));
        assertTrue(piece.hasMoved());
    }

    @Test
    void undoBeforeExecuteFails() {
        // Arrange
        Board board = new Board();
        MoveCommand command = new MoveCommand(board, new Position(1, 4), new Position(3, 4));

        // Act + Assert — espero una excepción: no hay nada que deshacer
        assertThrows(IllegalStateException.class, command::undo);
    }

    /**
     * Foto del tablero comparable por valor: casilla -> pieza.
     * Funciona porque Position es un record (equals por valor).
     */
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
