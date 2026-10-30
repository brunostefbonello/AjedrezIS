package chess.adapters.console;

import chess.core.model.Color;
import chess.core.model.Piece;
import chess.core.model.Position;
import chess.core.ports.IGameService;

public class ConsoleRenderer {

    public ConsoleRenderer() {
    }

    public void render(IGameService game) {
        System.out.println("\n    a   b   c   d   e   f   g   h");
        System.out.println("  +---+---+---+---+---+---+---+---+");

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

        // 🆕 Agregados del convenio v2: Puntaje, Turno y Ganador
        System.out.println("Puntaje -> BLANCAS: " + game.score(Color.WHITE) + " | NEGRAS: " + game.score(Color.BLACK));

        if (game.isOver()) {
            Color winner = game.winner();
            if (winner != null) {
                System.out.println("¡PARTIDA TERMINADA! Ganador: " + winner);
            } else {
                System.out.println("¡PARTIDA TERMINADA! Es un empate por límite de turnos.");
            }
        } else {
            System.out.println("Turno activo: " + game.currentTurn());
        }
    }

    private String getPieceSymbol(Piece piece) {
        if (piece == null) {
            return " ";
        }
        String name = piece.type().name(); // Ahora usa piece.type() por el nuevo convenio
        char letter = switch (name) {
            case "KING" -> 'K';
            case "QUEEN" -> 'Q';
            case "ROOK" -> 'R';
            case "BISHOP" -> 'B';
            case "KNIGHT" -> 'N';
            case "PAWN" -> 'P';
            default -> '?';
        };

        return piece.color() == Color.WHITE
                ? String.valueOf(letter).toUpperCase()
                : String.valueOf(letter).toLowerCase();
    }
}