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

class KnightMovementTest {

    private static final Position B1 = new Position(0, 1);

    @Test
    void knightCanMoveTwoForwardAndOneSideways() {
        // Arrange
        Board board = new Board();
        Piece knight = Pieces.knight(Color.WHITE);
        board.setPiece(B1, knight);

        // Act
        boolean result = knight.canMoveTo(B1, new Position(2, 2), board);

        // Assert — espero true: b1 -> c3 es una "L" (2 en fila, 1 en columna)
        assertTrue(result);
    }

    @Test
    void knightCanMoveOneForwardAndTwoSideways() {
        // Arrange
        Board board = new Board();
        Piece knight = Pieces.knight(Color.WHITE);
        board.setPiece(B1, knight);

        // Act
        boolean result = knight.canMoveTo(B1, new Position(1, 3), board);

        // Assert — espero true: b1 -> d2 es una "L" (1 en fila, 2 en columna)
        assertTrue(result);
    }

    @Test
    void knightCanMoveBackwards() {
        // Arrange
        Board board = new Board();
        Piece knight = Pieces.knight(Color.BLACK);
        Position from = new Position(4, 4);
        board.setPiece(from, knight);

        // Act
        boolean result = knight.canMoveTo(from, new Position(2, 3), board);

        // Assert — espero true: la "L" vale en cualquier dirección, también hacia atrás
        assertTrue(result);
    }

    @Test
    void knightCannotMoveInStraightLine() {
        // Arrange
        Board board = new Board();
        Piece knight = Pieces.knight(Color.WHITE);
        board.setPiece(B1, knight);

        // Act
        boolean result = knight.canMoveTo(B1, new Position(3, 1), board);

        // Assert — espero false: avanzar recto no es una "L"
        assertFalse(result);
    }

    @Test
    void knightCannotMoveDiagonally() {
        // Arrange
        Board board = new Board();
        Piece knight = Pieces.knight(Color.WHITE);
        board.setPiece(B1, knight);

        // Act
        boolean result = knight.canMoveTo(B1, new Position(2, 3), board);

        // Assert — espero false: b1 -> d3 es una diagonal (2 y 2), no una "L"
        assertFalse(result);
    }

    @Test
    void knightCannotMoveOneSquareAway() {
        // Arrange
        Board board = new Board();
        Piece knight = Pieces.knight(Color.WHITE);
        board.setPiece(B1, knight);

        // Act
        boolean result = knight.canMoveTo(B1, new Position(1, 1), board);

        // Assert — espero false: una sola casilla es muy poco para una "L"
        assertFalse(result);
    }

    @Test
    void knightJumpsOverSurroundingPieces() {
        // Arrange — el caballo está rodeado de piezas en todas las casillas contiguas
        Board board = new Board();
        Piece knight = Pieces.knight(Color.WHITE);
        Position from = new Position(3, 3);
        board.setPiece(from, knight);
        for (int row = 2; row <= 4; row++) {
            for (int col = 2; col <= 4; col++) {
                if (row != 3 || col != 3) {
                    board.setPiece(new Position(row, col), StubMovement.piece(Color.WHITE, false));
                }
            }
        }

        // Act
        boolean result = knight.canMoveTo(from, new Position(5, 4), board);

        // Assert — espero true: el caballo salta y no le importa lo que haya en el camino
        assertTrue(result);
    }

    @Test
    void knightCanCaptureEnemyOnLShapedSquare() {
        // Arrange
        Board board = new Board();
        Piece knight = Pieces.knight(Color.WHITE);
        board.setPiece(B1, knight);
        board.setPiece(new Position(2, 2), StubMovement.piece(Color.BLACK, false));   // enemigo en el destino

        // Act
        boolean result = knight.canMoveTo(B1, new Position(2, 2), board);

        // Assert — espero true: la geometría permite llegar y el enemigo se captura
        assertTrue(result);
    }
}
