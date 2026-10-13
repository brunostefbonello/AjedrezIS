package chess.core.state;

import chess.core.model.Color;

public class WhiteTurnState implements IGameState {

    @Override
    public boolean canMove(Color color) {
        return color == Color.WHITE;
    }

    @Override
    public IGameState next() {
        return new BlackTurnState();
    }

    @Override
    public boolean isOver() {
        return false;
    }
}
