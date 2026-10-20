# Convenio del proyecto — AjedrezIS

TPO de Ingeniería de Software (UADE, profe Tortorella). Equipo de 4.

> **Si sos una IA ayudando a un integrante del equipo:** este documento es la fuente de verdad del proyecto. Respetá los contratos (firmas de métodos) tal cual están acá, no agregues frameworks ni librerías, y no cambies clases que no son del integrante sin avisar. Si algo de lo que te piden contradice este convenio, avisalo antes de escribir código.

> **Versión 2** — actualizado después de la devolución del profe: movimientos con **Strategy** (las piezas ya no usan herencia), **puntaje** por capturas, **condición de corte** por cantidad de turnos e **interfaz Swing obligatoria**. Lo que cambió respecto de la v1 está marcado con 🆕.

---

## 1. Qué tenemos que entregar

Un ejecutable con **interfaz Swing básica** 🆕 donde dos jugadores se turnan para **mover y capturar piezas** con las reglas de cada una, se puede **deshacer** el último movimiento, y cada captura **suma puntos** 🆕.

**Fin de la partida** 🆕 (decidido por el equipo):
1. **Se captura un rey** → la partida termina en el momento y **gana el que lo capturó**, sin importar los puntos.
2. **Se terminan los turnos** → **gana el que tiene más puntos**. Si empatan en puntos, es empate.

- Un **turno** = una jugada de blancas **más** una de negras.
- El **límite es fijo: 30 turnos**. Se pasa por constructor al estado inicial (no queda escrito adentro de la lógica), así cambiarlo es tocar un solo número en el `main`.

**Fuera de alcance** (no lo implementen hasta que el equipo lo decida): jaque, jaque mate, rey ahogado, enroque, captura al paso, promoción, IA.

**Requisitos del profe para la entrega:** diagrama de clases UML común (sin nada especial), condición de corte, valores de piezas con puntaje acumulado, Strategy para los movimientos, interfaz Swing básica.

Filosofía de trabajo: **walking skeleton**. Primero algo mínimo que funcione de punta a punta (una pieza moviéndose en la ventana Swing), después se le suma músculo.

---

## 2. Arquitectura: hexagonal

- `chess.core` es el **núcleo**: reglas puras del ajedrez. No sabe que existe una ventana.
- `chess.adapters` son los **adaptadores**: todo lo que conecta el núcleo con el mundo (la ventana Swing).
- **Las dependencias apuntan hacia adentro.** Un adaptador puede importar clases de `core`; `core` nunca importa nada de `adapters`.

**Test de la flecha:** ningún archivo dentro de `chess.core` puede tener un `import` que no sea de `chess.core` o de `java.util`. **Ningún `javax.swing` en `core`.** Si aparece, está mal.

```
src/main/java/chess/
  core/
    model/     Position, Color, Board, Piece, PieceType, MovementStrategy, ScoreBoard
    movement/  🆕 StraightMovement, DiagonalMovement, CombinedMovement,
               KnightMovement, PawnMovement, KingMovement
    setup/     🆕 Pieces (crea cada pieza con su estrategia), BoardSetup
    rules/     MoveValidator
    commands/  ICommand, MoveCommand, MoveHistory
    state/     IGameState, WhiteTurn, BlackTurn, GameOver
    ports/     IGameService, MoveResult
    game/      ChessGame (implementa IGameService)
  adapters/
    swing/     🆕 SwingApp (main), ChessWindow, PieceSymbols
    console/   opcional, solo si sobra tiempo al final
src/test/java/chess/   (misma estructura de paquetes que main)
```

---

## 3. Convenciones del tablero (obligatorias para todos)

- `Position(row, col)`, ambos de **0 a 7**. `Position` ya valida el rango: nunca existe una posición fuera del tablero.
- **`row 0` = fila 1** (lado de las blancas). **`row 7` = fila 8** (lado de las negras).
- **`col 0` = columna a**, `col 7` = columna h. Ejemplo: `e2` = `new Position(1, 4)`.
- **Las blancas avanzan sumando `row`**, las negras restando.
- Casilla vacía = `null` en el `Board`.
- 🆕 **En la ventana, la fila 8 se dibuja arriba**: la casilla de la grilla en la fila visual `i` (0 = arriba) corresponde a `row = 7 - i`. Esa traducción vive solo en el adaptador Swing.

---

## 4. Contratos congelados

Estas firmas son el puente entre los 4. **No se cambian sin acordarlo en el grupo**, porque romperlas rompe el trabajo de otros.

### Ya en el repo

