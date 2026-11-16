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

class PawnMovementTest {

    private static final Position E2 = new Position(1, 4);
    private static final Position E7 = new Position(6, 4);

    // ---------- Blancas (avanzan sumando row) ----------

    @Test
    void whitePawnCanMoveOneSquareForward() {
        // Arrange
        Board board = new Board();
        Piece pawn = Pieces.pawn(Color.WHITE);
        board.setPiece(E2, pawn);

        // Act
        boolean result = pawn.canMoveTo(E2, new Position(2, 4), board);

        // Assert: true, e2 -> e3 vacío
        assertTrue(result);
    }

    @Test
    void pawnCanMoveTwoSquaresOnFirstMove() {
        // Arrange
        Board board = new Board();
        Piece pawn = Pieces.pawn(Color.WHITE);
        board.setPiece(E2, pawn);

        // Act
        boolean result = pawn.canMoveTo(E2, new Position(3, 4), board);

        // Assert: true, e2 -> e4 en primer movimiento
        assertTrue(result);
    }

    @Test
    void pawnCannotMoveTwoSquaresAfterItHasMoved() {
        // Arrange
        Board board = new Board();
        Piece pawn = Pieces.pawn(Color.WHITE);
        pawn.markAsMoved();
        board.setPiece(E2, pawn);

        // Act
        boolean result = pawn.canMoveTo(E2, new Position(3, 4), board);

        // Assert: false, doble paso solo al inicio
        assertFalse(result);
    }

    @Test
    void pawnCannotMoveThreeSquares() {
        // Arrange
        Board board = new Board();
        Piece pawn = Pieces.pawn(Color.WHITE);
        board.setPiece(E2, pawn);

        // Act
        boolean result = pawn.canMoveTo(E2, new Position(4, 4), board);

        // Assert: false, nunca tres casillas
        assertFalse(result);
    }

    @Test
    void whitePawnCannotMoveBackwards() {
        // Arrange
        Board board = new Board();
        Piece pawn = Pieces.pawn(Color.WHITE);
        Position from = new Position(3, 4);
        board.setPiece(from, pawn);

        // Act
        boolean result = pawn.canMoveTo(from, new Position(2, 4), board);

        // Assert: false, blancas no retroceden
        assertFalse(result);
    }

    @Test
    void pawnCannotMoveSideways() {
        // Arrange
        Board board = new Board();
        Piece pawn = Pieces.pawn(Color.WHITE);
        board.setPiece(E2, pawn);

        // Act
        boolean result = pawn.canMoveTo(E2, new Position(1, 5), board);

        // Assert: false, no se mueve de costado
        assertFalse(result);
    }

    // ---------- Bloqueos ----------

    @Test
    void pawnCannotAdvanceOntoEnemyInFront() {
        // Arrange
        Board board = new Board();
        Piece pawn = Pieces.pawn(Color.WHITE);
        board.setPiece(E2, pawn);
        board.setPiece(new Position(2, 4), StubMovement.piece(Color.BLACK, false));   // enemigo justo adelante

        // Act
        boolean result = pawn.canMoveTo(E2, new Position(2, 4), board);

        // Assert: false, no captura de frente
        assertFalse(result);
    }

    @Test
    void pawnCannotJumpOverPieceOnFirstMove() {
        // Arrange
        Board board = new Board();
        Piece pawn = Pieces.pawn(Color.WHITE);
        board.setPiece(E2, pawn);
        board.setPiece(new Position(2, 4), StubMovement.piece(Color.BLACK, false));   // algo en e3; e4 vacío

        // Act
        boolean result = pawn.canMoveTo(E2, new Position(3, 4), board);

        // Assert: false, doble paso bloqueado
        assertFalse(result);
    }

    @Test
    void pawnCannotAdvanceTwoOntoOccupiedSquare() {
        // Arrange
        Board board = new Board();
        Piece pawn = Pieces.pawn(Color.WHITE);
        board.setPiece(E2, pawn);
        board.setPiece(new Position(3, 4), StubMovement.piece(Color.BLACK, false));   // enemigo en e4, e3 vacía

        // Act
        boolean result = pawn.canMoveTo(E2, new Position(3, 4), board);

        // Assert: false, destino ocupado
        assertFalse(result);
    }

