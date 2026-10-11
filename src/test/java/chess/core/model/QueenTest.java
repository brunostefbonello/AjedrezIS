package chess.core.model;

import chess.doubles.StubPiece;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QueenTest {

    @Test
    void queenCanMoveInStraightLine() {
        // Arrange
        Board board = new Board();
        Queen queen = new Queen(Color.WHITE);
        board.setPiece(new Position(0, 3), queen);

        // Act
        boolean result = queen.canMoveTo(new Position(0, 3), new Position(6, 3), board);

        // Assert — espero true: columna libre
        assertTrue(result);
    }

    @Test
    void queenCanMoveDiagonally() {
        // Arrange
        Board board = new Board();
        Queen queen = new Queen(Color.WHITE);
        board.setPiece(new Position(0, 3), queen);

        // Act
        boolean result = queen.canMoveTo(new Position(0, 3), new Position(3, 0), board);

        // Assert — espero true: diagonal d1-a4 libre
        assertTrue(result);
    }

    @Test
    void queenCannotMoveLikeKnight() {
        // Arrange
        Board board = new Board();
        Queen queen = new Queen(Color.WHITE);
        board.setPiece(new Position(0, 3), queen);

        // Act
        boolean result = queen.canMoveTo(new Position(0, 3), new Position(2, 4), board);

        // Assert — espero false: ni recta ni diagonal
        assertFalse(result);
    }

    @Test
    void queenCannotJumpOverPieces() {
        // Arrange
        Board board = new Board();
        Queen queen = new Queen(Color.BLACK);
        board.setPiece(new Position(7, 3), queen);
        board.setPiece(new Position(5, 5), new StubPiece(Color.WHITE, false));

        // Act
        boolean result = queen.canMoveTo(new Position(7, 3), new Position(3, 7), board);

        // Assert — espero false: hay una pieza en f6, en la diagonal
        assertFalse(result);
    }

    @Test
    void queenCanCaptureEnemy() {
        // Arrange
        Board board = new Board();
        Queen queen = new Queen(Color.BLACK);
        board.setPiece(new Position(7, 3), queen);
        board.setPiece(new Position(1, 3), new StubPiece(Color.WHITE, false));

        // Act
        boolean result = queen.canMoveTo(new Position(7, 3), new Position(1, 3), board);

        // Assert — espero true: el destino tiene un enemigo y el camino está libre
        assertTrue(result);
    }
}
