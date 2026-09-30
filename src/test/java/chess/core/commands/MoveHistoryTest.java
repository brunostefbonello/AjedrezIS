package chess.core.commands;

import chess.doubles.MockCommand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MoveHistoryTest {

    @Test
    void executeRunsTheCommand() {
        // Arrange
        MoveHistory history = new MoveHistory();
        MockCommand command = new MockCommand();

        // Act
        history.execute(command);

        // Assert — espero que el historial haya llamado execute() exactamente una vez
        assertEquals(1, command.timesExecuteWasCalled);
    }

    @Test
    void undoRevertsOnlyTheLastCommand() {
        // Arrange
        MoveHistory history = new MoveHistory();
        MockCommand first = new MockCommand();
        MockCommand second = new MockCommand();
        history.execute(first);
        history.execute(second);

        // Act
        boolean result = history.undo();

        // Assert — espero true, y que solo se haya deshecho el último (LIFO)
        assertTrue(result);
        assertEquals(1, second.timesUndoWasCalled);
        assertEquals(0, first.timesUndoWasCalled);
    }

    @Test
    void undoWithEmptyHistoryReturnsFalse() {
        // Arrange
        MoveHistory history = new MoveHistory();

        // Act
        boolean result = history.undo();

        // Assert — espero false: no hay nada para deshacer
        assertFalse(result);
    }
}