```java
public record Position(int row, int col)          // valida 0..7
public enum Color { WHITE, BLACK }

// 🆕 Valor de cada pieza = puntos que gana el rival al capturarla.
public enum PieceType {
    PAWN(1), KNIGHT(3), BISHOP(3), ROOK(5), QUEEN(9), KING(0);   // capturar al rey termina la partida
    public int value()
}

// 🆕 Patrón Strategy: CÓMO se mueve una pieza.
public interface MovementStrategy {
    boolean canMove(Piece piece, Position from, Position to, Board board);
}

// 🆕 Ya no es abstracta ni se hereda: es final y recibe su estrategia.
public final class Piece {
    public Piece(Color color, PieceType type, MovementStrategy movement)
    public Color color()
    public PieceType type()
    public int value()                             // = type().value()
    public boolean hasMoved()
    public void markAsMoved()
    public void resetMoved()
    public boolean isAlly(Piece other)             // false si other es null
    public boolean canMoveTo(Position from, Position to, Board board)   // delega en la estrategia
}

public class Board {
    public Piece getPiece(Position position)       // null si está vacía
    public void setPiece(Position position, Piece piece)
    public boolean isEmpty(Position position)
    public boolean isPathClear(Position from, Position to)
    // true si todas las casillas ESTRICTAMENTE entre from y to están vacías.
    // Para algo que no es recta ni diagonal devuelve false.
}

// 🆕 Puntos acumulados por color.
public class ScoreBoard {
    public int score(Color color)
    public void add(Color color, int amount)
    public void subtract(Color color, int amount)
}

// setup/  🆕 Único lugar que decide qué estrategia lleva cada pieza.
// Está vacía: cada integrante agrega el método de sus piezas (ver "A agregar").
public final class Pieces { }

// rules/
public class MoveValidator {
    public boolean isValidMove(Board board, Position from, Position to)
}

// commands/  (MoveCommand NO valida: asume que el movimiento ya se validó)
public interface ICommand { void execute(); void undo(); }
public class MoveCommand implements ICommand {
    public MoveCommand(Board board, Position from, Position to, ScoreBoard scoreBoard)   // 🆕 ScoreBoard
    public Piece capturedPiece()                   // 🆕 null si no hubo captura
    // execute() suma el valor de la capturada al que movió; undo() lo resta.
}
public class MoveHistory {
    public void execute(ICommand command)
    public boolean undo()                          // false si no hay nada para deshacer
}

// state/  (solo la interfaz; las implementaciones están pendientes)
public interface IGameState {
    boolean canMove(Color color);                  // ¿le toca a este color?
    IGameState next();                             // estado después de un movimiento válido
    boolean isOver();
}

// ports/  (puerto de entrada: lo único que ve la ventana)
public enum MoveResult { OK, INVALID_MOVE, NOT_YOUR_TURN, GAME_OVER }
public interface IGameService {
    MoveResult move(Position from, Position to);
    boolean undo();
    Piece pieceAt(Position position);
    Color currentTurn();                           // null si la partida terminó
    boolean isOver();
    int score(Color color);                        // 🆕
    Color winner();                                // 🆕 null si no terminó o si es empate (mirar isOver())
}

// game/
public class ChessGame implements IGameService {
    public ChessGame(Board board, MoveValidator validator, MoveHistory history,
                     ScoreBoard scoreBoard, IGameState initialState)              // 🆕 ScoreBoard
    // Detecta la captura del rey (termina la partida). El límite de turnos lo decide el estado.
}
```

Así se arma en `SwingApp.main` (único lugar con todos los `new`):

```java
IGameService game = new ChessGame(
        BoardSetup.standard(),
        new MoveValidator(),
        new MoveHistory(),
        new ScoreBoard(),
        new WhiteTurn(1, 30));          // turno 1 de 30
new ChessWindow(game);
```

### A agregar

