package chess.core.rules;

import chess.core.model.Board;
import chess.core.model.Color;
import chess.core.model.Position;
import chess.doubles.StubPiece;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MoveValidatorTest {

    private final MoveValidator validator = new MoveValidator();

    @Test
    void rejectsMoveFromEmptySquare() {
        // Arrange
        Board board = new Board();

        // Act
        boolean result = validator.isValidMove(board, new Position(1, 4), new Position(3, 4));

        // Assert — espero false: no hay pieza en el origen
        assertFalse(result);
    }

    @Test
    void rejectsMoveToSameSquare() {
        // Arrange
        Board board = new Board();
        board.setPiece(new Position(1, 4), new StubPiece(Color.WHITE, true));

        // Act
        boolean result = validator.isValidMove(board, new Position(1, 4), new Position(1, 4));

        // Assert — espero false aunque la pieza diga que sí: origen y destino son iguales
        assertFalse(result);
    }

    @Test
    void rejectsMoveOntoAlly() {
        // Arrange
        Board board = new Board();
        board.setPiece(new Position(0, 0), new StubPiece(Color.WHITE, true));
        board.setPiece(new Position(0, 5), new StubPiece(Color.WHITE, true));

        // Act
        boolean result = validator.isValidMove(board, new Position(0, 0), new Position(0, 5));

        // Assert — espero false: en el destino hay una pieza del mismo color
        assertFalse(result);
    }

    @Test
    void acceptsCaptureOfEnemyWhenPieceAllowsIt() {
        // Arrange
        Board board = new Board();
        board.setPiece(new Position(0, 0), new StubPiece(Color.WHITE, true));
        board.setPiece(new Position(0, 5), new StubPiece(Color.BLACK, true));

        // Act
        boolean result = validator.isValidMove(board, new Position(0, 0), new Position(0, 5));

        // Assert — espero true: hay un enemigo en el destino y la pieza puede llegar
        assertTrue(result);
    }

    @Test
    void delegatesToPieceRules() {
        // Arrange
        Board board = new Board();
        board.setPiece(new Position(0, 0), new StubPiece(Color.WHITE, false));

        // Act
        boolean result = validator.isValidMove(board, new Position(0, 0), new Position(4, 4));

        // Assert — espero false: los chequeos generales pasan, pero la pieza dice que no
        assertFalse(result);
    }

    @Test
    void validatingDoesNotChangeTheBoard() {
        // Arrange
        Board board = new Board();
        StubPiece piece = new StubPiece(Color.WHITE, true);
        board.setPiece(new Position(0, 0), piece);

        // Act
        validator.isValidMove(board, new Position(0, 0), new Position(4, 4));

        // Assert — espero que la pieza siga en el origen y sin marcar: validar solo pregunta
        assertSame(piece, board.getPiece(new Position(0, 0)));
        assertTrue(board.isEmpty(new Position(4, 4)));
        assertFalse(piece.hasMoved());
    }
}
