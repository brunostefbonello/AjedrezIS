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

// Casos migrados de BishopTest (Gonzalo): misma lógica, ahora como estrategia.
class DiagonalMovementTest {

    @Test
    void bishopCanMoveDiagonally() {
        // Arrange
        Board board = new Board();
        Piece bishop = Pieces.bishop(Color.WHITE);
        board.setPiece(new Position(0, 2), bishop);

        // Act
        boolean result = bishop.canMoveTo(new Position(0, 2), new Position(5, 7), board);

        // Assert — espero true: diagonal c1-h6 libre
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

        // Assert — espero false: el alfil no se mueve en línea recta
        assertFalse(result);
    }

    @Test
    void bishopCannotJumpOverPieces() {
        // Arrange
        Board board = new Board();
        Piece bishop = Pieces.bishop(Color.BLACK);
        board.setPiece(new Position(7, 5), bishop);
        board.setPiece(new Position(6, 4), StubMovement.piece(Color.BLACK, false));

        // Act
        boolean result = bishop.canMoveTo(new Position(7, 5), new Position(4, 2), board);

        // Assert — espero false: hay una pieza en e7, en el camino
        assertFalse(result);
    }

    @Test
    void bishopCanCaptureEnemy() {
        // Arrange
        Board board = new Board();
        Piece bishop = Pieces.bishop(Color.BLACK);
        board.setPiece(new Position(7, 5), bishop);
        board.setPiece(new Position(3, 1), StubMovement.piece(Color.WHITE, false));

        // Act
        boolean result = bishop.canMoveTo(new Position(7, 5), new Position(3, 1), board);

        // Assert — espero true: el destino tiene un enemigo y el camino está libre
        assertTrue(result);
    }
}
