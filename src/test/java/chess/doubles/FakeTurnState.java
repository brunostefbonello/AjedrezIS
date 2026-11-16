package chess.doubles;

import chess.core.model.Color;
import chess.core.state.IGameState;

// FAKE: alterna turnos de verdad, simplificado
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
