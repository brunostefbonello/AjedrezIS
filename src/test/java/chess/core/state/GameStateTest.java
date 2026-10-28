package chess.core.state;

import chess.core.model.Color;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameStateTest {

    @Test
    void whiteTurnAllowsOnlyWhiteAndTransitionsToBlack() {
        // Arrange
        // Instanciamos el turno 1 con un máximo de 30
        IGameState state = new WhiteTurnState(1, 30);

        // Act & Assert
        assertTrue(state.canMove(Color.WHITE));
        assertFalse(state.canMove(Color.BLACK));
        assertFalse(state.isOver());

        IGameState nextState = state.next();
        assertTrue(nextState instanceof BlackTurnState);
    }

    @Test
    void blackTurnAllowsOnlyBlackAndTransitionsToWhiteWhenGameContinues() {
        // Arrange
        // Turno 1 de 30 (le toca a las negras)
        IGameState state = new BlackTurnState(1, 30);

        // Act & Assert
        assertTrue(state.canMove(Color.BLACK));
        assertFalse(state.canMove(Color.WHITE));
        assertFalse(state.isOver());

        // Al terminar las negras, pasa al turno 2 (juegan blancas)
        IGameState nextState = state.next();
        assertTrue(nextState instanceof WhiteTurnState);
    }

    @Test
    void blackTurnTransitionsToGameOverWhenMaxTurnsReached() {
        // Arrange
        // Simulamos que estamos en el turno 1, siendo 1 el límite máximo
        IGameState state = new BlackTurnState(1, 1);

        // Act
        IGameState nextState = state.next();

        // Assert - luego de mover las negras en el último turno, se acabó el juego
        assertTrue(nextState instanceof GameOverState);
    }

    @Test
    void gameOverBlocksAllMovesAndIsOver() {
        // Arrange
        IGameState state = new GameOverState();

        // Act & Assert
        assertFalse(state.canMove(Color.WHITE));
        assertFalse(state.canMove(Color.BLACK));
        assertTrue(state.isOver());

        // Sigue devolviéndose a sí mismo
        assertTrue(state.next() instanceof GameOverState);
    }
}