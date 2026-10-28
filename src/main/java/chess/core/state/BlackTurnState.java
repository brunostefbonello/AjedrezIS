package chess.core.state;

import chess.core.model.Color;

public class BlackTurnState implements IGameState {
    private final int turn;
    private final int maxTurns;

    public BlackTurnState(int turn, int maxTurns) {
        this.turn = turn;
        this.maxTurns = maxTurns;
    }

    @Override
    public boolean canMove(Color color) {
        return color == Color.BLACK;
    }

    @Override
    public IGameState next() {
        // Si las negras completaron el último turno, se acabó el juego
        if (turn >= maxTurns) {
            return new GameOverState();
        }
        // Si no, arranca un nuevo turno general para las blancas
        return new WhiteTurnState(turn + 1, maxTurns);
    }

    @Override
    public boolean isOver() {
        return false;
    }
}