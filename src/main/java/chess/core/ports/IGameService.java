package chess.core.ports;

import chess.core.model.Color;
import chess.core.model.Piece;
import chess.core.model.Position;

/**
 * Puerto de entrada: lo único que un adaptador (la ventana Swing, o una consola)
 * necesita conocer para jugar. El núcleo no sabe quién lo llama.
 */
public interface IGameService {

    MoveResult move(Position from, Position to);

    /** @return false si no había ningún movimiento para deshacer. */
    boolean undo();

    /** @return la pieza en esa casilla, o null si está vacía. */
    Piece pieceAt(Position position);

    /** @return el color al que le toca mover, o null si la partida terminó. */
    Color currentTurn();

    boolean isOver();

    /** Puntos acumulados por ese color con sus capturas. */
    int score(Color color);

    /**
     * Ganador de la partida terminada: el que capturó al rey, o si se acabaron los turnos,
     * el que tiene más puntos.
     *
     * @return null si la partida no terminó, o si terminó empatada en puntos.
     *         Chequear isOver() para distinguir los dos casos.
     */
    Color winner();
}
