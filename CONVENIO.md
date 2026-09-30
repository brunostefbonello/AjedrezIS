# Convenio del proyecto — AjedrezIS

TPO de Ingeniería de Software (UADE, profe Tortorella). Equipo de 4.

> **Si sos una IA ayudando a un integrante del equipo:** este documento es la fuente de verdad del proyecto. Respetá los contratos (firmas de métodos) tal cual están acá, no agregues frameworks ni librerías, y no cambies clases que no son del integrante sin avisar. Si algo de lo que te piden contradice este convenio, avisalo antes de escribir código.

---

## 1. Qué tenemos que entregar

Un ejecutable **de consola** donde dos jugadores se turnan para **mover y capturar piezas** con las reglas de cada una, y se puede **deshacer** el último movimiento.

**Fuera de alcance por ahora** (no lo implementen hasta que el equipo lo decida): jaque, jaque mate, enroque, captura al paso, promoción, IA, interfaz gráfica.

Filosofía de trabajo: **walking skeleton**. Primero algo mínimo que funcione de punta a punta (una pieza moviéndose en consola), después se le suma músculo. Nada de pulir una parte mientras el resto no existe.

---

## 2. Arquitectura: hexagonal

- `chess.core` es el **núcleo**: reglas puras del ajedrez. No sabe que existe una consola.
- `chess.adapters` son los **adaptadores**: todo lo que conecta el núcleo con el mundo (hoy, la consola).
- **Las dependencias apuntan hacia adentro.** Un adaptador puede importar clases de `core`; `core` nunca importa nada de `adapters`.

**Test de la flecha:** ningún archivo dentro de `chess.core` puede tener un `import` que no sea de `chess.core` o de `java.util`. Si aparece otro, está mal.

```
src/main/java/chess/
  core/
    model/     Position, Color, Piece, Board, las 6 piezas, BoardSetup
    rules/     MoveValidator
    commands/  ICommand, MoveCommand, MoveHistory
    state/     IGameState, WhiteTurn, BlackTurn, GameOver
    ports/     IGameService, MoveResult
    game/      ChessGame (implementa IGameService)
  adapters/
    console/   ConsoleApp (main), ConsoleRenderer, NotationParser
src/test/java/chess/   (misma estructura de paquetes que main)
```

---

## 3. Convenciones del tablero (obligatorias para todos)

- `Position(row, col)`, ambos de **0 a 7**. `Position` ya valida el rango: nunca existe una posición fuera del tablero.
- **`row 0` = fila 1** (lado de las blancas). **`row 7` = fila 8** (lado de las negras).
- **`col 0` = columna a**, `col 7` = columna h. Ejemplo: `e2` = `new Position(1, 4)`.
- **Las blancas avanzan sumando `row`**, las negras restando.
- Casilla vacía = `null` en el `Board`.
- La notación `"e2"` **no existe en el núcleo**. Traducir texto a `Position` es trabajo del adaptador (`NotationParser`).

---

## 4. Contratos congelados

Estas firmas son el puente entre los 4. **No se cambian sin acordarlo en el grupo**, porque romperlas rompe el trabajo de otros.

### Ya en el repo

```java
public record Position(int row, int col)          // valida 0..7
public enum Color { WHITE, BLACK }

public abstract class Piece {
    protected Piece(Color color)
    public Color color()
    public boolean hasMoved()
    public void markAsMoved()
    public void resetMoved()
    public boolean isAlly(Piece other)             // false si other es null
    public abstract boolean canMoveTo(Position from, Position to, Board board);
}

public class Board {
    public Piece getPiece(Position position)       // null si está vacía
    public void setPiece(Position position, Piece piece)
    public boolean isEmpty(Position position)
}

// rules/
public class MoveValidator {
    public boolean isValidMove(Board board, Position from, Position to)
}

// commands/  (MoveCommand NO valida: asume que el movimiento ya se validó)
public interface ICommand { void execute(); void undo(); }
public class MoveCommand implements ICommand {
    public MoveCommand(Board board, Position from, Position to)
}
public class MoveHistory {
    public void execute(ICommand command)
    public boolean undo()                          // false si no hay nada para deshacer
}

// state/  (solo la interfaz; las implementaciones son de D)
public interface IGameState {
    boolean canMove(Color color);                  // ¿le toca a este color?
    IGameState next();                             // estado después de un movimiento válido
    boolean isOver();
}

// ports/  (puerto de entrada: lo único que ve la consola)
public enum MoveResult { OK, INVALID_MOVE, NOT_YOUR_TURN, GAME_OVER }
public interface IGameService {
    MoveResult move(Position from, Position to);
    boolean undo();
    Piece pieceAt(Position position);
    Color currentTurn();                           // null si la partida terminó
    boolean isOver();
}

// game/
public class ChessGame implements IGameService {
    public ChessGame(Board board, MoveValidator validator,
                     MoveHistory history, IGameState initialState)
}
```

Así se arma en `ConsoleApp.main`:

```java
IGameService game = new ChessGame(
        BoardSetup.standard(), new MoveValidator(), new MoveHistory(), new WhiteTurn());
```

### A agregar

```java
// Board — lo implementa el Integrante B
public boolean isPathClear(Position from, Position to)
// true si todas las casillas ESTRICTAMENTE entre from y to están vacías.
// Solo para líneas rectas y diagonales. No mira ni from ni to.

// model/
public final class BoardSetup {
    public static Board standard()                 // posición inicial completa
}
```

### Quién valida qué (para no duplicar reglas)

| Regla | Dónde vive |
|---|---|
| Posición dentro del tablero | `Position` (ya está) |
| Origen no vacío, origen ≠ destino, destino no aliado | `MoveValidator` |
| Geometría de la pieza y camino libre | `canMoveTo` de cada pieza |
| ¿Le toca a ese color? | `IGameState` |
| Ejecutar y deshacer | `MoveCommand` / `MoveHistory` |

