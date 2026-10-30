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

        System.out.println("Puntos: Blancas " + game.score(Color.WHITE) + " | Negras " + game.score(Color.BLACK));

        if (game.isOver()) {
            Color winner = game.winner();
            if (winner != null) {
                System.out.println("¡Partida terminada! Ganan las " + nameOf(winner));
            } else {
                System.out.println("¡Partida terminada! Empate por límite de turnos.");
            }
        } else {
            System.out.println("Turno de las " + nameOf(game.currentTurn()));
        }
    }

    // Mayúscula = blancas, minúscula = negras (notación inglesa: N = caballo)
    private String getPieceSymbol(Piece piece) {
        if (piece == null) {
            return " ";
        }
        char letter = switch (piece.type()) {
            case KING -> 'K';
            case QUEEN -> 'Q';
            case ROOK -> 'R';
            case BISHOP -> 'B';
            case KNIGHT -> 'N';
            case PAWN -> 'P';
        };
        return piece.color() == Color.WHITE
                ? String.valueOf(letter)
                : String.valueOf(Character.toLowerCase(letter));
    }

    private String nameOf(Color color) {
        return color == Color.WHITE ? "Blancas" : "Negras";
    }
}
