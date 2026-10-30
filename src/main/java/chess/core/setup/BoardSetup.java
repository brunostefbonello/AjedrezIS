package chess.core.setup;

import chess.core.model.Board;
import chess.core.model.Color;
import chess.core.model.Position;

/**
 * Arma el tablero con la posición inicial del ajedrez.
 *
 * Las piezas salen de {@link Pieces}, así la estrategia de cada una se decide
 * en un solo lugar. Cada llamada devuelve un tablero nuevo con piezas nuevas:
 * dos partidas nunca comparten piezas (importa porque las piezas guardan si ya se movieron).
 */
public final class BoardSetup {

    private static final int WHITE_BACK_ROW = 0;   // fila 1
    private static final int WHITE_PAWN_ROW = 1;   // fila 2
    private static final int BLACK_PAWN_ROW = 6;   // fila 7
    private static final int BLACK_BACK_ROW = 7;   // fila 8

    private BoardSetup() {
    }

    public static Board standard() {
        Board board = new Board();
        placeBackRow(board, WHITE_BACK_ROW, Color.WHITE);
        placePawns(board, WHITE_PAWN_ROW, Color.WHITE);
        placePawns(board, BLACK_PAWN_ROW, Color.BLACK);
        placeBackRow(board, BLACK_BACK_ROW, Color.BLACK);
        return board;
    }

    // Columnas a..h: torre, caballo, alfil, reina, rey, alfil, caballo, torre.
    // La reina va en la columna d (en su color) y el rey en la e, para los dos colores.
    private static void placeBackRow(Board board, int row, Color color) {
        board.setPiece(new Position(row, 0), Pieces.rook(color));
        board.setPiece(new Position(row, 1), Pieces.knight(color));
        board.setPiece(new Position(row, 2), Pieces.bishop(color));
        board.setPiece(new Position(row, 3), Pieces.queen(color));
        board.setPiece(new Position(row, 4), Pieces.king(color));
        board.setPiece(new Position(row, 5), Pieces.bishop(color));
        board.setPiece(new Position(row, 6), Pieces.knight(color));
        board.setPiece(new Position(row, 7), Pieces.rook(color));
    }

    private static void placePawns(Board board, int row, Color color) {
        for (int col = 0; col < 8; col++) {
            board.setPiece(new Position(row, col), Pieces.pawn(color));
        }
    }
}
