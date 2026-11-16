package chess.doubles;

import chess.core.commands.ICommand;

// MOCK: solo registra cómo lo usaron
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
