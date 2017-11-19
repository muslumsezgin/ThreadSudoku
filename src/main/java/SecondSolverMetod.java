public class SecondSolverMetod implements Runnable{

    int[][] sudoku;

    MyBoolean myBoolean;



    public SecondSolverMetod(int[][] sudoku, MyBoolean myBoolean) {
        this.sudoku = sudoku;
        this.myBoolean = myBoolean;

    }

    public void run() {
        while (!myBoolean.isFinished()) {
            if (solve(0, 0, sudoku)) {    // solves in place
                System.out.println("Çözüldü");
                myBoolean.setFinished(true);
                return;
            } else
                System.out.println("none");
        }
    }

     boolean solve(int i, int j, int[][] cells) {
        if(!myBoolean.isFinished()) {
            if (i == 9) {
                i = 0;
                if (++j == 9)
                    return true;
            }
            if (cells[i][j] != 0)  // skip filled cells
                return solve(i + 1, j, cells);

            if (myBoolean.isFinished())
                return false;

            for (int val = 1; val <= 9; ++val) {
                if (legal(i, j, val, cells)) {
                    cells[i][j] = val;
                    if (solve(i + 1, j, cells))
                        return true;
                }
            }
            cells[i][j] = 0; // reset on backtrack
        }
        return false;
    }

     boolean legal(int i, int j, int val, int[][] cells) {
        for (int k = 0; k < 9; ++k)  // row
            if (val == cells[k][j])
                return false;

        for (int k = 0; k < 9; ++k) // col
            if (val == cells[i][k])
                return false;

        int boxRowOffset = (i / 3)*3;
        int boxColOffset = (j / 3)*3;
        for (int k = 0; k < 3; ++k) // box
            for (int m = 0; m < 3; ++m)
                if (val == cells[boxRowOffset+k][boxColOffset+m])
                    return false;

        return true; // no violations, so it's legal
    }


}
