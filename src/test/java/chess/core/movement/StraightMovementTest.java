package chess.core.movement;

import chess.core.model.Board;
import chess.core.model.Color;
import chess.core.model.Piece;
import chess.core.model.Position;
import chess.core.setup.Pieces;
import chess.doubles.StubMovement;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Casos migrados de RookTest: la torre usa StraightMovement.
class StraightMovementTest {

    @Test
    void rookCanMoveAlongColumn() {
        // Arrange
        Board board = new Board();
        Piece rook = Pieces.rook(Color.WHITE);
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
        Piece rook = Pieces.rook(Color.BLACK);
        board.setPiece(new Position(7, 7), rook);

        // Act
        boolean result = rook.canMoveTo(new Position(7, 7), new Position(7, 2), board);

        // Assert — espero true: fila libre
        assertTrue(result);
    }

    @Test
    void rookCanMoveDownColumn() {
        // Arrange
        Board board = new Board();
        Piece rook = Pieces.rook(Color.WHITE);
        board.setPiece(new Position(4, 3), rook);

        // Act
        boolean result = rook.canMoveTo(new Position(4, 3), new Position(0, 3), board);

        // Assert — espero true: d5 -> d1 (restando row), columna libre
        assertTrue(result);
    }

    @Test
    void rookCanMoveRightAlongRow() {
        // Arrange
        Board board = new Board();
        Piece rook = Pieces.rook(Color.WHITE);
        board.setPiece(new Position(4, 3), rook);

        // Act
        boolean result = rook.canMoveTo(new Position(4, 3), new Position(4, 7), board);

        // Assert — espero true: d5 -> h5 (sumando col), fila libre
        assertTrue(result);
    }

    @Test
    void rookCannotMoveDiagonally() {
        // Arrange
        Board board = new Board();
        Piece rook = Pieces.rook(Color.WHITE);
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
        Piece rook = Pieces.rook(Color.WHITE);
        board.setPiece(new Position(0, 0), rook);
        board.setPiece(new Position(2, 0), StubMovement.piece(Color.WHITE, false));   // algo en el camino

        // Act
        boolean result = rook.canMoveTo(new Position(0, 0), new Position(5, 0), board);

        // Assert — espero false: hay una pieza en el camino
        assertFalse(result);
    }

    @Test
    void rookCanCaptureEnemy() {
        // Arrange
        Board board = new Board();
        Piece rook = Pieces.rook(Color.WHITE);
        board.setPiece(new Position(0, 0), rook);
        board.setPiece(new Position(0, 6), StubMovement.piece(Color.BLACK, false));   // enemigo en el destino

        // Act
        boolean result = rook.canMoveTo(new Position(0, 0), new Position(0, 6), board);

        // Assert — espero true: el destino tiene un enemigo y el camino está libre
        assertTrue(result);
    }
}
