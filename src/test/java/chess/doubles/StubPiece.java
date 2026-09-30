package chess.doubles;

import chess.core.model.Board;
import chess.core.model.Color;
import chess.core.model.Piece;
import chess.core.model.Position;

/**
 * STUB: devuelve siempre la misma respuesta a canMoveTo, sin ninguna lógica.
 * Sirve para testear lo que rodea a las piezas (validador, comandos, partida)
 * sin depender de que la torre o el peón ya estén programados.
 */
public class StubPiece extends Piece {

    private final boolean canMove;

    public StubPiece(Color color, boolean canMove) {
        super(color);
        this.canMove = canMove;
    }

    @Override
    public boolean canMoveTo(Position from, Position to, Board board) {
        return canMove;
    }
}
