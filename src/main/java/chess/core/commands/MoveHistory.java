package chess.core.commands;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Ejecuta comandos y los recuerda para poder deshacerlos.
 * Pila (LIFO): el último movimiento que entró es el primero que se deshace.
 */
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
