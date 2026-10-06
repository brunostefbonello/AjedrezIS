package chess.adapters.console;

import chess.core.model.Color;
import chess.core.model.Piece;
import chess.core.model.Position;
import chess.core.ports.IGameService;

public class ConsoleRenderer {

    private final int totalRows;
    private final int totalCols;

    // Constructor que permite cualquier dimensión (8x8, 10x10, etc.)
    public ConsoleRenderer(int totalRows, int totalCols) {
        this.totalRows = totalRows;
        this.totalCols = totalCols;
    }

    public void render(IGameService game) {
        printColumnLabels();
        printSeparator();

        for (int row = totalRows - 1; row >= 0; row--) {
            // Imprime número de fila alineado
            System.out.printf("%2d |", (row + 1));
            for (int col = 0; col < totalCols; col++) {
                Piece piece = game.pieceAt(new Position(row, col));
                System.out.print(" " + getPieceSymbol(piece) + " |");
            }
            System.out.printf(" %-2d\n", (row + 1));
            printSeparator();
        }

        printColumnLabels();
        System.out.println();
    }

    private void printColumnLabels() {
        System.out.print("     ");
        for (int col = 0; col < totalCols; col++) {
            char colLetter = (char) ('a' + col);
            System.out.print(colLetter + "   ");
        }
        System.out.println();
    }

    private void printSeparator() {
        System.out.print("   +");
        for (int col = 0; col < totalCols; col++) {
            System.out.print("---+");
        }
        System.out.println();
    }

    private String getPieceSymbol(Piece piece) {
        if (piece == null) return " ";
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