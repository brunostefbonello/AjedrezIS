package chess.adapters.console;

import chess.core.model.Position;

public class NotationParser {

    public Position parse(String input) {
        if (input == null || input.trim().length() != 2) {
            return null;
        }

        String cleaned = input.trim().toLowerCase();
        char colChar = cleaned.charAt(0);
        char rowChar = cleaned.charAt(1);

        // Rango fijo 8x8 del convenio: 'a'..'h' y '1'..'8'
        if (colChar < 'a' || colChar > 'h' || rowChar < '1' || rowChar > '8') {
            return null;
        }

        int col = colChar - 'a';
        int row = rowChar - '1';

        return new Position(row, col);
    }
}