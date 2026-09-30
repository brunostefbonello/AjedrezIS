# AjedrezIS

TPO de Ingeniería de Software (UADE). Java 21 + Maven + JUnit 5.

## Estructura
- `core/` — núcleo puro. **Prohibido importar nada externo.**
  - `model/` — Position, Color, Piece, Board y las piezas concretas
  - `rules/` — validación de movimientos
  - `commands/` — MoveCommand y el historial (undo)
  - `state/` — estados de la partida (turnos)
  - `ports/` — interfaces de entrada y salida
- `adapters/console/` — UI de consola y el `main`
- `src/test/` — tests (AAA). `doubles/` para fakes, stubs y mocks.

## Reglas
- Código en inglés.
- Nunca push directo a `main`: rama propia + pull request.
- Toda pieza hereda de `Piece` e implementa `canMoveTo()`, con sus tests.

