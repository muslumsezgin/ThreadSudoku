package com.cosean.sudoku.backend;

import com.cosean.sudoku.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class FirstSolverMethod implements Runnable {

    public int[][] sudoku;
    int n = 9;
    public MyBoolean myBoolean;
    public Winner winner;
    public List<int[][]> pastSteps = new ArrayList<>();
    public double timer;

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
        long start = System.nanoTime();
        if (sudokuSolve()) {
            System.out.println(Thread.currentThread().getName() + " sonlandırıldı.");
            myBoolean.setFinished(true);
            timer = (System.nanoTime() - start) / 1e6;
            System.out.printf("Tasks took %.3f ms to run%n", timer);
            return;
        }
    }


    private boolean isProperly(int i, int j, int x) {

        // Satir kontrolu.
        for (int row = 0; row < n; row++) {
            if (sudoku[i][row] == x) {
                return false;
            }
        }

        // Sutun kontrolu
        for (int column = 0; column < n; column++) {
            if (sudoku[column][j] == x) {
                return false;
            }
        }

        // 3x3 kutuları kontrol.
        int boxRow = i - i % 3;
        int boxColumn = j - j % 3;

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                if (sudoku[boxRow + row][boxColumn + column] == x) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean sudokuSolve() {
        if (!myBoolean.isFinished()) {
            pastSteps.add(Utils.myClone(sudoku));
            int i = 0, j = 0;
            boolean isThereEmptyCell = false;

            switch (type) {
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
                        for (int jj = 8; jj > -1 && !isThereEmptyCell; jj--) {
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
                        for (int jj = 8; jj > -1 && !isThereEmptyCell; jj--) {
                            if (sudoku[ii][jj] == 0) {
                                isThereEmptyCell = true;
                                i = ii;
                                j = jj;
                            }
                        }
                    }
                    break;
                case Diagonal:
                    for (int line = 1; line <= (9 + 9 - 1); line++) {
                        int start_col = max(0, line - 9);
                        int count = min(line, (9 - start_col), 9);
                        for (int k = 0; k < count; k++)
                            if (sudoku[min(9, line) - k - 1][start_col + k] == 0) {
                                isThereEmptyCell = true;
                                i = min(9, line) - k - 1;
                                j = start_col + k;
                            }

                    }
                case Spiral:
                    int ii, k = 0, l = 0, m = 9, n = 9;
                    main:
                    while (k < m && l < n) {
                        for (ii = l; ii < n; ++ii) {
                            if (sudoku[k][ii] == 0) {
                                isThereEmptyCell = true;
                                i = k;
                                j = ii;
                                break main;
                            }
                        }
                        k++;
                        for (ii = k; ii < m; ++ii) {
                            if (sudoku[ii][n - 1] == 0) {
                                isThereEmptyCell = true;
                                i = ii;
                                j = n - 1;
                                break main;
                            }
                        }
                        n--;
                        if (k < m) {
                            for (ii = n - 1; ii >= l; --ii) {
                                if (sudoku[m - 1][ii] == 0) {
                                    isThereEmptyCell = true;
                                    i = m - 1;
                                    j = ii;
                                    break main;
                                }
                            }
                            m--;
                        }
                        if (l < n) {
                            for (ii = m - 1; ii >= k; --ii) {
                                if (sudoku[ii][l] == 0) {
                                    isThereEmptyCell = true;
                                    i = ii;
                                    j = l;
                                    break main;
                                }
                            }
                            l++;
                        }
                    }
                    break;
            }

            if (!isThereEmptyCell) {
                winner.setText(Thread.currentThread().getName());
                winner.setWinnerSudoku(sudoku);
                winner.setPastSteps(pastSteps);
                System.out.println("Biten Thread : " + Thread.currentThread().getName());

                return true;
            }

            if (myBoolean.isFinished()) {
                return false;
            }

            for (int x = 1; x < 10; x++) {
                if (isProperly(i, j, x)) {
                    sudoku[i][j] = x;
                    if (sudokuSolve() || myBoolean.isFinished()) {
                        return true;
                    }
                    sudoku[i][j] = 0;

                }

            }
        }
        return false;
    }


    private int min(int a, int b) {
        return (a < b) ? a : b;
    }

    private int min(int a, int b, int c) {
        return min(min(a, b), c);
    }

    private int max(int a, int b) {
        return (a > b) ? a : b;
    }

}

