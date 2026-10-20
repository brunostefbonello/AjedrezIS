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

        // Assert — espero true: no hay nada entre a1 y a8
        assertTrue(result);
    }

    @Test
    void pathIsBlockedOnStraightLine() {
        // Arrange
        Board board = new Board();
        board.setPiece(new Position(0, 3), StubMovement.piece(Color.BLACK, false));

        // Act
        boolean result = board.isPathClear(new Position(0, 0), new Position(0, 7));

        // Assert — espero false: hay una pieza en d1, entre a1 y h1
        assertFalse(result);
    }

    @Test
    void pathIsClearOnEmptyDiagonal() {
        // Arrange
        Board board = new Board();

        // Act
        boolean result = board.isPathClear(new Position(7, 7), new Position(0, 0));

        // Assert — espero true: la diagonal h8-a1 está vacía
        assertTrue(result);
    }

    @Test
    void pathIsBlockedOnDiagonal() {
        // Arrange
        Board board = new Board();
        board.setPiece(new Position(5, 2), StubMovement.piece(Color.WHITE, false));

        // Act
        boolean result = board.isPathClear(new Position(7, 0), new Position(3, 4));

        // Assert — espero false: hay una pieza en c6, sobre la diagonal a8-e4
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

        // Assert — espero true: solo se miran las casillas estrictamente entre from y to
        assertTrue(result);
    }

    @Test
    void pathBetweenAdjacentSquaresIsClear() {
        // Arrange
        Board board = new Board();

        // Act
        boolean result = board.isPathClear(new Position(3, 3), new Position(4, 4));

        // Assert — espero true: no hay casillas intermedias
        assertTrue(result);
    }

    @Test
    void pathIsNotClearForNonLinearMove() {
        // Arrange
        Board board = new Board();

        // Act
        boolean result = board.isPathClear(new Position(0, 1), new Position(2, 2));

        // Assert — espero false: un salto de caballo no es recta ni diagonal
        assertFalse(result);
    }
}
