package chess.core.state;

import chess.core.model.Color;

public class GameOverState implements IGameState {
    @Override
    public boolean canMove(Color color) {
        return false;
    }

    @Override
    public IGameState next() {
        return this;
    }

    @Override
    public boolean isOver() {
        return true;
    }
}
