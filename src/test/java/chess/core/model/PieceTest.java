package chess.core.model;

import chess.doubles.StubMovement;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PieceTest {

    @Test
    void canMoveToDelegatesToItsStrategy() {
        // Arrange
        Board board = new Board();
        Piece yes = new Piece(Color.WHITE, PieceType.ROOK, new StubMovement(true));
        Piece no = new Piece(Color.WHITE, PieceType.ROOK, new StubMovement(false));

        // Act
        boolean yesResult = yes.canMoveTo(new Position(0, 0), new Position(5, 5), board);
        boolean noResult = no.canMoveTo(new Position(0, 0), new Position(5, 5), board);

        // Assert — espero que cada pieza responda lo que dice su estrategia, mismo tipo y todo
        assertTrue(yesResult);
        assertFalse(noResult);
    }

    @Test
    void valueComesFromItsType() {
        // Arrange
        Piece queen = new Piece(Color.BLACK, PieceType.QUEEN, new StubMovement(true));

        // Act
        int value = queen.value();

        // Assert — espero 9: lo que vale una reina
        assertEquals(9, value);
    }
}
