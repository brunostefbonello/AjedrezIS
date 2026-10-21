package chess.adapters.console;

import chess.core.model.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotationParserTest {

    @Test
    void parsesValidAlgebraicNotationToPosition() {
        // Arrange
        NotationParser parser = new NotationParser();

        // Act
        Position e2 = parser.parse("e2");
        Position a1 = parser.parse("a1");
        Position h8 = parser.parse("h8");

        // Assert
        // e2: fila 2 (row 1), columna e (col 4)
        assertEquals(new Position(1, 4), e2);
        // a1: fila 1 (row 0), columna a (col 0)
        assertEquals(new Position(0, 0), a1);
        // h8: fila 8 (row 7), columna h (col 7)
        assertEquals(new Position(7, 7), h8);
    }

    @Test
    void returnsNullWhenInputIsInvalidOrOutOfRange() {
        // Arrange
        NotationParser parser = new NotationParser();

        // Act & Assert
        // Fuera de rango de columnas o filas
        assertNull(parser.parse("i1"));
        assertNull(parser.parse("a9"));
        assertNull(parser.parse("z0"));

        // Formatos incorrectos o longitudes inválidas
        assertNull(parser.parse("e"));
        assertNull(parser.parse("e22"));
        assertNull(parser.parse(""));
        assertNull(parser.parse("   "));
        assertNull(parser.parse(null));
    }
}