package chess.adapters.console;

import chess.core.commands.MoveHistory;
import chess.core.game.ChessGame;
import chess.core.model.Position;
import chess.core.model.ScoreBoard;
import chess.core.ports.IGameService;
import chess.core.ports.MoveResult;
import chess.core.rules.MoveValidator;
import chess.core.setup.BoardSetup;
import chess.core.state.WhiteTurnState;

import java.util.Scanner;

/**
 * Segundo adaptador: la misma partida que la ventana, jugada por consola.
 * Usa el mismo núcleo sin cambiarlo (demostración de arquitectura hexagonal).
 */
public class ConsoleApp {

    private static final int MAX_TURNS = 30;

    public static void main(String[] args) {
        // Ensamble de dependencias: el mismo que SwingApp
        IGameService game = new ChessGame(
                BoardSetup.standard(),
                new MoveValidator(),
                new MoveHistory(),
                new ScoreBoard(),
                new WhiteTurnState(1, MAX_TURNS));

        ConsoleRenderer renderer = new ConsoleRenderer();
        NotationParser parser = new NotationParser();
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Ajedrez IS ===");
        System.out.println("Escribí jugadas en formato 'origen destino' (ej: 'e2 e4') o 'undo' para deshacer.\n");

        while (!game.isOver()) {
            renderer.render(game);
            System.out.print("Jugada > ");
            if (!scanner.hasNextLine()) {
                break;                                  // se cerró la entrada
            }
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("undo")) {
                if (game.undo()) {
                    System.out.println("Movimiento deshecho.");
                } else {
                    System.out.println("No hay movimientos para deshacer.");
                }
                continue;
            }

            String[] parts = input.split("\\s+");
            if (parts.length != 2) {
                System.out.println("Formato inválido. Usá: 'e2 e4'");
                continue;
            }

            Position from = parser.parse(parts[0]);
            Position to = parser.parse(parts[1]);

            if (from == null || to == null) {
                System.out.println("Coordenadas fuera de rango o inválidas.");
                continue;
            }

            MoveResult result = game.move(from, to);
            if (result != MoveResult.OK) {
                System.out.println(messageFor(result));
            }
        }

        renderer.render(game);
        System.out.println("¡Fin de la partida!");
        scanner.close();
    }

    private static String messageFor(MoveResult result) {
        return switch (result) {
            case OK -> "";
            case INVALID_MOVE -> "Movimiento inválido.";
            case NOT_YOUR_TURN -> "No es tu turno.";
            case GAME_OVER -> "La partida ya terminó.";
        };
    }
}