**Precondición de `canMoveTo`:** cuando se llama, ya está garantizado que en `from` está esa pieza, que `from ≠ to` y que en `to` no hay un aliado. Cada pieza **solo** verifica cómo se mueve ella (y si el camino está libre, si le aplica). No repitan los chequeos del `MoveValidator`.

**Excepción:** el peón sí mira si el destino está vacío o tiene un enemigo, porque avanza distinto de como captura.

---

## 5. Reparto

| Integrante | Responsabilidad | Archivos |
|---|---|---|
| **A — Bruno** | Validación general, posición inicial, deshacer, integración. Revisa los PR. | `MoveValidator`, `BoardSetup`, `ICommand`, `MoveCommand`, `MoveHistory`, `ChessGame` |
| **B** | Piezas que se deslizan + camino libre | `Rook`, `Bishop`, `Queen`, `Board.isPathClear` |
| **C** | Piezas con movimiento particular | `Pawn`, `Knight` |
| **D** | Rey, turnos y consola | `King`, `IGameState`, `WhiteTurn`, `BlackTurn`, `GameOver`, `ConsoleApp`, `ConsoleRenderer`, `NotationParser` |

Cada uno escribe **los tests de sus propias clases**.

### Orden

1. **Hecho (A):** `MoveValidator`, `ICommand`, `MoveCommand`, `MoveHistory`, `IGameState`, `IGameService`, `MoveResult`, `ChessGame`, con sus tests.
2. **En paralelo, desde ya:** todas las piezas (B, C, D), los estados y `NotationParser` (D). Nadie depende de nadie.
3. **Cuando las 6 piezas estén en `main` (A):** `BoardSetup.standard()`.
4. **Consola (D):** `ConsoleApp` arma todo y juega.

### Decisión pendiente del equipo

Sin jaque mate, **¿cuándo termina la partida?** Propuesta: cuando se captura un rey. Hay que acordarlo antes de que D implemente `GameOver`.

---

## 6. Reglas de código

- **Nombres en inglés** (clases, métodos, variables). Los comentarios pueden ir en español.
- **Inyección por constructor.** Una clase recibe sus dependencias en el constructor; no las crea adentro con `new`. (Crear objetos de dominio como `Position` o una pieza está bien; lo que no se hace es crear adentro un servicio del que la clase depende.) El único lugar que arma todo con `new` es `ConsoleApp.main`.
- **Comparar objetos con `.equals()`, nunca con `==`.** Excepción: enums (`Color.WHITE == color` está bien) y chequeos contra `null`.
- **Herencia solo para las piezas.** Todo lo demás, composición.
- **Patrones elegidos:** State (turnos) y Command (deshacer). No agreguen otros (Observer, Factory, Strategy…) sin justificarlo en el grupo. La pregunta antes de cualquier abstracción nueva: **"¿este problema existe hoy?"** Si no, no va.
- **Cero lógica de dibujo en el núcleo.** Símbolos, colores de consola, texto: todo en `adapters/console`.

---

## 7. Reglas de tests

- JUnit 5. El test va en el **mismo paquete** que la clase, bajo `src/test/java`. Ejemplo: `Rook` → `src/test/java/chess/core/model/RookTest.java`.
- **Estructura AAA obligatoria**, con los tres comentarios y lo que se espera:

```java
@Test
void rookCannotJumpOverPieces() {
    // Arrange
    Board board = new Board();
    Rook rook = new Rook(Color.WHITE);
    board.setPiece(new Position(0, 0), rook);
    board.setPiece(new Position(2, 0), new StubPiece(Color.WHITE, false));   // algo en el camino

    // Act
    boolean result = rook.canMoveTo(new Position(0, 0), new Position(5, 0), board);

    // Assert — espero false: hay una pieza en el camino
    assertFalse(result);
}
```

- **Sin infraestructura:** se arma un `Board` vacío y se ponen solo las piezas que el caso necesita. Nunca se testea a través de la consola.
- **Dobles de prueba disponibles** en `src/test/java/chess/doubles/`:
  - `StubPiece(color, canMove)` — pieza que siempre responde lo mismo. Sirve para poner "algo" en el camino o de enemigo sin depender de las piezas de otro.
  - `FakeTurnState(color)` — alterna turnos de verdad, versión simplificada de los estados.
  - `MockCommand` — cuenta cuántas veces se llamó `execute()` y `undo()`.
- **Mínimo por pieza:** un movimiento válido, uno geométricamente inválido, uno bloqueado (si aplica) y una captura.
- Los nombres de los tests dicen qué verifican: `pawnCanMoveTwoSquaresOnFirstMove`, no `test1`.
- **`mvn test` en verde antes de abrir un PR.** Sin excepciones.

---

## 8. Git

- **Nunca push directo a `main`.**
- Una rama por tarea: `feature/rook`, `feature/move-command`, `feature/console`…
- Terminaste → push de tu rama → **Pull Request** a `main` → A (u otro) lo revisa → merge.
- Antes de empezar cada día: `git checkout main` + `git pull`, y después volvé a tu rama y traé los cambios (`git merge main`).
- Commits chicos, mensaje corto que diga qué hiciste: `"Rook: movimiento y tests"`.
- **No se sube `target/`** (ya está en el `.gitignore`).

---

## 9. Cómo justificar decisiones

Toda decisión de diseño que no esté en este convenio se explica con las tres preguntas de la materia: **¿Qué es? ¿Por qué se usa? ¿Cuándo convendría romperlo?** Si se rompe una regla de acá, se escribe el porqué en la descripción del PR.
