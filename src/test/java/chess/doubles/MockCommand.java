package chess.doubles;

import chess.core.commands.ICommand;

/**
 * MOCK: no hace nada real, pero guarda evidencia de cómo lo usaron
 * para poder verificarlo en el Assert.
 */
public class MockCommand implements ICommand {

    public int timesExecuteWasCalled = 0;
    public int timesUndoWasCalled = 0;

    @Override
    public void execute() {
        timesExecuteWasCalled++;
    }

    @Override
    public void undo() {
        timesUndoWasCalled++;
    }
}
