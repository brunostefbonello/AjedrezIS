package chess.adapters.swing;

import chess.core.commands.MoveHistory;
import chess.core.game.ChessGame;
import chess.core.model.ScoreBoard;
import chess.core.ports.IGameService;
import chess.core.rules.MoveValidator;
import chess.core.setup.BoardSetup;
import chess.core.state.WhiteTurnState;

import javax.swing.SwingUtilities;

public class SwingApp {

    private static final int MAX_TURNS = 30;

    public static void main(String[] args) {
        // Ensamble de dependencias: único lugar donde se arma todo con new
        IGameService game = new ChessGame(
                BoardSetup.standard(),
                new MoveValidator(),
                new MoveHistory(),
                new ScoreBoard(),
                new WhiteTurnState(1, MAX_TURNS));     // turno 1 de 30

        // La ventana se crea en el hilo de Swing
        SwingUtilities.invokeLater(() -> new ChessWindow(game));
    }
}
