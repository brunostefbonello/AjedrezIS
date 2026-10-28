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
        // Pasa el turno a las negras, pero seguimos en el mismo número de turno general
        return new BlackTurnState(turn, maxTurns);
    }

    @Override
    public boolean isOver() {
        return false;
    }
}