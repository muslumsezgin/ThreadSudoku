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
    double timer;

    public double getTimer() {
        return timer;
    }
    Utils.Type type;


    public FirstSolverMethod(int[][] sudoku, MyBoolean myBoolean, Winner winner, Utils.Type type) {
        this.sudoku = sudoku;
        this.myBoolean = myBoolean;
        this.winner = winner;
        this.type = type;
    }

    public void run() {
        long start=System.nanoTime();
        if (backtrackSolve()) {
            System.out.println(Thread.currentThread().getName()+" Bitti");
            myBoolean.setFinished(true);
            timer = ( System.nanoTime() - start) / 1e6;

            System.out.printf("Tasks took %.3f ms to run%n", timer);
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

            switch (type){
                case TopLeft:
                    for (int ii = 0; ii < n && !isThereEmptyCell; ii++) {
                        for (int jj = 0; jj < n && !isThereEmptyCell; jj++) {
                            if (sudoku[ii][jj] == 0) {
                                isThereEmptyCell = true;
                                i = ii;
                                j = jj;
                            }
                        }
                    }
                    break;
                case TopRight:
                    for (int ii = 0; ii < n && !isThereEmptyCell; ii++) {
                        for (int jj = 8; jj >-1 && !isThereEmptyCell; jj--) {
                            if (sudoku[ii][jj] == 0) {
                                isThereEmptyCell = true;
                                i = ii;
                                j = jj;
                            }
                        }
                    }
                    break;
                case BottomLeft:
                    for (int ii = 8; ii > -1 && !isThereEmptyCell; ii--) {
                        for (int jj = 0; jj < n && !isThereEmptyCell; jj++) {
                            if (sudoku[ii][jj] == 0) {
                                isThereEmptyCell = true;
                                i = ii;
                                j = jj;
                            }
                        }
                    }
                    break;
                case BottomRight:
                    for (int ii = 8; ii > -1 && !isThereEmptyCell; ii--) {
                        for (int jj = 8; jj >-1 && !isThereEmptyCell; jj--) {
                            if (sudoku[ii][jj] == 0) {
                                isThereEmptyCell = true;
                                i = ii;
                                j = jj;
                            }
                        }
                    }
                    break;
                case Diogonal:
                    for (int line=1; line<=(9 + 9 -1); line++)
                    {
                        int start_col =  max(0, line-9);
                        int count = min(line, (9-start_col), 9);

                        for (int k=0; k<count; k++)
                            if(sudoku[min(9, line)-k-1][start_col+k]==0){
                                isThereEmptyCell = true;
                                i = min(9, line)-k-1;
                                j = start_col+k;
                            }

                    }
                    break;
            }

            // We've done here.
            if (!isThereEmptyCell) {
                winner.setText(Thread.currentThread().getName());
                winner.setWinnerSudoku(sudoku);
                winner.setPastSteps(pastSteps);
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

     int min(int a, int b)
    { return (a < b)? a: b; }

     int min(int a, int b, int c)
    { return min(min(a, b), c);}

     int max(int a, int b)
    { return (a > b)? a: b; }

}

