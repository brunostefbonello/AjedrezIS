# AjedrezIS

TPO de Ingeniería de Software (UADE). Java 21 + Maven + JUnit 5 + Swing.

## Estructura
- `core/` — núcleo puro. **Prohibido importar nada externo** (ni Swing).
  - `model/` — Position, Color, Board, Piece, PieceType, MovementStrategy, ScoreBoard
  - `movement/` — una estrategia de movimiento por tipo de pieza (patrón Strategy)
  - `setup/` — Pieces (crea cada pieza con su estrategia) y BoardSetup
  - `rules/` — validación de movimientos
  - `commands/` — MoveCommand y el historial (undo, con puntaje)
  - `state/` — estados de la partida (turnos y fin por límite de turnos)
  - `ports/` — puerto de entrada (IGameService)
  - `game/` — ChessGame, implementa el puerto de entrada
- `adapters/swing/` — interfaz gráfica y el `main`
- `src/test/` — tests (AAA). `doubles/` para fakes, stubs y mocks.

## Reglas del juego (alcance del TP)
- Captura del rey → gana el que lo capturó.
- 30 turnos (blancas + negras = 1 turno) → gana el que tiene más puntos; si empatan, empate.
- Valores: peón 1, caballo 3, alfil 3, torre 5, reina 9.

## Reglas de trabajo
- Código en inglés.
- Nunca push directo a `main`: rama propia + pull request.
- Las piezas no se heredan: cada una lleva su `MovementStrategy`.

Las reglas completas están en [CONVENIO.md](CONVENIO.md).
