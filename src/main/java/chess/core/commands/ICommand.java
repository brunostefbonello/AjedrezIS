package chess.core.commands;

/**
 * Una acción convertida en objeto (patrón Command).
 * Al existir como objeto, se puede guardar en un historial y deshacer.
 */
public interface ICommand {

    void execute();

    void undo();
}
