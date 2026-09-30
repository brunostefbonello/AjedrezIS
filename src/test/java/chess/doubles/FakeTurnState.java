package chess.doubles;

import chess.core.model.Color;
import chess.core.state.IGameState;

/**
 * FAKE: funciona de verdad (alterna turnos), pero es una versión simplificada
 * de los estados reales. Permite testear ChessGame sin esperar a WhiteTurn/BlackTurn.
 */
public class FakeTurnState implements IGameState {

    private final Color turn;
    private final boolean over;

    public FakeTurnState(Color turn) {
        this(turn, false);
    }

    public FakeTurnState(Color turn, boolean over) {
        this.turn = turn;
        this.over = over;
    }

    @Override
    public boolean canMove(Color color) {
        return !over && color == turn;
    }

    @Override
    public IGameState next() {
        Color other = (turn == Color.WHITE) ? Color.BLACK : Color.WHITE;
        return new FakeTurnState(other, over);
    }

    @Override
    public boolean isOver() {
        return over;
    }
}
