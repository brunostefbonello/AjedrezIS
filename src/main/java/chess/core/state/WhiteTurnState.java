package chess.core.state;

import chess.core.model.Color;

public class WhiteTurnState implements IGameState {
    private final int turn;
    private final int maxTurns;

    public WhiteTurnState(int turn, int maxTurns) {
        this.turn = turn;
        this.maxTurns = maxTurns;
    }

    @Override
    public boolean canMove(Color color) {
        return color == Color.WHITE;
    }

    @Override
    public IGameState next() {
        // Mismo número de turno: lo cierran las negras
        return new BlackTurnState(turn, maxTurns);
    }

    @Override
    public boolean isOver() {
        return false;
    }
}