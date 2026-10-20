package chess.core.model;

/**
 * Patrón Strategy: CÓMO se mueve una pieza, separado de QUÉ es la pieza.
 *
 * Cada forma de moverse es una clase aparte (StraightMovement, DiagonalMovement, ...)
 * y la pieza la recibe por constructor. Así la reina reutiliza las reglas de la torre
 * y del alfil en vez de repetirlas.
 *
 * Precondición: cuando se llama, el MoveValidator ya garantizó que en {@code from}
 * está {@code piece}, que {@code from != to} y que en {@code to} no hay un aliado.
 * La estrategia solo decide la geometría (y si el camino está libre, si le aplica).
 */
public interface MovementStrategy {

    boolean canMove(Piece piece, Position from, Position to, Board board);
}