    // ---------- Capturas ----------

    @Test
    void whitePawnCanCaptureEnemyDiagonally() {
        // Arrange
        Board board = new Board();
        Piece pawn = Pieces.pawn(Color.WHITE);
        board.setPiece(E2, pawn);
        board.setPiece(new Position(2, 5), StubMovement.piece(Color.BLACK, false));   // enemigo en f3

        // Act
        boolean result = pawn.canMoveTo(E2, new Position(2, 5), board);

        // Assert: true, captura en diagonal adelante
        assertTrue(result);
    }

    @Test
    void pawnCanCaptureDiagonallyToTheLeft() {
        // Arrange
        Board board = new Board();
        Piece pawn = Pieces.pawn(Color.WHITE);
        board.setPiece(E2, pawn);
        board.setPiece(new Position(2, 3), StubMovement.piece(Color.BLACK, false));   // enemigo en d3

        // Act
        boolean result = pawn.canMoveTo(E2, new Position(2, 3), board);

        // Assert: true, captura a ambos lados
        assertTrue(result);
    }

    @Test
    void pawnCannotMoveDiagonallyToEmptySquare() {
        // Arrange
        Board board = new Board();
        Piece pawn = Pieces.pawn(Color.WHITE);
        board.setPiece(E2, pawn);

        // Act
        boolean result = pawn.canMoveTo(E2, new Position(2, 5), board);

        // Assert: false, diagonal sin enemigo
        assertFalse(result);
    }

    @Test
    void pawnCannotCaptureBackwards() {
        // Arrange
        Board board = new Board();
        Piece pawn = Pieces.pawn(Color.WHITE);
        Position from = new Position(3, 4);
        board.setPiece(from, pawn);
        board.setPiece(new Position(2, 5), StubMovement.piece(Color.BLACK, false));   // enemigo detrás en diagonal

        // Act
        boolean result = pawn.canMoveTo(from, new Position(2, 5), board);

        // Assert: false, solo captura hacia adelante
        assertFalse(result);
    }

    // ---------- Negras (avanzan restando row) ----------

    @Test
    void blackPawnCanMoveOneSquareForward() {
        // Arrange
        Board board = new Board();
        Piece pawn = Pieces.pawn(Color.BLACK);
        board.setPiece(E7, pawn);

        // Act
        boolean result = pawn.canMoveTo(E7, new Position(5, 4), board);

        // Assert: true, negras restan row
        assertTrue(result);
    }

    @Test
    void blackPawnCanMoveTwoSquaresOnFirstMove() {
        // Arrange
        Board board = new Board();
        Piece pawn = Pieces.pawn(Color.BLACK);
        board.setPiece(E7, pawn);

        // Act
        boolean result = pawn.canMoveTo(E7, new Position(4, 4), board);

        // Assert: true, e7 -> e5 al inicio
        assertTrue(result);
    }

    @Test
    void blackPawnCannotMoveBackwards() {
        // Arrange
        Board board = new Board();
        Piece pawn = Pieces.pawn(Color.BLACK);
        Position from = new Position(4, 4);
        board.setPiece(from, pawn);

        // Act
        boolean result = pawn.canMoveTo(from, new Position(5, 4), board);

        // Assert: false, negras no retroceden
        assertFalse(result);
    }

    @Test
    void blackPawnCanCaptureEnemyDiagonally() {
        // Arrange
        Board board = new Board();
        Piece pawn = Pieces.pawn(Color.BLACK);
        board.setPiece(E7, pawn);
        board.setPiece(new Position(5, 3), StubMovement.piece(Color.WHITE, false));   // enemigo en d6

        // Act
        boolean result = pawn.canMoveTo(E7, new Position(5, 3), board);

        // Assert: true, captura en diagonal restando row
        assertTrue(result);
    }
}
