package chess.adapters.swing;

import chess.core.model.Color;
import chess.core.model.Piece;
import chess.core.model.Position;
import chess.core.ports.IGameService;
import chess.core.ports.MoveResult;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ChessWindow extends JFrame {
    private final IGameService game;
    private final JButton[][] squares = new JButton[8][8];
    private final JLabel statusLabel = new JLabel("Iniciando partida...", SwingConstants.CENTER);
    private final JLabel scoreLabel = new JLabel("Puntos - Blancas: 0 | Negras: 0", SwingConstants.CENTER);
    private final JButton undoBtn = new JButton("Deshacer Jugada"); // 🆕 NUEVO
    private Position selectedPosition = null;

    // Paleta de colores sobrios
    private final java.awt.Color colorClaro = new java.awt.Color(232, 235, 239);
    private final java.awt.Color colorOscuro = new java.awt.Color(152, 164, 175);
    private final java.awt.Color colorSeleccion = new java.awt.Color(235, 123, 102);
    private final Font fuenteTexto = new Font("Segoe UI", Font.PLAIN, 15);
    private final java.awt.Color colorClaroHover = new java.awt.Color(212, 217, 223);
    private final java.awt.Color colorOscuroHover = new java.awt.Color(132, 144, 155);

    public ChessWindow(IGameService game) {
        this.game = game;
        setTitle("Ajedrez IS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new java.awt.Color(248, 249, 250)); // Fondo general claro

        // Panel superior (Textos de estado)
        JPanel topPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        topPanel.setBorder(new EmptyBorder(15, 10, 10, 10));
        topPanel.setOpaque(false);

        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 17));
        statusLabel.setForeground(new java.awt.Color(50, 50, 50));
        scoreLabel.setFont(fuenteTexto);
        scoreLabel.setForeground(java.awt.Color.DARK_GRAY);

        topPanel.add(statusLabel);
        topPanel.add(scoreLabel);
        add(topPanel, BorderLayout.NORTH);

        // Panel del tablero: Grilla 9x9 para hacerle lugar a las coordenadas
        JPanel boardPanel = new JPanel(new GridLayout(9, 9));
        boardPanel.setBorder(new EmptyBorder(0, 15, 0, 15));
        boardPanel.setOpaque(false);

        for (int visualRow = 0; visualRow < 8; visualRow++) {
            // 1. Margen izquierdo: Etiqueta del número de fila (8 a 1)
            JLabel rowLabel = new JLabel(String.valueOf(8 - visualRow), SwingConstants.CENTER);
            rowLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
            rowLabel.setForeground(java.awt.Color.GRAY);
            boardPanel.add(rowLabel);

            // 2. Los 8 botones de la fila
            for (int col = 0; col < 8; col++) {
                JButton btn = new JButton();
                btn.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 36));
                btn.setMargin(new java.awt.Insets(0, 0, 0, 0));
                btn.setFocusPainted(false);
                btn.setOpaque(true);
                btn.setBorderPainted(false);

                int finalVisualRow = visualRow;
                int finalCol = col;
                btn.addActionListener(e -> handleSquareClick(finalVisualRow, finalCol));

                btn.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent e) {
                        if (game.isOver()) return;
                        Position thisPos = new Position(7 - finalVisualRow, finalCol);

                        if (!thisPos.equals(selectedPosition)) {
                            boolean isLight = (finalVisualRow + finalCol) % 2 == 0;
                            btn.setBackground(isLight ? colorClaroHover : colorOscuroHover);
                        }
                    }

                    @Override
                    public void mouseExited(java.awt.event.MouseEvent e) {
                        if (game.isOver()) return;
                        Position thisPos = new Position(7 - finalVisualRow, finalCol);

                        if (!thisPos.equals(selectedPosition)) {
                            boolean isLight = (finalVisualRow + finalCol) % 2 == 0;
                            btn.setBackground(isLight ? colorClaro : colorOscuro);
                        }
                    }
                });

                squares[visualRow][col] = btn;
                boardPanel.add(btn);
            }
        }

        // Fila 9: Esquina inferior izquierda vacía + Letras (a-h)
        boardPanel.add(new JLabel(""));
        char[] columns = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
        for (char c : columns) {
            JLabel colLabel = new JLabel(String.valueOf(c), SwingConstants.CENTER);
            colLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
            colLabel.setForeground(java.awt.Color.GRAY);
            boardPanel.add(colLabel);
        }
        add(boardPanel, BorderLayout.CENTER);

        // Panel inferior (Botón Deshacer)
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBorder(new EmptyBorder(15, 10, 20, 10));
        bottomPanel.setOpaque(false);

        undoBtn.setFont(fuenteTexto);
        undoBtn.setBackground(new java.awt.Color(225, 230, 235));
        undoBtn.setFocusPainted(false);
        undoBtn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new java.awt.Color(190, 200, 210)),
                BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        undoBtn.addActionListener(e -> undo());
        bottomPanel.add(undoBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        setSize(620, 740); // Agrandamos un poco la ventana para que entre la grilla 9x9 holgada
        setLocationRelativeTo(null);
        refreshBoard();
        setVisible(true);
    }

    private void handleSquareClick(int visualRow, int col) {
        if (game.isOver()) return;

        int row = 7 - visualRow;
        Position clickedPos = new Position(row, col);

        if (selectedPosition == null) {
            if (game.pieceAt(clickedPos) != null) {
                selectedPosition = clickedPos;
                squares[visualRow][col].setBackground(colorSeleccion);
            }
        } else {
            MoveResult result = game.move(selectedPosition, clickedPos);
            selectedPosition = null;
            refreshBoard();

            if (result != MoveResult.OK) {
                // Alerta más prolija nativa de Swing
                JOptionPane.showMessageDialog(this, "Movimiento inválido: " + result, "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void undo() {
        if (game.undo()) {
            selectedPosition = null;
            refreshBoard();
        } else {
            JOptionPane.showMessageDialog(this, "No hay jugadas para deshacer.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void refreshBoard() {
        for (int visualRow = 0; visualRow < 8; visualRow++) {
            for (int col = 0; col < 8; col++) {
                boolean isLight = (visualRow + col) % 2 == 0;
                squares[visualRow][col].setBackground(isLight ? colorClaro : colorOscuro);

                int row = 7 - visualRow;
                Piece piece = game.pieceAt(new Position(row, col));
                squares[visualRow][col].setText(PieceSymbols.get(piece));

                if (piece != null) {
                    squares[visualRow][col].setForeground(piece.color() == Color.WHITE ? java.awt.Color.WHITE : java.awt.Color.BLACK);
                }
            }
        }

        scoreLabel.setText("Puntos -> BLANCAS: " + game.score(Color.WHITE) + " | NEGRAS: " + game.score(Color.BLACK));

        if (game.isOver()) {
            Color winner = game.winner();
            statusLabel.setText(winner != null ? "¡PARTIDA TERMINADA! Ganó " + winner : "¡PARTIDA TERMINADA! Empate.");
            undoBtn.setEnabled(false);
        } else {
            statusLabel.setText("Turno activo: " + game.currentTurn());
            undoBtn.setEnabled(true);
        }
    }
}