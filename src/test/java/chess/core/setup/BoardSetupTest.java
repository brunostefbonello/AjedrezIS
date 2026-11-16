package chess.core.setup;

import chess.core.model.Board;
import chess.core.model.Color;
import chess.core.model.Piece;
import chess.core.model.PieceType;
import chess.core.model.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;

class BoardSetupTest {

    private static final PieceType[] BACK_ROW = {
            PieceType.ROOK, PieceType.KNIGHT, PieceType.BISHOP, PieceType.QUEEN,
            PieceType.KING, PieceType.BISHOP, PieceType.KNIGHT, PieceType.ROOK
    };

    @Test
    void whiteBackRowIsInStandardOrder() {
        // Arrange
        Board board = BoardSetup.standard();

        // Act
        Piece[] row = rowOf(board, 0);

        // Assert: orden estándar, blancas en fila 1
        for (int col = 0; col < 8; col++) {
            assertEquals(BACK_ROW[col], row[col].type());
            assertEquals(Color.WHITE, row[col].color());
        }
    }

    @Test
    void blackBackRowIsInStandardOrder() {
        // Arrange
        Board board = BoardSetup.standard();

        // Act
        Piece[] row = rowOf(board, 7);

        // Assert: mismo orden, negras en fila 8
        for (int col = 0; col < 8; col++) {
            assertEquals(BACK_ROW[col], row[col].type());
            assertEquals(Color.BLACK, row[col].color());
        }
    }

    @Test
    void pawnsFillSecondAndSeventhRows() {
        // Arrange
        Board board = BoardSetup.standard();

        // Act
        Piece[] whitePawns = rowOf(board, 1);
        Piece[] blackPawns = rowOf(board, 6);

        // Assert: peones en filas 2 y 7
        for (int col = 0; col < 8; col++) {
            assertEquals(PieceType.PAWN, whitePawns[col].type());
            assertEquals(Color.WHITE, whitePawns[col].color());
            assertEquals(PieceType.PAWN, blackPawns[col].type());
            assertEquals(Color.BLACK, blackPawns[col].color());
        }
    }

    @Test
    void middleRowsAreEmpty() {
        // Arrange
        Board board = BoardSetup.standard();

        // Act & Assert: filas 3 a 6 vacías
        for (int row = 2; row <= 5; row++) {
            for (int col = 0; col < 8; col++) {
                assertNull(board.getPiece(new Position(row, col)));
            }
        }
    }

    @Test
    void piecesStartWithoutHavingMoved() {
        // Arrange
        Board board = BoardSetup.standard();

        // Act
        Piece whitePawn = board.getPiece(new Position(1, 4));   // e2

        // Assert: false, si no pierde el doble paso
        assertFalse(whitePawn.hasMoved());
    }

    @Test
    void eachCallCreatesNewPieces() {
        // Arrange
        Board first = BoardSetup.standard();
        Board second = BoardSetup.standard();

        // Act
        Piece firstPawn = first.getPiece(new Position(1, 0));
        Piece secondPawn = second.getPiece(new Position(1, 0));

        // Assert: piezas distintas entre partidas
        assertNotSame(firstPawn, secondPawn);
    }

    private static Piece[] rowOf(Board board, int row) {
        Piece[] pieces = new Piece[8];
        for (int col = 0; col < 8; col++) {
            pieces[col] = board.getPiece(new Position(row, col));
        }
        return pieces;
    }
}
