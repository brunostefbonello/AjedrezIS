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

// Casos del alfil (ex BishopTest)
class DiagonalMovementTest {

    @Test
    void bishopCanMoveDiagonally() {
        // Arrange
        Board board = new Board();
        Piece bishop = Pieces.bishop(Color.WHITE);
        board.setPiece(new Position(0, 2), bishop);

        // Act
        boolean result = bishop.canMoveTo(new Position(0, 2), new Position(5, 7), board);

        // Assert: true, diagonal c1-h6 libre
        assertTrue(result);
    }

    @Test
    void bishopCanMoveUpAndLeft() {
        // Arrange
        Board board = new Board();
        Piece bishop = Pieces.bishop(Color.WHITE);
        board.setPiece(new Position(3, 3), bishop);

        // Act
        boolean result = bishop.canMoveTo(new Position(3, 3), new Position(6, 0), board);

        // Assert: true, d4 -> a7 libre
        assertTrue(result);
    }

    @Test
    void bishopCanMoveDownAndRight() {
        // Arrange
        Board board = new Board();
        Piece bishop = Pieces.bishop(Color.WHITE);
        board.setPiece(new Position(3, 3), bishop);

        // Act
        boolean result = bishop.canMoveTo(new Position(3, 3), new Position(0, 6), board);

        // Assert: true, d4 -> g1 libre
        assertTrue(result);
    }

    @Test
    void bishopCannotMoveInStraightLine() {
        // Arrange
        Board board = new Board();
        Piece bishop = Pieces.bishop(Color.WHITE);
        board.setPiece(new Position(0, 2), bishop);

        // Act
        boolean result = bishop.canMoveTo(new Position(0, 2), new Position(4, 2), board);

        // Assert: false, no va en recta
        assertFalse(result);
    }

    @Test
    void bishopCannotJumpOverPieces() {
        // Arrange
        Board board = new Board();
        Piece bishop = Pieces.bishop(Color.BLACK);
        board.setPiece(new Position(7, 5), bishop);
        board.setPiece(new Position(6, 4), StubMovement.piece(Color.BLACK, false));   // algo en el camino

        // Act
        boolean result = bishop.canMoveTo(new Position(7, 5), new Position(4, 2), board);

        // Assert: false, pieza en e7
        assertFalse(result);
    }

    @Test
    void bishopCanCaptureEnemy() {
        // Arrange
        Board board = new Board();
        Piece bishop = Pieces.bishop(Color.BLACK);
        board.setPiece(new Position(7, 5), bishop);
        board.setPiece(new Position(3, 1), StubMovement.piece(Color.WHITE, false));   // enemigo en el destino

        // Act
        boolean result = bishop.canMoveTo(new Position(7, 5), new Position(3, 1), board);

        // Assert: true, enemigo en destino, camino libre
        assertTrue(result);
    }
}