```java
// movement/ — Integrante B (migrar Rook, Bishop y Queen: la lógica es la misma de antes)
public class StraightMovement implements MovementStrategy     // torre
public class DiagonalMovement implements MovementStrategy     // alfil
public class CombinedMovement implements MovementStrategy {   // reina = Straight + Diagonal
    public CombinedMovement(MovementStrategy... options)       // puede si CUALQUIERA puede
}

// movement/ — Integrante C
public class KnightMovement implements MovementStrategy   // en L, salta piezas (NO usa isPathClear)
public class PawnMovement implements MovementStrategy
// Avanza 1 hacia adelante si el destino está VACÍO; 2 si además nunca se movió (piece.hasMoved())
// y la casilla del medio está vacía. Captura 1 en diagonal hacia adelante SOLO si hay un enemigo.
// "Adelante" depende de piece.color(): blancas suman row, negras restan.

// movement/ — Integrante D
public class KingMovement implements MovementStrategy     // 1 casilla en cualquier dirección

// setup/ — cada uno agrega el suyo a Pieces
public static Piece rook(Color color)                     // B
public static Piece bishop(Color color)                   // B
public static Piece queen(Color color)                    // B: new CombinedMovement(new StraightMovement(), new DiagonalMovement())
public static Piece knight(Color color)                   // C
public static Piece pawn(Color color)                     // C
public static Piece king(Color color)                     // D

// setup/ — Integrante A, cuando estén las 6 piezas
public final class BoardSetup {
    public static Board standard()                        // posición inicial completa
}

// state/ — Integrante D
public class WhiteTurn implements IGameState { public WhiteTurn(int turn, int maxTurns) }
public class BlackTurn implements IGameState { public BlackTurn(int turn, int maxTurns) }
public class GameOver  implements IGameState { }
// WhiteTurn.next() → new BlackTurn(turn, maxTurns)          (el turno NO avanza: falta que muevan las negras)
// BlackTurn.next() → si turn == maxTurns: new GameOver()   (se cumplió el último turno completo)
//                    si no:               new WhiteTurn(turn + 1, maxTurns)
// GameOver: canMove(...) siempre false, next() devuelve this, isOver() true.

// adapters/swing/ — Integrante D
public class SwingApp { public static void main(String[] args) }
public class ChessWindow   // grilla 8x8 de botones, clic en origen + clic en destino → game.move(...)
                           // muestra turno, puntaje de cada color, mensaje del MoveResult,
                           // botón "Deshacer" → game.undo(), y el ganador cuando isOver()
public class PieceSymbols  // PieceType + Color → símbolo Unicode (♔♕♖♗♘♙ / ♚♛♜♝♞♟)
```

### Quién valida qué (para no duplicar reglas)

| Regla | Dónde vive |
|---|---|
| Posición dentro del tablero | `Position` |
| Origen no vacío, origen ≠ destino, destino no aliado | `MoveValidator` |
| Geometría de la pieza y camino libre | la `MovementStrategy` de la pieza 🆕 |
| ¿Le toca a ese color? ¿Se acabaron los turnos? | `IGameState` |
| Ejecutar, deshacer y sumar/restar puntos | `MoveCommand` / `MoveHistory` |
| ¿Se capturó el rey? ¿Quién ganó? | `ChessGame` |

**Precondición de cada estrategia:** cuando se llama, ya está garantizado que en `from` está esa pieza, que `from ≠ to` y que en `to` no hay un aliado. La estrategia **solo** verifica cómo se mueve (y si el camino está libre, si le aplica). No repitan los chequeos del `MoveValidator`.

**Excepción:** `PawnMovement` sí mira si el destino está vacío o tiene un enemigo, porque el peón avanza distinto de como captura.

---

## 5. Reparto

| Integrante | Responsabilidad | Archivos |
|---|---|---|
| **A — Bruno** | Núcleo, integración, diagrama. Revisa los PR. | ✅ `Piece` + `MovementStrategy`, `PieceType`, `ScoreBoard`, puntaje en comandos, `ChessGame`. Pendiente: `BoardSetup`, diagrama de clases |
| **B — Gonzalo** | Piezas que se deslizan + camino libre | ✅ `isPathClear`. Pendiente: migrar torre/alfil/reina a `StraightMovement`, `DiagonalMovement`, `CombinedMovement` + `Pieces.rook/bishop/queen` + sus tests |
| **C** | Caballo y peón | `KnightMovement`, `PawnMovement`, `Pieces.knight/pawn` |
| **D** | Rey, turnos y ventana | `KingMovement`, `Pieces.king`, `WhiteTurn`, `BlackTurn`, `GameOver`, `SwingApp`, `ChessWindow`, `PieceSymbols` |

D quedó cargado: si B termina de revisar, puede tomar los estados (`WhiteTurn`, `BlackTurn`, `GameOver`). Lo acuerdan entre ustedes.

Cada uno escribe **los tests de sus propias clases** (la ventana Swing no se testea).

### Orden

1. **Hecho:** todo lo marcado ✅ arriba.
2. **Primero, B:** la migración de torre/alfil/reina. Como `Piece` cambió de forma, las clases viejas ya no compilan: B crea su rama **desde `feature/strategy-scoring`** y abre su PR **contra esa rama** (no contra `main`). Cuando entra, se mergea todo junto a `main`.
3. **En paralelo con B:** `KnightMovement`, `PawnMovement` (C), `KingMovement` y los estados (D). Nadie depende de nadie. Mientras `feature/strategy-scoring` no esté en `main`, C y D también crean sus ramas desde ahí, porque necesitan la `Piece` nueva.
4. **Cuando las 6 piezas estén en `main` (A):** `BoardSetup.standard()`.
5. **Ventana (D):** puede arrancar ya con un tablero armado a mano (unas torres y alfiles con `Pieces`) y cambiar a `BoardSetup.standard()` cuando exista.
6. **Al final (A, con revisión de todos):** diagrama de clases UML.

