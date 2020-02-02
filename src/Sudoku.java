public class Sudoku {

    public static boolean checkSudoku (int [] [] sudoku, boolean printErrors)
    {
        for (int i = 0; i < sudoku.length; i++) {
            for (int j = 0; j < sudoku.length; j++) {
                int cell = sudoku [i] [j];
                if (cell == 0) {
                    continue;

                }
                if ((cell < 1) || (cell > 9)) {
                    if (printErrors) {
                        System.out.println ("W rzędzie " + i + " kolumnie " + j +
                                " jest zła wartość " + cell);
                    }
                    return false;
                }

                for (int m = 0; m < sudoku.length; m++) {
                    if ((j != m) && (cell == sudoku [i] [m])) {
                        if (printErrors) {
                            System.out.println ("Ta sama wartość w jednym wierszu");
                        }
                        return false;
                    }
                }

                for (int k = 0; k < sudoku.length; k++) {
                    if ((i != k) && (cell == sudoku [k] [j])) {
                        if (printErrors) {
                            System.out.println ("Ta sama wartość w w kolumnie");
                        }
                        return false;
                    }
                }


                for (int k = 0; k < 3; k++) {
                    for (int m = 0; m < 3; m++) {
                        int testRow = (i / 3 * 3) + k;
                        int testCol = (j / 3 * 3) + m;
                        if ((i != testRow) && (j != testCol) &&
                                (cell == sudoku [testRow] [testCol])) {
                            if (printErrors) {
                                System.out.println ("Ta sama wartość w kwadracie 3*3");
                            }
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public static boolean fillSudoku (int [] [] sudoku)
    {
        for(int r = 0; r < 9; r++) {
            for(int c = 0; c < 9; c++) {
                sudoku[r][c]= 8;
            }
        }
        return false;
    }
}