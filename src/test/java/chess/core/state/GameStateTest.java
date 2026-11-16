package chess.core.state;

import chess.core.model.Color;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameStateTest {

    @Test
    void whiteTurnAllowsOnlyWhiteAndTransitionsToBlack() {
        // Arrange
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
        IGameState state = new BlackTurnState(1, 30);

        // Act & Assert
        assertTrue(state.canMove(Color.BLACK));
        assertFalse(state.canMove(Color.WHITE));
        assertFalse(state.isOver());

        // Pasa al turno 2
        IGameState nextState = state.next();
        assertTrue(nextState instanceof WhiteTurnState);
    }

    @Test
    void blackTurnTransitionsToGameOverWhenMaxTurnsReached() {
        // Arrange
        // Turno 1 de un máximo de 1
        IGameState state = new BlackTurnState(1, 1);

        // Act
        IGameState nextState = state.next();

        // Assert: GameOver tras el último turno
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

        assertTrue(state.next() instanceof GameOverState);
    }

    @Test
    void oneTurnLimitEndsAfterWhiteAndBlackMove() {
        // Arrange
        IGameState state = new WhiteTurnState(1, 1);

        // Act
        IGameState afterWhite = state.next();
        IGameState afterBlack = afterWhite.next();

        // Assert: termina recién tras las negras
        assertFalse(afterWhite.isOver());
        assertTrue(afterWhite.canMove(Color.BLACK));
        assertTrue(afterBlack instanceof GameOverState);
    }

    @Test
    void twoTurnLimitAllowsASecondFullTurn() {
        // Arrange
        IGameState state = new WhiteTurnState(1, 2);

        // Act — turno 1 completo
        IGameState secondTurn = state.next().next();

        // Assert: turno 2 de blancas, luego GameOver
        assertTrue(secondTurn.canMove(Color.WHITE));
        assertFalse(secondTurn.isOver());
        assertTrue(secondTurn.next().next() instanceof GameOverState);
    }
}