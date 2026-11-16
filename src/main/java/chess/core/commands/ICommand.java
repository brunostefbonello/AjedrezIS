package chess.core.commands;

// Patrón Command: permite historial y deshacer
public interface ICommand {

    void execute();

    void undo();
}
