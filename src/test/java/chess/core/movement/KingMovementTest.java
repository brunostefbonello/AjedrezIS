package chess.core.movement;

import chess.core.model.Board;
import chess.core.model.Color;
import chess.core.model.Piece;
import chess.core.model.Position;
import chess.core.setup.Pieces;
import chess.doubles.StubMovement;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KingMovementTest {

    @Test
    void kingCanMoveOneSquareInAnyDirection() {
        // Arrange
        Board board = new Board();
        Piece king = Pieces.king(Color.WHITE);
        Position from = new Position(3, 3);
        Position to = new Position(4, 4);
        board.setPiece(from, king);

        // Act
        boolean result = king.canMoveTo(from, to, board);

        // Assert — espero true: se mueve 1 casilla en diagonal
        assertTrue(result);
    }

    @Test
    void kingCannotMoveMoreThanOneSquare() {
        // Arrange
        Board board = new Board();
        Piece king = Pieces.king(Color.WHITE);
        Position from = new Position(0, 4); // e1
        Position to = new Position(2, 4);   // e3 (salto de 2 casillas)
        board.setPiece(from, king);

        // Act
        boolean result = king.canMoveTo(from, to, board);

        // Assert — espero false: intentó moverse 2 casillas
        assertFalse(result);
    }

    @Test
    void kingCanCaptureEnemyPieceWithinOneSquare() {
        // Arrange
        Board board = new Board();
        Piece king = Pieces.king(Color.WHITE);
        Position from = new Position(3, 3);
        Position to = new Position(4, 3);

        board.setPiece(from, king);
        // Ponemos un enemigo genérico usando el doble de prueba del convenio
        board.setPiece(to, StubMovement.piece(Color.BLACK, false));

        // Act
        boolean result = king.canMoveTo(from, to, board);

        // Assert — espero true: captura moviéndose 1 casilla hacia adelante
        assertTrue(result);
    }
}
