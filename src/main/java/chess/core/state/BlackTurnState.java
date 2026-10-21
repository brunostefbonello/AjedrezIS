package chess.core.state;

import chess.core.model.Color;

public class BlackTurnState implements IGameState {

    @Override
    public boolean canMove(Color color) {
        return color == Color.BLACK;
    }

    @Override
    public IGameState next() {
        return new WhiteTurnState();
    }

    @Override
    public boolean isOver() {
        return false;
    }
}
