package chess.core.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class KingTest {

    @Test
    void kingCanMoveOneSquareInAnyDirection() {
        // Arrange
        Board board = new Board();
        King king = new King(Color.WHITE);
        Position from = new Position(3, 3);
        board.setPiece(from, king);

        // Act & Assert
        assertTrue(king.canMoveTo(from, new Position(4, 4), board)); // diagonal
        assertTrue(king.canMoveTo(from, new Position(4, 3), board)); // vertical
        assertTrue(king.canMoveTo(from, new Position(3, 4), board)); // horizontal
    }

    @Test
    void kingCannotMoveMoreThanOneSquare() {
        // Arrange
        Board board = new Board();
        King king = new King(Color.WHITE);
        Position from = new Position(3, 3);
        board.setPiece(from, king);

        // Act
        boolean result = king.canMoveTo(from, new Position(5, 3), board);

        // Assert
        // espero false: se movió 2 casillas
        assertFalse(result);
    }
}
