package chess.core.model;

import chess.doubles.StubPiece;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PawnTest {

    private static final Position E2 = new Position(1, 4);
    private static final Position E7 = new Position(6, 4);

    // ---------- Blancas (avanzan sumando row) ----------

    @Test
    void whitePawnCanMoveOneSquareForward() {
        // Arrange
        Board board = new Board();
        Pawn pawn = new Pawn(Color.WHITE);
        board.setPiece(E2, pawn);

        // Act
        boolean result = pawn.canMoveTo(E2, new Position(2, 4), board);

        // Assert — espero true: e2 -> e3 con el destino vacío
        assertTrue(result);
    }

    @Test
    void pawnCanMoveTwoSquaresOnFirstMove() {
        // Arrange
        Board board = new Board();
        Pawn pawn = new Pawn(Color.WHITE);
        board.setPiece(E2, pawn);

        // Act
        boolean result = pawn.canMoveTo(E2, new Position(3, 4), board);

        // Assert — espero true: e2 -> e4 es válido en el primer movimiento
        assertTrue(result);
    }

    @Test
    void pawnCannotMoveTwoSquaresAfterItHasMoved() {
        // Arrange
        Board board = new Board();
        Pawn pawn = new Pawn(Color.WHITE);
        pawn.markAsMoved();
        board.setPiece(E2, pawn);

        // Act
        boolean result = pawn.canMoveTo(E2, new Position(3, 4), board);

        // Assert — espero false: el doble paso es solo para el primer movimiento
        assertFalse(result);
    }

    @Test
    void pawnCannotMoveThreeSquares() {
        // Arrange
        Board board = new Board();
        Pawn pawn = new Pawn(Color.WHITE);
        board.setPiece(E2, pawn);

        // Act
        boolean result = pawn.canMoveTo(E2, new Position(4, 4), board);

        // Assert — espero false: ni en el primer movimiento se avanzan tres casillas
        assertFalse(result);
    }

    @Test
    void whitePawnCannotMoveBackwards() {
        // Arrange
        Board board = new Board();
        Pawn pawn = new Pawn(Color.WHITE);
        Position from = new Position(3, 4);
        board.setPiece(from, pawn);

        // Act
        boolean result = pawn.canMoveTo(from, new Position(2, 4), board);

        // Assert — espero false: las blancas no retroceden (restar row)
        assertFalse(result);
    }

    @Test
    void pawnCannotMoveSideways() {
        // Arrange
        Board board = new Board();
        Pawn pawn = new Pawn(Color.WHITE);
        board.setPiece(E2, pawn);

        // Act
        boolean result = pawn.canMoveTo(E2, new Position(1, 5), board);

        // Assert — espero false: moverse en la misma fila no es válido
        assertFalse(result);
    }

    // ---------- Bloqueos ----------

    @Test
    void pawnCannotAdvanceOntoEnemyInFront() {
        // Arrange
        Board board = new Board();
        Pawn pawn = new Pawn(Color.WHITE);
        board.setPiece(E2, pawn);
        board.setPiece(new Position(2, 4), new StubPiece(Color.BLACK, false));   // enemigo justo adelante

        // Act
        boolean result = pawn.canMoveTo(E2, new Position(2, 4), board);

        // Assert — espero false: el peón no captura de frente
        assertFalse(result);
    }

    @Test
    void pawnCannotJumpOverPieceOnFirstMove() {
        // Arrange
        Board board = new Board();
        Pawn pawn = new Pawn(Color.WHITE);
        board.setPiece(E2, pawn);
        board.setPiece(new Position(2, 4), new StubPiece(Color.BLACK, false));   // algo en e3, el destino e4 está vacío

        // Act
        boolean result = pawn.canMoveTo(E2, new Position(3, 4), board);

        // Assert — espero false: hay una pieza en el camino del doble paso
        assertFalse(result);
    }

    @Test
    void pawnCannotAdvanceTwoOntoOccupiedSquare() {
        // Arrange
        Board board = new Board();
        Pawn pawn = new Pawn(Color.WHITE);
        board.setPiece(E2, pawn);
        board.setPiece(new Position(3, 4), new StubPiece(Color.BLACK, false));   // enemigo en e4, e3 vacía

        // Act
        boolean result = pawn.canMoveTo(E2, new Position(3, 4), board);

        // Assert — espero false: el destino del doble paso está ocupado
        assertFalse(result);
    }

    // ---------- Capturas ----------

    @Test
    void whitePawnCanCaptureEnemyDiagonally() {
        // Arrange
        Board board = new Board();
        Pawn pawn = new Pawn(Color.WHITE);
        board.setPiece(E2, pawn);
        board.setPiece(new Position(2, 5), new StubPiece(Color.BLACK, false));   // enemigo en f3

        // Act
        boolean result = pawn.canMoveTo(E2, new Position(2, 5), board);

        // Assert — espero true: captura en diagonal hacia adelante
        assertTrue(result);
    }

    @Test
    void pawnCanCaptureDiagonallyToTheLeft() {
        // Arrange
        Board board = new Board();
        Pawn pawn = new Pawn(Color.WHITE);
        board.setPiece(E2, pawn);
        board.setPiece(new Position(2, 3), new StubPiece(Color.BLACK, false));   // enemigo en d3

        // Act
        boolean result = pawn.canMoveTo(E2, new Position(2, 3), board);

        // Assert — espero true: la captura vale hacia los dos lados
        assertTrue(result);
    }

    @Test
    void pawnCannotMoveDiagonallyToEmptySquare() {
        // Arrange
        Board board = new Board();
        Pawn pawn = new Pawn(Color.WHITE);
        board.setPiece(E2, pawn);

        // Act
        boolean result = pawn.canMoveTo(E2, new Position(2, 5), board);

        // Assert — espero false: sin enemigo en la diagonal no hay captura
        assertFalse(result);
    }

    @Test
    void pawnCannotCaptureBackwards() {
        // Arrange
        Board board = new Board();
        Pawn pawn = new Pawn(Color.WHITE);
        Position from = new Position(3, 4);
        board.setPiece(from, pawn);
        board.setPiece(new Position(2, 5), new StubPiece(Color.BLACK, false));   // enemigo detrás en diagonal

        // Act
        boolean result = pawn.canMoveTo(from, new Position(2, 5), board);

        // Assert — espero false: la captura es solo hacia adelante
        assertFalse(result);
    }

    // ---------- Negras (avanzan restando row) ----------

    @Test
    void blackPawnCanMoveOneSquareForward() {
        // Arrange
        Board board = new Board();
        Pawn pawn = new Pawn(Color.BLACK);
        board.setPiece(E7, pawn);

        // Act
        boolean result = pawn.canMoveTo(E7, new Position(5, 4), board);

        // Assert — espero true: e7 -> e6, las negras avanzan restando row
        assertTrue(result);
    }

    @Test
    void blackPawnCanMoveTwoSquaresOnFirstMove() {
        // Arrange
        Board board = new Board();
        Pawn pawn = new Pawn(Color.BLACK);
        board.setPiece(E7, pawn);

        // Act
        boolean result = pawn.canMoveTo(E7, new Position(4, 4), board);

        // Assert — espero true: e7 -> e5 en el primer movimiento
        assertTrue(result);
    }

    @Test
    void blackPawnCannotMoveBackwards() {
        // Arrange
        Board board = new Board();
        Pawn pawn = new Pawn(Color.BLACK);
        Position from = new Position(4, 4);
        board.setPiece(from, pawn);

        // Act
        boolean result = pawn.canMoveTo(from, new Position(5, 4), board);

        // Assert — espero false: las negras no retroceden (sumar row)
        assertFalse(result);
    }

    @Test
    void blackPawnCanCaptureEnemyDiagonally() {
        // Arrange
        Board board = new Board();
        Pawn pawn = new Pawn(Color.BLACK);
        board.setPiece(E7, pawn);
        board.setPiece(new Position(5, 3), new StubPiece(Color.WHITE, false));   // enemigo en d6

        // Act
        boolean result = pawn.canMoveTo(E7, new Position(5, 3), board);

        // Assert — espero true: captura en diagonal hacia adelante (restando row)
        assertTrue(result);
    }
}


