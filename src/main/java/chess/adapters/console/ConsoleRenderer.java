package chess.adapters.console;

import chess.core.model.Color;
import chess.core.model.Piece;
import chess.core.model.Position;
import chess.core.ports.IGameService;

public class ConsoleRenderer {

    // Constructor por defecto sin argumentos
    public ConsoleRenderer() {
    }

    public void render(IGameService game) {
        System.out.println("\n    a   b   c   d   e   f   g   h");
        System.out.println("  +---+---+---+---+---+---+---+---+");

        // Fila 8 (row 7) arriba hasta fila 1 (row 0) abajo
        for (int row = 7; row >= 0; row--) {
            System.out.print((row + 1) + " |");
            for (int col = 0; col < 8; col++) {
                Piece piece = game.pieceAt(new Position(row, col));
                System.out.print(" " + getPieceSymbol(piece) + " |");
            }
            System.out.println(" " + (row + 1));
            System.out.println("  +---+---+---+---+---+---+---+---+");
        }
        System.out.println("    a   b   c   d   e   f   g   h\n");
    }

    private String getPieceSymbol(Piece piece) {
        if (piece == null) {
            return " ";
        }
        String name = piece.getClass().getSimpleName();
        char letter = switch (name) {
            case "King" -> 'K';
            case "Queen" -> 'Q';
            case "Rook" -> 'R';
            case "Bishop" -> 'B';
            case "Knight" -> 'N';
            case "Pawn" -> 'P';
            default -> '?';
        };

        return piece.color() == Color.WHITE
                ? String.valueOf(letter).toUpperCase()
                : String.valueOf(letter).toLowerCase();
    }
}