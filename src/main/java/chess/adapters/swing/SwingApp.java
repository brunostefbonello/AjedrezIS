package chess.adapters.swing;

import chess.core.game.ChessGame;
import chess.core.model.Board;
import chess.core.model.Color;
import chess.core.model.Position;
import chess.core.model.ScoreBoard;
import chess.core.ports.IGameService;
import chess.core.rules.MoveValidator;
import chess.core.commands.MoveHistory;
import chess.core.state.WhiteTurnState;
import chess.core.setup.Pieces;

import javax.swing.SwingUtilities;

public class SwingApp {

    public static void main(String[] args) {
        // Ensamble de dependencias
        Board board = new Board();


        board.setPiece(new Position(0, 4), Pieces.king(Color.WHITE));
        board.setPiece(new Position(7, 4), Pieces.king(Color.BLACK));
        board.setPiece(new Position(1, 4), Pieces.pawn(Color.WHITE));

        MoveValidator validator = new MoveValidator();
        MoveHistory history = new MoveHistory();
        ScoreBoard scoreBoard = new ScoreBoard();

        // Inicializamos con el mismo estado estricto de turnos
        IGameService game = new ChessGame(board, validator, history, scoreBoard, new WhiteTurnState(1, 30));

        // Lanzamos la ventana en el hilo correcto de Swing
        SwingUtilities.invokeLater(() -> new ChessWindow(game));
    }
}