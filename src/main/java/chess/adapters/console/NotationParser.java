package chess.adapters.console;

import chess.core.model.Position;

public class NotationParser {

    private final int maxRows;
    private final int maxCols;

    public NotationParser(int maxRows, int maxCols) {
        this.maxRows = maxRows;
        this.maxCols = maxCols;
    }

    public Position parse(String input) {
        if (input == null || input.trim().length() < 2) {
            return null;
        }

        String cleaned = input.trim().toLowerCase();
        char colChar = cleaned.charAt(0);
        int col = colChar - 'a';

        if (col < 0 || col >= maxCols) {
            return null;
        }

        try {
            int rowNumber = Integer.parseInt(cleaned.substring(1));
            int row = rowNumber - 1;

            if (row < 0 || row >= maxRows) {
                return null;
            }

            return new Position(row, col);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}