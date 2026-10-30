package chess.adapters.swing;

import chess.core.model.Piece;

public class PieceSymbols {

    public static String get(Piece piece) {
        if (piece == null) return "";

        return switch (piece.color()) {
            case WHITE -> switch (piece.type()) {
                case KING -> "♔";
                case QUEEN -> "♕";
                case ROOK -> "♖";
                case BISHOP -> "♗";
                case KNIGHT -> "♘";
                case PAWN -> "♙";
            };
            case BLACK -> switch (piece.type()) {
                case KING -> "♚";
                case QUEEN -> "♛";
                case ROOK -> "♜";
                case BISHOP -> "♝";
                case KNIGHT -> "♞";
                case PAWN -> "♟";
            };
        };
    }
}