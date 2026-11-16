package chess.core.movement;

import chess.core.model.Board;
import chess.core.model.Color;
import chess.core.model.Piece;
import chess.core.model.Position;
import chess.core.setup.Pieces;
import chess.doubles.StubMovement;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KingMovementTest {

    @Test
    void kingCanMoveOneSquareInAnyDirection() {
        // Arrange
        Board board = new Board();
        Piece king = Pieces.king(Color.WHITE);
        Position from = new Position(3, 3);
        Position to = new Position(4, 4);
        board.setPiece(from, king);

        // Act
        boolean result = king.canMoveTo(from, to, board);

        // Assert: true, 1 casilla en diagonal
        assertTrue(result);
    }

    @Test
    void kingCannotMoveMoreThanOneSquare() {
        // Arrange
        Board board = new Board();
        Piece king = Pieces.king(Color.WHITE);
        Position from = new Position(0, 4); // e1
        Position to = new Position(2, 4);   // e3
        board.setPiece(from, king);

        // Act
        boolean result = king.canMoveTo(from, to, board);

        // Assert: false, 2 casillas
        assertFalse(result);
    }

    @Test
    void kingCanCaptureEnemyPieceWithinOneSquare() {
        // Arrange
        Board board = new Board();
        Piece king = Pieces.king(Color.WHITE);
        Position from = new Position(3, 3);
        Position to = new Position(4, 3);

        board.setPiece(from, king);
        board.setPiece(to, StubMovement.piece(Color.BLACK, false));

        // Act
        boolean result = king.canMoveTo(from, to, board);

        // Assert: true, captura a 1 casilla
        assertTrue(result);
    }
}
