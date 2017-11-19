import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FirstSolverMethod implements Runnable {

    int[][] sudoku;
    int n=9;
    MyBoolean myBoolean;
    Winner winner;
    List<int[][]>pastSteps =new ArrayList<int[][]>();


    public FirstSolverMethod(int[][] sudoku, MyBoolean myBoolean, Winner winner) {
        this.sudoku = sudoku;
        this.myBoolean = myBoolean;
        this.winner = winner;
    }

    public void run() {
        long start=System.nanoTime();
        if (backtrackSolve()) {
            System.out.println(Thread.currentThread().getName()+" Bitti");
            myBoolean.setFinished(true);
            long time = System.nanoTime() - start;
            System.out.printf("Tasks took %.3f ms to run%n", time/1e6);
            return;
        }
    }



    public boolean isSuitableToPutXThere(int i, int j, int x) {

        // Is 'x' used in row.
        for (int jj = 0; jj < n; jj++) {
            if (sudoku[i][jj] == x) {
                return false;
            }
        }

        // Is 'x' used in column.
        for (int ii = 0; ii < n; ii++) {
            if (sudoku[ii][j] == x) {
                return false;
            }
        }

        // Is 'x' used in sudoku 3x3 box.
        int boxRow = i - i % 3;
        int boxColumn = j - j % 3;

        for (int ii = 0; ii < 3; ii++) {
            for (int jj = 0; jj < 3; jj++) {
                if (sudoku[boxRow + ii][boxColumn + jj] == x) {
                    return false;
                }
            }
        }

        // Everything looks good.
        return true;
    }

    public boolean backtrackSolve() {
        if(!myBoolean.isFinished()) {
            pastSteps.add(myClone(sudoku));
            int i = 0, j = 0;
            boolean isThereEmptyCell = false;

            for (int ii = 0; ii < n && !isThereEmptyCell; ii++) {
                for (int jj = 0; jj < n && !isThereEmptyCell; jj++) {
                    if (sudoku[ii][jj] == 0) {
                        isThereEmptyCell = true;
                        i = ii;
                        j = jj;
                    }
                }
            }

            // We've done here.
            if (!isThereEmptyCell) {
                winner.setText(Thread.currentThread().getName());
                winner.setWinnerSudoku(sudoku);
                System.out.println("Biten Thread : "+Thread.currentThread().getName());

                return true;
            }

            if (myBoolean.isFinished()){
                return false;
            }

            for (int x = 1; x < 10; x++) {

                if (isSuitableToPutXThere(i, j, x)) {
                    sudoku[i][j] = x;

                    if (backtrackSolve()||myBoolean.isFinished()) {
                        return true;
                    }

                    sudoku[i][j] = 0; // We've failed.

                }

            }
        }
        return false; // Backtracking
    }

    private static int[][] myClone(int[][] sudokuMatris) {
        int [][] my =new int[9][9];
        for (int i = 0; i < sudokuMatris.length; i++) {
            for (int j = 0; j < sudokuMatris.length; j++) {
                my[i][j]=sudokuMatris[i][j];
            }
        }
        return my;
    }

}