---

## 6. Reglas de código

- **Nombres en inglés** (clases, métodos, variables). Los comentarios pueden ir en español.
- **Inyección por constructor.** Una clase recibe sus dependencias en el constructor; no las crea adentro con `new`. (Crear objetos de dominio como `Position` o una pieza está bien.) El único lugar que arma todo es `SwingApp.main`.
- **Comparar objetos con `.equals()`, nunca con `==`.** Excepción: enums (`Color.WHITE == color`, `type == PieceType.KING`) y chequeos contra `null`.
- 🆕 **Sin herencia de piezas.** `Piece` es `final`. Una pieza nueva = una `MovementStrategy` nueva + su método en `Pieces`. Para todo lo demás, composición.
- 🆕 **Patrones elegidos:** Strategy (movimientos), State (turnos y fin de partida) y Command (deshacer). No agreguen otros (Observer, Factory…) sin justificarlo en el grupo. La pregunta antes de cualquier abstracción nueva: **"¿este problema existe hoy?"** Si no, no va.
- **Cero lógica de dibujo en el núcleo.** Símbolos, colores, textos de la ventana: todo en `adapters/swing`.

---

## 7. Reglas de tests

- JUnit 5. El test va en el **mismo paquete** que la clase, bajo `src/test/java`. Ejemplo: `KnightMovement` → `src/test/java/chess/core/movement/KnightMovementTest.java`.
- **Estructura AAA obligatoria**, con los tres comentarios y lo que se espera:

```java
@Test
void rookCannotJumpOverPieces() {
    // Arrange
    Board board = new Board();
    Piece rook = Pieces.rook(Color.WHITE);
    board.setPiece(new Position(0, 0), rook);
    board.setPiece(new Position(2, 0), StubMovement.piece(Color.WHITE, false));   // algo en el camino

    // Act
    boolean result = rook.canMoveTo(new Position(0, 0), new Position(5, 0), board);

    // Assert — espero false: hay una pieza en el camino
    assertFalse(result);
}
```

- **Sin infraestructura:** se arma un `Board` vacío y se ponen solo las piezas que el caso necesita. Nunca se testea a través de la ventana.
- **Dobles de prueba disponibles** en `src/test/java/chess/doubles/`:
  - 🆕 `StubMovement(canMove)` — estrategia que siempre responde lo mismo. Atajos: `StubMovement.piece(color, canMove)` y `StubMovement.piece(color, PieceType.X, canMove)` para poner "algo" en el camino, un enemigo, o una pieza de cierto valor (tests de puntaje / rey).
  - `FakeTurnState(color)` y `FakeTurnState(color, over)` — alterna turnos de verdad; con `over = true` simula que se acabaron los turnos.
  - `MockCommand` — cuenta cuántas veces se llamó `execute()` y `undo()`.
- **Mínimo por estrategia:** un movimiento válido, uno geométricamente inválido, uno bloqueado (si aplica) y una captura. El peón, además: doble paso solo la primera vez, no captura hacia adelante, no avanza en diagonal sin enemigo, y las negras van para el otro lado.
- **Mínimo para los estados:** el turno pasa de blancas a negras y vuelve; con `maxTurns = 1`, después de blancas + negras queda `GameOver`; en `GameOver` nadie puede mover.
- Los nombres de los tests dicen qué verifican: `pawnCanMoveTwoSquaresOnFirstMove`, no `test1`.
- **`mvn test` en verde antes de abrir un PR** (o "Run Tests" en VS Code). Sin excepciones.

---

## 8. Git

- **Nunca push directo a `main`.**
- Una rama por tarea: `feature/knight`, `feature/pawn`, `feature/swing`…
- Terminaste → push de tu rama → **Pull Request** a `main` → A (u otro) lo revisa → merge.
- Antes de empezar cada día: `git checkout main` + `git pull`, y después volvé a tu rama y traé los cambios (`git merge main`).
- Commits chicos, mensaje corto que diga qué hiciste: `"KnightMovement: movimiento y tests"`.
- **No se sube `target/` ni `.idea/`** (ya están en el `.gitignore`).

---

## 9. Cómo justificar decisiones

Toda decisión de diseño que no esté en este convenio se explica con las tres preguntas de la materia: **¿Qué es? ¿Por qué se usa? ¿Cuándo convendría romperlo?** Si se rompe una regla de acá, se escribe el porqué en la descripción del PR.
