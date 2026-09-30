package chess.core.model;

public record Position(int row, int col) {

    public Position {
        if (row < 0 || row > 7 || col < 0 || col > 7) {
            throw new IllegalArgumentException("Position out of board: " + row + "," + col);
        }
    }
}