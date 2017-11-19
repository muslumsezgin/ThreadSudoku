package com.cosean.sudoku.utils;

public class Utils {

    public enum Type {
        TopLeft,
        TopRight,
        BottomLeft,
        BottomRight,
        Diagonal,
        Spiral,
    };

    public static double completePercent(int [][] firstSudokuMatris,int[][] lastSudokuMatris){
        int haveZero=0;
        int firstInput=0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if(firstSudokuMatris[i][j]==0)
                    firstInput++;
                if(lastSudokuMatris[i][j]==0)
                    haveZero++;
            }
        }
        System.out.println(firstInput+"/"+haveZero);

        return 100-((haveZero*100)/(firstInput));
    }

    public static int[][] myClone(int[][] sudokuMatris) {
        int[][] my = new int[9][9];
        for (int i = 0; i < sudokuMatris.length; i++) {
            for (int j = 0; j < sudokuMatris.length; j++) {
                my[i][j] = sudokuMatris[i][j];
            }
        }
        return my;
    }

    public static void writeMatrix(int[][] sudoku) {
        for (int i = 0; i < 9; ++i) {
            if (i % 3 == 0)
                System.out.println(" -----------------------");
            for (int j = 0; j < 9; ++j) {
                if (j % 3 == 0) System.out.print("| ");
                System.out.print(sudoku[i][j] == 0
                        ? " "
                        : Integer.toString(sudoku[i][j]));

                System.out.print(' ');
            }
            System.out.println("|");
        }
        System.out.println(" -----------------------");
    }

}
