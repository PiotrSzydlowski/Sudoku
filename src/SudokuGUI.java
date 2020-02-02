import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


public class SudokuGUI extends JPanel implements ActionListener {
    private static final int SIZE = 9;
    private static JFrame frame;
    private JPanel gridPanel;
    private JPanel menuPanel;
    private JTextField[][] squares;
    private JButton buttonSolve;
    private JButton buttonOpen;
    private JButton buttonSave;
    private JButton buttonLock;
    private JButton buttonVerify;


    public SudokuGUI() {
        super(new BorderLayout());
        this.setPreferredSize(new Dimension(600, 600));
        init();
    }


    private static void createAndShowUI() {
        frame = new JFrame("Sudoku Projekt Programowanie Zdarzeniowe");
        frame.add(new SudokuGUI(), BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JFrame.setDefaultLookAndFeelDecorated(true);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowUI();
            }
        });
    }

    private void init() {
        gridPanel = new JPanel(new GridLayout(SIZE, SIZE));
        menuPanel = new JPanel(new FlowLayout());
        squares = new JTextField[SIZE][SIZE];

        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                squares[r][c] = new JTextField("0");
                squares[r][c].setHorizontalAlignment((int) CENTER_ALIGNMENT);
                squares[r][c].setEditable(false);
                gridPanel.add(squares[r][c]);
            }
        }
        colorizeSquares();

        buttonOpen = new JButton("Otwórz");
        buttonSave = new JButton("Zapisz");
        buttonVerify = new JButton("Sprawdź");
        buttonLock = new JButton("Odblokuj");

        buttonOpen.addActionListener(this);
        buttonSave.addActionListener(this);
        buttonVerify.addActionListener(this);
        buttonLock.addActionListener(this);

        menuPanel.add(buttonOpen);
        menuPanel.add(buttonSave);
        menuPanel.add(buttonVerify);
        menuPanel.add(buttonLock);

        add(gridPanel, BorderLayout.CENTER);
        add(menuPanel, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        JButton source = (JButton) actionEvent.getSource();
        if (source == buttonOpen) {
            try {
                openFile();
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }

        // Button Zapisz
        if (source == buttonSave) {
            try {
                saveFile();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }

        // Button Sprawdź
        if (source == buttonVerify) {
            verify();
        }

        // Button Zablokuj/odblokuj
        if (source == buttonLock) {
            if (buttonLock.getText().equals("Odblokuj")) {
                setSquaresEditable(true);
                buttonLock.setText("Zablokuj");
            } else if (buttonLock.getText().equals("Zablokuj")) {
                setSquaresEditable(false);
                buttonLock.setText("Odblokuj");
            }
        }
    }

    private void openFile() throws FileNotFoundException, IllegalArgumentException {
        JFileChooser fileChooser = new JFileChooser();
        int returnVal = fileChooser.showDialog(this, "Otwórz");
        Scanner in;
        File file;
        String[] splitLine;

        // Jeśli user naciśnie cancel
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            return;
        }
        file = fileChooser.getSelectedFile();
        in = new Scanner(file);

        // Rzędy
        int r = 0;

        // Załaduj dane do komórek
        while (in.hasNextLine()) {
            splitLine = in.nextLine().split(",");

            // Sprawdxanie długości danych w rzędize
            if (splitLine.length != SIZE) {
                throw new IllegalArgumentException(String.format("Coś nie tak z wierszem! :/",
                        splitLine.length, file, r));
            }

            for (int c = 0; c < SIZE; c++) {
                // Sprawdzanie poprawności danych w wierszu
                if (splitLine[c].length() != 1 || !(Character.isDigit(splitLine[c].charAt(0)))) {
                    throw new IllegalArgumentException(String.format("Wprowadzona niepoprawna wartość",
                            splitLine[c], file, r, c));
            }
                squares[r][c].setText(splitLine[c]);
            }
            // Przejście do następnego wiersza
            r++;
        }
    }

    private void saveFile() throws IOException {
        JFileChooser fileChooser = new JFileChooser();
        int returnVal = fileChooser.showDialog(this, "Zapisz");
        BufferedWriter out = null;
        File file;

        // Jeśli user naciśnie cancel
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            return;
        }

        file = fileChooser.getSelectedFile();

        out = new BufferedWriter(new FileWriter(file));
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                out.write(squares[r][c].getText());
                if (c < SIZE - 1) {
                    out.write(",");
                }
            }
            out.write("\n");
        }
        out.close();
    }



    private void verify() {
        int[][] sudoku = getSudokuFromSquares();

        if (Sudoku.checkSudoku(sudoku, true)) {
            JOptionPane.showMessageDialog(this, "Sudoku poprawnie rozwiązane :)");
        } else {
            JOptionPane.showMessageDialog(this, "Sudoku NIE rozwiązane :(");
        }
    }

    private void setSquaresEditable(boolean editable) {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                squares[r][c].setEditable(editable);
            }
        }
    }

    private void colorizeSquares() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                // pierwsze trzy rzędy
                if (r >= 0 && r <= 2) {
                    // Pierwsze trzy kolumny
                    if (c >= 0 && c <= 2) {
                        squares[r][c].setBackground(Color.GRAY);
                        squares[r][c].setForeground(Color.WHITE);
                    }
                    // środkowe trzy kolumny
                    if (c >= 3 && c <= 5) {
                        squares[r][c].setBackground(Color.WHITE);
                    }
                    // Ostatnie trzy kolumny
                    if (c >= 6 && c <= 8) {
                        squares[r][c].setBackground(Color.GRAY);
                        squares[r][c].setForeground(Color.WHITE);
                    }
                }

                // środkowe trzy wiersze
                if (r >= 3 && r <= 5) {
                    // Pierwsze trzy kolumny
                    if (c >= 0 && c <= 2) {
                        squares[r][c].setBackground(Color.WHITE);
                    }
                    // środkowe trzy kolumny
                    if (c >= 3 && c <= 5) {
                        squares[r][c].setBackground(Color.GRAY);
                        squares[r][c].setForeground(Color.WHITE);
                    }
                    // Ostatnie trzy kolumny
                    if (c >= 6 && c <= 8) {
                        squares[r][c].setBackground(Color.WHITE);
                    }
                }

                // Ostatnie trzy rzędy
                if (r >= 6 && r <= 8) {
                    // Pierwsze trzy kolukmy
                    if (c >= 0 && c <= 2) {
                        squares[r][c].setBackground(Color.GRAY);
                        squares[r][c].setForeground(Color.WHITE);
                    }
                    // środkowe trzy kolumny
                    if (c >= 3 && c <= 5) {
                        squares[r][c].setBackground(Color.WHITE);
                    }
                    // Ostatnie trzy kolumny
                    if (c >= 6 && c <= 8) {
                        squares[r][c].setBackground(Color.GRAY);
                        squares[r][c].setForeground(Color.WHITE);
                    }
                }
            }
        }
    }


    public int[][] getSudokuFromSquares() {
        int[][] sudoku = new int[SIZE][SIZE];
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                sudoku[r][c] = Integer.parseInt(squares[r][c].getText());
            }
        }
        return sudoku;
    }
}