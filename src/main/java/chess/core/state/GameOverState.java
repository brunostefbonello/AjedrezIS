package chess.core.state;

import chess.core.model.Color;

public class GameOverState implements IGameState {

    @Override
    public boolean canMove(Color color) {
        return false; // Nadie mueve si terminó
    }

    @Override
    public IGameState next() {
        return this; // Se queda acá
    }

    @Override
    public boolean isOver() {
        return true;
    }
}