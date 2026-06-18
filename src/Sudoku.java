import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Sudoku {
    private final Font Font_Large = new Font("Calibri", Font.BOLD, 30);
    private final Font Font_Medium = new Font("Calibri", Font.BOLD, 28);
    private final Font Font_Small = new Font("Calibri", Font.BOLD, 24);

    private final Color Color_BG_Locked = new Color(230, 230, 230);
    private final Color Color_BG_Selected = new Color(180, 220, 250);
    private final Color Color_BG_Correct = new Color(180, 250, 180);
    private final Color Color_BG_Wrong = new Color(250, 200, 200);
    private final Color Color_Blue_Text = new Color(50, 150, 250);

    private Tile selectedTile = null;
    private int mistakesCounter = 0;

    private final int[][] solvedBoard = {
            {8, 1, 2, 7, 5, 3, 6, 4, 9},
            {9, 4, 3, 6, 8, 2, 1, 7, 5},
            {6, 7, 5, 4, 9, 1, 2, 8, 3},
            {1, 5, 4, 2, 3, 7, 8, 9, 6},
            {3, 6, 9, 8, 4, 5, 7, 2, 1},
            {2, 8, 7, 1, 6, 9, 5, 3, 4},
            {5, 2, 1, 9, 7, 4, 3, 6, 8},
            {4, 3, 8, 5, 2, 6, 9, 1, 7},
            {7, 9, 6, 3, 1, 8, 4, 5, 2}
    };

    private final int[][] board = {
            { 8, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 3, 6, 0, 0, 0, 0, 0 },
            { 0, 7, 0, 0, 9, 0, 2, 0, 0 },
            { 0, 5, 0, 0, 0, 7, 0, 0, 0 },
            { 0, 0, 0, 0, 4, 5, 7, 0, 0 },
            { 0, 0, 0, 1, 0, 0, 0, 3, 0 },
            { 0, 0, 1, 0, 0, 0, 0, 6, 8 },
            { 0, 0, 8, 5, 0, 0, 0, 1, 0 },
            { 0, 9, 0, 0, 0, 0, 4, 0, 0 }
    };

    private int width = 600;
    private int height = 800;
    private final JFrame frame = new JFrame("Sudoku Game");
    private final JLabel textLabel = new JLabel();
    private final JPanel textPanel = new JPanel();
    private final JPanel boardPanel = new JPanel();
    private final JPanel controlPanel = new JPanel();

    Sudoku() {
        frame.setSize(width, height);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        textLabel.setFont(Font_Large);
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Sudoku: " + mistakesCounter + " Mistakes");

        textPanel.add(textLabel);
        frame.add(textPanel, BorderLayout.NORTH);

        boardPanel.setLayout(new GridLayout(9,9));
        setupTiles();
        frame.add(boardPanel, BorderLayout.CENTER);

        setControlPanel();
        frame.add(controlPanel, BorderLayout.SOUTH);

        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(selectedTile != null) {
                    char c = e.getKeyChar();
                    if(c >= '1' && c <= '9') {
                        int number = Character.getNumericValue(c);
                        selectedTile.setValue(number);
                    }
                }
            }
        });

         /*
          Keep @setVisible and @setFocusable at the end
         */
        frame.setFocusable(true);
        frame.setVisible(true);
    }

    /*
    Abstractions
     */

    void setupTiles() {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                Tile tile = new Tile(r, c, board[r][c]);
                boardPanel.add(tile);
            }
        }
    }

    class Tile extends JButton {
        private final int row, col;
        private int value;

        public Tile(int row, int col, int value) {
            this.row = row;
            this.col = col;
            this.value = value;

            setFont(Font_Medium);
            setFocusPainted(false);
            setBorder(BorderFactory.createLineBorder(Color.GRAY));

            if (value != 0) {
                setText(String.valueOf(value));
                setForeground(Color.BLACK);
                setBackground(Color_BG_Locked);
                setEnabled(false);
            } else {
                setText("");
                setForeground(new Color(50,150,250));
                setBackground(Color.WHITE);
            }

            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    handleTileClick();
                }
            });
        }


        private void handleTileClick() {
            if(selectedTile != null && selectedTile.isEnabled()) {
                selectedTile.setBackground(Color.WHITE);
            }

            if (this.isEnabled()) {
                selectedTile = Tile.this;
                selectedTile.setBackground(Color_BG_Selected);
            } else {
                selectedTile = null;
            }
        }

        public void setValue(int value) {
            this.value = value;
            setText(String.valueOf(value));

            if (this.value == solvedBoard[row][col]) {
                setForeground(new Color(50,150,250));
                setBackground(Color_BG_Correct);
                board[row][col] = value;
            } else {
                mistakesCounter++;
                textLabel.setText("Sudoku: " + mistakesCounter + " Mistakes");
                setForeground(Color.RED);
                setBackground(Color_BG_Wrong);
                board[row][col] = value;
            }
        }

        public int getRow() { return row; }
        public int getCol() { return col; }
        public int getTileValue() { return value; }
    }



    void setControlPanel() {
        controlPanel.setLayout(new GridLayout(1,9,5,5));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        for (int i = 0; i <= 9; i++) {
            final int num = i;
            JButton numButton = new JButton(String.valueOf(num));
            numButton.setFont(Font_Small);
            numButton.setFocusPainted(false);

            numButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (selectedTile != null) selectedTile.setValue(num);
                }
            });

            controlPanel.add(numButton);
        }
    }
}
