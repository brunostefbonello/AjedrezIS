package chess.core.state;

import chess.core.model.Color;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameStateTest {

    @Test
    void whiteTurnAllowsOnlyWhiteAndTransitionsToBlack() {
        // Arrange
        IGameState state = new WhiteTurnState();

        // Act & Assert
        assertTrue(state.canMove(Color.WHITE));
        assertFalse(state.canMove(Color.BLACK));
        assertFalse(state.isOver());

        IGameState nextState = state.next();
        assertTrue(nextState instanceof BlackTurnState);
    }

    @Test
    void blackTurnAllowsOnlyBlackAndTransitionsToWhite() {
        // Arrange
        IGameState state = new BlackTurnState();

        // Act & Assert
        assertTrue(state.canMove(Color.BLACK));
        assertFalse(state.canMove(Color.WHITE));
        assertFalse(state.isOver());

        IGameState nextState = state.next();
        assertTrue(nextState instanceof WhiteTurnState);
    }

    @Test
    void gameOverBlocksAllMovesAndIsOver() {
        // Arrange
        IGameState state = new GameOverState();

        // Act & Assert
        assertFalse(state.canMove(Color.WHITE));
        assertFalse(state.canMove(Color.BLACK));
        assertTrue(state.isOver());
    }
}