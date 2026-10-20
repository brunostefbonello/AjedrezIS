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

// Casos migrados de QueenTest (Gonzalo): la reina = Straight + Diagonal combinados.
class CombinedMovementTest {

    @Test
    void queenCanMoveInStraightLine() {
        // Arrange
        Board board = new Board();
        Piece queen = Pieces.queen(Color.WHITE);
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
        Piece queen = Pieces.queen(Color.WHITE);
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
        Piece queen = Pieces.queen(Color.WHITE);
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
        Piece queen = Pieces.queen(Color.BLACK);
        board.setPiece(new Position(7, 3), queen);
        board.setPiece(new Position(5, 5), StubMovement.piece(Color.WHITE, false));

        // Act
        boolean result = queen.canMoveTo(new Position(7, 3), new Position(3, 7), board);

        // Assert — espero false: hay una pieza en f6, en la diagonal
        assertFalse(result);
    }

    @Test
    void queenCanCaptureEnemy() {
        // Arrange
        Board board = new Board();
        Piece queen = Pieces.queen(Color.BLACK);
        board.setPiece(new Position(7, 3), queen);
        board.setPiece(new Position(1, 3), StubMovement.piece(Color.WHITE, false));

        // Act
        boolean result = queen.canMoveTo(new Position(7, 3), new Position(1, 3), board);

        // Assert — espero true: el destino tiene un enemigo y el camino está libre
        assertTrue(result);
    }

    @Test
    void combinedMovementAllowsIfAnyOptionAllows() {
        // Arrange
        Board board = new Board();
        CombinedMovement movement = new CombinedMovement(new StubMovement(false), new StubMovement(true));

        // Act
        boolean result = movement.canMove(null, new Position(0, 0), new Position(5, 5), board);

        // Assert — espero true: alcanza con que una de las estrategias diga que sí
        assertTrue(result);
    }

    @Test
    void combinedMovementRejectsIfNoOptionAllows() {
        // Arrange
        Board board = new Board();
        CombinedMovement movement = new CombinedMovement(new StubMovement(false), new StubMovement(false));

        // Act
        boolean result = movement.canMove(null, new Position(0, 0), new Position(5, 5), board);

        // Assert — espero false: ninguna estrategia lo permite
        assertFalse(result);
    }
}
