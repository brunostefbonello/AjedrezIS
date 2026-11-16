package chess.core.model;

import chess.doubles.StubMovement;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BoardTest {

    @Test
    void pathIsClearOnEmptyStraightLine() {
        // Arrange
        Board board = new Board();

        // Act
        boolean result = board.isPathClear(new Position(0, 0), new Position(7, 0));

        // Assert: true, nada entre a1 y a8
        assertTrue(result);
    }

    @Test
    void pathIsBlockedOnStraightLine() {
        // Arrange
        Board board = new Board();
        board.setPiece(new Position(0, 3), StubMovement.piece(Color.BLACK, false));

        // Act
        boolean result = board.isPathClear(new Position(0, 0), new Position(0, 7));

        // Assert: false, pieza en d1
        assertFalse(result);
    }

    @Test
    void pathIsClearOnEmptyDiagonal() {
        // Arrange
        Board board = new Board();

        // Act
        boolean result = board.isPathClear(new Position(7, 7), new Position(0, 0));

        // Assert: true, diagonal h8-a1 vacía
        assertTrue(result);
    }

    @Test
    void pathIsBlockedOnDiagonal() {
        // Arrange
        Board board = new Board();
        board.setPiece(new Position(5, 2), StubMovement.piece(Color.WHITE, false));

        // Act
        boolean result = board.isPathClear(new Position(7, 0), new Position(3, 4));

        // Assert: false, pieza en c6
        assertFalse(result);
    }

    @Test
    void pathIgnoresPiecesOnOriginAndDestination() {
        // Arrange
        Board board = new Board();
        board.setPiece(new Position(0, 0), StubMovement.piece(Color.WHITE, false));
        board.setPiece(new Position(0, 4), StubMovement.piece(Color.BLACK, false));

        // Act
        boolean result = board.isPathClear(new Position(0, 0), new Position(0, 4));

        // Assert: true, solo mira casillas intermedias
        assertTrue(result);
    }

    @Test
    void pathBetweenAdjacentSquaresIsClear() {
        // Arrange
        Board board = new Board();

        // Act
        boolean result = board.isPathClear(new Position(3, 3), new Position(4, 4));

        // Assert: true, sin casillas intermedias
        assertTrue(result);
    }

    @Test
    void pathIsNotClearForNonLinearMove() {
        // Arrange
        Board board = new Board();

        // Act
        boolean result = board.isPathClear(new Position(0, 1), new Position(2, 2));

        // Assert: false, ni recta ni diagonal
        assertFalse(result);
    }
}
