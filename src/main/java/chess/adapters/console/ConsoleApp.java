package chess.adapters.console;

import chess.core.game.ChessGame;
import chess.core.model.Board;
//import chess.core.model.BoardSetup;
import chess.core.model.Color;
import chess.core.model.King;
import chess.core.model.Position;
import chess.core.ports.IGameService;
import chess.core.ports.MoveResult;
import chess.core.rules.MoveValidator;
import chess.core.commands.MoveHistory;
import chess.core.state.WhiteTurnState; // o WhiteTurnState según tu repo

import java.util.Scanner;

public class ConsoleApp {

    public static void main(String[] args) {
        // Ensamble de dependencias (único lugar permitido con new)
       Board board = new Board(); //instanciacion del tablero

        /*automatizar instanciacion de piezas en el board

        board.setPiece(new Position(0, 4), new King(Color.WHITE)); // Rey blanco en e1
    board.setPiece(new Position(7, 4), new King(Color.BLACK)); // Rey negro en e8
    board.setPiece(new Position(1, 4), new King(Color.BLACK));
         */

        MoveValidator validator = new MoveValidator();
        MoveHistory history = new MoveHistory();
        IGameService game = new ChessGame(board, validator, history, new WhiteTurnState());

        ConsoleRenderer renderer = new ConsoleRenderer();
        NotationParser parser = new NotationParser();
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Ajedrez IS ===");
        System.out.println("Escribí jugadas en formato 'origen destino' (ej: 'e2 e4') o 'undo' para deshacer.\n");

        while (!game.isOver()) {
            renderer.render(game);
            System.out.println("Turno: " + game.currentTurn());
            System.out.print("Jugada > ");
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
                System.out.println("Movimiento rechazado: " + result);
            }
        }

        renderer.render(game);
        System.out.println("¡Fin de la partida!");
        scanner.close();
    }
}
