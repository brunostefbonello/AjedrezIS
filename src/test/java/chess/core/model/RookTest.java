package chess.core.model;

import chess.doubles.StubPiece;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RookTest {

    @Test
    void rookCanMoveAlongColumn() {
        // Arrange
        Board board = new Board();
        Rook rook = new Rook(Color.WHITE);
        board.setPiece(new Position(0, 0), rook);

        // Act
        boolean result = rook.canMoveTo(new Position(0, 0), new Position(5, 0), board);

        // Assert — espero true: columna libre
        assertTrue(result);
    }

    @Test
    void rookCanMoveAlongRow() {
        // Arrange
        Board board = new Board();
        Rook rook = new Rook(Color.BLACK);
        board.setPiece(new Position(7, 7), rook);

        // Act
        boolean result = rook.canMoveTo(new Position(7, 7), new Position(7, 2), board);

        // Assert — espero true: fila libre
        assertTrue(result);
    }

    @Test
    void rookCannotMoveDiagonally() {
        // Arrange
        Board board = new Board();
        Rook rook = new Rook(Color.WHITE);
        board.setPiece(new Position(0, 0), rook);

        // Act
        boolean result = rook.canMoveTo(new Position(0, 0), new Position(3, 3), board);

        // Assert — espero false: la torre no se mueve en diagonal
        assertFalse(result);
    }

    @Test
    void rookCannotJumpOverPieces() {
        // Arrange
        Board board = new Board();
        Rook rook = new Rook(Color.WHITE);
        board.setPiece(new Position(0, 0), rook);
        board.setPiece(new Position(2, 0), new StubPiece(Color.WHITE, false));

        // Act
        boolean result = rook.canMoveTo(new Position(0, 0), new Position(5, 0), board);

        // Assert — espero false: hay una pieza en el camino
        assertFalse(result);
    }

    @Test
    void rookCanCaptureEnemy() {
        // Arrange
        Board board = new Board();
        Rook rook = new Rook(Color.WHITE);
        board.setPiece(new Position(0, 0), rook);
        board.setPiece(new Position(0, 6), new StubPiece(Color.BLACK, false));

        // Act
        boolean result = rook.canMoveTo(new Position(0, 0), new Position(0, 6), board);

        // Assert — espero true: el destino tiene un enemigo y el camino está libre
        assertTrue(result);
    }
}
