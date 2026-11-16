package chess.core.commands;

import java.util.ArrayDeque;
import java.util.Deque;

public class MoveHistory {

    private final Deque<ICommand> done = new ArrayDeque<>();

    public void execute(ICommand command) {
        command.execute();
        done.push(command);
    }

    /** @return false si no había nada para deshacer. */
    public boolean undo() {
        if (done.isEmpty()) {
            return false;
        }
        done.pop().undo();
        return true;
    }
}
